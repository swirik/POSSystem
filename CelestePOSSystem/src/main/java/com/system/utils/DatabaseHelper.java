// utils/DatabaseHelper.java
package system.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import system.Product;


public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:celeste_pos.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    public static Product getProductByCode(String code) {
        String query = "SELECT * FROM products WHERE code = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateProductQuantity(String code, int quantityChange) {
        String query = "UPDATE products SET quantity = quantity + ? WHERE code = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantityChange);
            stmt.setString(2, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();

        String query = "SELECT * FROM products";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

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

        return productList;
    }


}
