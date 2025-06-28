package game;

import java.util.ArrayList;

public class GameObject {
	public Transform transform;
	public RenderUnit renderUnit;
	
	private boolean enabled = true;
	private boolean scaledByCam = true;
	
	public GameObject() {
		transform = new Transform();
		addComponent(transform);
		renderUnit = null;
		Game.scene.addObject(this);
	}
	
	public GameObject(Scene scene) {
		transform = new Transform();
		addComponent(transform);
		renderUnit = null;
		scene.addObject(this);
	}
	
	private ArrayList<Component> components = new ArrayList<Component>();
	
	public <T extends Component> T getComponent(Class<T> type) {
		for (int i = 0; i < components.size(); i++) {
			if(components.get(i).getClass().equals(type))
				return (T) components.get(i);
		}
		
		return null;
	}
	
	public boolean addComponent(Component component) {
		if(getComponent(component.getClass()) != null || component.getClass() == Component.class)
			return false;
		
		components.add(component);	
		component.setGameObject(this);
		
		return true;
	}
	
	public boolean removeComponent(Component component) {
		return components.remove(component);
	}
	
	public void setRenderUnit(RenderUnit renderUnit) {
		this.renderUnit = renderUnit;
		addComponent(renderUnit);
	}
	
	public void Start() {
		for(int i = 0; i < components.size(); i++) {
			components.get(i).Start();
		}
	}
	
	public void Update() {
		for(int i = 0; i < components.size(); i++) {
			components.get(i).Update();
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean state) {
		enabled = state;
	}
	
	public boolean isScaledByCam() {
		return scaledByCam;
	}
	
	public void setScaledByCam(boolean state) {
		scaledByCam = state;
	}
}
