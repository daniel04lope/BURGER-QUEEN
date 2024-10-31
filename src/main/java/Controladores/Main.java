package Controladores;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Cargar la fuente personalizada
        Font.loadFont(getClass().getResourceAsStream("/Insanibu.ttf"), 12);

        // Configurar el Stage principal como TRANSPARENT
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Mostrar la pantalla principal
        showMainScreen();

        // Mostrar la pantalla de login en un Stage separado
        showLoginScreen();
    }

    private void showMainScreen() {
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));

            // Crear la escena con fondo transparente
            Scene scene = new Scene(root, 600, 500);
            scene.setFill(Color.TRANSPARENT); // Fondo de la escena transparente
            scene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.setScene(scene);
            primaryStage.setTitle("BURGER QUEEN");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoginScreen() {
        try {
            Pane login = FXMLLoader.load(getClass().getResource("/Vistas/Login.fxml"));

            // Crear la escena del login con fondo transparente
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT); // Fondo de la escena transparente

            // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT); // Sin decoración
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
