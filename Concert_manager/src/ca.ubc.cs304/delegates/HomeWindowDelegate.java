package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.PerformancesModel;

import java.sql.Date;

public interface HomeWindowDelegate {
    public void databaseSetup();
    public void deletePerformances(String name);
    public void insertPerformances(PerformancesModel model);
    public void showPerformances();
    public void updatePerformances(int showid, String sname, Date sdate, int sTime, String sAddress, int numPerformers,
                                   String conductor, String composer);
    public void showNestedAggregation();
    public void showDivision();
    public void showTickets();
    public void showVenues();
    public void showFilm();
    public void showVideographers();
    public void terminalTransactionsFinished();

}
