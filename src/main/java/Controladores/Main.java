package Controladores;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

public class Main extends Application {

    private Stage principalStage; // Stage principal de la aplicación

    @Override
    public void start(Stage primaryStage) {
        // Método que se ejecuta al iniciar la aplicación
        this.principalStage = primaryStage; // Asignar el stage principal
        Font.loadFont(getClass().getResourceAsStream("/Insanibu.ttf"), 12); // Cargar la fuente personalizada
        primaryStage.initStyle(StageStyle.TRANSPARENT); // Establecer el estilo de la ventana como transparente
        // Muestramain(); // Llamar al método para mostrar la pantalla principal (descomentado si se desea mostrar)
        Muestralogin(); // Mostrar la ventana de login
    }

    private void Muestramain() {
        // Método para mostrar la pantalla principal
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml")); // Cargar el FXML de la pantalla principal
            AnchorPane root = loader.load(); // Cargar el layout en un AnchorPane
            Scene scene = new Scene(root, 600, 500); // Crear la escena con el layout y dimensiones
            principalStage.initStyle(StageStyle.DECORATED); // Establecer el estilo de la ventana como decorado
            principalStage.getIcons().add(new Image(getClass().getResourceAsStream("@/icono.png")));

          
            principalStage.setResizable(false); // No permitir el redimensionamiento de la ventana
            principalStage.setScene(scene); // Asignar la escena al stage
            principalStage.setTitle("PANTALLA PRINCIPAL"); // Título de la ventana
            principalStage.show(); // Mostrar la ventana
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    private void Muestralogin() {
        // Método para mostrar la ventana de login
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml")); // Cargar el FXML de la ventana de login
            Pane login = cargador.load(); // Cargar el layout en un Pane
            Scene loginScene = new Scene(login, 450, 600); // Crear la escena con el layout y dimensiones
            loginScene.setFill(Color.TRANSPARENT); // Establecer el fondo de la escena como transparente
            Stage loginStage = new Stage(); // Crear un nuevo stage para el login
            loginStage.initStyle(StageStyle.TRANSPARENT); // Establecer el estilo de la ventana como transparente
            loginStage.initModality(Modality.APPLICATION_MODAL); // Hacer que la ventana de login sea modal
            loginStage.setScene(loginScene); // Asignar la escena al stage
            loginStage.setTitle("LOGIN"); // Título de la ventana de login
            loginStage.show(); // Mostrar la ventana de login
           
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    public static void main(String[] args) {
        launch(args); // Método principal que lanza la aplicación
    }
}