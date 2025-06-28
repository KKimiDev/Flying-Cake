package game;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameManager extends Component {
	private Player player;
	private Background bg;
	private Terrain terrain;
	
	private TextField coinsUI;
	
	private GameObject[] holes = new GameObject[5];
	private GameObject[] forks = new GameObject[5];
	private GameObject[] coins = new GameObject[40];
	
	static AudioInputStream audioIn = null;
	static Clip coinSound = null;
	
	static void loadCoinSound(String soundFile) // todo sound manager ?? -> must close?
			throws LineUnavailableException, MalformedURLException, UnsupportedAudioFileException, IOException {
		File f = new File(soundFile);
		audioIn = AudioSystem.getAudioInputStream(f);
		coinSound = AudioSystem.getClip();
	}
	
	static void playCoinSound(String soundFile) {
		File f = new File(soundFile);
		AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(f);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(audioIn);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clip.start();
	}
	
	void playSound(String soundFile)
			throws LineUnavailableException, MalformedURLException, UnsupportedAudioFileException, IOException {
		File f = new File(soundFile);
		System.out.println(f.canRead());
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.start();
	}
	
	@Override
	public void Start() {
		lastObstacleShift = 0;
		lastObstacleCam = 0;
		GameState.setSpeed(500);
		Image forkImg = null;
		try {
			forkImg = ImageIO.read(new File("rsc\\fork.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Image holeImg = null;
		try {
			holeImg = ImageIO.read(new File("rsc\\hole.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Image[] coinSprites = null;
		try {
			coinSprites = new Image[] {
					ImageIO.read(new File("rsc\\coin1.png")),
					ImageIO.read(new File("rsc\\coin2.png")),
					ImageIO.read(new File("rsc\\coin3.png")),
					ImageIO.read(new File("rsc\\coin4.png")),
					ImageIO.read(new File("rsc\\coin5.png")),
					ImageIO.read(new File("rsc\\coin6.png"))
				};
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			loadCoinSound("rsc\\coin.wav");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < 5; i++)
		{
			holes[i] = new GameObject();
			forks[i] = new GameObject();
			
			// Holes
			holes[i].setEnabled(false);
			holes[i].addComponent(new Obstacle(new Vector2(), new Vector2(250, 250), player));
			holes[i].transform.scale = new Vector2(250, 250);
			holes[i].transform.zIndex = 120;
			holes[i].setRenderUnit(new Sprite(holeImg));
			
			// Forks
			forks[i].setEnabled(false);
			forks[i].addComponent(new Fork(new Vector2(), new Vector2(250, 250), player));
			forks[i].transform.scale = new Vector2(250, 250);
			forks[i].transform.zIndex = 120;
			forks[i].setRenderUnit(new Sprite(forkImg));
			
			// Coins
			for(int k = 0; k < 8; k++) {
				coins[8*i+k] = new GameObject();
				coins[8*i+k].setEnabled(false);
				coins[8*i+k].setRenderUnit(new Sprite(coinSprites, 40));
				coins[8*i+k].transform.scale = new Vector2(60,60);
				coins[8*i+k].addComponent(new Obstacle(new Vector2(), new Vector2(60, 60), player));
			}
		}
	}
	
	public GameManager(Player player, Background bg, Terrain terrain) {
		this.player = player;
		this.bg = bg;
		this.terrain = terrain;
		
		player.setGameManager(this);
		
		coinsUI = new TextField(Game.scene, GameState.getCoins() + "");
	}
	
	int forkIndex = 0;
	
	void GenerateFork() {
		forks[forkIndex].transform.position = new Vector2(2120 + Camera.position.x, 800);
		forks[forkIndex].setEnabled(true);
		forkIndex = (forkIndex + 1) % 5;
	}
	
	
	int holeIndex = 0;
	
	void GenerateHole(int height) {
		holes[holeIndex].transform.position = new Vector2(2120 + Camera.position.x, height);
		holes[holeIndex].setEnabled(true);
		holeIndex = (holeIndex + 1) % 5;
	}
	
	
	int coinIndex = 0;
	
	void GenerateCoins(int obstacleY, boolean fork) {
		int rand = 60 + (int) ((Math.random()) * ((fork)?400:300)); 
		if(rand > obstacleY - 200)
			rand += 400; 
		
		int x = (int) (2120 + Camera.position.x);
		
		coins[coinIndex*8+0].transform.position = new Vector2(x - 90, rand - 30);
		coins[coinIndex*8+1].transform.position = new Vector2(x - 30, rand - 30);
		coins[coinIndex*8+2].transform.position = new Vector2(x + 30, rand - 30);
		coins[coinIndex*8+3].transform.position = new Vector2(x + 90, rand - 30);
		
		coins[coinIndex*8+4].transform.position = new Vector2(x - 90, rand + 30);
		coins[coinIndex*8+5].transform.position = new Vector2(x - 30, rand + 30);
		coins[coinIndex*8+6].transform.position = new Vector2(x + 30, rand + 30);
		coins[coinIndex*8+7].transform.position = new Vector2(x + 90, rand + 30);
		
		for(int k = 0; k < 8; k++) {
			coins[8*coinIndex+k].setEnabled(true);
		}
		
		coinIndex = (coinIndex + 1) % 5;
	}
	
	private double lastObstacleShift = 0;
	private double lastObstacleCam = Camera.position.x;
	
	private float rand = 1;
	
	@Override
	public void Update() {
		bg.getGameObject().transform.position.x -= GameState.getDeltaTime() * 10;
		if(!GameState.isRunning)
			return;
		
		if(GameState.getSpeed() < 2000)
    		GameState.setSpeed(GameState.getSpeed() + GameState.getDeltaTime() * 10);
		
		double shift = -GameState.getSpeed() * GameState.getDeltaTime();
		Shift(shift);
		
		lastObstacleShift += shift;
		
		if(lastObstacleShift + lastObstacleCam - Camera.position.x < -2000 + 800 * rand) {
			rand = (float) Math.random();
			GenerateObstacle();
			
			lastObstacleShift = 0;
			lastObstacleCam = Camera.position.x;
		}
		
		
		
		


		for (int i = 0; i < 5; i++) {
			if (player.collidesWith(forks[i].getComponent(Fork.class)) || player.collidesWith(holes[i].getComponent(Obstacle.class))) {
				player.Die();
			}
		}

		for (int i = 39; i >= 0; i--) {
			if(!coins[i].isEnabled())
				continue;
			if (player.collidesWith(coins[i].getComponent(Obstacle.class))) {
				coins[i].setEnabled(false);
				playCoinSound("rsc\\coin.wav");
				GameState.addCoins(1);
			}
		}
	}
	
	void GenerateObstacle() {
		int height = (int) (100 + Math.random() * 400);
		if(Math.random() > 0.5) {
			GenerateHole(height);
			GenerateCoins(height, false);
		}
		else {
			GenerateFork();
			height = 820;
			GenerateCoins(height, true);
		}
	}
	
	public void Shift(double shift) {
		terrain.getGameObject().transform.position.x += shift;
		bg.getGameObject().transform.position.x += shift / 4;
		
		for(GameObject obst : holes) {
			obst.getComponent(Obstacle.class).Shift(shift);
		}
		
		for(GameObject obst : forks) {
			obst.getComponent(Fork.class).Shift(shift);
		}
		
		for(GameObject obst : coins) {
			obst.getComponent(Obstacle.class).Shift(shift);
		}
	}
}
