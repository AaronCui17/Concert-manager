package ca.ubc.cs304.database;

import ca.ubc.cs304.model.VenuesModel;
import ca.ubc.cs304.util.PrintablePreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class VenuesHandler {

    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection;

    public VenuesHandler(DatabaseConnectionHandler dbHandler) {
        connection = dbHandler.connection;
    }
    public void deleteVenues(String vAddress) {
        try {
            String query = "DELETE FROM Venues WHERE vAddress = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, vAddress);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Venue at " + vAddress + " does not exist");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertVenues(VenuesModel model) {
        try {
            String query = "INSERT INTO Venues VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getvAddress());
            ps.setString(2, model.getvName());
            if (model.getOccupancy() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, model.getOccupancy());
            }

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public VenuesModel[] getVenuesInfo() {
        ArrayList<VenuesModel> result = new ArrayList<VenuesModel>();

        try {
            String query = "SELECT * FROM Venues";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                VenuesModel model = new VenuesModel(
                        rs.getString("vAddress"),
                        rs.getString("vName"),
                        rs.getInt("occupancy"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new VenuesModel[result.size()]);
    }


    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
    public void databaseSetup() {
//        dropVenuesTableIfExists();


        try {
            String query = "CREATE TABLE Venues\n" +
                    "                    (vAddress VARCHAR(255) PRIMARY KEY,\n" +
                    "                            vName VARCHAR(255) NOT NULL,\n" +
                    "                           occupancy INTEGER)" ;

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        VenuesModel venues1 = new VenuesModel("123 Maple Street, Toronto, ON M5V 2N7", "Rogers Arena", 200);
        insertVenues(venues1);

        VenuesModel venues2 = new VenuesModel("456 Cedar Avenue, Vancouver, BC V6B 2P4", "Oracle Centre", 100);;
        insertVenues(venues2);

        VenuesModel venues3 = new VenuesModel("789 Birch Lane, Calgary, AB T2P 3H9", "TD Garden", 400);
        insertVenues(venues3);

        VenuesModel venues4 = new VenuesModel("567 Oak Road, Ottawa, ON K1A 0G9", "Scotiabank Arena", 50);
        insertVenues(venues4);

        VenuesModel venues5 = new VenuesModel("321 Pine Drive, Montreal, QC H2Z 1J4", "The Sphere", 250);
        insertVenues(venues5);
    }

    public void dropVenuesTableIfExists() {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals("venues")) {
                    ps.execute("DROP TABLE venues");
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
