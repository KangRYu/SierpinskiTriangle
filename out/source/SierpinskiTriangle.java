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

// Object arrays
ArrayList<TriClass> allTriangles = new ArrayList<TriClass>();
ArrayList<TriClass> trianglesToBeAdded = new ArrayList<TriClass>();
ArrayList<TriClass> trianglesToBeRemoved = new ArrayList<TriClass>();

// Triangle states
float maxTriangleLength = 80; // The largest possible triangle in length
float currentTriangleLength = 320; // The current length
PVector zoomPoint; // The point the triangle is zooming into
// Settings
float zoomAmount = 1.01f; // The rate at which the triangles are zooming in
float fadeAmount = 5; // The rate at which the triangles are fading

public void setup() {
    
    noStroke();
    // Spawn triangles
    allTriangles.add(new TriClass(width/2, height/2, currentTriangleLength)); // Spawn first triangle
    allTriangles.get(0).setColor();
    // Load a blank zoom point
    zoomPoint = new PVector(0, 0);
}

public void draw() {
    background(30); // Redraws background
    // Update zoom point
    zoomPoint.x = mouseX;
    zoomPoint.y = mouseY;
    // Updates the triangles
    updateTriangles();
    zoom(zoomAmount, zoomPoint);
    if(currentTriangleLength > maxTriangleLength) {
        splitAllTriangles();
        fadeOutAll();
    }
}

public void spawnTriangle(float x, float y, float argLength) {
    TriClass temp = new TriClass(x, y, argLength);
    temp.setColor();
    trianglesToBeAdded.add(temp);
}

public void zoom(float amount, PVector point) { // Zooms in all triangles
    for(TriClass tri : allTriangles) {
        tri.zoom(amount, point);
    }
    currentTriangleLength *= amount; // Update the current triangle length variable
}

public void updateTriangles() {
    // Updates every triangle
    for(TriClass tri : allTriangles) {
        tri.update();
    }
    // Remove triangles
    for(TriClass tri : trianglesToBeRemoved) {
        allTriangles.remove(tri);
    }
    trianglesToBeRemoved.clear();
    // Add triangles
    for(TriClass tri : trianglesToBeAdded) {
        allTriangles.add(tri);
    }
    trianglesToBeAdded.clear();
}

public void splitAllTriangles() {
    for(TriClass tri : allTriangles) {
        if(!tri.isFadingOut()) {
            tri.split();
        }
    }
    currentTriangleLength /= 2;
}

public void fadeOutAll() {
    for(TriClass tri : allTriangles) {
        tri.fadeOut();
    }
}
class TriClass { // A triangle object
    // Position Properties
    private PVector position;
    private float length; // The lenth of each corner from the center of the triangle
    private PVector topCorner;
    private PVector rightCorner;
    private PVector leftCorner;
    // The colors of the triangle
    private float r;
    private float g;
    private float b;
    private float a; // Alpha
    // Fading states
    private boolean fadingIn = true;
    private boolean fadingOut = false;

    // Initialization functions
    public TriClass(float x, float y, float argLength) {
        // Save arguments
        position = new PVector(x, y);
        length = argLength;
        // Calculate corners
        calcCorners();
    }
    public void setColor() { // Set color function with no arguments that picks a random color
        r = (float)(Math.random() * 255);
        g = (float)(Math.random() * 255);
        b = (float)(Math.random() * 255);
        a = 0;
    }
    public void setColor(float argR, float argG, float argB) {
        r = argR;
        g = argG;
        b = argB;
        a = 0;
    }

    // Public call classes
    public void update() {
        show();
        updateFade();
        if(!visible()) { // If not visibile, delete the triangle
            delete();
        }
    }
    public void zoom(float amount, PVector point) { // Reposition and rescales triangle based on scale factor and scale point
        PVector difference = PVector.sub(position, point);
        difference.mult(amount - 1);
        position.add(difference);
        length *= amount;
        calcCorners();
    }
    public void delete() { // Deletes this triangle
        trianglesToBeRemoved.add(this);
    }

    // Private functions
    private void updateFade() { // Updates the alpha
        if(a < 255 && fadingIn) {
            a += fadeAmount; // Increment alpha
            if(a > 255) {
                a = 255;
            }
        }
        if(fadingOut) {
            a -= fadeAmount; // Decrement alpha
            if(a <= 0) { // Delete triangle if it is invisible
                delete();
            }
        }
    }
    private boolean visible() { // Check if the triangle is still within the screen
        if(rightCorner.x < 0 || leftCorner.x > width) {
            return false;
        }
        if(rightCorner.y < 0 || topCorner.y > height) {
            return false;
        }
        return true;
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
    }
    private void show() {
        fill(r, g, b, a);
        triangle(topCorner.x, topCorner.y, leftCorner.x, leftCorner.y, rightCorner.x, rightCorner.y);
    }

    // Setters and getters
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
    public boolean isFadingIn() {
        return fadingIn;
    }
    public boolean isFadingOut() {
        return fadingOut;
    }
    public void fadeOut() { // Transitions triangle from fading in to fading out
        fadingIn = false;
        fadingOut = true;
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
