package game;

public class Vector2 {
	public double x = 0;
	public double y = 0;
	
	public Vector2() {
		
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 Add(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}
	
	public Vector2 Mul(double scalar) {
		return new Vector2(x * scalar, y * scalar);
	}
}
