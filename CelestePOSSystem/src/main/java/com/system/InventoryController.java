package system;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import system.Inventory;
import system.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class InventoryController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;

    @FXML private Button addButton;
    @FXML private Button searchButton;
    @FXML private Button sortButton;
    @FXML private Button removeButton;

    private final Inventory inventory = new Inventory();

    @FXML
    public void initialize() {
        // Bind table columns to product properties
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Optional: add sample products
        inventory.addProduct(new Product("Hammer", 120.0, 10));
        inventory.addProduct(new Product("Screwdriver", 80.0, 15));
        inventory.addProduct(new Product("Wire", 50.0, 100));

        productTable.setItems(inventory.getProducts());

        // Button actions
        addButton.setOnAction(e -> handleAdd());
        searchButton.setOnAction(e -> handleSearch());
        sortButton.setOnAction(e -> handleSort());
        removeButton.setOnAction(e -> handleRemove());
    }

    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_product.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(loader.load()));

            AddProductController controller = loader.getController();
            controller.setInventory(inventory); // Pass inventory instance

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void handleSearch() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Product");
        dialog.setHeaderText("Enter product name to search:");
        dialog.showAndWait().ifPresent(name -> {
            Product result = inventory.searchByName(name);
            if (result != null) {
                productTable.getSelectionModel().select(result);
            } else {
                showAlert("Not found", "Product not found.");
            }
        });
    }

    private void handleSort() {
        inventory.sortByName();
    }

    private void handleRemove() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            inventory.removeProductByName(selected.getName());
        } else {
            showAlert("No selection", "Please select a product to remove.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
