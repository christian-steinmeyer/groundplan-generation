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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector3)) {
            return false;
        }

        Vector3 vector3 = (Vector3) o;

        if (Float.compare(vector3.getX(), getX()) != 0) {
            return false;
        }
        if (Float.compare(vector3.getY(), getY()) != 0) {
            return false;
        }
        return Float.compare(vector3.getZ(), getZ()) == 0;

    }

    @Override
    public int hashCode() {
        int result = (getX() != +0.0f ? Float.floatToIntBits(getX()) : 0);
        result = 31 * result + (getY() != +0.0f ? Float.floatToIntBits(getY()) : 0);
        result = 31 * result + (getZ() != +0.0f ? Float.floatToIntBits(getZ()) : 0);
        return result;
    }
}
