package ca.ubc.cs304.model;

import java.sql.Date;

public class GroupByModel {
    private final Date sDate;
    private final int count;

    public GroupByModel(Date sDate, int count) {
        this.sDate = sDate;
        this.count = count;
    }

    public Date getsDate() { return sDate;}
    public int getcount() { return count;}
}
