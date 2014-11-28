/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.botalgorithm;

/**
 *
 * @author Karl
 */
import java.util.ArrayList;

import java.util.Collections;

public class Vertex implements Comparable<Vertex> {

    private final String name;

    private Edge[] adjacencies;

    private double minDistance = Double.POSITIVE_INFINITY;

    private Vertex previous;

    public Vertex(String argName) {

        name = argName;

    }

    public Edge[] getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(Edge[] adjacencies) {
        this.adjacencies = adjacencies;
    }

    public double getMinDistance() {
        return minDistance;

    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public String toString() {

        return name;

    }

    public int compareTo(Vertex other) {

        return Double.compare(minDistance, other.minDistance);

    }
}
