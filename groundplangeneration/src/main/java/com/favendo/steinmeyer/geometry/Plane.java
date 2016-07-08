package com.favendo.steinmeyer.geometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 *
 * Represents a plane in 3D space. represented by a point and a normal vector.
 */
public class Plane {

    final float ACCURACY = 0.5f;

    Collection<Vector3> points = new ArrayList<>();
    Vector3 point;
    Vector3 normal;

    public Plane (Face face) {
        this.points.addAll(face.getPoints());
        point = face.getPoint();
        normal = face.getNormal();
    }

    public Collection<Vector3> getPoints() {
        return points;
    }

    public void mergeFace(Face face) {
        points.addAll(face.getPoints());
        updateNormal(face);
    }

    private void updateNormal(final Face face) {
        int numberOfFaces = points.size() / 3;
        Vector3 sum = Vector3Utils.add(face.normal, Vector3Utils.scalar(numberOfFaces, normal));
        normal = Vector3Utils.norm(Vector3Utils.scalar(1f / (numberOfFaces + 1), sum));
    }

    /**
     * @return true if and only if the given {@link Face} face is parallel to this {@link Plane} and
     * all its vertices lie in the {@link Plane}, both with an accuracy of {@link #ACCURACY}
     */
    public boolean contains(Face face) {
        return isParallelTo(face) && includes(face.getPoints());
    }

    private boolean includes(final Collection<Vector3> points) {
        for (Vector3 point : points) {
            if (!includes(point)){
                return false;
            }
        }
        return true;
    }

    private boolean includes(Vector3 v) {
        Vector3 difference = Vector3Utils.subtract(v, point);
        return Math.abs(Vector3Utils.dot(normal, difference)) < ACCURACY;
    }

    private boolean isParallelTo(Face f) {
        return Math.abs(Vector3Utils.angle(normal, f.normal)) < ACCURACY;
    }

}
