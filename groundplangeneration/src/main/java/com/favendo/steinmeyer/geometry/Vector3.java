package com.favendo.steinmeyer.geometry;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 * <p>
 * A very slim class for a Vector with 3 dimensions. All methods for handling these are offered by
 * {@link Vector3Utils}.
 */
public class Vector3 {

    float x;
    float y;
    float z;

    public Vector3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
