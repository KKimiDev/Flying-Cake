package game;

import java.awt.Graphics2D;
import java.awt.Image;

public class Terrain extends RenderUnit {
	private Image image;
	private int width = 400;
	private int height = 220;
	private double offset = 0;
	
	public Terrain(Image image) {
		setImage(image);
	}
	
	public Terrain() {
		image = null;
	}
	
	public Image getImage() { return image; }
	
	public void setImage(Image image) { this.image = image; }
	
	@Override
	public void Update() {
		getGameObject().transform.position.x %= width;
	}
	
	@Override
	public void render(Graphics2D g, double camX, double camY) {
		Transform transform = getGameObject().transform;
		
		int x = (int) (transform.position.x - camX);
		int y = (int) (transform.position.y - camY);
		
		
		int width_ = (int)(width * Camera.getScale().x);
		int height_ = (int)(height * Camera.getScale().y);
		
		for(int i = -3; (i-3) * width + offset < 1920 + camX; i++) {
			g.drawImage(image, (int) ((x * Camera.getScale().x + i * width_)), (int) (y * Camera.getScale().y) + 1, (int) (width_), (int) (height_), null);
		}
	}
}
