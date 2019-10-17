package org.clas.detectors;

import java.util.Arrays;
import java.util.List;

import org.clas.viewer.DetectorMonitor;
import org.jlab.detector.calib.utils.DatabaseConstantProvider;
import org.jlab.groot.data.H1F;
import org.jlab.groot.group.DataGroup;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;
import org.jlab.utils.groups.IndexedTable;

public class BMTmonitor extends DetectorMonitor {

    int sparseSample;
    int numberOfSamples;
    int samplingTime;

    int maxNumberLayers;
    int maxNumberSectors;
    int maxNumberStrips;
    int numberStrips[];

    boolean isZ[];
    boolean mask[][][];

    int numberOfHitsPerDetector[][];

    int runNumber = 0;
    int defaultRunNumber = 2284;

    public BMTmonitor(String name) {
        super(name);

        this.loadConstantsFromCCDB(defaultRunNumber);

        this.setDetectorTabNames("Occupancy C", "Occupancy Z", "TimeMax", "Multiplicity");
        this.init(false);
    }

    public void loadConstantsFromCCDB(int runNumber) {
        List<String> tablesFitter = null;
        List<String> keysFitter = null;

        keysFitter   = Arrays.asList(new String[]{"BMTconfig"});
        tablesFitter = Arrays.asList(new String[]{"/daq/config/bmt"});
        this.getCcdb().init(keysFitter, tablesFitter);

        IndexedTable bmtConfig = this.getCcdb().getConstants(runNumber, "BMTconfig");

        this.sparseSample = bmtConfig.getIntValue("sparse", 0, 0, 0);
        this.numberOfSamples = (bmtConfig.getIntValue("number_sample", 0, 0, 0) - 1) * (this.sparseSample + 1) + 1;
        this.samplingTime = (byte) bmtConfig.getDoubleValue("sampling_time", 0, 0, 0);

        DatabaseConstantProvider dbprovider = new DatabaseConstantProvider(runNumber, "default");
        dbprovider.loadTable("/geometry/cvt/mvt/bmt_layer");

        this.maxNumberLayers = dbprovider.length("/geometry/cvt/mvt/bmt_layer/Layer");
        this.maxNumberSectors = dbprovider.getInteger("/geometry/cvt/mvt/bmt_layer/Nsector", 0);
        this.maxNumberStrips = 0;
        this.numberStrips = new int[maxNumberLayers + 1];
        this.isZ = new boolean[maxNumberLayers + 1];
        for (int layer = 1; layer < maxNumberLayers + 1; layer++) {
            this.numberStrips[layer] = dbprovider.getInteger("/geometry/cvt/mvt/bmt_layer/Nstrip", (layer - 1));
            if (this.numberStrips[layer] > this.maxNumberStrips) {
                this.maxNumberStrips = this.numberStrips[layer];
            }
            if (dbprovider.getInteger("/geometry/cvt/mvt/bmt_layer/Axis", (layer - 1)) == 1) {
                this.isZ[layer] = true;
            } else {
                this.isZ[layer] = false;
            }
        }

        this.mask = new boolean[maxNumberSectors + 1][maxNumberLayers + 1][maxNumberStrips + 1];

        for (int sector = 1; sector <= maxNumberSectors; sector++) {
            for (int layer = 1; layer <= maxNumberLayers; layer++) {
                for (int component = 1; component <= numberStrips[layer]; component++) {
                    this.mask[sector][layer][component] = true;
                }
            }
        }

        this.numberOfHitsPerDetector = new int[maxNumberSectors + 1][maxNumberLayers + 1];
    }

