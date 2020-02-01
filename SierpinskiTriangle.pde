// Object arrays
ArrayList<TriClass> allTriangles = new ArrayList<TriClass>();
ArrayList<TriClass> trianglesToBeAdded = new ArrayList<TriClass>();
ArrayList<TriClass> trianglesToBeRemoved = new ArrayList<TriClass>();

// Triangle states
float maxTriangleLength = 80; // The largest possible triangle in length
float currentTriangleLength = 320; // The current length
Vect zoomPoint; // The point the triangle is zooming into
// Settings
float zoomAmount = 1.01; // The rate at which the triangles are zooming in
float fadeAmount = 5; // The rate at which the triangles are fading

public void setup() {
    size(500, 500);
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