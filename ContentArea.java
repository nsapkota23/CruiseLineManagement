package application;

import javafx.scene.layout.VBox;
public abstract class ContentArea {
	protected VBox content;
	
	public VBox getContent() {
		return content;
	}
	
	public abstract void initialize();
	
}
