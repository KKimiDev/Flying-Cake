package game;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.Font;

public class Game {
	public static JFrame frame;
	public static Camera cam;
	public static Scene scene;
	private static Scene newScene = null;

	private static int screenState = 1;

	public static int getScreenState() {
		return screenState;
	}

	public static Scene[] scenes = new Scene[2];

	public static void LoadScene(int index) {
		if (scenes != null && index >= 0 && index < scenes.length) {
			newScene = scenes[index];
		}
	}

	static GraphicsDevice selectedDevice = null;

	public static void main(String[] args) {
		GameState.Load();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(800, 600);
		if (screenState == 0)
			frame.setSize(1920, 1080);

		frame.setResizable(false);
		frame.setUndecorated(screenState == 2);

		try {
			Start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			isRunning = true;
			run();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static GameState gameState;

	public static boolean isRunning = false;

	private static int keyCode1 = KeyEvent.VK_SPACE;
	private static int keyCode2 = KeyEvent.VK_E;

	public static void Start() throws InterruptedException, IOException {
		cam = new Camera();

		gameState = new GameState();

		frame.add(cam);
		frame.setVisible(true);
		frame.addWindowFocusListener(gameState);
		frame.addKeyListener(gameState);
		frame.addMouseListener(gameState);
		frame.addComponentListener(gameState);
		frame.addWindowListener(gameState);

		cam.updateBounds();

		scenes[0] = new Scene() {
			@Override
			public void Load() {
				super.Load();

				TextField field = new TextField(this, "0") {

					@Override
					public void Start() {
						field.setHorizontalAlignment(JLabel.CENTER);
						field.setFont(new Font("Arial", 0, (int) (100 * Camera.getScale().y)));
					}

					@Override
					public void Update() {
						this.field.setText(
								((GameState.isRunning) ? GameState.getCoins() : GameState.getLastCoins()) + "");
						transform.position.x = (GameState.screenWidth * 1080 / GameState.screenHeight / 2);
					}
				};
				field.transform.zIndex = 10002;
				field.transform.position = new Vector2(1000, 100);
				field.transform.scale = new Vector2(1000, 200);

				TextField field2 = new TextField(this, "HIGHSCORE") {

					@Override
					public void Start() {
						field.setHorizontalAlignment(JLabel.TRAILING);
						field.setFont(new Font("Arial", 0, (int) (50 * Camera.getScale().y)));
					}

					@Override
					public void Update() {
						this.field.setText("BEST: " + GameState.getHighScore());
						transform.position.x = (GameState.screenWidth * 1080 / GameState.screenHeight) - 550;
					}
				};
				field2.transform.zIndex = 10002;
				field2.transform.position = new Vector2(1000, 100);
				field2.transform.scale = new Vector2(1000, 200);

				Image img = null;
				try {
					img = ImageIO.read(new File("rsc\\cake.png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				GameObject player = new GameObject(this);
				player.transform.scale = new Vector2(100, 100);
				player.transform.position = new Vector2(300, 300);
				player.addComponent(new Player());
				player.setRenderUnit(new Sprite(img));

				GameObject terrain = new GameObject(this);

				Image img2 = null;
				try {
					img2 = ImageIO.read(new File("rsc\\ground.png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Terrain terrainR = new Terrain(img2);
				terrain.setRenderUnit(terrainR);

				terrain.transform.position = new Vector2(0, 860);

				GameObject bg = new GameObject(this);

				Image img3 = null;
				try {
					img3 = ImageIO.read(new File("rsc\\bg.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Background bgR = new Background(img3);
				bg.setRenderUnit(bgR);

				bg.transform.zIndex = -10;

				GameObject gameManagerObj = new GameObject(this);
				GameManager gameManager = new GameManager(player.getComponent(Player.class), bgR, terrainR);
				gameManagerObj.addComponent(gameManager);

				GameObject but = new Button(this, "Play") {

					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed())
							GameState.isRunning = true;
						super.Release(e);
					}
				};
				but.transform.zIndex = 10001;
				but.transform.position = new Vector2(400, 550);
				but.transform.scale = new Vector2(200, 100);

				GameObject but2 = new Button(this, "Options") {

					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed()) {
							LoadScene(1);
						}
						super.Release(e);
					}
				};
				but2.transform.zIndex = 10000;
				but2.transform.position = new Vector2(400, 660);
				but2.transform.scale = new Vector2(200, 100);

				GameObject but3 = new Button(this, "Exit") {

					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed()) {
							GameState.Save();
							frame.dispose();
							System.exit(0);
						}
						super.Release(e);
					}
				};
				but3.transform.zIndex = 10000;
				but3.transform.position = new Vector2(400, 770);
				but3.transform.scale = new Vector2(200, 100);

				GameState.setSpeed(500);

				Camera.position = new Vector2();

				int a = 0;
			}
		};

		scenes[1] = new Scene() {
			@Override
			public void Load() {
				super.Load();

				GameObject but = new Button(this, "Back") {
					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed())
							LoadScene(0);
						super.Release(e);
					}
				};
				but.transform.zIndex = 10001;
				but.transform.position = new Vector2(400, 900);
				but.transform.scale = new Vector2(200, 100);

				Button but2 = new Button(this, "1920x1080") {
					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed()) {
							screenState = 0;
							setFullScreen(false);
							Game.frame.setSize(1920, 1080);
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									cam.updateBounds();
								}
							});
						}
						super.Release(e);
					}

					@Override
					public void Update() {
						if (screenState == 0) {
							button.setBackground(Color.GRAY);
						} else {
							button.setBackground(Color.WHITE);
						}
					}
				};
				but2.transform.zIndex = 10001;
				but2.transform.position = new Vector2(750, 200);
				but2.transform.scale = new Vector2(200, 100);
				but2.button.setBackground(Color.BLUE);

				Button but3 = new Button(this, "800x600") {
					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed()) {
							screenState = 1;
							setFullScreen(false);
							Game.frame.setSize(800, 600);

							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									cam.updateBounds();
								}
							});
						}
						super.Release(e);

					}

					@Override
					public void Update() {
						if (screenState == 1) {
							button.setBackground(Color.GRAY);
						} else {
							button.setBackground(Color.WHITE);
						}
					}
				};
				but3.transform.zIndex = 10001;
				but3.transform.position = new Vector2(500, 200);
				but3.transform.scale = new Vector2(200, 100);

				Button but4 = new Button(this, "Fullscreen") {
					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed()) {
							screenState = 2;

							setFullScreen(true);
							updateFullScreen();
						}
						super.Release(e);

					}

					@Override
					public void Update() {
						if (screenState == 2) {
							button.setBackground(Color.GRAY);
						} else {
							button.setBackground(Color.WHITE);
						}
					}
				};
				but4.transform.zIndex = 10001;
				but4.transform.position = new Vector2(1000, 200);
				but4.transform.scale = new Vector2(200, 100);

				Button but5 = new Button(this, "Space") {
					private boolean state = false;

					@Override
					public void Release(MouseEvent e) {
						if (super.IsPressed()) {
							state = !state;
						} else {
							state = false;
						}
						super.Release(e);

					}

					@Override
					public void Start() {
						this.button.setText(KeyEvent.getKeyText(keyCode1));
					}

					@Override
					public void Update() {
						if (state) {
							this.button.setBackground(Color.GRAY);
						} else {
							this.button.setBackground(Color.WHITE);
						}

						if (!state)
							return;

						if (GameState.isPressed(KeyEvent.VK_ESCAPE)) {
							state = false;
							return;
						}

						int[] keys = GameState.getKeys();
						if (keys != null && keys.length > 0) {
							keyCode1 = keys[0];
							this.button.setText(KeyEvent.getKeyText(keyCode1));
							state = false;
						}
					}
				};
				but5.transform.zIndex = 10001;
				but5.transform.position = new Vector2(500, 450);
				but5.transform.scale = new Vector2(200, 100);

				Button but6 = new Button(this, "Caps") {
					private boolean state = false;

					@Override
					public void Release(MouseEvent e) {

						System.out.println(super.IsPressed());
						if (super.IsPressed()) {
							state = !state;
						} else {
							state = false;
						}
						super.Release(e);

					}

					@Override
					public void Start() {
						this.button.setText(KeyEvent.getKeyText(keyCode2));
					}

					@Override
					public void Update() {
						if (state) {
							this.button.setBackground(Color.GRAY);
						} else {
							this.button.setBackground(Color.WHITE);
						}

						if (!state)
							return;

						if (GameState.isPressed(KeyEvent.VK_ESCAPE)) {
							state = false;
							return;
						}

						int[] keys = GameState.getKeys();
						if (keys != null && keys.length > 0) {
							keyCode2 = keys[0];
							this.button.setText(KeyEvent.getKeyText(keyCode2));
							state = false;
						}
					}
				};
				but6.transform.zIndex = 10001;
				but6.transform.position = new Vector2(500, 650);
				but6.transform.scale = new Vector2(200, 100);

				TextField field0 = new TextField(this, "Grafik") {
					@Override
					public void Update() {
						field.setFont(new Font("Arial", 0, (int) (40 * Camera.getScale().y)));
					}
				};
				field0.field.setHorizontalAlignment(JLabel.LEADING);
				field0.transform.zIndex = 10001;
				field0.transform.position = new Vector2(300, 100);
				field0.transform.scale = new Vector2(400, 100);

				TextField field = new TextField(this, "Bildschirmgröße") {
					@Override
					public void Update() {
						field.setFont(new Font("Arial", 0, (int) (25 * Camera.getScale().y)));
					}
				};
				field.field.setFont(new Font("Arial", 0, (int) (25 * Camera.getScale().y)));
				field.field.setHorizontalAlignment(JLabel.LEADING);
				field.transform.zIndex = 10001;
				field.transform.position = new Vector2(300, 200);
				field.transform.scale = new Vector2(400, 100);

				TextField field2 = new TextField(this, "Steuerung") {
					@Override
					public void Update() {
						field.setFont(new Font("Arial", 0, (int) (40 * Camera.getScale().y)));
					}
				};
				field2.field.setFont(new Font("Arial", 0, (int) (40 * Camera.getScale().y)));
				field2.field.setHorizontalAlignment(JLabel.LEADING);
				field2.transform.zIndex = 10001;
				field2.transform.position = new Vector2(300, 350);
				field2.transform.scale = new Vector2(400, 100);

				TextField field3 = new TextField(this, "Fliegen") {
					@Override
					public void Update() {
						field.setFont(new Font("Arial", 0, (int) (25 * Camera.getScale().y)));
					}
				};
				field3.field.setFont(new Font("Arial", 0, (int) (25 * Camera.getScale().y)));
				field3.field.setHorizontalAlignment(JLabel.LEADING);
				field3.transform.zIndex = 10001;
				field3.transform.position = new Vector2(300, 450);
				field3.transform.scale = new Vector2(400, 100);

				TextField field4 = new TextField(this, "Dash") {
					@Override
					public void Update() {
						field.setFont(new Font("Arial", 0, (int) (25 * Camera.getScale().y)));
					}
				};
				field4.field.setFont(new Font("Arial", 0, (int) (25 * Camera.getScale().y)));
				field4.field.setHorizontalAlignment(JLabel.LEADING);
				field4.transform.zIndex = 10001;
				field4.transform.position = new Vector2(300, 650);
				field4.transform.scale = new Vector2(400, 100);
			}
		};

		LoadScene(0);

		if (screenState == 2)
			updateFullScreen();
	}

	public static int getKeyCode1() {
		return keyCode1;
	}

	public static int getKeyCode2() {
		return keyCode2;
	}

	public static void setKeyCode1(int val) {
		keyCode1 = val;
	}

	public static void setKeyCode2(int val) {
		keyCode2 = val;
	}

	public static void setScreenState(int state) {
		if (frame == null)
			screenState = state;
	}

	private static void setFullScreen(boolean state) {
		if (frame.isUndecorated() == state)
			return;

		System.out.println("teest");

		frame.dispose();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(1920, 1080);
		frame.setResizable(false);
		frame.add(cam);
		frame.addWindowFocusListener(gameState);
		frame.addKeyListener(gameState);
		frame.addMouseListener(gameState);
		frame.addComponentListener(gameState);
		frame.addWindowListener(gameState);
		frame.setUndecorated(state);
		frame.setVisible(true);
	}

	public static void updateFullScreen() {
		if (Game.getScreenState() == 2) {
			// Get the current screen size based on the JFrame's location
			Point frameLocation = Game.frame.getLocationOnScreen(); // Get the frame's location on screen
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = ge.getScreenDevices();

			Rectangle screenBounds = new Rectangle(1920, 1080);

			// Loop through all available screen devices and check which one contains the
			// frame's location
			for (GraphicsDevice device : devices) {
				screenBounds = device.getDefaultConfiguration().getBounds();
				if (screenBounds.contains(frameLocation)) {
					Game.frame.setBounds(screenBounds);
					break; // Exit the loop once the correct screen is found
				}
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					cam.updateBounds();
				}
			});
		}
	}

	public static void run() throws InterruptedException {
		isRunning = true;
		GameState.Update();
		while (isRunning) {

			if (newScene != null) {
				scene = newScene;
				newScene = null;
				scene.Load();
				scene.UpdateScene();
				scene.Start();
			}
			GameState.Update();
			scene.UpdateObjects();
			scene.UpdateScene();
			cam.repaint();
		}
	}

}