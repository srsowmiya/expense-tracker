package com.expenseTracker.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.expenseTracker.dao.CategoryDAO;
import com.expenseTracker.dao.ExpenseDAO;
import com.expenseTracker.model.Category;
import com.expenseTracker.model.Expense;

public class ExpenseGUI extends JFrame {

    private JComboBox<String> categoryComboBox;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JButton addButton;
    private JTable expenseTable;
    private JButton deleteButton;

    private CategoryDAO categoryDAO;
    private ExpenseDAO expenseDAO;

    public ExpenseGUI() {
        categoryDAO = new CategoryDAO();
        expenseDAO = new ExpenseDAO();

        setTitle("Expense Tracker");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        // Category dropdown
        panel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>();
        loadCategories();
        panel.add(categoryComboBox);

        // Description
        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField(15);
        panel.add(descriptionField);

        // Amount
        panel.add(new JLabel("Amount:"));
        amountField = new JTextField(10);
        panel.add(amountField);

        // Date
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(10);
        panel.add(dateField);

        // Add button
        addButton = new JButton("Add Expense");
        addButton.addActionListener(e -> addExpense());
        panel.add(addButton);

        deleteButton = new JButton("Delete EXpense");
        deleteButton.addActionListener(e -> deleteExpense());
        panel.add(deleteButton);

        // Add panel to top
        add(panel, BorderLayout.NORTH);

        // Expense table
        expenseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load existing expenses
        loadExpenses();
    }

    private void loadCategories() {
        try {
            categoryComboBox.removeAllItems();
            for (Category category : categoryDAO.getAllCategories()) {
                categoryComboBox.addItem(category.getName());
            }
        } catch (Exception e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
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
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
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

    private void loadExpenses() {
        try {
            List<Expense> expenses = expenseDAO.getAllExpenses();
            String[] columnNames = {"ID", "Category", "Description", "Amount", "Date"};
            Object[][] data = new Object[expenses.size()][5];

            for (int i = 0; i < expenses.size(); i++) {
                Expense exp = expenses.get(i);
                data[i][0] = exp.getId();
                data[i][1] = exp.getCategoryName() != null ? exp.getCategoryName() : "Unknown";
                data[i][2] = exp.getDescription();
                data[i][3] = exp.getAmount();
                data[i][4] = exp.getDate();
            }

            expenseTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (Exception e) {
            System.err.println("Error loading expenses: " + e.getMessage());
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

    public static void main(String[] args) {
        new ExpenseGUI();
    }
}
