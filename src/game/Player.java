package game;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player extends Collider {
	public Player() {
		super(new Vector2(), new Vector2(80, 80));
	}

	double velocity = 0;
	boolean spaceWasPressed = false;
	
	@Override
	public void Start() {
		getGameObject().transform.zIndex = 100;
	}
	
	@Override
	public void Update() {
		if(!GameState.isRunning)
			return;
		GameObject obj = getGameObject();
		Transform transform = obj.transform;

		if (GameState.isPressed(Game.getKeyCode1())) {
			velocity -= GameState.getDeltaTime() * 8000;
		}

		velocity += GameState.getDeltaTime() * 1500;

		if (GameState.isPressed(Game.getKeyCode2()) && dashStart - System.currentTimeMillis() < -400) {
			dashStart = System.currentTimeMillis();
			velocity = 0;
		}

		if (dashStart - System.currentTimeMillis() > -225) {
			getGameObject().transform.position.x += GameState.getDeltaTime() * 2000;
		}

		if (getGameObject().transform.position.y < 0 && velocity < 0)
			velocity *= -0.2;

		if (getGameObject().transform.position.y > 880 - getGameObject().transform.scale.y / 2) {
			if(velocity > 0)
				velocity = 0;
			Die();
		}

		getGameObject().transform.position.y += velocity * GameState.getDeltaTime();

		if (Camera.position.x < transform.position.x - 300) {
			Camera.position.x += 20 * GameState.getDeltaTime()
					* (0.1 + Math.pow(transform.position.x - 300 - Camera.position.x, 0.75));
		} 
	}

	GameManager manager;

	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	long dashStart = 0;

	public void Die() {
		GameState.isRunning = false;
		
		GameState.GameOver();
		
		Game.LoadScene(0);
	}
}
