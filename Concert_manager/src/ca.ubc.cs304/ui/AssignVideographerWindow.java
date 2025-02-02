package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.FilmHandler;
import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.database.VideographersHandler;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.model.FilmModel;
import ca.ubc.cs304.model.PerformancesModel;
import ca.ubc.cs304.model.VideographersModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AssignVideographerWindow extends JFrame implements ActionListener {
    HomeWindowDelegate delegate;
    FilmHandler fHandler;
    VideographersHandler vHandler;
    PerformancesHandler pHandler;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 300;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    JLabel vid;
    JLabel pid;
    JComboBox vidInput;
    JComboBox pidInput;
    JButton cancel;
    JButton assignVid;
    String[] pids;
    String[] vids;

    public AssignVideographerWindow(HomeWindowDelegate delegate, FilmHandler fHandler, VideographersHandler vHandler, PerformancesHandler pHandler) {
        this.delegate = delegate;
        this.fHandler = fHandler;
        this.vHandler = vHandler;
        this.pHandler = pHandler;
        this.setTitle("Assign Videographer");
        this.setVisible(true);
        this.setLayout(new GridLayout(4, 1));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
        vid = new JLabel("Assign videographer ");
        VideographersModel[] vs = vHandler.getVideographersInfo();
        vids = new String[vs.length];
        for (int i = 0; i < vs.length; i++) {
            vids[i] = String.valueOf(vs[i].getVid());
        }
        vidInput = new JComboBox(vids);
        vidInput.setPreferredSize(new Dimension(60, 30));
        pid = new JLabel("to event ");
        PerformancesModel[] ps = pHandler.getPerformancesInfo();
        pids = new String[ps.length];
        for (int i = 0; i < ps.length; i++) {
            pids[i] = String.valueOf(ps[i].getshowid());
        }
        pidInput = new JComboBox(pids);
        pidInput.setPreferredSize(new Dimension(60, 30));
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        assignVid = new JButton("assign videographer");
        assignVid.addActionListener(this);

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();

        p2.add(vid);
        p2.add(vidInput);
        p2.add(pid);
        p2.add(pidInput);
        p4.add(cancel);
        p4.add(assignVid);


        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(assignVid)) {
            int videographer = Integer.parseInt(vids[vidInput.getSelectedIndex()]);
            int performance = Integer.parseInt(pids[pidInput.getSelectedIndex()]);
            FilmModel filmModel = new FilmModel(performance,videographer);
            fHandler.insertFilm(filmModel);
            JOptionPane.showMessageDialog(this, "Assigned videographer #" + videographer + " to performance #" + performance);
            this.dispose();
        } else {
            this.dispose();
        }
    }
}
