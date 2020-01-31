// Object arrays
ArrayList<Triangle> allTriangles = new ArrayList<Triangle>();
ArrayList<Triangle> trianglesToBeAdded = new ArrayList<Triangle>();
ArrayList<Triangle> trianglesToBeRemoved = new ArrayList<Triangle>();

// Triangle states
float maxTriangleLength = 10; // The largest possible triangle in length
float currentTriangleLength = 80; // The current length
PVector zoomPoint; // The point the triangle is zooming into
// Settings
float zoomAmount = 1.01; // The rate at which the triangles are zooming in
float fadeAmount = 5; // The rate at which the triangles are fading

public void setup() {
    size(500, 500);
    // Spawn triangles
    allTriangles.add(new Triangle(width/2, height/2, currentTriangleLength)); // Spawn first triangle
    allTriangles.get(0).setColor();
    // Pick Random Point
    int numOfTriangles = allTriangles.size();
    int triangleIndex = (int)(Math.random() * numOfTriangles);
    zoomPoint = allTriangles.get(triangleIndex).getLeftCorner();
}

public void draw() {
    background(100); // Redraws background
    updateTriangles();
    zoom(zoomAmount, zoomPoint);
    if(currentTriangleLength > maxTriangleLength) {
        splitAllTriangles();
        fadeOutAll();
    }
}

public void spawnTriangle(float x, float y, float argLength) {
    Triangle temp = new Triangle(x, y, argLength);
    temp.setColor();
    trianglesToBeAdded.add(temp);
}

public void zoom(float amount, PVector point) { // Zooms in all triangles
    for(Triangle tri : allTriangles) {
        tri.zoom(amount, point);
    }
    currentTriangleLength *= amount; // Update the current triangle length variable
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

public void splitAllTriangles() {
    for(Triangle tri : allTriangles) {
        if(!tri.isFadingOut()) {
            tri.split();
        }
    }
    currentTriangleLength /= 2;
}

public void fadeOutAll() {
    for(Triangle tri : allTriangles) {
        tri.fadeOut();
    }
}