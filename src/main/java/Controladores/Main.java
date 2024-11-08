package Controladores;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

        // Mostrar la pantalla principal y la pantalla de login
        Pantalla_principal pantallaPrincipalController = showMainScreen();
        showLoginScreen();
    }

    private Pantalla_principal showMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
            AnchorPane root = loader.load();

            // Obtener el controlador de Pantalla_principal
            Pantalla_principal pantallaPrincipalController = loader.getController();
           
         
            // Crear la escena con fondo transparente
            Scene scene = new Scene(root, 600, 500);
            
            primaryStage.initStyle(StageStyle.DECORATED);
            scene.getStylesheets().add(getClass().getResource("Pantalla_Principal.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("BURGER QUEEN");
            primaryStage.show();

            return pantallaPrincipalController;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = loader.load();
         
         
            // Crear la escena del login con fondo transparente
            Scene loginScene = new Scene(login, 450, 600);
            loginScene.setFill(Color.TRANSPARENT);

            // Crear un nuevo Stage para el login y configurarlo sin decoración y transparente
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
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
