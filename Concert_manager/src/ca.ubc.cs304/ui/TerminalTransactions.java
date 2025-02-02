package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.PerformancesHandler;
import ca.ubc.cs304.database.TicketsHandler;
import ca.ubc.cs304.database.VenuesHandler;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.PerformancesModel;
import ca.ubc.cs304.model.VenuesModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;

import static java.sql.Types.NULL;

/**
 * The class is only responsible for handling terminal text inputs. 
 */
public class TerminalTransactions {

	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;

	private int showidCounter;
	private BufferedReader bufferedReader = null;
	private TerminalTransactionsDelegate delegate = null;

	private VenuesHandler vHandler;
	private TicketsHandler tHandler;
	private PerformancesHandler pHandler;

	public TerminalTransactions(PerformancesHandler pHandler, TicketsHandler tHandler, VenuesHandler vHandler) {
		// start showid from 1000
		this.pHandler = pHandler;
		this.tHandler = tHandler;
		this.vHandler = vHandler;
		// TODO: this counter needs to be implemented in insert window as well
		showidCounter = 1000;
	}
	
	/**
	 * Sets up the database to have a performances table with two tuples so we can insert/update/delete from it.
	 * Refer to the databaseSetup.sql file to determine what tuples are going to be in the table.
	 */
	public void setupDatabase(TerminalTransactionsDelegate delegate) {
		this.delegate = delegate;
		
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while(choice != 1 && choice != 2) {
			System.out.println("If you have a table called Performances in your database (capitialization of the name does not matter), it will be dropped and a new Performances table will be created.\nIf you want to proceed, enter 1; if you want to quit, enter 2.");
			
			choice = readInteger(false);
			
			if (choice != INVALID_INPUT) {
				switch (choice) {
				case 1:  
					delegate.databaseSetup(); 
					break;
				case 2:  
					handleQuitOption();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.\n");
					break;
				}
			}
		}
	}

	/**
	 * Displays simple text interface
	 */ 
	public void showMainMenu(TerminalTransactionsDelegate delegate) {
		this.delegate = delegate;
		
	    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while (choice != 11) {
			System.out.println();
			System.out.println("1. Insert performances");
			System.out.println("2. Delete performances");
			System.out.println("3. Update performances");
			System.out.println("4. Show performances");
			System.out.println("5. Find avg num of performers per venues (nested aggregation)");
			System.out.println("6. Find videographers who filmed all performances (division)");
			System.out.println("7. Show Tickets");
			System.out.println("8. Show Venues");
			System.out.println("9. Show Videographers");
			System.out.println("10. Show Film (Videographers and Performances relation)");
			System.out.println("11. Quit");
			System.out.println("12. Find performances on a specific date (join)");
			System.out.println("13. Show number of performances on each date (group by)");
			System.out.println("14. Show dates and number of performers where 100 or more people performed (having)");
			System.out.println("15: Show select columns from a table (projection)");
			System.out.println("16: Show performances based on custom filters (selection)");
			System.out.println("17: Show names of all tables (projection front end helper function)");
			System.out.println("18: Get names of all columns of a table (projection front end helper function)");
			System.out.print("Please choose one of the above 18 options: ");

			choice = readInteger(false);

			System.out.println(" ");

			if (choice != INVALID_INPUT) {
				switch (choice) {
				case 1:  
					handleInsertOption(); 
					break;
				case 2:  
					handleDeleteOption(); 
					break;
				case 3: 
					handleUpdateOption();
					break;
				case 4:  
					delegate.showPerformances();
					break;
				case 5:
					delegate.showNestedAggregation();
					break;
				case 6:
					delegate.showDivision();
					break;
				case 7:
					delegate.showTickets();
					break;
				case 8:
					delegate.showVenues();
					break;
				case 9:
					delegate.showVideographers();
					break;
				case 10:
					delegate.showFilm();
					break;
				case 11:
					handleQuitOption();
					break;
				case 12:
					handleJoinOption();
					break;
				case 13:
					delegate.showGroupBy();
					break;
				case 14:
					delegate.showHaving();
					break;
				case 15:
					handleProjectionOption();
					break;
				case 16:
					handleSelectionOption();
					break;
				case 17:
					delegate.showTableNames();
					break;
				case 18:
					handleGetColumnNames();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
					break;
				}
			}
		}		
	}
	
