package Controladores;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class Pantalla_principal implements Initializable {
    @FXML
    public Text Username;

    private Login login;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("Pantalla_principal inicializado correctamente");
	}
}
