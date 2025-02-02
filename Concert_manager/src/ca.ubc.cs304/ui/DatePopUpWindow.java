package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatePopUpWindow extends JFrame implements ActionListener {
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JScrollPane scroll;
    JLabel msg;
    JButton back;
    JList list;
    public DatePopUpWindow(JList list) {
        back = new JButton("back");
        back.addActionListener(this);
        this.setTitle("Events");
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x,y,WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setLayout(new GridLayout(2,1));
        this.list = list;
        scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(350, 100));
        msg = new JLabel(" ");
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
