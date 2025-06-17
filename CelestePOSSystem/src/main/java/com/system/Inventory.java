package system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    public ObservableList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProductByName(String name) {
        return products.removeIf(p -> p.getName().equalsIgnoreCase(name));
    }

    public Product searchByName(String name) {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void sortByName() {
        FXCollections.sort(products, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }
}
