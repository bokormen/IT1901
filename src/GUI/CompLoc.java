package GUI;

import java.awt.Component;

public class CompLoc {
	private Component component;
	private int x;
	private int y;
	private int width;
	private int height;
	private String name;
	private String actionCommand;
	private int state;
	private int state2;

	// trenger kanskje ikke, var en dårlig ide.
	public CompLoc(Component component, int x, int y, int width, int height,
			String name, String actionCommand, int state, int state2) {
		this.component = component;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.actionCommand = actionCommand;
		this.state = state;
		this.state2 = state2;
	}

	public int getState2() {
		return state2;
	}

	public void setState2(int state2) {
		this.state2 = state2;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Component getComponent() {
		return component;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
