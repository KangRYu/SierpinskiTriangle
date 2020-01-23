public void setup()
{
    size(500, 500);
    sierpinski(100, 400, 300);
}
public void draw()
{

}
public void mouseDragged()//optional
{

}
public void sierpinski(int x, int y, int len) 
{
    if(len <= 20) {
        triangle(x, y, x + len/2, y - len, x + len, y);
    }
    else {
        sierpinski(x, y, len/2);
        sierpinski(x + len/2, y, len/2);
        sierpinski(x + len/4, y - len/2, len/2);
    }
}
class Triangle {
    private PVector position;
    private length;
    public Triangle(float x, float y, float argLength) {
        position = new PVector(x, y);
        length = argLength;
    }
    public void show() {
        PVector leftCorner = position.add(length, 0).rotate();
    }
}