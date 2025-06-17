package system;

import system.Inventory;
import system.Product;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProductController {

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;

    private Inventory inventory;

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @FXML
    public void handleAdd() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        inventory.addProduct(new Product(name, price, quantity));

        // Close the dialog
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
