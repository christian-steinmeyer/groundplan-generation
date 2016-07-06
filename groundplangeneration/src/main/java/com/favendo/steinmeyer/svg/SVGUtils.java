package com.favendo.steinmeyer.svg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by Chrstian Steinmeyer on 04.07.2016.
 */
public class SVGUtils {

    final int STROKE_WIDTH = 2;

    String svg;

    private SVGUtils() {
        super();
    }

    private void initializeSVG(int width, int height) {
        svg = "<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n" +
                "  \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                "<svg width=\"" + width + "\" height=\"" + height + "\" version=\"1.1\"\n" +
                "     xmlns=\"http://www.w3.org/2000/svg\">\n";
    }


    public static SVGUtils newSVG(int width, int height) {
        SVGUtils result = new SVGUtils();
        result.initializeSVG(width, height);
        return result;
    }

    public SVGUtils addDescription(String description) {
        svg += "  <desc>" + description + "\n" +
                "  </desc>\n";
        return this;
    }

    public SVGUtils drawLine(float x1, float y1, float x2, float y2) {
        svg += "<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 +
                "\" stroke=\"rgb(0,0,0)\" stroke-width=\"" + STROKE_WIDTH + "\" />\n";
        return this;
    }

    public String build() {
        svg += "\n" + "</svg>";
        return svg;
    }

}
