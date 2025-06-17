package system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        Image icon = new Image("src/pos-system.png");
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
