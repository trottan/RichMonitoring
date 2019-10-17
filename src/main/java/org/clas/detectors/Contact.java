package org.clas.detectors;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;


public class Contact  extends JTabbedPane {        
    JPanel monitoringPanel =  new JPanel(new BorderLayout());
    
    public Contact() {
        this.add("Monitoring Contacts", this.monitoringPanel);
        JTextPane monitoringContacts = new JTextPane();
        monitoringContacts.setText("\nContact for the shift taker online monitoring:\n\n"
                + "Stefan Diehl,   sdiehl@jlab.org\n\n"
                + "Contact persons for the expert monitoring:\n\n"
                + "CND:\t\t\tDaniel Carman,\t\t\tcarman@jlab.org,\t\t\t757-344-7204\n"
                + "CTOF:\t\t\tDaniel Carman,\t\t\tcarman@jlab.org,\t\t\t757-344-7204\n"
                + "CVT:\t\t\tYuri Gotra,\t\t\tgotra@jlab.org,\t\t\t757-541-7539\n"
                + "DC:\t\t\tMac Mesteyer,\t\t\tmestayer@jlab.org,\t\t757-329-4844\n"
                + "ECAL:\t\t\tCole Smith,\t\t\tlcsmith@jlab.org,\t\t\t757-810-1489\n"
                + "FTOF:\t\t\tDaniel Carman,\t\t\tcarman@jlab.org,\t\t\t757-344-7204\n"
                + "FTCAL:\t\t\tMarco Battaglieri,\t\t\tbattaglieri@ge.infn.it,\t\t757-344-1848\n"
                + "FTHODO:\t\tMarco Battaglieri,\t\t\tbattaglieri@ge.infn.it,\t\t757-344-1848\n"
                + "FTTRK:\t\t\tMarco Battaglieri,\t\t\tbattaglieri@ge.infn.it,\t\t757-344-1848\n"
                + "HTTC:\t\t\tYouri Sharabian,\t\t\tyouris@jlab.org,\t\t\t757-344-7174\n"
                + "LTTC:\t\t\tMaurizio Ungaro,\t\t\tungaro@jlab.org,\t\t\t757-329-4846\n"
                + "DAQ:\t\t\tSergey Boiarinov,\t\t\tboiarino@jlab.org,\t\t\t757-869-2188\n"
                + "Slow Control:\t\tNathan Baltzell,\t\t\tbaltzell@jlab.org,\t\t\t757-748-6922\n"
                + "Beamline:\t\tStepan Stepanyan,\t\tstepanya@jlab.org,\t\t757-303-3996\n"
                + "Engineering:\t\t\t\t\t\t\t\t\t\t757-748-5048");
        this.monitoringPanel.add(monitoringContacts,BorderLayout.CENTER);
        monitoringContacts.setFont(new Font("Avenir",Font.PLAIN,16));
        monitoringContacts.setEditable(false);
    }
      
}
