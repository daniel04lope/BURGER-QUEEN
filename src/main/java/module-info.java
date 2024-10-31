module Controladores {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	requires java.desktop;
    
    opens Controladores to javafx.fxml; // Permite que javafx.fxml acceda a los miembros de Controladores
    exports Controladores; // Exporta el paquete Controladores
}
