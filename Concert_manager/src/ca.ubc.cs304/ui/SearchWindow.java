package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.PerformancesModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchWindow extends JFrame implements ActionListener {
    int filterCount = 0;
    HomeWindowDelegate delegate;
    PerformancesHandler pHandler;
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 600;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JScrollPane scroll;
    JLabel msg;
    JLabel instruction;
    JComboBox connector;
    JComboBox attribute;
    JTextField value;
    JButton cancel;
    JButton addFilter;
    JButton clearFilter;
    JButton search;
    JLabel equal;
    String filter;
    String[] attributeOptions;
    String[] connectorOptions;
    PerformancesModel[] results;
    ArrayList<String> values = new ArrayList<>();
    ArrayList<String> attributes = new ArrayList<>();
    ArrayList<String> connectors = new ArrayList<>();
    String[] actualAttributesName = {"showid", "sName", "sDate", "sTime", "sAddress", "numPerformers", "conductor", "composer"};
    java.sql.Date sqlDate;

    public SearchWindow(HomeWindowDelegate delegate, PerformancesHandler pHandler) {
        this.delegate = delegate;
        this.pHandler = pHandler;
        this.setTitle("Update an Event");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
        connectorOptions = new String[]{"and", "or"};
        connector = new JComboBox(connectorOptions);
        connector.setVisible(false);
        attributeOptions = new String[]{"id", "name", "date", "time", "address", "number of performers", "conductor", "composer"};
        attribute = new JComboBox(attributeOptions);
        scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(600, 200));
        msg = new JLabel("search results: ");
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        addFilter = new JButton("add filter");
        addFilter.addActionListener(this);
        clearFilter = new JButton("clear filter");
        clearFilter.addActionListener(this);
        search = new JButton("search");
        search.addActionListener(this);
        equal = new JLabel(" = ");
        value = new JTextField();
        value.setPreferredSize(new Dimension(200,30));
        instruction = new JLabel("Use filters such as: conductor = Tony ");


        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();

        p1.add(msg);
        p1.add(scroll);
        p2.add(instruction);
        p3.add(connector);
        p3.add(attribute);
        p3.add(equal);
        p3.add(value);
        p4.add(cancel);
        p4.add(clearFilter);
        p4.add(addFilter);
        p4.add(search);

        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);



    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancel)) {
            this.dispose();
        } else if (e.getSource().equals(addFilter)) {
            if (value.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please specify the value first", "Error", JOptionPane.ERROR_MESSAGE);
            }


            connector.setVisible(true);
            filterCount++;
            String input = value.getText();
            String selectedAttribute = actualAttributesName[attribute.getSelectedIndex()];
            if (selectedAttribute.equals("showid") || selectedAttribute.equals("stime") || selectedAttribute.equals("numperformers")) {
                try {
                    Integer.parseInt(value.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, "Please enter a number for the selected field", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (selectedAttribute.equals("sname") || selectedAttribute.equals("saddress") ||
                    selectedAttribute.equals("conductor") || selectedAttribute.equals("composer")) {
                input = "'" + input + "'";
            } else if (selectedAttribute.equals("sdate")) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

                    java.util.Date utilDate = dateFormat.parse(input);
                    sqlDate = new java.sql.Date(utilDate.getTime());

                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please enter in MM-dd-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            values.add(input);
            attributes.add(actualAttributesName[attribute.getSelectedIndex()]);
            if (filterCount > 1) {
                connectors.add(connectorOptions[connector.getSelectedIndex()]);
            } else {
                connectors.add("");
            }
            JOptionPane.showMessageDialog(this, "filter added");
        } else if (e.getSource().equals(clearFilter)) {
            filterCount = 0;
            values.clear();
            attributes.clear();
            connectors.clear();
            connector.setVisible(false);
            JOptionPane.showMessageDialog(this, "Filters cleared");
        } else {
            if (attributes.size() == 0) {
                JOptionPane.showMessageDialog(this, "You did not add any filters yet", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = 0; i < connectors.size() - 1; i++) {
                connectors.set(i, connectors.get(i + 1));
            }
            results = pHandler.getSelectionInfo(attributes,values,connectors);
            String[] information = new String[results.length];
            for (int i = 0; i < results.length; i++) {
                information[i] = "Id: " + results[i].getshowid() + ", name: " + results[i].getsName() + ", date: " + results[i].getsDate() +
                        ", time: " + results[i].getsTime() + ", address: " + results[i].getsAddress() + ", number of performers: " + results[i].getNumPerformers() +
                        ", conductor: " + results[i].getConductor() +  ", composer: " + results[i].getComposer();
            }
            JList list = new JList<>(information);
            scroll.setViewportView(list);
        }

    }


}
