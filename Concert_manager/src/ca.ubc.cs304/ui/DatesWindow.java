package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.database.VenuesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.GroupByModel;
import ca.ubc.cs304.model.JoinModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DatesWindow extends JFrame implements ActionListener {
    HomeWindowDelegate delegate;
    PerformancesHandler pHandler;
    VenuesHandler vHandler;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JLabel message;
    JTextField date;
    JButton cancel;
    JButton check;
    JButton showEvents;
    GroupByModel[] datesAndCount;

    DatesWindow(HomeWindowDelegate delegate, VenuesHandler vHandler, PerformancesHandler pHandler) {
        this.delegate = delegate;
        this.vHandler = vHandler;
        this.pHandler = pHandler;
        this.setTitle("See Dates");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
        message = new JLabel("Enter a date(MM-dd-yyyy): ");
        date = new JTextField();
        date.setPreferredSize(new Dimension(350,30));
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        check = new JButton("check number of events");
        check.addActionListener(this);
        showEvents = new JButton("show events on that day");
        showEvents.addActionListener(this);

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();

        p2.add(message);
        p2.add(date);
        p4.add(cancel);
        p4.add(check);
        p4.add(showEvents);


        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(check)) {
            String input = date.getText();
            java.sql.Date sqlDate = null;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

                java.util.Date utilDate = dateFormat.parse(input);
                sqlDate = new java.sql.Date(utilDate.getTime());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please enter in MM-dd-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            datesAndCount = pHandler.getGroupByInfo();

            for (int i = 0; i < datesAndCount.length; i++) {
                if (datesAndCount[i].getsDate().equals(sqlDate)) {
                    JOptionPane.showMessageDialog(this, "there are " + datesAndCount[i].getcount() + " events on " + sqlDate);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "there is no event on " + sqlDate);

        } else if (e.getSource().equals(showEvents)) {
            String input = date.getText();
            java.sql.Date sqlDate = null;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

                java.util.Date utilDate = dateFormat.parse(input);
                sqlDate = new java.sql.Date(utilDate.getTime());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please enter in MM-dd-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            datesAndCount = pHandler.getGroupByInfo();
            boolean hasEvent = false;
            for (int i = 0; i < datesAndCount.length; i++) {
                if (datesAndCount[i].getsDate().equals(sqlDate)) {
                    hasEvent = true;
                }
            }
            if (hasEvent) {
                JoinModel[] model = pHandler.getJoinInfo(String.valueOf(sqlDate));
                String[] information = new String[model.length];
                for (int i = 0; i < model.length; i++) {
                    information[i] = model[i].getsName() + " (time: " + model[i].getsTime() + ") " + " at " + model[i].getvName();
                }
                JList list = new JList<>(information);
                new DatePopUpWindow(list);

            } else {
                JOptionPane.showMessageDialog(this, "there is no event on " + sqlDate);
            }

        } else {
            this.dispose();
        }

    }
}
