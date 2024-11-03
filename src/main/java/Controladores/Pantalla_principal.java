package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

public class Pantalla_principal implements Initializable {
    @FXML
     Text Username;

    @FXML
    private AnchorPane drawer;

    @FXML
    private Button Desplegable;
    @FXML
    private Button Cerrar;

    private boolean drawerVisible = false;
    private boolean Cerrardesplegar = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Muestra un mensaje en consola para asegurar que el controlador se cargó correctamente
        System.out.println("Pantalla_principal inicializado correctamente");

        // Configura el botón para que al hacer clic cambie la visibilidad del drawer
       
    }

    public void toggleDrawer() {
    	System.out.println("Funciona");
    	Cerrardesplegar=!Cerrardesplegar;
        drawerVisible = !drawerVisible;
        Cerrar.setVisible(Cerrardesplegar);
        drawer.setVisible(drawerVisible);
    }
    
}
