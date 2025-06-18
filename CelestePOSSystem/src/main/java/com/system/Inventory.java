package system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import system.utils.DatabaseHelper;

import java.sql.*;

public class Inventory {
    // A shared list of products for UI binding
    private static final ObservableList<Product> productList = FXCollections.observableArrayList();

    // Automatically load products from DB on startup
    static {
        loadFromDatabase();
    }

    // Auto-generate unique product code (based on max code in DB)
    private static String generateNextProductCode() {
        String sql = "SELECT MAX(code) AS maxCode FROM products";
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String maxCode = rs.getString("maxCode");
                int next = (maxCode != null) ? Integer.parseInt(maxCode) + 1 : 1;
                return String.format("%07d", next);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0000001"; // fallback
    }

    // Add new product
    public static void addProduct(String name, double price, int quantity) {
        String code = generateNextProductCode();
        Product product = new Product(code, name, price, quantity);
        addProduct(product); // call the overloaded method below
    }

    // Add product to database and list
    public static void addProduct(Product product) {
        String sql = "INSERT INTO products (code, name, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getCode());
            pstmt.setString(2, product.getName());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());
            pstmt.executeUpdate();
            productList.add(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load products from DB into observable list
    public static void loadFromDatabase() {
        productList.clear();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get product list for TableView
    public static ObservableList<Product> getProductList() {
        return productList;
    }

    // Remove product by name (case-insensitive)
    public static boolean removeProductByName(String name) {
        Product product = productList.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        if (product != null) {
            String sql = "DELETE FROM products WHERE code = ?";
            try (Connection conn = DatabaseHelper.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, product.getCode());
                pstmt.executeUpdate();
                productList.remove(product);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Search product by name
    public static Product searchByName(String name) {
        return productList.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Sort list alphabetically
    public static void sortByName() {
        FXCollections.sort(productList, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }
}
