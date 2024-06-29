package com.example.database.Dao;

import com.example.database.ConnectToDatabase;
import com.example.database.ResultSetToDomain;
import com.example.models.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO {

    public static Group getGroup(String group_name) {
        String query = "SELECT * FROM product_group WHERE group_name = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group_name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return ResultSetToDomain.toGroup(resultSet);
            }
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean createGroup(Group group) {
        String query = "INSERT INTO product_group (group_name, description) VALUES (?, ?)";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group.getGroup_name());
            statement.setString(2, group.getDescription());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean updateGroup(Group group, String old_name) {
        String query = "UPDATE product_group SET group_name=?, description = ? WHERE group_name = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group.getGroup_name());
            statement.setString(2, group.getDescription());
            statement.setString(3, old_name);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean deleteGroup(String group_name) {
        String query = "DELETE FROM product_group WHERE group_name = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group_name);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
