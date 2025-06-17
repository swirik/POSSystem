package system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import java.io.IOException;

public class DashboardController {

    @FXML private Button btnHome;
    @FXML private Button btnInventory;
    @FXML private Button btnSales;
    @FXML private Button btnExit;
    @FXML private StackPane mainContent;
    @FXML private Label labelContent;

    @FXML
    public void initialize() {
        btnHome.setOnAction(e -> showHome());
        btnInventory.setOnAction(e -> loadFXMLIntoMain("/views/inventory.fxml"));
        btnSales.setOnAction(e -> labelContent.setText("Sales Overview"));
        btnExit.setOnAction(e -> System.exit(0));
    }

    private void showHome() {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(labelContent);
        labelContent.setText("Welcome to Celeste POS!");
    }

    private void loadFXMLIntoMain(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/inventory.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
