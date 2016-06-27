package com.favendo.steinmeyer.groundplan.generation;

import com.momchil_atanasov.data.front.parser.IOBJParser;
import com.momchil_atanasov.data.front.parser.OBJModel;
import com.momchil_atanasov.data.front.parser.OBJParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

public class Groundplan {
    OBJModel model;


    public Groundplan(File file) throws IllegalArgumentException, IOException {
        try (final InputStream in = new FileInputStream(file)) {

            if (!file.getName().endsWith(".obj")) {
                throw new IllegalArgumentException(
                        "The file must be a wavefront object file (ending in '.obj').");
            }
            // Create an OBJParser and parse the resource
            final IOBJParser parser = new OBJParser();
            model = parser.parse(in);

        }
    }

    public String getInformation() {
        // Use the model representation to get some basic info

        int numberOfVertices = model.getVertices().size();
        int numberOfNormals = model.getNormals().size();
        int numberOfTexCoords = model.getTexCoords().size();
        int numberOfObjects = model.getObjects().size();
        return MessageFormat
                .format("OBJ model has {0} vertices, {1} normals, {2} texture coordinates, " +
                                "and {3} objects.", numberOfVertices, numberOfNormals,
                        numberOfTexCoords,
                        numberOfObjects);
    }

}
