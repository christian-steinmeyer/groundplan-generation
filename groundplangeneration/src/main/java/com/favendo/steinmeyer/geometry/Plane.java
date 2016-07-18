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

    final float THRESHOLD = 6;

    Collection<Quadrant> quadrants = new ArrayList<>();

    int numberOfPoints = 0;

    Vector3 point;
    Vector3 normal;

    public Plane(Face face) {
        point = Vector3Utils.average(face.getPoints());
        for (Vector3 vector : face.getPoints()) {
            addPoint(vector);
        }
        normal = face.getNormal();
    }

    private void addPoint(final Vector3 vector) {
        Quadrant target = getQuadrantForVector(vector);
        if (target != null) {
            target.addPoint(vector);
        } else {
            quadrants.add(new Quadrant(vector));
        }
        numberOfPoints++;
    }

    private Quadrant getQuadrantForVector(final Vector3 vector) {
        Vector3 quadrantOrigin = QuadrantUtils.getSpecs(vector);
        for (Quadrant quadrant : quadrants) {
            if (quadrant.getOrigin().equals(quadrantOrigin)) {
                return quadrant;
            }
        }
        return null;
    }

    public Collection<Vector3> getPoints() {
        Collection<Vector3> points = new ArrayList<>();
        for (Quadrant quadrant : quadrants) {
            points.addAll(quadrant.getPoints());
        }
        return points;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public Vector3 getPoint() {
        return point;
    }

    public void mergeFace(Face face) {
        for (Vector3 vector : face.getPoints()) {
            addPoint(vector);
        }
        updateNormal(face);
        updatePoint(face);
    }

    private void updateNormal(final Face face) {
        int numberOfFaces = numberOfPoints / 3;
        Vector3 sum = Vector3Utils.add(face.normal, Vector3Utils.scalar(numberOfFaces, normal));
        normal = Vector3Utils.norm(Vector3Utils.scalar(1f / (numberOfFaces + 1), sum));
    }

    private void updatePoint(final Face face) {
        int numberOfFaces = numberOfPoints / 3;
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

    public void removeOutliers() {
        Collection<Quadrant> newQuadrants = new ArrayList<>();
        for (Quadrant quadrant : quadrants) {
            if (quadrant.getSize() > THRESHOLD) {
                newQuadrants.add(quadrant);
            } else {
                Collection<Quadrant> neighbors =
                        getQuadrants(QuadrantUtils.getNeighborOrigins(quadrant));
                int numberOfNeighbors = 0;
                for (Quadrant neighbor : neighbors) {
                    numberOfNeighbors += neighbor.getSize();
                }
                if (numberOfNeighbors > 3 * THRESHOLD){
                    newQuadrants.add(quadrant);
                }
            }
        }
    }

    private Collection<Quadrant> getQuadrants(final Collection<Vector3> origins) {
        Collection<Quadrant> result = new ArrayList<>();
        for (Quadrant quadrant : quadrants) {
            for (Vector3 origin : origins) {
                if (quadrant.getOrigin().equals(origin)){
                    result.add(quadrant);
                }
            }
        }
        return result;
    }

    /**
     * Since the normal vector is normalized, the distance is given by the projection of any vector
     * between the given point and a point on the plane onto the normal.
     */
    public void resetPoints(final Collection<Vector3> newPoints) {
        this.quadrants = new ArrayList<>();
        for (Vector3 point : newPoints) {
            addPoint(point);
        }
        this.point = Vector3Utils.average(newPoints);
    }

    /**
     * Since the normal vector is normalized, the distance is given by the projection of any vector
     * between the given point and a point on the plane onto the normal.
     */
    public float getDistance(final Vector3 point) {
        return Math.abs(Vector3Utils.dot(Vector3Utils.subtract(point, this.point), normal));
    }
}
