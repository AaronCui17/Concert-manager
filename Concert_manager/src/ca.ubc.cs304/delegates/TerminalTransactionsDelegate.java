package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.PerformancesModel;

import java.sql.Date;
import java.util.ArrayList;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 * 
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
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
	public void showJoin(String date);
	public void showGroupBy();
	public void showHaving();
	public void showProjection(String table, String columns);
	public void showSelection(ArrayList<String> attributes, ArrayList<String> values, ArrayList<String> connectors);
	public void showTableNames();
	public void showColumnNames(String tableName);
	public void terminalTransactionsFinished();
}
