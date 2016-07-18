package com.favendo.steinmeyer.geometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 *
 * Created by Christian Steinmeyer on 18.07.2016.
 */
public class QuadrantUtils {

    final static float SIDE_LENGTH_IN_CENTIMETERS = 10f;

    public static Vector3 getSpecs(Vector3 vector) {
        float x = roundToQuadrantSideLength(vector.getX());
        float y = roundToQuadrantSideLength(vector.getY());
        float z = roundToQuadrantSideLength(vector.getZ());
        return new Vector3(x, y, z);
    }

    private static float roundToQuadrantSideLength(float inMeters) {
        return Math.round(inMeters * SIDE_LENGTH_IN_CENTIMETERS) / SIDE_LENGTH_IN_CENTIMETERS;
    }

    public static Collection<Vector3> getNeighborOrigins(Quadrant quadrant) {
        Collection<Vector3> result = new ArrayList<>();
        Vector3 origin = quadrant.getOrigin();
        float delta = 1f / SIDE_LENGTH_IN_CENTIMETERS;
        result.add(new Vector3(origin.getX() + delta, origin.getY(), origin.getZ()));
        result.add(new Vector3(origin.getX() - delta, origin.getY(), origin.getZ()));
        result.add(new Vector3(origin.getX(), origin.getY() + delta, origin.getZ()));
        result.add(new Vector3(origin.getX(), origin.getY() - delta, origin.getZ()));
        result.add(new Vector3(origin.getX(), origin.getY(), origin.getZ() + delta));
        result.add(new Vector3(origin.getX(), origin.getY(), origin.getZ() - delta));
        return result;

    }

}
