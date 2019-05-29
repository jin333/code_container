package hufs.ces.svgload;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SVGLoadMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("SVG Load");
		
		SVGLoadController root = new SVGLoadController();
		root.parentStage = primaryStage;
		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
