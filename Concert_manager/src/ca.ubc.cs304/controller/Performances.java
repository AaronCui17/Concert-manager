package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.*;
import ca.ubc.cs304.delegates.HomeWindowDelegate;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.*;
import ca.ubc.cs304.ui.HomeWindow;
import ca.ubc.cs304.ui.LoginWindow;

import java.sql.Date;
import java.util.ArrayList;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class Performances implements LoginWindowDelegate, TerminalTransactionsDelegate, HomeWindowDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;

	private PerformancesHandler bHandler = null;
	private TicketsHandler tHandler = null;
	private VenuesHandler vHandler = null;
	private VideographersHandler vidHandler = null;
	private FilmHandler fHandler = null;


	public Performances() {
		dbHandler = new DatabaseConnectionHandler();
	}

	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
		//test


	}

	/**
	 * LoginWindowDelegate Implementation
	 *
	 * connects to Oracle database with supplied username and password
	 */
	public void login(String username, String password) {
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();

			// Once connected, display home window
			// HomeWindow homeWindow = new HomeWindow();


			// once connected, set handlers
			bHandler = new PerformancesHandler(dbHandler);
			tHandler = new TicketsHandler(dbHandler);
			vHandler = new VenuesHandler(dbHandler);
			vidHandler = new VideographersHandler(dbHandler);
			fHandler = new FilmHandler(dbHandler);
			// test
			databaseSetup();
			HomeWindow homeWindow = new HomeWindow(this, vHandler,bHandler,vidHandler,fHandler);

//			TerminalTransactions transaction = new TerminalTransactions(bHandler, tHandler, vHandler);
//			transaction.setupDatabase(this);
//			transaction.showMainMenu(this);
		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}

	/**
	 * TermainalTransactionsDelegate Implementation
	 *
	 * Insert a branch with the given info
	 */
	public void insertPerformances(PerformancesModel model) {
		bHandler.insertPerformances(model);
	}

	/**
	 * TermainalTransactionsDelegate Implementation
	 *
	 * Delete branch with given branch ID.
	 */
	public void deletePerformances(String name) {
		bHandler.deletePerformances(name);
	}

	/**
	 * TermainalTransactionsDelegate Implementation
	 *
	 * Update the branch name for a specific ID
	 */

	public void updatePerformances(int showid, String sname, Date sdate, int sTime, String sAddress, int numPerformers,
								   String conductor, String composer) {
		bHandler.updatePerformances(showid, sname, sdate, sTime, sAddress, numPerformers, conductor, composer);
	}

	@Override
	public void showNestedAggregation() {
		NestedAggregationModel[] models = bHandler.getNestedAggregationInfo();

		for (int i = 0; i < models.length; i++) {
			NestedAggregationModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-50.50s", model.getsAddress());
			if (model.getAvgNumPerformers() == 0) {
				System.out.printf("%-5.5s", " ");
			} else {
				System.out.printf("%-5.5s", model.getAvgNumPerformers());
			}
			System.out.println();
		}

	}

	@Override
	public void showDivision() {
		VideographersModel[] models = fHandler.getDivisonInfo();

		for (int i = 0; i < models.length; i++) {
			VideographersModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-4.4s", model.getVid());
			System.out.printf("%-20.20s", model.getvName());
			System.out.println();
		}

	}

	@Override
	public void showTickets() {
		TicketsModel[] models = tHandler.getTicketsInfo();

		for (int i = 0; i < models.length; i++) {
			TicketsModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-4.4s", model.getSeatNum());
			System.out.printf("%-4.4s", model.gettRow());
			if (model.gettType() == null) {
				System.out.printf("%-10.10s", " ");
			} else {
				System.out.printf("%-10.10s", model.gettType());
			}
			System.out.printf("%-8.8s", model.getshowid());
			if (model.getEmail() == null) {
				System.out.printf("%-30.30s", " ");
			} else {
				System.out.printf("%-30.30s", model.getEmail());
			}
			System.out.println();
		}
	}

	@Override
	public void showVenues() {
		VenuesModel[] models = vHandler.getVenuesInfo();

		for (int i = 0; i < models.length; i++) {
			VenuesModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-50.50s", model.getvAddress());
			System.out.printf("%-30.30s", model.getvName());
			if (model.getOccupancy() == 0) {
				System.out.printf("%-5.5s", " ");
			} else {
				System.out.printf("%-5.5s", model.getOccupancy());
			}
			System.out.println();
		}
	}

	@Override
	public void showFilm() {
		FilmModel[] models = fHandler.getFilmInfo();

		for (int i = 0; i < models.length; i++) {
			FilmModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-4.4s", model.getShowid());
			System.out.printf("%-4.4s", model.getVid());
			System.out.println();
		}
	}

	@Override
	public void showVideographers() {
		VideographersModel[] models = vidHandler.getVideographersInfo();

		for (int i = 0; i < models.length; i++) {
			VideographersModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-4.4s", model.getVid());
			System.out.printf("%-20.20s", model.getvName());
			System.out.println();
		}

	}

	/**
	 * TermainalTransactionsDelegate Implementation
	 *
	 * Displays information about varies bank branches.
	 */
	public void showPerformances() {
		PerformancesModel[] models = bHandler.getPerformancesInfo();

		for (int i = 0; i < models.length; i++) {
			PerformancesModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-5.5s", model.getshowid());
			System.out.printf("%-30.30s", model.getsName());
			System.out.printf("%-12.12s", model.getsDate());
			System.out.printf("%-6.6s", model.getsTime());
			System.out.printf("%-50.50s", model.getsAddress());
			if (model.getNumPerformers() == 0) {
				System.out.printf("%-5.5s", " ");
			} else {
				System.out.printf("%-5.5s", model.getNumPerformers());
			}
			if (model.getConductor() == null) {
				System.out.printf("%-20.20s", " ");
			} else {
				System.out.printf("%-20.20s", model.getConductor());
			}
			if (model.getComposer() == null) {
				System.out.printf("%-20.20s", " ");
			} else {
				System.out.printf("%-20.20s", model.getComposer());
			}

			System.out.println();

    	}
    }


	public void showJoin(String date) {
		JoinModel[] models = bHandler.getJoinInfo(date);

		for (int i = 0; i < models.length; i++) {
			JoinModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-30.30s", model.getsName());
			System.out.printf("%-6.6s", model.getsTime());
			System.out.printf("%-30.30s", model.getvName());
			System.out.println();
		}
	}

	public void showGroupBy() {
		GroupByModel[] models = bHandler.getGroupByInfo();

		for (int i = 0; i < models.length; i++) {
			GroupByModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-12.12s", model.getsDate());
			System.out.printf("%-6.6s", model.getcount());
			System.out.println();
		}
	}

	public void showHaving() {
		GroupByModel[] models = bHandler.getHavingInfo();

		for (int i = 0; i < models.length; i++) {
			GroupByModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-12.12s", model.getsDate());
			System.out.printf("%-6.6s", model.getcount());
			System.out.println();
		}
	}

	public void showProjection(String table, String columns) {
		ProjectionModel[] models = bHandler.getProjectionInfo(table, columns);

		for (int i = 0; i < models.length; i++) {
			ProjectionModel model = models[i];
			ArrayList<String> attributes = model.getattributes();

			// simplified output formatting; truncation may occur
			for (int j = 0; j < attributes.size(); j++) {
				System.out.printf("%-20.20s", attributes.get(j));
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public void showSelection(ArrayList<String> attributes, ArrayList<String> values, ArrayList<String> connectors) {
		PerformancesModel[] models = bHandler.getSelectionInfo(attributes, values, connectors);

		for (int i = 0; i < models.length; i++) {
			PerformancesModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-5.5s", model.getshowid());
			System.out.printf("%-30.30s", model.getsName());
			System.out.printf("%-12.12s", model.getsDate());
			System.out.printf("%-6.6s", model.getsTime());
			System.out.printf("%-50.50s", model.getsAddress());
			if (model.getNumPerformers() == 0) {
				System.out.printf("%-5.5s", " ");
			} else {
				System.out.printf("%-5.5s", model.getNumPerformers());
			}
			if (model.getConductor() == null) {
				System.out.printf("%-20.20s", " ");
			} else {
				System.out.printf("%-20.20s", model.getConductor());
			}
			if (model.getComposer() == null) {
				System.out.printf("%-20.20s", " ");
			} else {
				System.out.printf("%-20.20s", model.getComposer());
			}

			System.out.println();
		}
	}

	public void showTableNames() {
		namesModel[] models = bHandler.getTableNames();

		for (int i = 0; i < models.length; i++) {
			namesModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-12.12s", model.getName());
			System.out.println();
		}
	}

	public void showColumnNames(String tableName) {
		namesModel[] models = bHandler.getColumnNames(tableName);

		for (int i = 0; i < models.length; i++) {
			namesModel model = models[i];

			// simplified output formatting; truncation may occur
			System.out.printf("%-12.12s", model.getName());
			System.out.println();
		}
	}

    /**

	 * TerminalTransactionsDelegate Implementation
	 *
	 * The TerminalTransaction instance tells us that it is done with what it's
	 * doing so we are cleaning up the connection since it's no longer needed.
	 */
	public void terminalTransactionsFinished() {
		dbHandler.close();
		dbHandler = null;

		System.exit(0);
	}

	/**
	 * TerminalTransactionsDelegate Implementation
	 *
	 * The TerminalTransaction instance tells us that the user is fine with dropping any existing table
	 * called branch and creating a new one for this project to use
	 */
	public void databaseSetup() {
		fHandler.dropFilmTableIfExists();
		vidHandler.dropVideographersTableIfExists();
		tHandler.dropTicketsTableIfExists();
		bHandler.dropPerformancesTableIfExists();
		vHandler.dropVenuesTableIfExists();

		vHandler.databaseSetup();
		bHandler.databaseSetup();
		tHandler.databaseSetup();
		vidHandler.databaseSetup();
		fHandler.databaseSetup();
	}

	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		Performances performances = new Performances();
		performances.start();
	}
}
