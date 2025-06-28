package game;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class Button extends GameObject {
	public JButton button = new JButton();
	private boolean isPressed = false;
	
	public Button(Scene scene, String text) {
		super(scene);
		
		button.setText(text);
		super.setRenderUnit(new RenderUnit() {
			@Override
			public void render(Graphics2D g, double camX, double camY) {
				if(new Rectangle((int) ((transform.position.x - transform.scale.x / 2) * Camera.getScale().x), (int) ((transform.position.y - transform.scale.y / 2) * Camera.getScale().y), (int) (transform.scale.x * Camera.getScale().x), (int) (transform.scale.y * Camera.getScale().y)).contains(SwingUtilities.convertPoint(Game.frame, (Game.frame.getMousePosition() == null) ? (new Point(-1,-1)) : (Game.frame.getMousePosition()), Game.cam)))
				{
					button.getModel().setRollover(true);
				} else {
					button.getModel().setRollover(false);
				}
				if(!GameState.isRunning){
					button.getModel().setPressed(isPressed);
					button.getModel().setArmed(isPressed);
					button.setBounds(0,0, (int) (transform.scale.x*Camera.getScale().x), (int) (transform.scale.y*Camera.getScale().y));
					g.translate((int) (transform.position.x - transform.scale.x / 2) * Camera.getScale().x, (int) (transform.position.y - transform.scale.y / 2) * Camera.getScale().y);
					button.paint(g);
					g.translate(-(int) (transform.position.x - transform.scale.x / 2) * Camera.getScale().x,-(int) (transform.position.y - transform.scale.y / 2) * Camera.getScale().y);
				}
			}
		});
	}
	
	public void Click(MouseEvent evt) {
		isPressed = true;
	}
	
	public void Release(MouseEvent evt) {
		isPressed = false;
	}
	
	public boolean IsPressed() {
		return isPressed;
	}
}
