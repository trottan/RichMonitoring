package org.clas.detectors;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;


public class Acronyms  extends JTabbedPane {        
    JPanel acronymsPanel =  new JPanel(new BorderLayout());
    
    public Acronyms() {
        this.add("Acronyms", this.acronymsPanel);
        JTextPane acronymsDefinitions = new JTextPane();
        acronymsDefinitions.setText("Detectors:\n\n"
                + "CND:\t\tCentral Neutron Detector \n"
                + "CTOF:\t\tCentral Time of Flight \n"
                + "DC:\t\tDrift Chambers \n"
                + "ECAL:\t\tElectromagnetic Calorimeter \n"
                + "FMT:\t\tForward Micromegas Tracker \n"
                + "FTOF:\t\tForward Time of Flight \n"
                + "FTTRK:\t\tForward Tagger Tracker \n"
                + "FTHODO:\tForward Tagger Hodoscope \n"
                + "FTCAL:\t\tForward Tagger Calorimeter \n"
                + "HTCC:\t\tHigh Threshold Cherenkov Counter \n"
                + "LTCC:\t\tLow Threshold Cherenkov Counter \n"
                + "BMT:\t\tBarrel Micromegas Tracker \n"
                + "FMT:\t\tForward Micromegas Tracker \n"
                + "PCal:\t\tPreshower Calorimeter  \n"
                + "RICH:\t\tRing-Imaging Cherenkov Detector \n"
                + "BST:\t\tBarrel Silicon Vertex Tracker \n\n"
                + "Components:\n\n"
                + "ADC:\t\tAnalog to Digital Converter \n"
                + "TDC:\t\tTime to Digital Converter \n"
                + "PMT:\t\tPhoto Multiplier Tube\n"
                + "SiPM:\t\tSilicon Photomultiplier\n");
        this.acronymsPanel.add(acronymsDefinitions,BorderLayout.CENTER);
        acronymsDefinitions.setFont(new Font("Avenir",Font.PLAIN,16));
        acronymsDefinitions.setEditable(false);
    }
      
}