    @Override
    public void createHistos() {

        // create histograms
        this.setNumberOfEvents(0);

        H1F summary = new H1F("summary", "summary", maxNumberSectors * maxNumberLayers, 0.5, maxNumberSectors * maxNumberLayers + 0.5);
        summary.setTitleX("detector");
        summary.setTitleY("occupancy");
        summary.setTitle("BMT");
        summary.setFillColor(38);
        DataGroup sum = new DataGroup(1, 1);
        sum.addDataSet(summary, 0);
        this.setDetectorSummary(sum);

        H1F histmulti = new H1F("multi", "multi", 250, -0.5, 249.5);
        histmulti.setTitleX("hit multiplicity");
        histmulti.setTitleY("counts");
        histmulti.setTitle("Multiplicity of BMT channels");
        DataGroup occupancyGroup = new DataGroup("");
        occupancyGroup.addDataSet(histmulti, 0);
        this.getDataGroup().add(occupancyGroup, 0, 0, 0);

        for (int sector = 1; sector <= maxNumberSectors; sector++) {
            for (int layer = 1; layer <= maxNumberLayers; layer++) {
                H1F hitmapHisto = new H1F("Occupancy Layer " + layer + " Sector " + sector, "Occupancy Layer " + layer + " Sector " + sector + " (detector " + (3 * (layer - 1) + sector) + ")",
                        (numberStrips[layer]) + 1, 0., (double) (numberStrips[layer]) + 1);
                hitmapHisto.setTitleX("Strips (Layer " + layer + " Sector " + sector + ")");
                hitmapHisto.setTitleY("Nb of hits");
                if (isZ[layer]) {
                    hitmapHisto.setFillColor(4);
                } else {
                    hitmapHisto.setFillColor(8);
                }
                DataGroup hitmapGroup = new DataGroup("");
                hitmapGroup.addDataSet(hitmapHisto, 0);
                this.getDataGroup().add(hitmapGroup, sector, layer, 2);

                H1F timeMaxHisto = new H1F("TimeOfMax : Layer " + layer + " Sector " + sector, "TimeOfMax : Layer " + layer + " Sector " + sector,
                        samplingTime * numberOfSamples, 1., samplingTime * numberOfSamples);
                timeMaxHisto.setTitleX("Time of max (Layer " + layer + " Sector " + sector + ")");
                timeMaxHisto.setTitleY("Nb hits");
                if (isZ[layer]) {
                    timeMaxHisto.setFillColor(4);
                } else {
                    timeMaxHisto.setFillColor(8);
                }
                DataGroup timeOfMaxGroup = new DataGroup("");
                timeOfMaxGroup.addDataSet(timeMaxHisto, 0);
                this.getDataGroup().add(timeOfMaxGroup, sector, layer, 1);
            }
        }

    }