	private void handleDeleteOption() {
		String sName = null;
		while(sName == null || sName.length() <= 0) {
			System.out.print("Please enter the show name you wish to delete: ");
			sName = readLine().trim();
		}
		delegate.deletePerformances(sName);
	}
	
	private void handleInsertOption() {
        // TODO: make sure the user values are sanitized when implementing the GUI
		String sName = null;
		while (sName == null || sName.length() <= 0) {
			System.out.print("Please enter the performances name you wish to insert: ");
			sName = readLine().trim();
		}

		String year = null;
		String month = null;
		String day = null;

		// TODO: set the GUI so that the users can only select reasonable dates
		while (year == null) {
			System.out.print("Please enter the year of the show you wish to update eg '23': ");
			year = readIntegerTwoDigitAsLine(false);
		}
		while (month == null) {
			System.out.print("Please enter the month of the show you wish to insert eg '02': ");
			month = readIntegerTwoDigitAsLine(false);
		}
		while (day == null) {
			System.out.print("Please enter the day of the show you wish to insert eg '30': ");
			day = readIntegerTwoDigitAsLine(false);
		}
		String date = "20"+ year + "-" + month + "-" + day;
		Date sDate = Date.valueOf(date);

		int sTime = INVALID_INPUT;
		while (sTime == INVALID_INPUT) {
			System.out.print("Please enter the performances time you wish to insert: ");
			sTime = readInteger(false);
		}

//		TODO: need to display venues and have the users choose one of them
		VenuesModel[] models = vHandler.getVenuesInfo();

		for (int i = 0; i < models.length; i++) {
			VenuesModel model = models[i];
			// simplified output formatting; truncation may occur
			int index = i+1;
			System.out.printf("%-4.4s", index + ")");
			System.out.printf("%-50.50s", model.getvAddress());
			System.out.printf("%-30.30s", model.getvName());
			if (model.getOccupancy() == 0) {
				System.out.printf("%-5.5s", " ");
			} else {
				System.out.printf("%-5.5s", model.getOccupancy());
			}
			System.out.println();
		}

		int selection = INVALID_INPUT;
		while (selection == INVALID_INPUT || selection < 0 || selection > models.length) {
			System.out.print("Please enter the venue number you wish to hold your performance in: ");
			selection = readInteger(false);
		}
		selection--;
		String sAddress = models[selection].getvAddress();

		// numPerformers is allowed to be null so we don't need to repeatedly ask for numPerformers
		System.out.print("Please enter the number of performers you wish to insert: ");
		int numPerformers = readInteger(true);
		if (numPerformers < 0) {
			numPerformers = NULL;
		}

		// conductor is allowed to be null so we don't need to repeatedly ask for the address
		System.out.print("Please enter the name of conductor you wish to insert: ");
		String conductor = readLine().trim();
		if (conductor.length() == 0) {
			conductor = null;
		}

		// composer is allowed to be null so we don't need to repeatedly ask for the address
		System.out.print("Please enter the name of composer you wish to insert: ");
		String composer = readLine().trim();
		if (composer.length() == 0) {
			composer = null;
		}

		PerformancesModel model = new PerformancesModel(showidCounter,
											sName,
											sDate,
											sTime,
											sAddress,
											numPerformers,
											conductor,
											composer);
		showidCounter++;
		delegate.insertPerformances(model);
	}
	
