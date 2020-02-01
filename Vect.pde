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