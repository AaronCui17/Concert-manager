package ca.ubc.cs304.model;

import java.util.ArrayList;

public class ProjectionModel {

    // One string that has all the values selected attributes separated by spaces
    private final ArrayList<String> attributes;

    public ProjectionModel(ArrayList<String> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<String> getattributes() { return attributes;}
}