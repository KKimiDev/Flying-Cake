package game;

public class Obstacle extends Collider {
	Player player;
	public Obstacle (Player player) {
		super();
		this.player = player;
	}
	
	public Obstacle (Vector2 offset, Vector2 size, Player player) {
		super(offset, size);
		this.player = player;
	}
	
	@Override
	public void Update() {
		if(getGameObject().transform.position.x - Camera.position.x < -1000) {
			getGameObject().setEnabled(false);
		}
	}
	
	public void Shift(double shift) {
		getGameObject().transform.position.x += shift;
	}
}
