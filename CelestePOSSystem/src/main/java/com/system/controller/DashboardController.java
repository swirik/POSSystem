package system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private Parent inventoryView;
    private InventoryController inventoryController;

    private Parent salesView;
    private SalesController salesController;



    @FXML
    public void initialize() throws IOException {
        FXMLLoader inventoryLoader = new FXMLLoader(getClass().getResource("/inventory.fxml"));
        inventoryView = inventoryLoader.load();
        inventoryController = inventoryLoader.getController();

        FXMLLoader salesLoader = new FXMLLoader(getClass().getResource("/sales.fxml"));
        salesView = salesLoader.load();
        salesController = salesLoader.getController();

        salesController.setInventoryController(inventoryController);

        btnHome.setOnAction(e -> showHome());
        btnInventory.setOnAction(e -> loadInventory());
        btnSales.setOnAction(e -> loadSales());
        btnExit.setOnAction(e -> System.exit(0));

        showHome();
    }

    private void showHome() {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(labelContent);
        labelContent.setText("(Home Screen)");
    }

    private void loadInventory() {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(inventoryView);
    }

    private void loadSales() {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(salesView);
    }

}
