package com.favendo.steinmeyer.groundplan.generation;

import com.favendo.steinmeyer.geometry.Face;
import com.favendo.steinmeyer.geometry.Plane;
import com.favendo.steinmeyer.geometry.Vector3;
import com.favendo.steinmeyer.geometry.Vector3Utils;
import com.favendo.steinmeyer.svg.SVGUtils;
import com.favendo.steinmeyer.wavefront.WavefrontFormatException;
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

    File file;
    WavefrontObject model;
    Collection<Plane> planes = new ArrayList<>();


    public Groundplan(File file) throws IllegalArgumentException {
        if (!file.getName().endsWith(".obj")) {
            throw new IllegalArgumentException(
                    "The file must be a wavefront object file (ending in '.obj').");
        } else {
            this.file = file;
        }
    }

    public void build() throws IOException, WavefrontFormatException {
        publish("Parsing file");
        model = new WavefrontParser().parseWavefrontFile(file);
        publish("Finding Planes for " + model.getFaces().size() + " faces: 0%");
        findPlanes();
        removeUnimportantPlanes();
    }

    private void findPlanes() {
        Collection<Face> verticalFaces = findVerticalFaces(model.getFaces());
        int numberOfFaces = verticalFaces.size();
        int progressStep = numberOfFaces / 100 + 1;
        int progress = 0;
        int counter = 1;
        log.warning(
                model.getFaces().size() + " faces out of which " + numberOfFaces + " are vertical");
        for (Face face : verticalFaces) {
            boolean isNewPlane = true;
            for (Plane plane : planes) {
                if (plane.contains(face)) {
                    plane.mergeFace(face);
                    isNewPlane = false;
                }
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
    }

    private Collection<Face> findVerticalFaces(final Collection<Face> faces) {
        Collection<Face> result = new ArrayList<>();
        for (Face face : faces) {
            if (Math.abs(Vector3Utils.dot(face.getNormal(), new Vector3(0, 0, 1))) < 0.01f) {
                result.add(face);
            }
        }
        return result;
    }

    private void removeUnimportantPlanes() {
        Collection<Plane> importantPlanes = new ArrayList<>();
        for (Plane plane : planes) {
            if (plane.getPoints().size() > 5000) {
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

    public String generateSVG() {
        return SVGUtils.newSVG(100, 100).addDescription("Some Description")
                                .drawLine(10, 10, 20, 20).drawLine(10, 20, 20, 20)
                                .drawLine(20, 10, 20, 20).build();
    }

    private void publish(String message) {
        setChanged();
        notifyObservers(message);
    }

}
