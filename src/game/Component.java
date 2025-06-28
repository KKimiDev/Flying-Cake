package game;

import java.io.IOException;

public class Component {
	private GameObject gameObject;
	
	public GameObject getGameObject() {
		return gameObject;
	}
	
	public void setGameObject(GameObject object) {
		if(gameObject != null)
			gameObject.removeComponent(this);
		
		gameObject = object;
	}
	
	public void Start() {
		
	}
	
	public void Update() {
		
	}
}
