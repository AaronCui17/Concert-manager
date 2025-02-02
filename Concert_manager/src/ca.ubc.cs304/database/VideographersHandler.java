package ca.ubc.cs304.database;

import ca.ubc.cs304.model.VideographersModel;
import ca.ubc.cs304.util.PrintablePreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VideographersHandler {

    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection;

    public VideographersHandler(DatabaseConnectionHandler dbHandler) {
        connection = dbHandler.connection;
    }

    public void insertVideographers(VideographersModel model) {
        try {
            String query = "INSERT INTO Videographers VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getVid());
            ps.setString(2, model.getvName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public VideographersModel[] getVideographersInfo() {
        ArrayList<VideographersModel> result = new ArrayList<VideographersModel>();

        try {
            String query = "SELECT * FROM Videographers";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                VideographersModel model = new VideographersModel(
                        rs.getInt("vid"),
                        rs.getString("vName"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new VideographersModel[result.size()]);
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
    public void databaseSetup() {

        try {
            String query = "CREATE TABLE Videographers\n" +
                    "\t(vid INTEGER PRIMARY KEY,\n" +
                    "\tvName varchar(255) NOT NULL)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        VideographersModel Videographers1 = new VideographersModel(1, "Amelia Parker");
        insertVideographers(Videographers1);

        VideographersModel Videographers2 = new VideographersModel(2, "Benjamin Smith");;
        insertVideographers(Videographers2);

        VideographersModel Videographers3 = new VideographersModel(3, "Chloe Davis");
        insertVideographers(Videographers3);
    }

    public void dropVideographersTableIfExists() {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals("videographers")) {
                    ps.execute("DROP TABLE videographers");
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
