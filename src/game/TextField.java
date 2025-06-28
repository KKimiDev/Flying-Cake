package game;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TextField extends GameObject {
	public JLabel field = new JLabel();
	private boolean isPressed = false;
	
	public TextField(Scene scene, String text) {
		super(scene);
		
		field.setText(text);
		super.setRenderUnit(new RenderUnit() {
			@Override
			public void render(Graphics2D g, double camX, double camY) {
					field.setBounds(0,0, (int) (transform.scale.x*Camera.getScale().x), (int) (transform.scale.y*Camera.getScale().y));
					g.translate((int) (transform.position.x - transform.scale.x / 2) * Camera.getScale().x, (int) (transform.position.y - transform.scale.y / 2) * Camera.getScale().y);
					field.paint(g);
					g.translate(-(int) (transform.position.x - transform.scale.x / 2) * Camera.getScale().x,-(int) (transform.position.y - transform.scale.y / 2) * Camera.getScale().y);
					}
		});
	}
}
