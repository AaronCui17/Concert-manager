package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.model.GroupByModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatesWithLargestPerformanceWindow extends JFrame implements ActionListener {
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JScrollPane scroll;
    JLabel msg;
    JButton back;
    JList videographers;
    public DatesWithLargestPerformanceWindow(PerformancesHandler pHandler) {
        back = new JButton("back");
        back.addActionListener(this);
        this.setTitle("Dates With Largest Events");
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x,y,WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setLayout(new GridLayout(2,1));
        GroupByModel[] models = pHandler.getHavingInfo();
        String[] information = new String[models.length];
        for (int i = 0; i < models.length; i++) {
            if (models[i].getsDate() != null) {
                information[i] = "We have a total of " + models[i].getcount() + " on " + models[i].getsDate();
            } else {
                information[i] = "We have a total of " + models[i].getcount() + " on " + "DATE NOT RECORDED";
            }

        }
        videographers = new JList<>(information);
        scroll = new JScrollPane(videographers);
        scroll.setPreferredSize(new Dimension(350, 100));
        msg = new JLabel("date(s) that have more than 100 performers              ");
        msg.setPreferredSize(new Dimension(400,30));
        msg.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p1.add(msg);
        p1.add(scroll);
        p2.add(back);
        this.add(p1);
        this.add(p2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();

    }
}
