package com.favendo.steinmeyer.wavefront;

import com.favendo.steinmeyer.geometry.Face;
import com.favendo.steinmeyer.geometry.Vector3;
import com.favendo.steinmeyer.geometry.Vertex;
import com.favendo.steinmeyer.geometry.VertexNormal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import sun.security.pkcs.ParsingException;

/**
 * Created by Favendo on 13.07.2016.
 */
public class WavefrontGenerator {

    public String generateWavefront(WavefrontObject object){

        StringBuilder builder = new StringBuilder();
        for (Vertex vertex : object.getVertices()) {
            builder.append(writeVertex(vertex));
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Vertices are of the form <p> <code>v x y z</code> </p>
     * <p>
     * where <code>x, y, z</code> are floats
     */
    private String writeVertex(final Vertex vertex) {
        return "v" + writeVector3(vertex);
    }

    /**
     * Vertex normals are of the form <p> <code>vn x y z</code> </p>
     * <p>
     * where <code>x, y, z</code> are floats
     */
    private String writeVertexNormal(final VertexNormal vertexNormal) {
        return "vn" + writeVector3(vertexNormal);
    }

    private String writeVector3(final Vector3 v) {
        return String.format(" %f %f %f", v.getX(), v.getY(), v.getZ());
    }

}
