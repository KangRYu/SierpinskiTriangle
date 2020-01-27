ArrayList<Triangle> allTriangles = new ArrayList<Triangle>();
ArrayList<Triangle> trianglesToBeAdded = new ArrayList<Triangle>();
ArrayList<Triangle> trianglesToBeRemoved = new ArrayList<Triangle>();

float maxTriangleLength = 30;
float zoomAmount = 1.01;
PVector zoomPoint;

public void setup()
{
    size(500, 500);
    // Spawn triangles
    allTriangles.add(new Triangle(width/2, height/2, 100)); // Spawn first triangle
    updateTriangles();
    // Pick random target
    int numOfTriangles = allTriangles.size();
    int targetIndex = (int)(Math.random() * numOfTriangles);
    zoomPoint = allTriangles.get(targetIndex).getPosition();
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
    point.add(new PVector(1 - amount, 0));
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
    public PVector getPosition() {
        return position;
    }
    public void update() {
        show();
        if(length >= maxTriangleLength) { // Split triangle if it is too big
            split();
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