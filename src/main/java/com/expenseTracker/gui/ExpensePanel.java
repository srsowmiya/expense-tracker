package com.expenseTracker.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.expenseTracker.dao.CategoryDAO;
import com.expenseTracker.dao.ExpenseDAO;
import com.expenseTracker.model.Expense;

public class ExpensePanel extends JPanel {

    private JComboBox<String> categoryComboBox;
    private JTextField descriptionField, amountField, dateField;
    private JButton addButton, deleteButton, refreshButton, updateButton, backButton;
    private JTable expenseTable;
    private CategoryDAO categoryDAO;
    private ExpenseDAO expenseDAO;
    private MainGUI mainFrame;

    public ExpensePanel(MainGUI frame) {
        this.mainFrame = frame;
        categoryDAO = new CategoryDAO();
        expenseDAO = new ExpenseDAO();
        setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- BACK BUTTON PANEL (top-left) ---
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        backButton.addActionListener(e -> mainFrame.showInitialButtons());
        backPanel.add(backButton);
        topPanel.add(backPanel, BorderLayout.NORTH);

        // --- INPUT PANEL ---
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>();
        loadCategories();
        categoryComboBox.addActionListener(e -> filterExpenses());
        inputPanel.add(categoryComboBox);

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField(10);
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(10);
        inputPanel.add(dateField);

        topPanel.add(inputPanel, BorderLayout.CENTER);

        // --- BUTTON PANEL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        addButton = new JButton("Add Expense");
        deleteButton = new JButton("Delete Expense");
        refreshButton = new JButton("Refresh");
        updateButton = new JButton("Update");

        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        refreshButton.addActionListener(e -> loadExpenses());
        updateButton.addActionListener(e -> updateExpense());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // --- TABLE ---
        expenseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        loadExpenses();
    }

    // --- Existing methods (loadCategories, loadExpenses, addExpense, deleteExpense, updateExpense, filterExpenses) ---
    private void loadCategories() {
        try {
            categoryComboBox.removeAllItems();
            for (var category : categoryDAO.getAllCategories()) {
                categoryComboBox.addItem(category.getName());
            }
        } catch (Exception e) { System.err.println("Error loading categories: " + e.getMessage()); }
    }

    private void loadExpenses() {
        try {
            List<Expense> expenses = expenseDAO.getAllExpenses();
            String[] columns = {"ID", "Category", "Description", "Amount", "Date"};
            Object[][] data = new Object[expenses.size()][5];

            for (int i = 0; i < expenses.size(); i++) {
                Expense exp = expenses.get(i);
                data[i][0] = exp.getId();
                data[i][1] = exp.getCategoryName() != null ? exp.getCategoryName() : "Unknown";
                data[i][2] = exp.getDescription();
                data[i][3] = exp.getAmount();
                data[i][4] = exp.getDate();
            }

            expenseTable.setModel(new DefaultTableModel(data, columns));
        } catch (Exception e) { System.err.println("Error loading expenses: " + e.getMessage()); }
    }

    private void addExpense() {
        String categoryName = (String) categoryComboBox.getSelectedItem();
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        String date = dateField.getText();

        if (categoryName == null || description.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try { amount = Double.parseDouble(amountText); } 
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int categoryId = categoryDAO.getCategoryIdByName(categoryName);
            Expense expense = new Expense();
            expense.setCategoryId(categoryId);
            expense.setDescription(description);
            expense.setAmount(amount);
            expense.setDate(date);

            expenseDAO.addExpense(expense);

            descriptionField.setText("");
            amountField.setText("");
            dateField.setText("");

            loadExpenses();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding expense: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No expense selected.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int expenseId = (int) expenseTable.getValueAt(selectedRow, 0);
        try {
            expenseDAO.deleteExpense(expenseId);
            loadExpenses();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting expense: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int expenseId = (int) expenseTable.getValueAt(selectedRow, 0);
        String currentCategoryName = (String) expenseTable.getValueAt(selectedRow, 1);
        String currentDescription = (String) expenseTable.getValueAt(selectedRow, 2);
        double currentAmount = (double) expenseTable.getValueAt(selectedRow, 3);
        String currentDateStr = (String) expenseTable.getValueAt(selectedRow, 4);

        String newDescription = JOptionPane.showInputDialog(this, "Update description:", currentDescription);
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            try {
                int categoryId = categoryDAO.getCategoryIdByName(currentCategoryName);
                java.sql.Date sqlDate = java.sql.Date.valueOf(currentDateStr);
                expenseDAO.updateExpense(expenseId, currentAmount, newDescription.trim(), sqlDate, categoryId);
                loadExpenses();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error updating expense: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filterExpenses() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if (selectedCategory == null) return;
        try {
            int categoryId = categoryDAO.getCategoryIdByName(selectedCategory);
            var expenses = expenseDAO.getExpensesByCategory(categoryId);
            String[] columns = {"ID", "Category", "Description", "Amount", "Date"};
            Object[][] data = new Object[expenses.size()][5];
            for (int i = 0; i < expenses.size(); i++) {
                var exp = expenses.get(i);
                data[i][0] = exp.getId();
                data[i][1] = exp.getCategoryName();
                data[i][2] = exp.getDescription();
                data[i][3] = exp.getAmount();
                data[i][4] = exp.getDate();
            }
            expenseTable.setModel(new DefaultTableModel(data, columns));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error filtering expenses: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
