class TriClass { // A triangle object
    // Position Properties
    private PVector pos;
    private float len; // The lenth of each corner from the center of the triangle
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
        pos = new PVector(x, y);
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
    public void zoom(float amount, PVector point) { // Reposition and rescales triangle based on scale factor and scale point
        PVector difference = PVector.sub(pos, point);
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
        PVector temp = new PVector(pos.x, pos.y);
        PVector temp2 = new PVector(len, 0).rotate(-PI/2);
        temp.x += temp2.x;
        temp.y += temp2.y;
        topCorner = temp;

        temp = new PVector(pos.x, pos.y);
        temp2 = new PVector(len, 0).rotate(PI/6);
        temp.x += temp2.x;
        temp.y += temp2.y;
        rightCorner = temp;

        temp = new PVector(pos.x, pos.y);
        temp2 = new PVector(len, 0).rotate(-7 * PI/6);
        temp.x += temp2.x;
        temp.y += temp2.y;
        leftCorner = temp;

        //topCorner = PVector.add(pos, new PVector(len, 0).rotate(-PI/2));
        //rightCorner = PVector.add(pos, new PVector(len, 0).rotate(PI/6));
        //leftCorner = PVector.add(pos, new PVector(len, 0).rotate(-7 * PI/6));
    }
    private void split() { // Split the triangle into 3 smaller pieces
        // Spawn new children triangles
        PVector tempPosition = PVector.add(pos, new PVector(len/2, 0).rotate(-PI/2));
        spawnTriangle(tempPosition.x, tempPosition.y, len/2);
        tempPosition = PVector.add(pos, new PVector(len/2, 0).rotate(PI/6));
        spawnTriangle(tempPosition.x, tempPosition.y, len/2);
        tempPosition = PVector.add(pos, new PVector(len/2, 0).rotate(-7 * PI/6));
        spawnTriangle(tempPosition.x, tempPosition.y, len/2);
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