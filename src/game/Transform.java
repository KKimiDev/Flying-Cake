package game;

public class Transform extends Component {
	public Vector2 position;
	public Vector2 scale;
	public float rotation;
	public int zIndex;
	
	public Transform() {
		position = new Vector2();
		scale = new Vector2();
		rotation = 0;
		zIndex = 0;
	}
}
