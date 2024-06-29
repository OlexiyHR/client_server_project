package com.example.database;

import com.example.models.Group;
import com.example.models.User;
import com.example.models.Good;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetToDomain {

    public static User toUser(ResultSet resultSet) throws SQLException {
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        return new User(login, password);
    }

    public static Good toGood(ResultSet resultSet) throws SQLException {
        String product_name = resultSet.getString("product_name");
        String description = resultSet.getString("description");
        String producer = resultSet.getString("producer");
        int amount = resultSet.getInt("amount");
        BigDecimal price = resultSet.getBigDecimal("price");
        String group_name = resultSet.getString("group_name");
        return new Good(product_name, description, producer, amount, price, group_name);
    }

    public static Group toGroup(ResultSet resultSet) throws SQLException {
        String group_name = resultSet.getString("group_name");
        String description = resultSet.getString("description");
        return new Group(group_name, description);
    }
    public static List<Good> toGoodList(ResultSet resultSet) throws SQLException {
        List<Good> goods = new ArrayList<>();

        while (resultSet.next()) {
            Good good = new Good();
            good.setProduct_name(resultSet.getString("product_name"));
            good.setDescription(resultSet.getString("description"));
            good.setProducer(resultSet.getString("producer"));
            good.setAmount(resultSet.getInt("amount"));
            good.setPrice(resultSet.getBigDecimal("price"));
            good.setGroup_name(resultSet.getString("group_name"));
            goods.add(good);
        }
        return goods;
    }
}
