package ca.ubc.cs304.model;

public class VideographersModel {
    private final int vid;
    private final String vName;

    public VideographersModel(int vid, String vName) {
        this.vid = vid;
        this.vName = vName;
    }

    public int getVid() {
        return vid;
    }
    public String getvName() {
        return vName;
    }
}
