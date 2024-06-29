package com.example.database.Dao;

import com.example.database.ConnectToDatabase;
import com.example.database.ResultSetToDomain;
import com.example.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static User getUserByLogin(String login) {
        String query = "SELECT * FROM user WHERE login = ?";
        try (Connection connection = ConnectToDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return ResultSetToDomain.toUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
