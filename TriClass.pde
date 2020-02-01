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
        PVector temp = position.copy();
        PVector temp2 = new PVector(length, 0).rotate(-PI/2);
        temp.x += temp2.x;
        temp.y += temp2.y;
        topCorner = temp;
        
        temp = position.copy();
        temp2 = new PVector(length, 0).rotate(PI/6);
        temp.x += temp2.x;
        temp.y += temp2.y;
        rightCorner = temp;

        temp = position.copy();
        temp2 = new PVector(length, 0).rotate(-7 * PI/6);
        temp.x += temp2.x;
        temp.y += temp2.y;
        leftCorner = temp;
        //topCorner = PVector.sub(position, new PVector(-length, 0).rotate(-PI/2));
        //rightCorner = PVector.sub(position, new PVector(-length, 0).rotate(PI/6));
        //leftCorner = PVector.sub(position, new PVector(-length, 0).rotate(-7 * PI/6));
    }
    private void split() { // Split the triangle into 3 smaller pieces
        // Spawn new children triangles
        PVector tempPosition = PVector.sub(position, new PVector(-length/2, 0).rotate(-PI/2));
        spawnTriangle(tempPosition.x, tempPosition.y, length/2);
        tempPosition = PVector.sub(position, new PVector(-length/2, 0).rotate(PI/6));
        spawnTriangle(tempPosition.x, tempPosition.y, length/2);
        tempPosition = PVector.sub(position, new PVector(-length/2, 0).rotate(-7 * PI/6));
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