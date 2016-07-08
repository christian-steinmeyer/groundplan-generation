package com.favendo.steinmeyer.wavefront;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 *
 * Represents a possible type for an object represented by one line in a wavefront file.
 */
public enum WavefrontType {

    COMMENT("#"), VERTEX("v"), VERTEX_NORMAL("vn"), FACE("f");

    private String identifier;

    WavefrontType(final String identifier) {
        this.identifier = identifier;
    }

    public static WavefrontType fromString(String string) {
        switch (string) {
            case "#":
                return COMMENT;
            case "v":
                return VERTEX;
            case "vn":
                return VERTEX_NORMAL;
            case "f":
                return FACE;
            default:
                throw new IllegalArgumentException(
                        "'" + string + "' is no valid WavefrontType identifier");
        }
    }
}
