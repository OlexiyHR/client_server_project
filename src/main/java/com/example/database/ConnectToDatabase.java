package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/warehouse";
    private static final String USER = "root";
    private static final String PASSWORD = "ol12345";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connection has been established.");
        return  connection;
    }
}
