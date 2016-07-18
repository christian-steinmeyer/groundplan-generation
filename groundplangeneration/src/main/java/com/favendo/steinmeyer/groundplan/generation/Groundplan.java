package com.favendo.steinmeyer.groundplan.generation;

import com.favendo.steinmeyer.geometry.Face;
import com.favendo.steinmeyer.geometry.Plane;
import com.favendo.steinmeyer.geometry.Vector3;
import com.favendo.steinmeyer.geometry.Vector3Utils;
import com.favendo.steinmeyer.geometry.Vertex;
import com.favendo.steinmeyer.svg.SVGUtils;
import com.favendo.steinmeyer.wavefront.WavefrontFormatException;
import com.favendo.steinmeyer.wavefront.WavefrontGenerator;
import com.favendo.steinmeyer.wavefront.WavefrontObject;
import com.favendo.steinmeyer.wavefront.WavefrontParser;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.logging.Logger;

public class Groundplan extends Observable {

    private Logger log = Logger.getLogger(this.getClass().getName());

    private final float ACCURACY_RADIANS = 0.087266f; // ~ 5° = 2 * pi / 360 * 5
    private final float ACCURACY_METERS = 0.05f;
    private final int NOISE_REDUCTION_ITERATIONS = 3;

    File file;
    WavefrontObject model;
    ArrayList<Vertex> vertices;
    ArrayList<Plane> planes = new ArrayList<>();


    public Groundplan(File file) throws IllegalArgumentException {
        if (!file.getName().endsWith(".obj")) {
            throw new IllegalArgumentException(
                    "The file must be a wavefront object file (ending in '.obj').");
        } else {
            this.file = file;
        }
    }

    public void build() throws IOException, WavefrontFormatException {
        publish("Parsing file... This can take a while");
        model = new WavefrontParser().parseWavefrontFile(file);
        vertices = new ArrayList<>(model.getVertices());
        findPlanes();
        for (int i = 0; i < NOISE_REDUCTION_ITERATIONS; i++) {
            removeOutliers();
            removeUnimportantPlanes(vertices.size() / 50);
        }
    }

    private void removeOutliers() {
        for (Plane plane : planes) {
            log.warning("Removing outliers for plane:" + plane);
            plane.removeOutliers();
            Collection<Vector3> cleanedPoints = new ArrayList<>();
            for (Vector3 point : plane.getPoints()) {
                if (plane.getDistance(point) < 3 * ACCURACY_METERS) {
                    cleanedPoints.add(point);
                }
            }
            log.warning((plane.getPoints().size() - cleanedPoints.size()) +
                    " from " + plane.getPoints().size() + " points.");
            plane.resetPoints(cleanedPoints);
        }
    }

    private void findPlanes() {
        publish("Finding Planes for " + model.getFaces().size() + " faces: 0%");
        Collection<Face> verticalFaces = findVerticalFaces(model.getFaces());
        int numberOfFaces = verticalFaces.size();
        int progressStep = numberOfFaces / 100 + 1;
        int progress = 0;
        int counter = 1;
        log.warning(
                model.getFaces().size() + " faces out of which " + numberOfFaces + " are vertical");
        for (Face face : verticalFaces) {
            boolean isNewPlane = true;
            int bestMatch = -1;
            float angle = Integer.MAX_VALUE;
            for (Plane plane : planes) {
                if (plane.contains(face)) {
                    float thisAngle = Vector3Utils.angle(face.getNormal(), plane.getNormal());
                    bestMatch = thisAngle < angle ? planes.indexOf(plane) : bestMatch;
                    angle = Math.min(angle, thisAngle);
                }
            }
            if (bestMatch > 0) {
                planes.get(bestMatch).mergeFace(face);
                isNewPlane = false;
            }
            if (isNewPlane) {
                planes.add(new Plane(face));
            }
            if (counter / progressStep > progress) {
                progress++;
                publish("Finding Planes for " + numberOfFaces + " faces: " + progress + "%");
            }
            counter++;
        }
        removeUnimportantPlanes(vertices.size() / 50);
    }

    private Collection<Face> findVerticalFaces(final Collection<Face> faces) {
        Collection<Face> result = new ArrayList<>();
        for (Face face : faces) {
            if (Math.abs(Vector3Utils.dot(face.getNormal(), new Vector3(0, 0, 1))) <
                    ACCURACY_RADIANS) {
                result.add(face);
            }
        }
        return result;
    }

    private void removeUnimportantPlanes(int threshold) {
        Collection<Plane> importantPlanes = new ArrayList<>();
        for (Plane plane : planes) {
            if (plane.getPoints().size() > threshold) {
                importantPlanes.add(plane);
            }
        }
        planes.retainAll(importantPlanes);
    }

    public String getInformation() {
        // Use the model representation to get some basic info

        int numberOfVertices = model.getVertices().size();
        int numberOfNormals = model.getNormals().size();
        int numberOfFaces = model.getFaces().size();
        int numberOfPlanes = planes.size();
        return MessageFormat
                .format("The given Wavefront Object model has {0} vertices, {1} normals, {2} " +
                                "faces, and was analyzed to have {3} planes.", numberOfVertices,
                        numberOfNormals, numberOfFaces, numberOfPlanes);
    }

    public String generateSVG(String description) {
        SVGUtils svg = SVGUtils.newSVG("cm", description);
        for (Plane plane : planes) {

            Vector3 min = plane.getPoint();
            Vector3 max = plane.getPoint();

            for (Vector3 point : plane.getPoints()) {
                if (point.getX() < min.getX()) {
                    min = point;
                }
                if (point.getX() > max.getX()) {
                    max = point;
                }
            }
            // if plane parallel to y axis, redo for y values
            if (Math.abs(min.getX() - max.getX()) < ACCURACY_METERS) {
                for (Vector3 point : plane.getPoints()) {
                    if (point.getY() < min.getY()) {
                        min = point;
                    }
                    if (point.getY() > max.getY()) {
                        max = point;
                    }
                }
            }
            svg.addLine(min.getX(), min.getY(), max.getX(), max.getY());
        }
        return svg.build();
    }

    public String generateWavefront() {
        WavefrontGenerator wavefront = new WavefrontGenerator();
        int counter = 1;
        for (Plane plane : planes) {
            publish("Generating Wavefront for plane  " + counter + " / " + planes.size() + "...");
            for (Vector3 v : plane.getPoints()) {
                wavefront.writeVertex(new Vertex(v.getX(), v.getY(), v.getZ()));
            }
        }
        return wavefront.build();
    }

    private void publish(String message) {
        setChanged();
        notifyObservers(message);
    }

}
