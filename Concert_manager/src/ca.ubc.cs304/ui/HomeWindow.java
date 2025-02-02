package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.*;
import ca.ubc.cs304.delegates.HomeWindowDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HomeWindow extends JFrame implements ActionListener {
    public static int count = 8;
    public static int vidIdCount = 4;
    private HomeWindowDelegate delegate;
    private VenuesHandler vHandler;
    private TicketsHandler tHandler;
    private PerformancesHandler pHandler;
    private VideographersHandler vidHandler;
    private FilmHandler fHandler;
    JButton register;
    JButton remove;
    JButton update;
    JButton search;
    JButton show;
    JButton venues;
    JButton dates;
    JButton addVideographer;
    JButton bestVideographers;
    JButton assignVideographer;
    JButton seeDatesWithMostPerformers;
    JButton seeEventsByDate;



    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BUTTON_HEIGHT = 100;
    private static final int BUTTON_WIDTH = 200;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - WINDOW_WIDTH) / 2;
    int y = (screenSize.height - WINDOW_HEIGHT) / 2;
    public HomeWindow(HomeWindowDelegate delegate, VenuesHandler vHandler, PerformancesHandler pHandler, VideographersHandler vidHandler, FilmHandler fHandler) {
        this.delegate = delegate;
        this.vHandler = vHandler;
        this.pHandler = pHandler;
        this.vidHandler = vidHandler;
        this.fHandler = fHandler;
        this.setTitle("Home");
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(false);
        this.setBounds(x,y,WINDOW_WIDTH,WINDOW_HEIGHT);

        register = new JButton("Register an Event");
        remove = new JButton("Remove an Event");
        update = new JButton("Update an Event");
        search = new JButton("Search Events");
        show = new JButton("Show Information");
        venues = new JButton("See Venues");
        dates = new JButton("See Events by Dates");
        addVideographer = new JButton("Register a Videographer");
        bestVideographers = new JButton("Our Best Videographers");
        assignVideographer = new JButton("Assign a Videographer");
        seeDatesWithMostPerformers = new JButton("Dates With Most Performers");


        register.setBounds(50,50, BUTTON_WIDTH, BUTTON_HEIGHT);
        remove.setBounds(250,50, BUTTON_WIDTH,BUTTON_HEIGHT);
        update.setBounds(450,50, BUTTON_WIDTH,BUTTON_HEIGHT);
        search.setBounds(50,150, BUTTON_WIDTH,BUTTON_HEIGHT);
        show.setBounds(250,150,BUTTON_WIDTH,BUTTON_HEIGHT);
        venues.setBounds(450,150,BUTTON_WIDTH, BUTTON_HEIGHT);
        dates.setBounds(50, 250, BUTTON_WIDTH, BUTTON_HEIGHT);
        addVideographer.setBounds(250,250, BUTTON_WIDTH,BUTTON_HEIGHT);
        bestVideographers.setBounds(450,250, BUTTON_WIDTH,BUTTON_HEIGHT);
        assignVideographer.setBounds(50,350,BUTTON_WIDTH,BUTTON_HEIGHT);
        seeDatesWithMostPerformers.setBounds(250,350,BUTTON_WIDTH,BUTTON_HEIGHT);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                delegate.terminalTransactionsFinished();

            }
        });
        register.addActionListener(this);
        remove.addActionListener(this);
        update.addActionListener(this);
        search.addActionListener(this);
        show.addActionListener(this);
        venues.addActionListener(this);
        dates.addActionListener(this);
        addVideographer.addActionListener(this);
        bestVideographers.addActionListener(this);
        assignVideographer.addActionListener(this);
        seeDatesWithMostPerformers.addActionListener(this);

        this.add(register);
        this.add(remove);
        this.add(update);
        this.add(search);
        this.add(show);
        this.add(venues);
        this.add(dates);
        this.add(addVideographer);
        this.add(bestVideographers);
        this.add(assignVideographer);
        this.add(seeDatesWithMostPerformers);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(register)) {
            new RegisterWindow(delegate, vHandler);
        } else if (source.equals(remove)) {
            new RemoveWindow(delegate,pHandler);
        } else if (source.equals(update)) {
            new UpdateWindow(delegate, pHandler, vHandler);
        } else if (source.equals(search)) {
            new SearchWindow(delegate, pHandler);
        } else if (source.equals(show)) {
            new ProjectFromWindow(delegate,pHandler);
        } else if (source.equals(venues)) {
            new VenuesWindow(delegate, vHandler, pHandler);
        } else if (source.equals(dates)) {
            new DatesWindow(delegate,vHandler,pHandler);
        } else if (source.equals(addVideographer)) {
            new AddVideographerWindow(delegate,vidHandler);
        } else if (source.equals(assignVideographer)) {
            new AssignVideographerWindow(delegate,fHandler, vidHandler,pHandler);
        } else if (source.equals(bestVideographers)) {
            new BestVideographersWindow(fHandler);
        } else if (source.equals(seeDatesWithMostPerformers)) {
            new DatesWithLargestPerformanceWindow(pHandler);
        }
    }
}
