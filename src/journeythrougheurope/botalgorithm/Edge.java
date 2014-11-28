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
public class Edge {

    private final Vertex target;
    private final double weight;

    public Edge(Vertex argTarget, double argWeight) {

        target = argTarget;
        weight = argWeight;

    }
    
    public Vertex getTarget()
    {
        return target;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public String toString()
    {
        return target.toString();
    }

}