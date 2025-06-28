package game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

public class UI extends RenderUnit {
	String text = "";
	Image img = null;
	
	@Override
	public void render(Graphics2D g, double _X, double _Y) {
		if(img != null) {
			g.drawImage(img, 800, 100, 100, 100, null);
			return;
		}
		
		g.setFont(new Font("Arial", 0, 100));
		g.drawString(text, 800, 100);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setIcon(Image img) {
		this.img = img;
	}
}
