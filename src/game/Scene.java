package game;

import java.util.ArrayList;

public class Scene {
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	
	private ArrayList<GameObject> addObjects = new ArrayList<GameObject>();
	
	private ArrayList<GameObject> removeObjects = new ArrayList<GameObject>();
	
	public void addObject(GameObject object) {
		addObjects.add(object);
	}
	
	public GameObject getObject(int i) {
		return objects.get(i);
	}
	
	public int size() {
		return objects.size();
	}
	
	public void Start() {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).Start();
		}
	}
	
	public void UpdateObjects() {
		for (GameObject obj : objects) {
			if(obj.isEnabled())
				obj.Update();
		}
	}
	
	public void removeObject(GameObject object) {
		removeObjects.add(object);
	}
	
	public void UpdateScene() {
		for(int i = addObjects.size() - 1; i >= 0; i--) {
			if(!objects.contains(addObjects.get(i))) {
				objects.add(addObjects.get(i));
				updateZIndex(addObjects.get(i));
			}
			addObjects.remove(i);
		}
		
		for(int i = removeObjects.size() - 1; i >= 0; i--) {
			objects.remove(removeObjects.get(i));
			removeObjects.remove(i);
		}
	}
	
	public boolean updateZIndex(GameObject object) {
		if(object.transform == null)
			return false;
		if(!objects.remove(object)) {
			return false;
		}
		
		int index = object.transform.zIndex;
		for(int i = 0; i < objects.size(); i++) {
			if(objects.get(i).transform != null && objects.get(i).transform.zIndex >= index) {
				objects.add(i, object);
				return true;
			}
		}
		
		objects.add(object);
		return true;
	}

	public void Load() {
		objects = new ArrayList<GameObject>();
		addObjects = new ArrayList<GameObject>();
		removeObjects = new ArrayList<GameObject>();
	}
}
