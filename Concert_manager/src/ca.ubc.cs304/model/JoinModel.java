package ca.ubc.cs304.model;

public class JoinModel {
    private final String sName;
    private final int sTime;
    private final String vName;

    public JoinModel(String sName, int sTime, String vName) {
        this.sName = sName;
        this.sTime = sTime;
        this.vName = vName;
    }

    public String getsName() { return sName;}
    public int getsTime() { return sTime;}
    public String getvName() { return vName;}
}
