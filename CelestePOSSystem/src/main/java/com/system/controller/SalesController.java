package system.controller;
import javafx.application.Platform;
import system.SalesCartItem;
import system.Product;
import system.utils.DatabaseHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SalesController {

    public Button addToCartButton;
    public Button completeSaleButton;

    @FXML private TextField productCodeField;
    @FXML private TextField quantityField;
    @FXML private TableView<SalesCartItem> cartTable;
    @FXML private TableColumn<SalesCartItem, String> codeColumn;
    @FXML private TableColumn<SalesCartItem, String> nameColumn;
    @FXML private TableColumn<SalesCartItem, Number> priceColumn;
    @FXML private TableColumn<SalesCartItem, Number> quantityColumn;
    @FXML private TableColumn<SalesCartItem, Number> subtotalColumn;
    @FXML private Label totalLabel;

    private final ObservableList<SalesCartItem> cartItems = FXCollections.observableArrayList();
    private InventoryController inventoryController;

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(data -> data.getValue().codeProperty());
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
        subtotalColumn.setCellValueFactory(data -> data.getValue().subtotalProperty());

        cartTable.setItems(cartItems);
        updateTotal();

        Platform.runLater(() -> productCodeField.requestFocus());
        productCodeField.setOnAction(e -> {handleAddScannedProduct();});
    }

    public void setInventoryController(InventoryController controller) {
        this.inventoryController = controller;
    }

    @FXML
    private void handleAddToCart() {
        String code = productCodeField.getText().trim();
        String quantityText = quantityField.getText().trim();
   //     quantityField.setDisable(true);
   //     quantityField.setVisible(false);
        if (code.isEmpty() || quantityText.isEmpty()) {
            showAlert("Missing Input", "Please enter both product code and quantity.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Invalid Quantity", "Quantity must be a positive number.");
            return;
        }

        // You can enhance this with actual database lookup.
        Product product = DatabaseHelper.getProductByCode(code);
        if (product == null) {
            showAlert("Product Not Found", "No product found with code: " + code);
            return;
        }

        if (product.getQuantity() < quantity) {
            showAlert("Insufficient Stock", "Not enough stock available.");
            return;
        }

        boolean found = false;
        for (SalesCartItem item : cartItems) {
            if (item.getCode().equals(product.getCode())) {
                int newQty = item.getQuantity() + quantity;

                if (product.getQuantity() < newQty) {
                    showAlert("Insufficient Stock", "Only " + product.getQuantity() + " left in stock.");
                    return;
                }

                item.setQuantity(newQty);
                item.setSubtotal(product.getPrice() * newQty);
                found = true;
                break;
            }
        }

        // ✅ If not in cart, add it
        if (!found) {
            cartItems.add(new SalesCartItem(
                    product.getCode(), product.getName(), product.getPrice(), quantity
            ));
        }
        updateTotal();
        productCodeField.clear();
        quantityField.clear();
    }
    private void handleAddScannedProduct() {
        String code = productCodeField.getText().trim();
        if (code.isEmpty()) return;

        Product product = DatabaseHelper.getProductByCode(code);
        if (product == null) {
            showAlert("Product Not Found", "No product found with code: " + code);
            productCodeField.clear();
            return;
        }

        // 🔍 Check if product is already in cart
        for (SalesCartItem item : cartItems) {
            if (item.getCode().equals(product.getCode())) {
                int newQty = item.getQuantity() + 1;

                if (newQty > product.getQuantity()) {
                    showAlert("Insufficient Stock", "Only " + product.getQuantity() + " available.");
                    productCodeField.clear();
                    return;
                }

                item.setQuantity(newQty);
                item.setSubtotal(newQty * item.getPrice());
                updateTotal();
                productCodeField.clear();
                Platform.runLater(() -> productCodeField.requestFocus());
                return;
            }
        }

        // 🆕 If not in cart, add as new row
        if (product.getQuantity() < 1) {
            showAlert("Out of Stock", "This product is currently out of stock.");
            productCodeField.clear();
            return;
        }

        cartItems.add(new SalesCartItem(
                product.getCode(), product.getName(), product.getPrice(), 1
        ));

        updateTotal();
        productCodeField.clear();
        Platform.runLater(() -> productCodeField.requestFocus());
    }




    @FXML
    private void handleCompleteSale() {
        if (cartItems.isEmpty()) {
            showAlert("Cart Empty", "No items in cart to complete the sale.");
            return;
        }

        for (SalesCartItem item : cartItems) {
            DatabaseHelper.updateProductQuantity(item.getCode(), -item.getQuantity()); // Deduct quantity
        }

        showAlert("Sale Completed", "Transaction complete. Thank you!");
        cartItems.clear();
        updateTotal();
        if (inventoryController != null) {
            inventoryController.refreshTable(); // 🔁 refresh inventory screen
        }
    }

    @FXML
    private void handleClearCart() {
        cartItems.clear();
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (SalesCartItem item : cartItems) {
            total += item.getSubtotal();
        }
        totalLabel.setText(String.format("Total: ₱%.2f", total));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void requestFocusOnBarcodeField() {
        Platform.runLater(() -> productCodeField.requestFocus());
    }


}
