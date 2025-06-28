package game;

public class Fork extends Obstacle {
	Player player;
	public Fork(Player player) {
		super(player);
		this.player = player;
	}
	
	public Fork(Vector2 offset, Vector2 size, Player player) {
		super(offset, size, player);
		this.player = player;
	}
	
	boolean move = false;
	
	@Override
	public void Update() {
		super.Update();
		
		if(player.getGameObject().transform.position.x > getGameObject().transform.position.x - 80) {
			move = true;
		}
		
		if(move) {
			getGameObject().transform.position.y += GameState.getDeltaTime() * -2000 * GameState.getSpeed() / 500;
		}
		
		if(getGameObject().transform.position.x - Camera.position.x < -1000) {
			move = false;
		}
	}
	
	@Override
	public void Shift(double shift) {
		super.Shift(shift);
	}
}
