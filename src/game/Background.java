package game;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Background extends RenderUnit {
	private Image image;
	private int widthHeightRatio = 2;
	
	public Background(Image image) {
		setImage(image);
	}
	
	public Background() {
		image = null;
	}
	
	public Image getImage() { return image; }
	
	public void setImage(Image image) { this.image = image; }
	
	int count = 0;
	
	@Override
	public void Update() {
		getGameObject().transform.position.x %= (int) (widthHeightRatio * GameState.screenHeight * Camera.getScale().x);
	}
	
	private Image scaledInstance = null;
	
	int lastScaleX = 0;
	int lastScaleY = 0;
	
	private BufferedImage tiledImage = null;
	
	private void scaleImage() {
		int scaleX = (int) (GameState.screenHeight * widthHeightRatio * Camera.getScale().x);
		int scaleY = (int) (GameState.screenHeight * Camera.getScale().y);
		
		if(scaleX == lastScaleX && scaleY == lastScaleY) {
			return;
		}
		
		lastScaleX = scaleX;
		lastScaleY = scaleY;
		
		scaledInstance = image.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
		
		// Calculate how many images are needed in both the x and y directions
        int imgWidth = scaleX;
        int imgHeight = scaleY;
		
		// Create a new buffered image with the panel's size
        tiledImage = new BufferedImage(imgWidth * 4, imgHeight * 4, BufferedImage.TYPE_INT_ARGB);
		
		// Graphics2D object to draw the image on the tiledImage
        Graphics2D g2d = tiledImage.createGraphics();
        
        // Tile the image once onto the tiledImage
        for (int x = 0; x < 4 * imgWidth; x += imgWidth) {
            for (int y = 0; y < 4 * imgHeight; y += imgHeight) {
                g2d.drawImage(scaledInstance, x, y, null);
            }
        }
		
		// Dispose the Graphics2D object after tiling is done
        g2d.dispose();
	}
	
	@Override
	public void render(Graphics2D g, double camX, double camY) {
		Transform transform = getGameObject().transform;
		
		double width = (GameState.screenHeight * widthHeightRatio * Camera.getScale().x);
		
		int x = (int) (transform.position.x - camX/4) % (int) width; 
		
		scaleImage();
		g.drawImage(tiledImage, x + (int) (GameState.screenHeight * widthHeightRatio * Camera.getScale().x * (-1)), (int) (-camY/4 + GameState.screenHeight * Camera.getScale().y * (-1)), null);
		
	}
}
