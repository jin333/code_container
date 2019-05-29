package hufs.ces.svgload;

import hufs.ces.grimpan.svg.SVGGrimShape;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class DrawPane extends Pane {

	public ObservableList<SVGGrimShape> shapeList = null;

	public DrawPane() {
		this(800, 600);
	}
	public DrawPane(double width, double height) {
		this.setStyle("-fx-background-color: rgba(255,255,255,0)");
		this.setWidth(width);
		this.setHeight(height);
		this.setPrefWidth(width);
		this.setPrefHeight(height);
		this.shapeList = FXCollections.observableArrayList();
	}
	
	public void clear() {
		this.setStyle("-fx-background-color: rgba(255,255,255,0)");
		this.getChildren().clear();		
	}
	public void redraw() {
		clear();
		//System.out.println("Shape Count="+model.shapeList.size());
		this.setStyle("-fx-background-color: rgba(255,255,255,1)");
		for (SVGGrimShape gsh:this.shapeList){
			this.getChildren().add(gsh.getShape());
		}
	}
}
