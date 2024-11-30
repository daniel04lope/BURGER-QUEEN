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

    private Stage principalStage;

    @Override
    public void start(Stage primaryStage) {
        this.principalStage = primaryStage;
        Font.loadFont(getClass().getResourceAsStream("/Insanibu.ttf"), 12);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //Muestramain();
        Muestralogin();
    }

    private void Muestramain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Pantalla-Principal.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, 600, 500);
            principalStage.initStyle(StageStyle.DECORATED);
            
            principalStage.setResizable(false);
            principalStage.setScene(scene);
            principalStage.setTitle("PANTALLA PRINCIPAL");
            principalStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Muestralogin() {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
            Pane login = cargador.load();
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