    @Override
    public void plotHistos() {

        this.getDetectorCanvas().getCanvas("Occupancy C").divide(maxNumberSectors, maxNumberLayers / 2);
        this.getDetectorCanvas().getCanvas("Occupancy C").setGridX(false);
        this.getDetectorCanvas().getCanvas("Occupancy C").setGridY(false);
        this.getDetectorCanvas().getCanvas("Occupancy C").setAxisTitleSize(12);
        this.getDetectorCanvas().getCanvas("Occupancy C").setAxisLabelSize(12);

        this.getDetectorCanvas().getCanvas("Occupancy Z").divide(maxNumberSectors, maxNumberLayers / 2);
        this.getDetectorCanvas().getCanvas("Occupancy Z").setGridX(false);
        this.getDetectorCanvas().getCanvas("Occupancy Z").setGridY(false);
        this.getDetectorCanvas().getCanvas("Occupancy Z").setAxisTitleSize(12);
        this.getDetectorCanvas().getCanvas("Occupancy Z").setAxisLabelSize(12);

        this.getDetectorCanvas().getCanvas("TimeMax").divide(maxNumberSectors, maxNumberLayers);
        this.getDetectorCanvas().getCanvas("TimeMax").setGridX(false);
        this.getDetectorCanvas().getCanvas("TimeMax").setGridY(false);
        this.getDetectorCanvas().getCanvas("TimeMax").setAxisTitleSize(12);
        this.getDetectorCanvas().getCanvas("TimeMax").setAxisLabelSize(12);

        for (int sector = 1; sector <= maxNumberSectors; sector++) {
            for (int layer = 1; layer <= maxNumberLayers; layer++) {
                int column = maxNumberSectors - sector;
                int row;
                int numberOfColumns = maxNumberSectors;
                switch (layer) {
                    case 1:
                        row = 2;
                        break;
                    case 2:
                        row = 2;
                        break;
                    case 3:
                        row = 1;
                        break;
                    case 4:
                        row = 1;
                        break;
                    case 5:
                        row = 0;
                        break;
                    case 6:
                        row = 0;
                        break;
                    default:
                        row = -1;
                        break;
                }
                if (isZ[layer]) {
                    this.getDetectorCanvas().getCanvas("Occupancy Z").cd(column + numberOfColumns * row);
                    this.getDetectorCanvas().getCanvas("Occupancy Z").draw(
                            this.getDataGroup().getItem(sector, layer, 2).getH1F("Occupancy Layer " + layer + " Sector " + sector));
                } else {
                    this.getDetectorCanvas().getCanvas("Occupancy C").cd(column + numberOfColumns * row);
                    this.getDetectorCanvas().getCanvas("Occupancy C").draw(
                            this.getDataGroup().getItem(sector, layer, 2).getH1F("Occupancy Layer " + layer + " Sector " + sector));
                }
                switch (layer) {
                    case 1:
                        row = 5;
                        break;
                    case 2:
                        row = 2;
                        break;
                    case 3:
                        row = 1;
                        break;
                    case 4:
                        row = 4;
                        break;
                    case 5:
                        row = 0;
                        break;
                    case 6:
                        row = 3;
                        break;
                    default:
                        row = -1;
                        break;
                }

                this.getDetectorCanvas().getCanvas("TimeMax").cd(column + numberOfColumns * row);
                this.getDetectorCanvas().getCanvas("TimeMax").draw(
                        this.getDataGroup().getItem(sector, layer, 1).getH1F("TimeOfMax : Layer " + layer + " Sector " + sector));
            }
        }
        this.getDetectorCanvas().getCanvas("Occupancy Z").update();
        this.getDetectorCanvas().getCanvas("Occupancy C").update();
        this.getDetectorCanvas().getCanvas("TimeMax").update();

        this.getDetectorCanvas().getCanvas("Multiplicity").divide(1, 1);
        this.getDetectorCanvas().getCanvas("Multiplicity").setGridX(false);
        this.getDetectorCanvas().getCanvas("Multiplicity").setGridY(false);
        this.getDetectorCanvas().getCanvas("Multiplicity").cd(0);
        this.getDetectorCanvas().getCanvas("Multiplicity").draw(this.getDataGroup().getItem(0, 0, 0).getH1F("multi"));
        this.getDetectorCanvas().getCanvas("Multiplicity").update();
    }

    public void processEvent(DataEvent event) {

        if (this.getNumberOfEvents() >= super.eventResetTime_current && super.eventResetTime_current > 0) {
            resetEventListener();
        }

        if (this.runNumber == 0) {
            int numberOfEvents = this.getNumberOfEvents();
            if (event.hasBank("RUN::config")) {
                DataBank head = event.getBank("RUN::config");
                runNumber = head.getInt("run", 0);
            } else {
                runNumber = 2284;
            }
            this.loadConstantsFromCCDB(runNumber);
            this.createHistos();
            this.plotHistos();
            this.setNumberOfEvents(numberOfEvents); //Cause number of events got reset when re-creating histos
        }

		//if (!testTriggerMask()) return;
        if (event.hasBank("BMT::adc") == true) {
            DataBank bank = event.getBank("BMT::adc");

            this.getDataGroup().getItem(0, 0, 0).getH1F("multi").fill(bank.rows());

            for (int i = 0; i < bank.rows(); i++) {
                int sector = bank.getByte("sector", i);
                int layer = bank.getByte("layer", i);
                int strip = bank.getShort("component", i);
                float timeOfMax = bank.getFloat("time", i);

//				if (strip < 0 || !mask[sector][layer][strip]){
//					continue;
//				}
                this.getDataGroup().getItem(sector, layer, 2).getH1F("Occupancy Layer " + layer + " Sector " + sector).fill(strip);
                if ((samplingTime < timeOfMax) && (timeOfMax < samplingTime * (numberOfSamples - 1))) {
                    this.getDataGroup().getItem(sector, layer, 1).getH1F("TimeOfMax : Layer " + layer + " Sector " + sector).fill(timeOfMax);
                }
                this.numberOfHitsPerDetector[sector][layer]++;
                this.getDetectorSummary().getH1F("summary").setBinContent(maxNumberSectors * (layer - 1) + (sector - 1), (double) this.numberOfHitsPerDetector[sector][layer] / ((double) this.getNumberOfEvents()));
            }
        }
    }
}
