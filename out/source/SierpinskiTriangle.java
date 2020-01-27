import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SierpinskiTriangle extends PApplet {

ArrayList<Triangle> allTriangles = new ArrayList<Triangle>();
ArrayList<Triangle> trianglesToBeAdded = new ArrayList<Triangle>();
ArrayList<Triangle> trianglesToBeRemoved = new ArrayList<Triangle>();

float maxTriangleLength = 10;
float zoomAmount = 1.01f;
PVector zoomPoint;

public void setup()
{
    
    // Spawn triangles
    allTriangles.add(new Triangle(width/2, height/2, 100)); // Spawn first triangle
    while(allTriangles.get(0).getLength() > maxTriangleLength) { // Makes spawns the rest of the triangles
        updateTriangles();
    }
    // Pick Random Point
    int numOfTriangles = allTriangles.size();
    int triangleIndex = (int)(Math.random() * numOfTriangles);
    zoomPoint = allTriangles.get(triangleIndex).getLeftCorner();
}
public void draw()
{
    background(100); // Redraws background
    updateTriangles();
    zoom(zoomAmount, zoomPoint);
}
public void spawnTriangle(float x, float y, float argLength) {
    trianglesToBeAdded.add(new Triangle(x, y, argLength));
}
public void removeTriangle(Triangle ref) {
    trianglesToBeRemoved.add(ref);
}
public void zoom(float amount, PVector point) { // Zooms in all triangles
    for(Triangle tri : allTriangles) {
        tri.zoom(amount, point);
    }
}
public void updateTriangles() {
    // Updates every triangle
    for(Triangle tri : allTriangles) {
        tri.update();
    }
    // Remove triangles
    for(Triangle tri : trianglesToBeRemoved) {
        allTriangles.remove(tri);
    }
    trianglesToBeRemoved.clear();
    // Add triangles
    for(Triangle tri : trianglesToBeAdded) {
        allTriangles.add(tri);
    }
    trianglesToBeAdded.clear();
}
class Triangle {
    private PVector position;
    private float length; // The lenth of each corner from the center of the triangle
    private PVector topCorner;
    private PVector rightCorner;
    private PVector leftCorner;
    public Triangle(float x, float y, float argLength) {
        // Save arguments
        position = new PVector(x, y);
        length = argLength;
        // Calculate corners
        calcCorners();
    }
    public PVector getLeftCorner() {
        return leftCorner;
    }
    public PVector getRightCorner() {
        return rightCorner;
    }
    public PVector getTopCorner() {
        return topCorner;
    }
    public float getLength() {
        return length;
    }
    public void update() {
        show();
        if(length >= maxTriangleLength) { // Split triangle if it is too big
            split();
        }
        if(rightCorner.x < 0 || leftCorner.x > width) {
            removeTriangle(this);
        }
        if(rightCorner.y < 0 || topCorner.y > height) {
            removeTriangle(this);
        }
    }
    public void zoom(float amount, PVector point) { // Reposition and rescales triangle based on scale factor and scale point
        PVector difference = PVector.sub(position, point);
        difference.mult(amount - 1);
        position.add(difference);
        length *= amount;
        calcCorners();
    }
    private void calcCorners() { // Calculate corners
        topCorner = PVector.add(position, new PVector(length, 0).rotate(-PI/2));
        rightCorner = PVector.add(position, new PVector(length, 0).rotate(PI/6));
        leftCorner = PVector.add(position, new PVector(length, 0).rotate(-7 * PI/6));
    }
    private void split() { // Split the triangle into 3 smaller pieces
        // Spawn new children triangles
        PVector tempPosition = PVector.add(position, new PVector(length/2, 0).rotate(-PI/2));
        spawnTriangle(tempPosition.x, tempPosition.y, length/2);
        tempPosition = PVector.add(position, new PVector(length/2, 0).rotate(PI/6));
        spawnTriangle(tempPosition.x, tempPosition.y, length/2);
        tempPosition = PVector.add(position, new PVector(length/2, 0).rotate(-7 * PI/6));
        spawnTriangle(tempPosition.x, tempPosition.y, length/2);
        // Remove self
        removeTriangle(this);
    }
    private void show() {
        triangle(topCorner.x, topCorner.y, leftCorner.x, leftCorner.y, rightCorner.x, rightCorner.y);
    }
}
  public void settings() {  size(500, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SierpinskiTriangle" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
