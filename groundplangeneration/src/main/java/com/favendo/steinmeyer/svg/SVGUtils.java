package com.favendo.steinmeyer.svg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by Chrstian Steinmeyer on 04.07.2016.
 */
public class SVGUtils {

    private Logger log = Logger.getLogger(this.getClass().getName());


    final int STROKE_WIDTH = 2;

    String svg;
    String description;
    String unit;
    float width;
    float height;
    float xOffset;
    float yOffset;
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
        float x1 = line.x1 + xOffset;
        float x2 = line.x2 + xOffset;
        // svg's origin is top left
        float y1 = height - (line.y1 + yOffset);
        float y2 = height - (line.y2 + yOffset);

        svg += "\t<line x1=\"" + x1 + unit + "\" y1=\"" + y1 + unit + "\" x2=\"" + x2 + unit +
                "\" y2=\"" + y2 + unit + "\" stroke=\"rgb(0,0,0)\" stroke-width=\"" + STROKE_WIDTH +
                "\" />\n";

        float centerX = (x1 + x2) / 2;
        float centerY = (y1 + y2) / 2;
        float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        String lengthString = String.format(Locale.ENGLISH, "%.2f m", length);

        svg += "\t<text x=\"" + centerX + unit + "\" y=\"" + centerY + unit +
                "\" fill=\"rgb(0,0,0)\">" + lengthString + "</text>\n";
    }

    public String build() {
        determineSVGDimensions();
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

    private void determineSVGDimensions() {
        float maxY = Integer.MIN_VALUE;
        float minY = Integer.MAX_VALUE;
        float maxX = Integer.MIN_VALUE;
        float minX = Integer.MAX_VALUE;
        for (Line line : lines) {
            maxX = Math.max(maxX, Math.max(line.x1, line.x2));
            minX = Math.min(minX, Math.min(line.x1, line.x2));
            maxY = Math.max(maxY, Math.max(line.y1, line.y2));
            minY = Math.min(minY, Math.min(line.y1, line.y2));
        }
        height = Math.abs(maxY - minY) + 0.5f;
        width = Math.abs(maxX - minX) + 0.5f;
        xOffset = -minX;
        yOffset = -minY;
    }

}
