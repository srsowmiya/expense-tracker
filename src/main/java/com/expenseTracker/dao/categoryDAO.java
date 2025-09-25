package com.expenseTracker.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.expenseTracker.model.Category;
import com.expenseTracker.util.DataBaseConnection;
public class CategoryDAO {
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Category category = new Category(resultSet.getString("name"));
                category.setId(resultSet.getInt("id"));
                categories.add(category);
            }
        }
        return categories;

    }

    public int addCategory(String name) throws SQLException {
        String query = "INSERT INTO category (name) VALUES (?)";
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
    }

    public int getCategoryIdByName(String name) throws SQLException {
        String query = "SELECT id FROM category WHERE LOWER(name) = ?";
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name.toLowerCase(Locale.ROOT));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Category not found with name: " + name);
                }
            }
        }
    }

    public String getCategoryNameById(int id) throws SQLException {
        // Example implementation, replace with actual DB logic if needed
        for (Category category : getAllCategories()) {
            if (category.getId() == id) {
                return category.getName();
            }
        }
        return "Unknown";
    }
}
