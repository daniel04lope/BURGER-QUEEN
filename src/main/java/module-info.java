module Controladores {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls; // Añade esta línea para que el módulo reconozca ControlFX
    
    opens Controladores to javafx.fxml;
    exports Controladores;
}
