package system.controller;

import system.Product;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProductController {

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        codeField.setText(product.getCode());
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
    }

    @FXML
    public void handleAdd() {
        try {
            product.setName(nameField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            ((Stage) nameField.getScene().getWindow()).close();
        } catch (Exception e) {
            // Add validation here just like AddProduct if needed
        }
    }
}
