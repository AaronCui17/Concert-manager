package ca.ubc.cs304.database;

import ca.ubc.cs304.model.PerformancesModel;
import ca.ubc.cs304.model.TicketsModel;
import ca.ubc.cs304.util.PrintablePreparedStatement;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class TicketsHandler {

    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection;

    public TicketsHandler(DatabaseConnectionHandler dbHandler) {
        connection = dbHandler.connection;
    }
    public void deleteTickets(int seatNum, String tRow, String sName) {
        try {
            String query = "DELETE FROM Tickets WHERE seatNum = ? AND tRow = ? AND sName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, seatNum);
            ps.setString(2, tRow);
            ps.setString(3, sName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Tickets " + tRow + "-" + seatNum + " does not exist for " + sName);
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertTickets(TicketsModel model) {
        try {
            String query = "INSERT INTO Tickets VALUES (?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getSeatNum());
            ps.setString(2, model.gettRow());
            if (model.gettType() == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, model.gettType());
            }
            ps.setInt(4, model.getshowid());
            ps.setString(5, model.getEmail());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public TicketsModel[] getTicketsInfo() {
        ArrayList<TicketsModel> result = new ArrayList<TicketsModel>();

        try {
            String query = "SELECT * FROM Tickets";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                TicketsModel model = new TicketsModel(
                        rs.getInt("seatNum"),
                        rs.getString("tRow"),
                        rs.getString("tType"),
                        rs.getInt("showid"),
                        rs.getString("email"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new TicketsModel[result.size()]);
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
    public void databaseSetup() {
//        dropTicketsTableIfExists();

        try {
            String query = "CREATE TABLE Tickets\n" +
                    "                    (seatNum INTEGER,\n" +
                    "                            tRow CHAR(2),\n" +
                    "                            tType VARCHAR(255),\n" +
                    "                            showid INTEGER,\n" +
                    "                            email VARCHAR(255),\n" +
                    "                            PRIMARY KEY (seatNum, tRow, showid),\n" +
                    "                            FOREIGN KEY (showid) REFERENCES Performances(showid)\n" +
                    "                            ON DELETE CASCADE)";
            // TODO: add foreign keys here after initializing venues
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        TicketsModel tickets1 = new TicketsModel(1, "AA", "Balcony", 1,  "john@gmail.com");
        insertTickets(tickets1);

        TicketsModel tickets2 = new TicketsModel(2, "AA", "Balcony", 2, "sarah@gmail.com");;
        insertTickets(tickets2);

        TicketsModel tickets3 = new TicketsModel(3, "AA", "Balcony", 3, "michael@gmail.com");
        insertTickets(tickets3);

        TicketsModel tickets4 = new TicketsModel(4, "AA", "Balcony", 4, "emily@gmail.com");
        insertTickets(tickets4);

        TicketsModel tickets5 = new TicketsModel(5, "AA", "Balcony", 5, "david@gmail.com");
        insertTickets(tickets5);
    }

    public void dropTicketsTableIfExists() {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals("tickets")) {
                    ps.execute("DROP TABLE tickets");
                    break;
                }
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
