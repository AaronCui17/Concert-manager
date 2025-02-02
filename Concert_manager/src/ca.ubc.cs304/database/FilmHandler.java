package ca.ubc.cs304.database;

import ca.ubc.cs304.model.FilmModel;
import ca.ubc.cs304.model.VideographersModel;
import ca.ubc.cs304.util.PrintablePreparedStatement;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection;

    public FilmHandler(DatabaseConnectionHandler dbHandler) {
        connection = dbHandler.connection;
    }

    public void insertFilm(FilmModel model) {
        try {
            String query = "INSERT INTO Film VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getShowid());
            ps.setInt(2, model.getVid());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public FilmModel[] getFilmInfo() {
        ArrayList<FilmModel> result = new ArrayList<FilmModel>();

        try {
            String query = "SELECT * FROM Film";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                FilmModel model = new FilmModel(
                        rs.getInt("showid"),
                        rs.getInt("vid"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new FilmModel[result.size()]);
    }

    public VideographersModel[] getDivisonInfo() {
        ArrayList<VideographersModel> result = new ArrayList<VideographersModel>();
        try {
            String query = "SELECT v.vid AS vid, v.vName AS vName\n" +
                    "FROM Videographers v\n" +
                    "WHERE NOT EXISTS \n" +
                    "\t(( SELECT p.showid\n" +
                    "\tFROM Performances p)\n" +
                    "\tMINUS\n" +
                    "\t( SELECT f.showid\n" +
                    "\tFROM Film f\n" +
                    "WHERE f.vid = v.vid))";
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
            String query = "CREATE TABLE Film\n" +
                    "  (showid INTEGER,\n" +
                    "  vid INTEGER,\n" +
                    "  PRIMARY KEY (showid, vid),\n" +
                    "  FOREIGN KEY (showid) REFERENCES Performances(showid)\n" +
                    "    ON DELETE CASCADE,\n" +
                    "  FOREIGN KEY (vid) REFERENCES Videographers(vid)\n" +
                    "    ON DELETE CASCADE)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        FilmModel film1 = new FilmModel(1, 1);
        insertFilm(film1);

        FilmModel film2 = new FilmModel(2, 1);;
        insertFilm(film2);

        FilmModel film3 = new FilmModel(3, 1);
        insertFilm(film3);

        FilmModel film4 = new FilmModel(4, 1);
        insertFilm(film4);

        FilmModel film5 = new FilmModel(5, 1);
        insertFilm(film5);

        FilmModel film6 = new FilmModel(6, 1);
        insertFilm(film6);

        FilmModel film7 = new FilmModel(7, 1);;
        insertFilm(film7);

        FilmModel film10 = new FilmModel(3,2);
        insertFilm(film10);

        FilmModel film11 = new FilmModel(4,2);
        insertFilm(film11);

        FilmModel film12 = new FilmModel(5,2);
        insertFilm(film12);

        FilmModel film13 = new FilmModel(1,3);
        insertFilm(film13);

    }

    public void dropFilmTableIfExists() {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals("film")) {
                    ps.execute("DROP TABLE film");
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
