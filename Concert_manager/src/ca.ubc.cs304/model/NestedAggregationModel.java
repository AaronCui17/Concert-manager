package ca.ubc.cs304.model;

public class NestedAggregationModel {
    private final String sAddress;
    private final int avgNumPerformers;

    public NestedAggregationModel(String sAddress, int avgNumPerformers) {
        this.sAddress = sAddress;
        this.avgNumPerformers = avgNumPerformers;
    }

    public String getsAddress() { return sAddress;}
    public int getAvgNumPerformers() { return avgNumPerformers;}
}
