module Controladores {
    requires javafx.controls;
<<<<<<< HEAD
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
	requires javafx.graphics; // Añade esta línea para que el módulo reconozca ControlFX
    
    opens Controladores to javafx.fxml;
=======
	requires javafx.fxml;
>>>>>>> branch 'Pablo' of https://github.com/daniel04lope/BURGER-QUEEN.git
    exports Controladores;
}
