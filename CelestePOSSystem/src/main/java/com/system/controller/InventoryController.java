package system.controller;


import system.Inventory;
import system.Product;
import system.utils.DatabaseHelper;


import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.io.IOException;

public class InventoryController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> codeColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;

    @FXML private Button addButton;
    @FXML private Button searchButton;
    @FXML private Button removeButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final Inventory inventory = new Inventory();
    private FilteredList<Product> filteredList;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        // Bind table columns to product properties
        filteredList = new FilteredList<>(Inventory.getProductList(), p -> true);
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Optional: add sample products

        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(productTable.comparatorProperty());

        productTable.setItems(sortedList);
        productTable.setItems(Inventory.getProductList());

        // Button actions
        addButton.setOnAction(e -> handleAdd());
        removeButton.setOnAction(e -> handleRemove());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e ->handleDeleteProduct());
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_product.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(loader.load()));

            AddProductController controller = loader.getController();
            controller.setInventory(inventory); // inventory handles code creation

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEdit() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a product to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_product.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.setScene(new Scene(loader.load()));

            EditProductController controller = loader.getController();
            controller.setProduct(selected);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();

        filteredList.setPredicate(product -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return product.getName().toLowerCase().contains(searchText) ||
                    product.getCode().toLowerCase().contains(searchText);
        });
    }

    private void handleSort() {
        Inventory.sortByName();
    }

    @FXML
    private void handleRemove() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this product?");
            alert.setContentText("Product: " + selectedProduct.getName());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Inventory.getProductList().remove(selectedProduct);
                    // TODO: Also delete from database later
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void refreshTable() {
        productTable.setItems(FXCollections.observableArrayList(DatabaseHelper.getAllProducts()));
    }

    @FXML
    private void handleDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a product to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Product");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean deleted = DatabaseHelper.deleteProductByCode(selected.getCode());
                if (deleted) {
                    refreshTable();
                    showAlert("Deleted", "Product was successfully removed.");
                } else {
                    showAlert("Failed", "Failed to delete product.");
                }
            }
        });
    }
   // boolean updated = DatabaseHelper.updateProduct(oldCode, new Product(
   //         newCode, newName, newPrice, newQty
   // ));


}