	private void handleQuitOption() {
		System.out.println("Good Bye!");
		
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("IOException!");
			}
		}
		
		delegate.terminalTransactionsFinished();
	}
	
	private void handleUpdateOption() {
		PerformancesModel[] models = pHandler.getPerformancesInfo();

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

		int selection1 = INVALID_INPUT;
		while (selection1 < 0 || selection1 > models.length) {
			System.out.print("Please enter the performance number you wish to update: ");
			selection1 = readInteger(false);
		}
		selection1--;

		// initialize all the values with the previous model elements
		int showid = models[selection1].getshowid();
		String sName = models[selection1].getsName();
		Date sDate = models[selection1].getsDate();
		int sTime = models[selection1].getsTime();
		String sAddress = models[selection1].getsAddress();
		int numPerformers  = models[selection1].getNumPerformers();
		String conductor = models[selection1].getConductor();
		String composer = models[selection1].getComposer();

		// TODO: make sure the user values are sanitized when implementing the GUI

		// update performances name
		String yorn = null;
		while (yorn == null || (!yorn.equals("y") && !yorn.equals("n"))) {
			System.out.print("Would you like to update the performances name? Write y/n");
			yorn = readLine().trim();
		}

		if (yorn.equals("y")) {
			String sName1 = null;
			while (sName1 == null || sName1.length() <= 0) {
				System.out.print("Please enter the performances name you wish to update: ");
				sName1 = readLine().trim();
			}
			sName = sName1;
		}

		// update performances  date
		yorn = null;

		while (yorn == null || (!yorn.equals("y") && !yorn.equals("n"))) {
			System.out.print("Would you like to update the performances date? Write y/n");
			yorn = readLine().trim();
		}

		if (yorn.equals("y")) {
			String year = null;
			String month = null;
			String day = null;

			// TODO: set the GUI so that the users can only select reasonable dates
			while (year == null) {
				System.out.print("Please enter the year of the show you wish to update eg '23': ");
				year = readIntegerTwoDigitAsLine(false);
			}
			while (month == null) {
				System.out.print("Please enter the month of the show you wish to insert eg '02': ");
				month = readIntegerTwoDigitAsLine(false);
			}
			while (day == null) {
				System.out.print("Please enter the day of the show you wish to insert eg '30': ");
				day = readIntegerTwoDigitAsLine(false);
			}
			String date = "20"+ year + "-" + month + "-" + day;
			sDate = Date.valueOf(date);
		}


		// update performances times
		yorn = null;
		while (yorn == null || (!yorn.equals("y") && !yorn.equals("n"))) {
			System.out.print("Would you like to update the performances time? Write y/n");
			yorn = readLine().trim();
		}

		if (yorn.equals("y")) {
			int sTime1 = INVALID_INPUT;
			while (sTime1 == INVALID_INPUT) {
				System.out.print("Please enter the performances time you wish to insert: ");
				sTime1 = readInteger(false);
			}
			sTime = sTime1;
		}

		// update performances venue
		yorn = null;
		while (yorn == null || (!yorn.equals("y") && !yorn.equals("n"))) {
			System.out.print("Would you like to update the performances venue? Write y/n");
			yorn = readLine().trim();
		}

		if (yorn.equals("y")) {
			VenuesModel[] vmodels = vHandler.getVenuesInfo();

			for (int i = 0; i < vmodels.length; i++) {
				VenuesModel vmodel = vmodels[i];
				// simplified output formatting; truncation may occur
				int index = i+1;
				System.out.printf("%-4.4s", index + ")");
				System.out.printf("%-50.50s", vmodel.getvAddress());
				System.out.printf("%-30.30s", vmodel.getvName());
				if (vmodel.getOccupancy() == 0) {
					System.out.printf("%-5.5s", " ");
				} else {
					System.out.printf("%-5.5s", vmodel.getOccupancy());
				}
				System.out.println();
			}

			int selection = INVALID_INPUT;
			while (selection < 0 || selection > models.length) {
				System.out.print("Please enter the venue number you wish to hold your performance in: ");
				selection = readInteger(false);
			}
			selection--;
			sAddress = vmodels[selection].getvAddress();
		}

		// update number of performers
		yorn = null;
		while (yorn == null || (!yorn.equals("y") && !yorn.equals("n"))) {
			System.out.print("Would you like to update the number of performers? Write y/n");
			yorn = readLine().trim();
		}

		if (yorn.equals("y")) {
			// numPerformers is allowed to be null so we don't need to repeatedly ask for numPerformers
			System.out.print("Please enter the number of performers you wish to insert: ");
			int numPerformers1 = readInteger(true);
			if (numPerformers1 < 0) {
				numPerformers1 = NULL;
			}
			numPerformers = numPerformers1;
		}

		// update name of conductor
		yorn = null;
		while (yorn == null || (!yorn.equals("y") && !yorn.equals("n"))) {
			System.out.print("Would you like to update the name of conductor? Write y/n");
			yorn = readLine().trim();
		}

		if (yorn.equals("y")) {
			// conductor is allowed to be null so we don't need to repeatedly ask for numPerformers
			System.out.print("Please enter the name of conductor you wish to insert: ");
			String conductor1 = readLine().trim();
			if (conductor1.isEmpty()) {
				conductor1 = null;
			}
			conductor = conductor1;
		}

		// update name of composer
		yorn = null;
		while (yorn == null || (!yorn.equals("y") && !yorn.equals("n"))) {
			System.out.print("Would you like to update the name of composer? Write y/n");
			yorn = readLine().trim();
		}

		if (yorn.equals("y")) {
			// composer is allowed to be null so we don't need to repeatedly ask for the address
			System.out.print("Please enter the name of composer you wish to insert: ");
			String composer1 = readLine().trim();
			if (composer1.isEmpty()) {
				composer1 = null;
			}
			composer = composer1;
		}

		delegate.updatePerformances(showid, sName, sDate, sTime, sAddress, numPerformers, conductor, composer);
	}

	private void handleJoinOption() {
		String date = null;
		while(date == null || date.length() <= 0) {
			System.out.print("Please enter the date of which you would like to search for performances eg '2022-03-27': ");
			date = readLine().trim();
		}

		delegate.showJoin(date);
	}

	private void handleProjectionOption() {
		String table = null;

		while(table == null || table.length() <= 0) {
			System.out.print("Please enter the name of the table you would like to view columns from eg 'Performances': ");
			table = readLine().trim();
		}

		String columns = null;

		while(columns == null || columns.length() <= 0) {
			System.out.print("Please enter the names of the columns you would like to view eg 'showid, sName, sDate': ");
			columns = readLine().trim();
		}


		delegate.showProjection(table, columns);
	}

	private void handleSelectionOption() {
		ArrayList<String> attributes = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> connectors = new ArrayList<String>();

		boolean first = true;

		String yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by showid? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where showid is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("showid");
			values.add(ans);
			first = false;
		}

		yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by sName? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where sName is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("sName");
			values.add(ans);
			first = false;
		}

		yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by sDate? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where sDate is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("sDate");
			values.add(ans);
			first = false;
		}

		yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by sTime? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where sTime is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("sTime");
			values.add(ans);
			first = false;
		}

		yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by sAddress? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where sAddress is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("sAddress");
			values.add(ans);
			first = false;
		}

		yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by numPerformers? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where numPerformers is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("numPerformers");
			values.add(ans);
			first = false;
		}

		yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by conductor? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where conductor is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("conductor");
			values.add(ans);
			first = false;
		}

		yn = null;
		while(yn == null || yn.length() <= 0) {
			System.out.print("Would you like to filter by composer? y/n: ");
			yn = readLine().trim();
		}
		if (yn.equals("y")) {
			if (!first) {
				String connector = null;
				while(connector == null || connector.length() <= 0) {
					System.out.print("How would you like to connect this condition to the previous on? AND/OR: ");
					connector = readLine().trim();
				}
				connectors.add(connector);
			}
			String ans = null;
			while(ans == null || ans.length() <= 0) {
				System.out.print("Show only performances where composer is equal to what: ");
				ans = readLine().trim();
			}
			attributes.add("composer");
			values.add(ans);
			first = false;
		}

		connectors.add("Spacer");
		delegate.showSelection(attributes, values, connectors);
	}

	private void handleGetColumnNames() {
		String tableName = null;

		while(tableName == null || tableName.length() <= 0) {
			System.out.print("Please enter the name of the table you would like to view columns names of: ");
			tableName = readLine().trim();
		}


		delegate.showColumnNames(tableName);
	}
	
	private int readInteger(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}

	private String readIntegerTwoDigitAsLine(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else if (line.length() != 2) {
				System.out.println(WARNING_TAG + " Your input is more than two digits long");
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return line;
	}
	
	private String readLine() {
		String result = null;
		try {
			result = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}
}
