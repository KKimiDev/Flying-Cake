package game;

import java.awt.Event;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.SwingUtilities;

public class GameState implements KeyListener, MouseListener, WindowFocusListener, ComponentListener, WindowListener {

	static ArrayList<Integer> keyCodes = new ArrayList<Integer>();
	static int screenWidth = 1920 / 2;
	static int screenHeight = 1080 / 2;
	static double speed;
	static long startTime = -1;
	static int coins = 0;
	private static int lastCoins = 0;
	public static boolean isRunning = false;

	private static int highscore = 0;

	public static int getHighScore() {
		return highscore;
	}

	public static double getSpeed() {
		return speed;
	}

	public static void addCoins(int amount) {
		coins += amount;
	}

	public static int getCoins() {
		return coins;
	}

	public static void setSpeed(double d) {
		GameState.speed = d;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		for (int i = 0; i < Game.scene.size(); i++) {
			GameObject obj = Game.scene.getObject(i);
			if (Button.class.isAssignableFrom(obj.getClass())) {
				Transform transform = obj.transform;
				Rectangle rect = new Rectangle(
						(int) ((transform.position.x - transform.scale.x / 2) * Camera.getScale().x),
						(int) ((transform.position.y - transform.scale.y / 2) * Camera.getScale().y),
						(int) (transform.scale.x * Camera.getScale().x),
						(int) (transform.scale.y * Camera.getScale().y));
				if (rect.contains(SwingUtilities.convertPoint(Game.frame, e.getPoint(), Game.cam))) {
					((Button) obj).Click(e);
					return;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < Game.scene.size(); i++) {
			GameObject obj = Game.scene.getObject(i);
			if (Button.class.isAssignableFrom(obj.getClass())) {
				((Button) obj).Release(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (!keyCodes.contains(e.getKeyCode()))
			keyCodes.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		keyCodes.remove((Integer) e.getKeyCode());
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		keyCodes = new ArrayList<Integer>();
	}

	public static boolean isPressed(int keyCode) {
		return keyCodes.contains((Integer) keyCode);
	}

	private static long lastUpdate = System.nanoTime();
	private static double deltaTime = 0;

	public static void Update() {
		long deltaTime_ = System.nanoTime() - lastUpdate;

		long d = 10000000 - deltaTime_;

		if (d > 0) {
			try {
				Thread.sleep(d / 1000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		deltaTime = (System.nanoTime() - lastUpdate) / 1000000000.0;
		lastUpdate = System.nanoTime();
	}

	public static double getDeltaTime() {
		return deltaTime;
	}

	@Override
	public void componentResized(ComponentEvent evt) {
		keyCodes = new ArrayList<Integer>();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		keyCodes = new ArrayList<Integer>();

		Game.updateFullScreen();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		keyCodes = new ArrayList<Integer>();

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		keyCodes = new ArrayList<Integer>();
	}
	
	// Sample path and highscore for demonstration
    private static final String dataPath = "data.fc";
    
    // AES key and IV (hardcoded for simplicity)
    private static final String SECRET_KEY = "1234567890123456"; // 16-byte key for AES-128
    private static final String INIT_VECTOR = "RandomInitVector"; // 16-byte IV for AES CBC mode

    // Method to encrypt data using AES
    private static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(INIT_VECTOR.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(data);
    }

    // Method to decrypt data using AES
    private static byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(INIT_VECTOR.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(data);
    }

    // Method to save the highscore as encrypted bytes
    public static boolean Save() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(dataPath))) {
            // Prepare the data to be saved
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(byteStream);

            // Write the highscore and other game data as integers
            dataStream.writeInt(highscore);
            dataStream.writeInt(Game.getKeyCode1());
            dataStream.writeInt(Game.getKeyCode2());
            dataStream.writeInt(Game.getScreenState());

            // Get the data in byte array
            byte[] dataToEncrypt = byteStream.toByteArray();
            
            // Encrypt the data before saving it
            byte[] encryptedData = encrypt(dataToEncrypt);

            // Save the encrypted data
            dos.write(encryptedData);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving the game data: " + e.getMessage());
            return false;
        }
    }

    // Method to load the highscore from the encrypted file
    public static boolean Load() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(dataPath))) {
            // Read the encrypted data
            byte[] encryptedData = new byte[dis.available()];
            dis.read(encryptedData);

            // Decrypt the data
            byte[] decryptedData = decrypt(encryptedData);

            // Convert the decrypted data back into integers
            ByteArrayInputStream byteStream = new ByteArrayInputStream(decryptedData);
            DataInputStream dataStream = new DataInputStream(byteStream);

            highscore = dataStream.readInt();
            Game.setKeyCode1(dataStream.readInt());
            Game.setKeyCode2(dataStream.readInt());
            Game.setScreenState(dataStream.readInt());

            return true;
        } catch (Exception e) {
            System.err.println("Error loading the game data: " + e.getMessage());
            return false;
        }
    }
    
    
    
    public static int getLastCoins() {
    	
    	return lastCoins;
    }

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		Save();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public static int[] getKeys() {
		// Convert List<Integer> to int[]
		return keyCodes.stream().mapToInt(Integer::intValue).toArray();
	}

	public static void GameOver() {
		if (coins > highscore) {
			highscore = coins;
			Save();
		}

		lastCoins = coins;
		coins = 0;
	}
}
