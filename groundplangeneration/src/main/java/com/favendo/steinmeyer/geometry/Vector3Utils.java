package com.favendo.steinmeyer.geometry;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 *
 * Offers methods for handling {@link Vector3}s.
 */
public class Vector3Utils {

    public static Vector3 add(Vector3 v, Vector3 o){
        float newX = v.x + o.x;
        float newY = v.y + o.y;
        float newZ = v.z + o.z;
        return new Vector3(newX, newY, newZ);
    }

    /**
     * @return v - o
     */
    public static Vector3 subtract(Vector3 v, Vector3 o){
        float newX = v.x - o.x;
        float newY = v.y - o.y;
        float newZ = v.z - o.z;
        return new Vector3(newX, newY, newZ);
    }

    public static Vector3 cross(Vector3 v, Vector3 o){
        float newX = v.y * o.z - v.z * o.y;
        float newY = v.z * o.x - v.x * o.z;
        float newZ = v.x * o.y - v.y * o.x;
        return new Vector3(newX, newY, newZ);
    }

    public static Vector3 scalar(float f, Vector3 v){
        float newX = f * v.x;
        float newY = f * v.y;
        float newZ = f * v.z;
        return new Vector3(newX, newY, newZ);
    }

    public static float dot(Vector3 v, Vector3 o){
        float x = v.x * o.x;
        float y = v.y * o.y;
        float z = v.z * o.z;
        return x + y + z;
    }

    public static double length(Vector3 v){
        return Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    public static Vector3 norm(Vector3 v){
        float newX = v.x / (float) length(v);
        float newY = v.y / (float) length(v);
        float newZ = v.z / (float) length(v);
        return new Vector3(newX, newY, newZ);
    }

    public static float angle(Vector3 v, Vector3 o){
        return (float) Math.acos(dot(norm(v), norm(o)));
    }
}
