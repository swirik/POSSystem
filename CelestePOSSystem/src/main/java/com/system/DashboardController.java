package system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;

public class DashboardController {

    @FXML private Button btnHome;
    @FXML private Button btnInventory;
    @FXML private Button btnSales;
    @FXML private Button btnExit;
    @FXML private StackPane mainContent;
    @FXML private Label labelContent;

    @FXML
    public void initialize() {
        btnHome.setOnAction(e -> labelContent.setText("Welcome to Celeste POS!"));
        btnInventory.setOnAction(e -> labelContent.setText("Inventory Management"));
        btnSales.setOnAction(e -> labelContent.setText("Sales Overview"));
        btnExit.setOnAction(e -> Platform.exit());
    }
}
