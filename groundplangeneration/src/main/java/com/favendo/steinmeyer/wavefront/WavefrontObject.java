package com.favendo.steinmeyer.wavefront;

import com.favendo.steinmeyer.geometry.Face;
import com.favendo.steinmeyer.geometry.Vertex;
import com.favendo.steinmeyer.geometry.VertexNormal;

import java.util.ArrayList;

/**
 * Created by Christian Steinmeyer on 08.07.2016.
 *
 * A very lightweight representation of an object modelling the contents of wavefront files. Handles
 * Vertices, Vertex Normals and Faces.
 */
public class WavefrontObject {

    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<VertexNormal> normals = new ArrayList<>();
    ArrayList<Face> faces = new ArrayList<>();

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<VertexNormal> getNormals() {
        return normals;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addVertexNormal(VertexNormal normal) {
        normals.add(normal);
    }

    public void addFace(Face face) {
        faces.add(face);
    }
}
