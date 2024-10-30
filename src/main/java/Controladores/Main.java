package Controladores;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

	public class Main extends Application {
		@Override
		public void start(Stage primaryStage) {
			try {
				 Font.loadFont(getClass().getResourceAsStream("/Insanibu.ttf"), 12);
				AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
				 
				Scene scene = new Scene(root,600,500);
				scene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
				primaryStage.setScene(scene);
				 primaryStage.setTitle("BURGER QUEEN");
				primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void main(String[] args) {
			launch(args);
		}
	}
