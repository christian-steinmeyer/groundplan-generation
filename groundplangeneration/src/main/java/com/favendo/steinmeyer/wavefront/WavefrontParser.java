package com.favendo.steinmeyer.wavefront;

import com.favendo.steinmeyer.geometry.Face;
import com.favendo.steinmeyer.geometry.Vector3;
import com.favendo.steinmeyer.geometry.Vertex;
import com.favendo.steinmeyer.geometry.VertexNormal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import sun.security.pkcs.ParsingException;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 *
 * Is able to parse a simple file in the wavefront format. It can handle comments, vertices, vertex
 * normals, and faces.
 */
public class WavefrontParser {


    public WavefrontObject parseWavefrontFile(File file)
            throws IOException, WavefrontFormatException {
        WavefrontObject result = new WavefrontObject();
        try (final BufferedReader input = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (!line.startsWith("#")) {
                    WavefrontType type =
                            WavefrontType.fromString(line.substring(0, line.indexOf(" ")));
                    switch (type) {
                        case VERTEX:
                            result.addVertex(parseVertex(line));
                            break;
                        case VERTEX_NORMAL:
                            result.addVertexNormal(parseVertexNormal(line));
                            break;
                        case FACE:
                            result.addFace(parseFace(line, result));
                            break;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new WavefrontFormatException(
                    "The given file does not comply with the requirements: not enough arguments " +
                            "were supplied. " +
                            e.getLocalizedMessage());
        } catch (ParsingException e) {
            throw new WavefrontFormatException(
                    "The given file does not comply with the requirements: at least one wrong " +
                            "value was supplied. " +
                            e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Vertices are of the form <p> <code>v x y z</code> </p>
     * <p>
     * where <code>x, y, z</code> are floats
     */
    private Vertex parseVertex(final String line) {
        Vector3 v = parseVector3(line);
        return new Vertex(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Vertex Normals are of the form <p> <code>vn x y z</code> </p>
     * <p>
     * where <code>x, y, z</code> are floats
     */
    private VertexNormal parseVertexNormal(final String line) {
        Vector3 v = parseVector3(line);
        return new VertexNormal(v.getX(), v.getY(), v.getZ());
    }

    private Vector3 parseVector3(final String line) {
        String[] words = line.split(" ");
        float x = Float.parseFloat(words[1]);
        float y = Float.parseFloat(words[2]);
        float z = Float.parseFloat(words[3]);
        return new Vector3(x, y, z);
    }

    /**
     * Faces are of the form <p> <code>f i/j k/l...</code> </p>
     * <p>
     * where <code>i, k</code> are indices of vertices and <code>j, l</code> are indices of vertex
     * normals parsed earlier
     */
    private Face parseFace(final String line, WavefrontObject object) {
        String[] words = line.split(" ");
        Vertex[] vertices = new Vertex[3];
        for (int offset = 1; offset < 4; offset++) {
            // words[offset] has format "i/j" where i, j are integers
            int index = Integer.parseInt(words[offset].substring(0, words[offset].indexOf("/")));
            vertices[offset - 1] = object.getVertices().get(index - 1);
        }

        return new Face(vertices[0], vertices[1], vertices[2]);
    }
}
