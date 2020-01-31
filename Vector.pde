static class Vector {
    public double x;
    public double y;

    // Constructors
    public Vector() { // Creates an empty vector if no arguments are given
        x = 0;
        y = 0;
    }
    public Vector(double argX, double argY) {
        x = argX;
        y = argY;
    }

    // Public functions
    public void add(Vector input) { // Adds the inputed vector into this vector
        x += input.x;
        y += input.y;
    }
    public static Vector add(Vector input, Vector input2) { // A static function for adding two vectors
        double tempX = input.x + input2.x;
        double tempY = input.y + input2.y;
        return new Vector(tempX, tempY);
    }
    public void rotate(double angle) {
        double tempX;
        double tempY;
    }

    // Getters
    public float fx() { // Returns the x as a float
        return (float)x;
    }
    public float fy() {
        return (float)y;
    }
}