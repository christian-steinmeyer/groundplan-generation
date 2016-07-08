package com.favendo.steinmeyer.wavefront;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 *
 * Represents a format error within the processing of wavefront files.
 */
public class WavefrontFormatException extends Exception {

    public WavefrontFormatException(final String message) {
        super(message);
    }
}
