package ca.ubc.cs304.model;

import java.sql.Date;

public class VenuesModel {
    private final String vAddress;

    private final String vName;

    private final int occupancy;

    public VenuesModel(String vAddress, String vName, int occupancy) {
        this.vAddress = vAddress;
        this.vName = vName;
        this.occupancy = occupancy;
    }

    public String getvAddress() {
        return vAddress;
    }

    public String getvName() {
        return vName;
    }

    public int getOccupancy() {
        return occupancy;
    }
}
