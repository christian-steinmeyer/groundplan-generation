package com.favendo.steinmeyer.groundplan.generation;

import com.favendo.steinmeyer.svg.SVGUtils;
import com.momchil_atanasov.data.front.parser.IOBJParser;
import com.momchil_atanasov.data.front.parser.OBJModel;
import com.momchil_atanasov.data.front.parser.OBJParser;
import com.momchil_atanasov.data.front.parser.OBJVertex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

public class Groundplan extends Observable {

    File file;
    OBJModel model;
    Collection<OBJVertex> floorVertices = new ArrayList<>();
    Collection<OBJVertex> higherVertices = new ArrayList<>();


    public Groundplan(File file) throws IllegalArgumentException {
        if (!file.getName().endsWith(".obj")) {
            throw new IllegalArgumentException(
                    "The file must be a wavefront object file (ending in '.obj').");
        } else {
            this.file = file;
        }
    }

    public void build() throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            publish("Parsing file");

            // Create an OBJParser and parse the resource
            final IOBJParser parser = new OBJParser();
            model = parser.parse(in);
            separateFloor();
        }

    }

    private void separateFloor() {
        publish("Analyzing floor");
        Collection<OBJVertex> vertices = model.getVertices();

        float minimumZ = Float.MAX_VALUE;
        for (OBJVertex vertex : vertices) {
            minimumZ = Math.min(minimumZ, vertex.z);
        }
        publish("Separating floor");
        float maxDeviation = 0.5f; // 2 cm
        float threshold = minimumZ + maxDeviation;
        for (OBJVertex vertex : vertices) {
            if (vertex.z <= threshold) {
                floorVertices.add(vertex);
            } else {
                higherVertices.add(vertex);
            }
        }


        // only possible with API 24 and up

//        final float minimumZ =
//                vertices.parallelStream().min((v1, v2) -> Float.compare(v1.z, v2.z)).get().z;
//        floorVertices = vertices.parallelStream().filter(v -> v.z <= minimumZ + maxDeviation)
//                                .collect(Collectors.toList());
//        higherVertices =
//                vertices.parallelStream().filter(v -> v.z > minimumZ + maxDeviation).collect(
//                        Collectors.toList());
    }

    public String getInformation() {
        // Use the model representation to get some basic info

        int numberOfVertices = model.getVertices().size();
        int numberOfFloorVertices = floorVertices.size();
        int numberOfHigherVertices = higherVertices.size();
        return MessageFormat
                .format("OBJ model has {0} vertices, out of which {1} are part of the floor, and " +
                                "{2} are not.", numberOfVertices, numberOfFloorVertices,
                        numberOfHigherVertices);
    }

    public String generateSVG() {
        String result = SVGUtils.newSVG(100, 100).addDescription("Some Description")
                                .drawLine(10, 10, 20, 20).drawLine(10, 20, 20, 20)
                                .drawLine(20, 10, 20, 20).build();
        return result;
    }

    private void publish(String message) {
        setChanged();
        notifyObservers(message);
    }

}
