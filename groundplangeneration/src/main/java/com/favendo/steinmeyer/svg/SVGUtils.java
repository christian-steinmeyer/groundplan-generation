package com.favendo.steinmeyer.svg;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Chrstian Steinmeyer on 04.07.2016.
 */
public class SVGUtils {

    final int STROKE_WIDTH = 2;

    String svg;
    String description;
    String unit;
    float width;
    float height;
    Collection<Line> lines = new ArrayList<>();

    private SVGUtils() {
        super();
    }

    private void initializeSVG(String unit, String description) {
        this.unit = unit;
    }


    public static SVGUtils newSVG(String unit, String description) {
        SVGUtils result = new SVGUtils();
        result.initializeSVG(unit, description);
        return result;
    }

    public SVGUtils addLine(float x1, float y1, float x2, float y2) {
        lines.add(new Line(x1, y1, x2, y2));
        return this;
    }

    private void drawLine(Line line) {
        float xOffset = height / 2;
        float yOffset = width / 2;
        float x1 = line.x1 + xOffset;
        float x2 = line.x2 + xOffset;
        float y1 = line.y1 + yOffset;
        float y2 = line.y2 + yOffset;
        svg += "\t<line x1=\"" + x1 + unit + "\" y1=\"" + y1 + unit + "\" x2=\"" + x2 + unit +
                "\" y2=\"" + y2 + unit + "\" stroke=\"rgb(0,0,0)\" stroke-width=\"" + STROKE_WIDTH +
                "\" />\n";
    }

    public String build() {
        determineWidth();
        determineHeight();
        svg = "<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n" +
                "\t\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                "<svg width=\"" + width + unit + "\" height=\"" + height + unit +
                "\" version=\"1.1\"\n" + "\txmlns=\"http://www.w3.org/2000/svg\">\n";
        svg += "\t<desc>\n\t\t" + description + "\n" +
                "\t</desc>\n";
        for (Line line : lines) {
            drawLine(line);
        }
        svg += "</svg>";
        return svg;
    }

    private void determineHeight() {
        float max = Integer.MIN_VALUE;
        float min = Integer.MAX_VALUE;
        for (Line line : lines) {
            max = Math.max(max, Math.max(line.x1, line.x2));
            min = Math.min(min, Math.max(line.x1, line.x2));
        }
        height = Math.abs(max - min);
    }

    private void determineWidth() {
        float max = Integer.MIN_VALUE;
        float min = Integer.MAX_VALUE;
        for (Line line : lines) {
            max = Math.max(max, Math.max(line.y1, line.y2));
            min = Math.min(min, Math.max(line.y1, line.y2));
        }
        width = Math.abs(max - min);
    }

}
