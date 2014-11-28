/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.botalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author Karl
 */
public class Dijkstra {

    public static void computePaths(Vertex source) {

        source.setMinDistance(0);
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();

        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {

            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.getAdjacencies()) {

                Vertex v = e.getTarget();
                double weight = e.getWeight();

                double distanceThroughU = u.getMinDistance() + weight;
                if (distanceThroughU < v.getMinDistance()) {
                    vertexQueue.remove(v);
                    v.setMinDistance(distanceThroughU);
                    v.setPrevious(u);

                    vertexQueue.add(v);
                }

            }

        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {

        List<Vertex> path = new ArrayList<Vertex>();

        for (Vertex vertex = target; vertex != null; vertex = vertex.getPrevious()) {
            path.add(vertex);
        }

        Collections.reverse(path);

        return path;

    }
}

