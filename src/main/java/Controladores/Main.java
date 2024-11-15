package Controladores;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Font.loadFont(getClass().getResourceAsStream("/Insanibu.ttf"), 12);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        showMainScreen();
        showLoginScreen();
    }

    private void showMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, 600, 500);
            primaryStage.initStyle(StageStyle.DECORATED);
            scene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("BURGER QUEEN");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = loader.load();
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setScene(loginScene);
            loginStage.setTitle("LOGIN");
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
