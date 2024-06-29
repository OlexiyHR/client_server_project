package com.example.database.Dao;

import com.example.database.ConnectToDatabase;
import com.example.database.ResultSetToDomain;
import com.example.models.Good;

import java.sql.*;
import java.util.List;

public class GoodDAO {

    public static Good getGood(String product_name) {
        String query = "SELECT * FROM product WHERE product_name = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product_name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return ResultSetToDomain.toGood(resultSet);
            }
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean createGood(Good good) {
        String query = "INSERT INTO product (product_name, description, producer, amount, price, group_name) VALUES (?, ?, ?, 0, ?, ?)";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, good.getProduct_name());
            statement.setString(2, good.getDescription());
            statement.setString(3, good.getProducer());
            statement.setBigDecimal(4, good.getPrice());
            statement.setString(5, good.getGroup_name());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean updateGood(Good good, String old_name) {
        String query = "UPDATE product SET product_name = ?, description = ?, producer = ?, price = ?, group_name = ? WHERE product_name = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, good.getProduct_name());
            statement.setString(2, good.getDescription());
            statement.setString(3, good.getProducer());
            statement.setBigDecimal(4, good.getPrice());
            statement.setString(5, good.getGroup_name());
            statement.setString(6, old_name);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean deleteGood(String product_name) {
        String query = "DELETE FROM product WHERE product_name = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product_name);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean credit(String product_name, int amount) {
        String query = "UPDATE product SET amount = amount + ? WHERE product_name = ?";
        try (Connection conn = ConnectToDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, amount);
            stmt.setString(2, product_name);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean writeOff(String product_name, int amount) {
        String query = "UPDATE product SET amount = amount - ? WHERE product_name = ?";
        try (Connection conn = ConnectToDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, amount);
            stmt.setString(2, product_name);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) return true;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static List<Good> getAllGoods() {
        String query = "SELECT * FROM product";
        try (Connection conn = ConnectToDatabase.getConnection();
             Statement stmt = conn.createStatement()) {
             ResultSet rs = stmt.executeQuery(query);
            return ResultSetToDomain.toGoodList(rs);
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static List<Good> getAllGoodsFromGroup(String group_name) {
        String query = "SELECT * FROM product WHERE group_name = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group_name);
            ResultSet rs = statement.executeQuery();
            return ResultSetToDomain.toGoodList(rs);
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
