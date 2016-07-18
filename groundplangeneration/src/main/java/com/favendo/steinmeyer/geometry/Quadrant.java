package com.favendo.steinmeyer.geometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Representation of a small region with a set of points.
 *
 * Created by Christian Steinmeyer on 18.07.2016.
 */
public class Quadrant {

    Vector3 origin;

    private Collection<Vector3> points = new ArrayList<>();

    public Vector3 getOrigin() {
        return origin;
    }

    public Collection<Vector3> getPoints() {
        return points;
    }

    public Quadrant(Vector3 vector) {
        origin = QuadrantUtils.getSpecs(vector);
    }

    public void addPoint(Vector3 point){
        points.add(point);
    }

    public int getSize(){
        return points.size();
    }

}
