package ca.ubc.cs304.model;

public class FilmModel {
    private final int showid;
    private final int vid;
    public FilmModel(int showid, int vid) {
        this.showid = showid;
        this.vid = vid;
    }

    public int getShowid() {
        return showid;
    }

    public int getVid() {
        return vid;
    }
}
