package com.favendo.steinmeyer.geometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 * <p>
 * Represents a plane in 3D space. represented by a point and a normal vector.
 */
public class Plane {

    final float ACCURACY = 0.5f; // radians

    Collection<Vector3> points = new ArrayList<>();
    Vector3 point;
    Vector3 normal;

    public Plane(Face face) {
        this.points.addAll(face.getPoints());
        point = Vector3Utils.average(face.getPoints());
        normal = face.getNormal();
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

    public void mergeFace(Face face) {
        points.addAll(face.getPoints());
        updateNormal(face);
        updatePoint(face);
    }

    private void updateNormal(final Face face) {
        int numberOfFaces = points.size() / 3;
        Vector3 sum = Vector3Utils.add(face.normal, Vector3Utils.scalar(numberOfFaces, normal));
        normal = Vector3Utils.norm(Vector3Utils.scalar(1f / (numberOfFaces + 1), sum));
    }

    private void updatePoint(final Face face) {
        int numberOfFaces = points.size() / 3;
        Vector3 sum = Vector3Utils.add(Vector3Utils.average(face.getPoints()),
                Vector3Utils.scalar(numberOfFaces, point));
        point = Vector3Utils.scalar(1f / (numberOfFaces + 1), sum);
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
            if (!includes(point)) {
                return false;
            }
        }
        return true;
    }

    public boolean includes(Vector3 v) {
        Vector3 difference = Vector3Utils.subtract(v, point);
        return Math.abs(Vector3Utils.dot(normal, difference)) < ACCURACY;
    }

    private boolean isParallelTo(Face f) {
        return Math.abs(Vector3Utils.angle(normal, f.normal)) < ACCURACY;
    }

    /**
     * Since the normal vector is normalized, the distance is given by the projection of any vector
     * between the given point and a point on the plane onto the normal.
     */
    public void resetPoints(final Collection<Vector3> newPoints) {
        this.points = new ArrayList<>(newPoints);
        this.point = Vector3Utils.average(points);
    }

    /**
     * Since the normal vector is normalized, the distance is given by the projection of any vector
     * between the given point and a point on the plane onto the normal.
     */
    public float getDistance(final Vector3 point) {
        return Math.abs(Vector3Utils.dot(Vector3Utils.subtract(point, this.point), normal));
    }
}
