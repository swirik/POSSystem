module system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens system to javafx.fxml;
    exports system;
    exports system.utils;
    exports system.controller to javafx.fxml;
    opens system.controller;
    opens system.utils to javafx.fxml;
}