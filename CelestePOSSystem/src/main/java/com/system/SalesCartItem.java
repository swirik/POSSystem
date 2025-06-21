package system;

import javafx.beans.property.*;

public class SalesCartItem {
    private final StringProperty code;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final DoubleProperty subtotal;

    public SalesCartItem(String code, String name, double price, int quantity) {
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.subtotal = new SimpleDoubleProperty(price * quantity);
    }

    public String getCode() { return code.get(); }
    public void setCode(String value) { code.set(value); }
    public StringProperty codeProperty() { return code; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    public double getPrice() { return price.get(); }
    public void setPrice(double value) { price.set(value); }
    public DoubleProperty priceProperty() { return price; }

    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int value) {
        quantity.set(value);
        setSubtotal(getPrice() * value);
    }
    public IntegerProperty quantityProperty() { return quantity; }

    public double getSubtotal() { return subtotal.get(); }
    public void setSubtotal(double value) { subtotal.set(value); }
    public DoubleProperty subtotalProperty() { return subtotal; }
}
