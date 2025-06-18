package system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import system.utils.DatabaseInitializer;

import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initialize();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/form-style.css").toExternalForm());
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/pos-system.png")).toString());
        stage.getIcons().add(icon);
        stage.setTitle("Celeste POS Dashboard");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// INSTRUCTIONS

// 1. Search by Code
// 2. Sorting System (Price, Z-A, Quantity)
// 3. Move a product up and down
// 4. When you search by name, as you type a letter the list updates.
// 5. Full CRUD functionality
// 6.