package com.expenseTracker.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.expenseTracker.model.Expense;
import com.expenseTracker.util.DataBaseConnection;

public class ExpenseDAO {

    // Get all expenses with category names
    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> expenses = new ArrayList<>();

        String query = "SELECT e.expense_id, e.description, e.amount, e.date, e.id AS category_id, c.name AS category_name " +
                       "FROM expenses e LEFT JOIN category c ON e.id = c.id";

        try (Connection conn = DataBaseConnection.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Expense exp = new Expense();
                exp.setId(rs.getInt("expense_id"));
                exp.setCategoryId(rs.getInt("category_id"));
                exp.setCategoryName(rs.getString("category_name") != null ? rs.getString("category_name") : "Unknown");
                exp.setDescription(rs.getString("description"));
                exp.setAmount(rs.getDouble("amount"));
                exp.setDate(rs.getString("date"));
                expenses.add(exp);
            }
        }

        return expenses;
    }

    // Add a new expense
    public int addExpense(Expense expense) throws SQLException {
        String query = "INSERT INTO expenses (description, amount, date, id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setDate(3, java.sql.Date.valueOf(expense.getDate()));
            stmt.setInt(4, expense.getCategoryId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        }
    }

    public List<Expense> getExpensesByCategory(int categoryId) throws SQLException {
    List<Expense> expenses = new ArrayList<>();
    String query = "SELECT e.expense_id, e.description, e.amount, e.date, e.id AS category_id, c.name AS category_name " +
                   "FROM expenses e LEFT JOIN category c ON e.id = c.id WHERE e.id = ?";

    try (Connection conn = DataBaseConnection.getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, categoryId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Expense exp = new Expense();
                exp.setId(rs.getInt("expense_id"));
                exp.setCategoryId(rs.getInt("category_id"));
                exp.setCategoryName(rs.getString("category_name"));
                exp.setDescription(rs.getString("description"));
                exp.setAmount(rs.getDouble("amount"));
                exp.setDate(rs.getString("date"));
                expenses.add(exp);
            }
        }
    }

    return expenses;
}


    // Delete expense
    public int deleteExpense(int expenseId) throws SQLException {
        String query = "DELETE FROM expenses WHERE expense_id = ?";

        try (Connection conn = DataBaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, expenseId);
            return stmt.executeUpdate();
        }
    }

  
    public void updateExpense(int expenseId, double amount, String description, Date date, int categoryId) throws SQLException {
        String query = "UPDATE expenses SET amount = ?, description = ?, date = ?, id = ? WHERE expense_id = ?";
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, date); 
            preparedStatement.setInt(4, categoryId);
            preparedStatement.setInt(5, expenseId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Updating expense failed, no rows affected.");
            }
        }
    }

    public Expense getExpenseById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
