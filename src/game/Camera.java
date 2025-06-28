package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class Camera extends JPanel {
	public static Vector2 position = new Vector2(0,0);
	private static Vector2 scale = new Vector2(1,1);
	
	public void updateBounds() {
		Rectangle bounds = Game.frame.getContentPane().getBounds();
		
		double scale = bounds.height / 1080.0;
    	setScale(new Vector2(scale, scale));
    	GameState.screenWidth = bounds.width;
    	GameState.screenHeight = bounds.height;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Scene scene = Game.scene;
		
		if(scene == null) {
			return;
		}
		
		// Cast the Graphics object to Graphics2D for better control
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smooth drawing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set rendering hints for better image quality
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        
        //long longest = 0;
        //String name = "";
        
		super.paintComponent(g);
		for (int i = 0; i < scene.size(); i++) {
			RenderUnit renderUnit = scene.getObject(i).renderUnit;
			if(renderUnit != null && renderUnit.getGameObject().isEnabled()) {
				//long d = System.currentTimeMillis();
				renderUnit.render(g2d, position.x, position.y);
				/*
				if(System.currentTimeMillis() - d > longest)  {
					longest = System.currentTimeMillis() - d;
					name =  renderUnit.getClass().getName();
				}
				*/
			}
		}
		//System.out.println(name);
		//System.out.println(longest);
	}
	
	public static Vector2 getScale() {
		return new Vector2(scale.x, scale.y);
	}
	
	public static void setScale(Vector2 scale) {
		Camera.scale = scale;
	}
}
