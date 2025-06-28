package game;

import java.awt.Graphics2D;
import java.awt.Image;

public class Sprite extends RenderUnit{
	private Image[] imgs = null;
	private double cycle = 1;
	private Image image = null;
	
	public Sprite(Image image) {
		setImage(image);
	}
	
	public Sprite(Image[] imgs, double cycle) {
		this.imgs = imgs;
		scaledInstances = new Image[imgs.length];
		lastChange = System.currentTimeMillis();
		this.cycle = cycle;
	}
	
	public Image getImage() { return image; }
	
	public void setImage(Image image) { this.image = image; }
	
	long lastChange = 0;
	int imgIndex = 0;
	
	@Override
	public void Update() {
		if(imgs != null) {
			imgIndex = (int) (( System.currentTimeMillis() / cycle + (getGameObject().transform.position.x + getGameObject().transform.position.y) / 60) % imgs.length);
		}
	}
	
	private Image[] scaledInstances = null;
	private Image scaledInstance = null;
	private Vector2 instanceScale = new Vector2();
	
	private void scaleImage() {
		Transform transform = getGameObject().transform;
		double scaleX = transform.scale.x * Camera.getScale().x;
		double scaleY = transform.scale.y * Camera.getScale().y;
		
		if(scaleX == instanceScale.x && scaleY == instanceScale.y) 
			return;
		
		if(image != null) {
			scaledInstance = image.getScaledInstance((int) (scaleX), (int) scaleY, Image.SCALE_SMOOTH);
		} else if(imgs != null && imgs[imgIndex] != null) {
			for(int i = 0; i < imgs.length; i++) {
				scaledInstances[i] = imgs[i].getScaledInstance((int) scaleX, (int) scaleY, Image.SCALE_SMOOTH);
			}
		}
		
		instanceScale.x = scaleX;
		instanceScale.y = scaleY;
	}
	
	@Override
	public void render(Graphics2D g, double camX, double camY) {
		Transform transform = getGameObject().transform;
		
		scaleImage();
		
		int x1 = (int) (transform.position.x - transform.scale.x / 2);
		int y1 = (int) (transform.position.y - transform.scale.y / 2);
		int x2 = (int) (transform.scale.x);
		int y2 = (int) (transform.scale.y);
		
		if(image != null) {
			g.drawImage(scaledInstance,(int) ((x1 - camX) * Camera.getScale().x), (int)((y1 - (camY)) * Camera.getScale().y), Game.cam);
		} else if(imgs != null && imgs[imgIndex] != null) {
			g.drawImage(scaledInstances[imgIndex],(int) ((x1 - camX) * Camera.getScale().x), (int)((y1 - (camY)) * Camera.getScale().y), Game.cam);
		}
		
		/*
		Collider col = getGameObject().getComponent(Obstacle.class);
		if(col != null) {
			Vector2 position2 = getGameObject().transform.position;
			Vector2 size2 = col.getSize();
			int ox1 = (int) (position2.x - size2.x / 2);
			int oy1 = (int) (position2.y - size2.y / 2);
			g.drawRect(ox1, oy1, (int) size2.x, (int) size2.y);
		}
		*/
	}
}
