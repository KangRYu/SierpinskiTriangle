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
Vect zoomPoint; // The point the triangle is zooming into
// Settings
float zoomAmount = 1.01f; // The rate at which the triangles are zooming in
float fadeAmount = 5; // The rate at which the triangles are fading

public void setup() {
    
    noStroke();
    // Spawn triangles
    allTriangles.add(new TriClass(width/2, height/2, currentTriangleLength)); // Spawn first triangle
    allTriangles.get(0).setColor();
    // Load a blank zoom point
    zoomPoint = new Vect(0, 0);
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

public void zoom(float amount, Vect point) { // Zooms in all triangles
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
    private Vect pos;
    private float len; // The lenth of each corner from the center of the triangle
    private Vect topCorner;
    private Vect rightCorner;
    private Vect leftCorner;
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
        pos = new Vect(x, y);
        len = argLength;
        // Calculate corners
        calcCorners();
    }
    public void setColor() {
        r = (float)(Math.random() * 255);
        g = (float)(Math.random() * 255);
        b = (float)(Math.random() * 255);
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
    public void zoom(float amount, Vect point) { // Reposition and rescales triangle based on scale factor and scale point
        Vect difference = pos.clone();
        difference.sub(point);
        difference.mult(amount - 1);
        pos.add(difference);
        len *= amount;
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
        //topCorner = PVector.add(pos, new PVector(len, 0).rotate(-PI/2));
        //rightCorner = PVector.add(pos, new PVector(len, 0).rotate(PI/6));
        //leftCorner = PVector.add(pos, new PVector(len, 0).rotate(-7 * PI/6));
        Vect tempPos = pos.clone();
        Vect dis = new Vect(len, 0);
        dis.rotateTo(-PI/2);
        tempPos.add(dis);
        topCorner = tempPos;

        tempPos = pos.clone();
        dis = new Vect(len, 0);
        dis.rotateTo(PI/6);
        tempPos.add(dis);
        rightCorner = tempPos;

        tempPos = pos.clone();
        dis = new Vect(len, 0);
        dis.rotateTo(-7 * PI/6);
        tempPos.add(dis);
        leftCorner = tempPos;
    }
    private void split() { // Split the triangle into 3 smaller pieces
        // Spawn new children triangles
        Vect tempPos = pos.clone();
        Vect dis = new Vect(len/2, 0);
        dis.rotateTo(-PI/2);
        tempPos.add(dis);
        spawnTriangle(tempPos.x, tempPos.y, len/2);

        tempPos = pos.clone();
        dis = new Vect(len/2, 0);
        dis.rotateTo(PI/6);
        tempPos.add(dis);
        spawnTriangle(tempPos.x, tempPos.y, len/2);

        tempPos = pos.clone();
        dis = new Vect(len/2, 0);
        dis.rotateTo(-7 * PI/6);
        tempPos.add(dis);
        spawnTriangle(tempPos.x, tempPos.y, len/2);
        
        //Vect tempPosition = PVector.add(pos, new PVector(len/2, 0).rotate(-PI/2));
        //spawnTriangle(tempPosition.x, tempPosition.y, len/2);
        //tempPosition = PVector.add(pos, new PVector(len/2, 0).rotate(PI/6));
        //spawnTriangle(tempPosition.x, tempPosition.y, len/2);
        //tempPosition = PVector.add(pos, new PVector(len/2, 0).rotate(-7 * PI/6));
        //spawnTriangle(tempPosition.x, tempPosition.y, len/2);
    }
    private void show() {
        fill(r, g, b, a);
        triangle(topCorner.x, topCorner.y, leftCorner.x, leftCorner.y, rightCorner.x, rightCorner.y);
    }

    // Setters and getters
    public Vect getLeftCorner() {
        return leftCorner;
    }
    public Vect getRightCorner() {
        return rightCorner;
    }
    public Vect getTopCorner() {
        return topCorner;
    }
    public float getLength() {
        return len;
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
class Vect {
    public float x;
    public float y;

    // Constructors
    public Vect(float argX, float argY) {
        x = argX;
        y = argY;
    }

    // Public functions
    public void add(Vect input) { // Adds the inputed vector into this vector
        x += input.x;
        y += input.y;
    }
    public void sub(Vect input) {
        x -= input.x;
        y -= input.y;
    }
    public Vect clone() {
        return new Vect(x, y);
    }
    public void mult(float mag) { // Scales vector
        float angle = atan2(y, x);
        float len = sqrt(pow(x, 2) + pow(y, 2));
        len *= mag;
        x = cos(angle) * len;
        y = sin(angle) * len;
    }
    public void rotateTo(float angle) {
        float len = sqrt(pow(x, 2) + pow(y, 2));
        x = cos(angle) * len;
        y = sin(angle) * len;
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
