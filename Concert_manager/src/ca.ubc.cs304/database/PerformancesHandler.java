package ca.ubc.cs304.database;

import ca.ubc.cs304.model.*;
import ca.ubc.cs304.util.PrintablePreparedStatement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class PerformancesHandler {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection;

    public PerformancesHandler(DatabaseConnectionHandler dbHandler) {
        connection = dbHandler.connection;
    }
    public void deletePerformances(String name) {
        try {
            String query = "DELETE FROM Performances WHERE sName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Performances " + name + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertPerformances(PerformancesModel model) {
        try {
            String query = "INSERT INTO performances VALUES (?,?,?,?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getshowid());
            ps.setString(2, model.getsName());
            ps.setDate(3, model.getsDate());
            ps.setInt(4, model.getsTime());
            ps.setString(5, model.getsAddress());
            if (model.getNumPerformers() == 0) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, model.getNumPerformers());
            }
            ps.setString(7, model.getConductor());
            ps.setString(8, model.getComposer());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public PerformancesModel[] getPerformancesInfo() {
        ArrayList<PerformancesModel> result = new ArrayList<PerformancesModel>();

        try {
            String query = "SELECT * FROM performances";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                PerformancesModel model = new PerformancesModel(
                        rs.getInt("showid"),
                        rs.getString("sName"),
                        rs.getDate("sDate"),
                        rs.getInt("sTime"),
                        rs.getString("sAddress"),
                        rs.getInt("numPerformers"),
                        rs.getString("conductor"),
                        rs.getString("composer"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new PerformancesModel[result.size()]);
    }

    public NestedAggregationModel[] getNestedAggregationInfo() {
        ArrayList<NestedAggregationModel> result = new ArrayList<NestedAggregationModel>();
        try {
            String query = "SELECT p.sAddress AS sAddress, AVG(p.numPerformers) AS avgNumPerformers\n" +
                    "FROM Performances p\n" +
                    "GROUP BY p.sAddress\n" +
                    "HAVING 1 < (SELECT COUNT (*)\n" +
                    "\t\t FROM Performances p2\n" +
                    "WHERE p.sAddress = p2.sAddress)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                NestedAggregationModel model = new NestedAggregationModel(
                        rs.getString("sAddress"),
                        rs.getInt("avgNumPerformers"));

                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new NestedAggregationModel[result.size()]);
    }

    public void updatePerformances(int showid, String sName, Date sDate, int sTime, String sAddress, int numPerformers,
                                   String conductor, String composer) {
        try {
            String query = "UPDATE performances SET sName = ?, sDate = ?, sTime = ?, sAddress = ?, numPerformers = ?," +
                    "conductor = ?, composer = ? WHERE showid = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, sName);
            ps.setDate(2, sDate);
            ps.setInt(3, sTime);
            ps.setString(4, sAddress);
            ps.setInt(5, numPerformers);
            ps.setString(6, conductor);
            ps.setString(7, composer);
            ps.setInt(8, showid);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Performance " + showid + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public JoinModel[] getJoinInfo(String date) {
        ArrayList<JoinModel> result = new ArrayList<JoinModel>();

        try {
            String query = "SELECT p.sName, p.sTime, v.vName\n" +
                    "FROM Performances p, Venues v\n" +
                    "WHERE p.sAddress = v.vAddress AND p.sDate = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                JoinModel model = new JoinModel(
                        rs.getString("sName"),
                        rs.getInt("sTime"),
                        rs.getString("vName"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new JoinModel[result.size()]);
    }

    public GroupByModel[] getGroupByInfo() {
        ArrayList<GroupByModel> result = new ArrayList<GroupByModel>();

        try {
            String query = "SELECT p.sDate, COUNT(*) AS sCount\n" +
                    "FROM Performances p\n" +
                    "GROUP BY p.sDate\n" +
                    "ORDER BY p.sDate";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                GroupByModel model = new GroupByModel(
                        rs.getDate("sDate"),
                        rs.getInt("sCount"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupByModel[result.size()]);
    }

    public GroupByModel[] getHavingInfo() {
        ArrayList<GroupByModel> result = new ArrayList<GroupByModel>();

        try {
            String query = "SELECT p.sDate, SUM(p.numPerformers) AS pCount\n" +
                    "FROM Performances p\n" +
                    "GROUP BY p.sDate\n" +
                    "HAVING SUM(p.numPerformers) >= 100\n" +
                    "ORDER BY p.sDate";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                GroupByModel model = new GroupByModel(
                        rs.getDate("sDate"),
                        rs.getInt("pCount"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupByModel[result.size()]);
    }

    public ProjectionModel[] getProjectionInfo(String table, String columns) {
        ArrayList<ProjectionModel> result = new ArrayList<ProjectionModel>();

        try {
            String query = "SELECT " + columns + "\n" +
                    "FROM " + table;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            String[] attribute_list = columns.split(", ");
            while(rs.next()) {
                ArrayList<String> word_list = new ArrayList<String>();

                for (int i = 0; i < attribute_list.length; i++) {
                    String value = rs.getString(attribute_list[i]);
                    word_list.add(value);
                }

                ProjectionModel model = new ProjectionModel(word_list);
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ProjectionModel[result.size()]);
    }

    public PerformancesModel[] getSelectionInfo(ArrayList<String> attributes, ArrayList<String> values, ArrayList<String> connectors) {
        ArrayList<PerformancesModel> result = new ArrayList<PerformancesModel>();

        ArrayList<String> string_attributes = new ArrayList<String>();
        string_attributes.add("sName");
        string_attributes.add("sDate");
        string_attributes.add("sAddress");
        string_attributes.add("conductor");
        string_attributes.add("composer");

        ArrayList<String> conditions_list = new ArrayList<String>();
        for (int i = 0; i < attributes.size(); i++) {
            String attribute = attributes.get(i);

            conditions_list.add(attributes.get(i));
            conditions_list.add(" = ");
            if (string_attributes.contains(attribute)) {
                conditions_list.add("'");
            }
            conditions_list.add(values.get(i));
            if (string_attributes.contains(attribute)) {
                conditions_list.add("'");
            }
            conditions_list.add(" " + connectors.get(i) + " ");
        }
        conditions_list.remove(conditions_list.size()-1);

        String conditions = String.join("", conditions_list);

        try {
            String query = "SELECT *\n" +
                    "FROM performances\n" +
                    "WHERE " + conditions;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                PerformancesModel model = new PerformancesModel(
                        rs.getInt("showid"),
                        rs.getString("sName"),
                        rs.getDate("sDate"),
                        rs.getInt("sTime"),
                        rs.getString("sAddress"),
                        rs.getInt("numPerformers"),
                        rs.getString("conductor"),
                        rs.getString("composer"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new PerformancesModel[result.size()]);
    }

    public namesModel[] getTableNames() {
        ArrayList<namesModel> result = new ArrayList<namesModel>();

        try {
            String query = "SELECT table_name FROM user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                namesModel model = new namesModel(
                        rs.getString("table_name"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new namesModel[result.size()]);
    }

    public namesModel[] getColumnNames(String tableName) {
        ArrayList<namesModel> result = new ArrayList<namesModel>();

        try {
            String query = "SELECT column_name\n" +
                    "FROM user_tab_columns\n" +
                    "WHERE table_name = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, tableName);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                namesModel model = new namesModel(
                        rs.getString("column_name"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new namesModel[result.size()]);
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
    public void databaseSetup() {
//        dropPerformancesTableIfExists();

        try {
            String query = "CREATE TABLE Performances\n" +
                    "                    (showid INTEGER PRIMARY KEY,\n" +
                    "                         sName VARCHAR(255) NOT NULL,\n" +
                    "                            sDate DATE,\n" +
                    "                            sTime INTEGER,\n" +
                    "                            sAddress VARCHAR(255) NOT NULL,\n" +
                    "                            numPerformers INTEGER,\n" +
                    "                            conductor VARCHAR(255),\n" +
                    "                            composer VARCHAR(255),\n" +
                    "  FOREIGN KEY (sAddress) REFERENCES Venues(vAddress))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        PerformancesModel performances1 = new PerformancesModel(1, "Moonlight Sonata", Date.valueOf("2023-10-23"),
                1930, "123 Maple Street, Toronto, ON M5V 2N7", 1, "John Doe", null);
        insertPerformances(performances1);

        PerformancesModel performances2 = new PerformancesModel(2, "Rhythmic Fusion", Date.valueOf("2023-11-12") ,
                2000, "456 Cedar Avenue, Vancouver, BC V6B 2P4", 1, "Michelle Jeans", null);
        insertPerformances(performances2);

        PerformancesModel performances3 = new PerformancesModel(3, "Jazz Vibes Extravaganza", Date.valueOf("2023-12-05")  ,
                1845, "789 Birch Lane, Calgary, AB T2P 3H9", 1, "Sophia Anderson", null);
        insertPerformances(performances3);

        PerformancesModel performances4 = new PerformancesModel(4, "HarmonyFest2023", Date.valueOf("2023-01-18")  ,
                1400, "567 Oak Road, Ottawa, ON K1A 0G9", NULL, null, "Raymond Johnson");
        insertPerformances(performances4);

        PerformancesModel performances5 = new PerformancesModel(5, "Rock Legends Reunion", Date.valueOf("2023-01-18")  ,
                2315, "567 Oak Road, Ottawa, ON K1A 0G9", 50, "Carol Brooks", null);
        insertPerformances(performances5);

        // model to test nested aggregation (find avg num of performers per venue)
        PerformancesModel performances6 = new PerformancesModel(6, "Rock Legends Reunion", Date.valueOf("2023-01-18")  ,
                2315, "567 Oak Road, Ottawa, ON K1A 0G9", 100, "Carol Brooks", null);
        insertPerformances(performances6);

        PerformancesModel performances7 = new PerformancesModel(7, "Jazz Vibes Extravaganza", Date.valueOf("2023-12-05")  ,
                1845, "789 Birch Lane, Calgary, AB T2P 3H9", 250, "Sophia Anderson", null);
        insertPerformances(performances7);

    }

    public void dropPerformancesTableIfExists() {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if(rs.getString(1).toLowerCase().equals("performances")) {
                    ps.execute("DROP TABLE performances");
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
