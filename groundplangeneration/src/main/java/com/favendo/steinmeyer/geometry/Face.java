package com.favendo.steinmeyer.geometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 *
 * Represents a face in a wavefront object file. Consists of 3 points and a normal vector.
 */
public class Face {

    Collection<Vector3> points = new ArrayList<>();
    Vector3 point;
    Vector3 normal;

    float distanceToOrigin;

    public Face(Vertex v1, Vertex v2, Vertex v3) {
        Vector3 a = Vector3Utils.subtract(v2, v1);
        Vector3 b = Vector3Utils.subtract(v3, v1);
        normal = Vector3Utils.norm(Vector3Utils.cross(a, b));
        point = v1;
        points.add(v1);
        points.add(v2);
        points.add(v3);
        distanceToOrigin = Vector3Utils.dot(point, normal);
    }

    public Collection<Vector3> getPoints() {
        return points;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public Vector3 getPoint() {
        return point;
    }

    float getDistanceToOrigin() {
        return distanceToOrigin;
    }

}
