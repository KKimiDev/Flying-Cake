package game;

public class Collider extends Component {
	private Vector2 offset;
	private Vector2 size;
	
	public Collider () {
		offset = new Vector2();
		size = new Vector2();
	}
	
	public Collider (Vector2 offset, Vector2 size) {
		this.offset = offset;
		this.size = size;
	}
	
	public boolean collidesWith(Collider other) {
		Vector2 position = getGameObject().transform.position;
		Vector2 position2 = other.getGameObject().transform.position;
		Vector2 offset2 = other.getOffset();
		Vector2 size2 = other.getSize();
		
		double x1 = position.x + offset.x - size.x / 2;
		double y1 = position.y + offset.y - size.y / 2;
		double x2 = position.x + offset.x + size.x / 2;
		double y2 = position.y + offset.y + size.y / 2;
		
		double ox1 = position2.x + offset2.x - size2.x / 2;
		double oy1 = position2.y + offset2.y - size2.y / 2;
		double ox2 = position2.x + offset2.x + size2.x / 2;
		double oy2 = position2.y + offset2.y + size2.y / 2;
		
		if (x1 > x2) {
			double x_ = x2;
			x2 = x1;
			x1 = x_;
		}
		
		return !(((ox1 < x1 && ox2 < x1) || (ox1 > x2 && ox2 > x2)) || ((oy1 < y1 && oy2 < y1) || (oy1 > y2 && oy2 > y2)));
	}
	
	public Vector2 getOffset() {
		return offset;
	}
	
	public Vector2 getSize() {
		return size;
	}
}
