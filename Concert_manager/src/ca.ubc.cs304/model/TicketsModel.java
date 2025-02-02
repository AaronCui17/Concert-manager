package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single ticket
 */
public class TicketsModel {
    private final int seatNum;
    private final String tRow;
    private final String tType;
    private final int showid;
    private final String email;

    public TicketsModel(int seatNum, String tRow, String tType, int showid, String email) {
        this.seatNum = seatNum;
        this.tRow = tRow;
        this.tType = tType;
        this.showid = showid;
        this.email = email;
    }
    public int getSeatNum() {
        return seatNum;
    }
    public String gettRow() {return tRow;}
    public String gettType() {
        return tType;
    }
    public int getshowid() {
        return showid;
    }
    public String getEmail() {
        return email;
    }
}
