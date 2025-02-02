package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.database.VenuesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.PerformancesModel;
import ca.ubc.cs304.model.VenuesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UpdateWindowNext extends JFrame implements ActionListener {
    HomeWindowDelegate delegate;
    PerformancesModel p;
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 600;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    private static final int TF_WIDTH = 350;
    private static final int TF_HEIGHT = 30;
    Dimension d = new Dimension(TF_WIDTH,TF_HEIGHT);
    JLabel message;
    JLabel sNameLb;
    JLabel sDateLb;
    JLabel sTimeLb;
    JLabel addressLb;
    JLabel numOfPerformersLb;
    JLabel conductorLb;
    JLabel composerLb;
    JTextField sNameTf;
    JTextField sDateTf;
    JTextField sTimeTf;
    JComboBox<String> addressCb;
    JTextField numOfPerformersTf;
    JTextField conductorTf;
    JTextField composerTf;
    JButton cancel;
    JButton register;

    UpdateWindowNext(HomeWindowDelegate delegate, PerformancesHandler pHandler, VenuesHandler vHandler, PerformancesModel p) {
        this.p = p;
        this.delegate = delegate;
        this.setTitle("Update Show");
        this.setVisible(true);
        this.setLayout(new GridLayout(9, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initialize components
        message = new JLabel("<html><b>Please provide update the information in the corresponding field</b></html>");
        message.setPreferredSize(new Dimension(500,50));
        sNameLb = new JLabel("Event Name: *             ");
        sDateLb = new JLabel("Date(MM-dd-yyyy):  ");
        sTimeLb = new JLabel("Event Time:               ");
        addressLb = new JLabel("Event Address: *        ");   // can't be null since reference key
        numOfPerformersLb = new JLabel("Number of Performers:");
        conductorLb = new JLabel("Conductor:                  ");
        composerLb = new JLabel("Composer:                  ");
        sNameTf = new JTextField(p.getsName());
        sNameTf.setPreferredSize(d);
        if (p.getsDate() == null) {
            sDateTf = new JTextField();
        } else {
            sDateTf = new JTextField(String.valueOf(p.getsDate()));
        }
        sDateTf.setPreferredSize(d);
        sTimeTf = new JTextField(String.valueOf(p.getsTime()));
        sTimeTf.setPreferredSize(d);
        VenuesModel[] venues = vHandler.getVenuesInfo();
        String[] venuesName = new String[venues.length];
        for (int i = 0; i < venues.length; i++) {
            venuesName[i] = venues[i].getvAddress();
        }
        addressCb = new JComboBox(venuesName);
        addressCb.setPreferredSize(d);
        numOfPerformersTf = new JTextField(String.valueOf(p.getNumPerformers()));
        numOfPerformersTf.setPreferredSize(d);
        conductorTf = new JTextField(p.getConductor());
        conductorTf.setPreferredSize(d);
        composerTf = new JTextField(p.getComposer());
        composerTf.setPreferredSize(d);
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        cancel.setPreferredSize(new Dimension(100,30));
        register = new JButton("update");
        register.addActionListener(this);
        register.setPreferredSize(new Dimension(100,30));

        // Create panels
        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = new JPanel(new FlowLayout());
        JPanel p3 = new JPanel(new FlowLayout());
        JPanel p4 = new JPanel(new FlowLayout());
        JPanel p5 = new JPanel(new FlowLayout());
        JPanel p6 = new JPanel(new FlowLayout());
        JPanel p7 = new JPanel(new FlowLayout());
        JPanel p8 = new JPanel(new FlowLayout());
        JPanel p9 = new JPanel(new FlowLayout());

        // Add components to panels
        p1.add(message);
        p2.add(sNameLb);
        p2.add(sNameTf);
        p3.add(sDateLb);
        p3.add(sDateTf);
        p4.add(sTimeLb);
        p4.add(sTimeTf);
        p5.add(addressLb);
        p5.add(addressCb);
        p6.add(numOfPerformersLb);
        p6.add(numOfPerformersTf);
        p7.add(conductorLb);
        p7.add(conductorTf);
        p8.add(composerLb);
        p8.add(composerTf);
        p9.add(cancel);
        p9.add(register);


        // Add panels to the frame
        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
        this.add(p5);
        this.add(p6);
        this.add(p7);
        this.add(p8);
        this.add(p9);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancel)) {
            this.dispose();
        } else {
            // check if name is provided;
            if (sNameTf.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Show name can not be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check if date is valid format
            String inputDate = sDateTf.getText();
            java.sql.Date sqlDate = null;
            if (!inputDate.trim().isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

                    java.util.Date utilDate = dateFormat.parse(inputDate);
                    sqlDate = new java.sql.Date(utilDate.getTime());
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please enter in MM-dd-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            int id = p.getshowid();
            String name = sNameTf.getText();
            int time = 0;
            if (!sTimeTf.getText().trim().isEmpty()) {
                time = Integer.parseInt(sTimeTf.getText());
            }
            String address = (String) addressCb.getSelectedItem();
            int numPerformers = 0;
            if (!numOfPerformersTf.getText().trim().isEmpty()) {
                numPerformers = Integer.parseInt(numOfPerformersTf.getText());
            }
            String conductor = conductorTf.getText();
            String composer = composerTf.getText();
            delegate.updatePerformances(id,name,sqlDate,time,address,numPerformers,conductor,composer);
            String msg = "Performance: " + name + " has been updated";
            JOptionPane.showMessageDialog(this, msg);
            this.dispose();
        }

    }
}
