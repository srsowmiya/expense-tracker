package com.expenseTracker.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.expenseTracker.dao.CategoryDAO;
import com.expenseTracker.model.Category;

public class CategoryPanel extends JPanel {

    private CategoryDAO categoryDAO;
    private JTable categoryTable;
    private JTextField textField;
    private MainGUI mainFrame;
    private JButton backButton;

    public CategoryPanel(MainGUI frame) {
        this.mainFrame = frame;
        categoryDAO = new CategoryDAO();
        setLayout(new BorderLayout(10, 10));

        // --- BACK BUTTON PANEL (top-left) ---
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        backButton.addActionListener(e -> mainFrame.showInitialButtons());
        backPanel.add(backButton);
        add(backPanel, BorderLayout.NORTH);

        // FORM PANEL
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel label = new JLabel("Category Name:");
        textField = new JTextField(15);
        formPanel.add(label);
        formPanel.add(textField);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton addButton = new JButton("Add Category");
        JButton updateButton = new JButton("Update Category");
        JButton deleteButton = new JButton("Delete Category");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // NORTH PANEL (form + buttons)
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.NORTH);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.CENTER);

        // TABLE PANEL
        categoryTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.SOUTH);

        // BUTTON ACTIONS
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            String[] columnNames = {"ID", "Name"};
            Object[][] data = new Object[categories.size()][2];
            for (int i = 0; i < categories.size(); i++) {
                data[i][0] = categories.get(i).getId();
                data[i][1] = categories.get(i).getName();
            }
            categoryTable.setModel(new DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading categories: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        String name = textField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            categoryDAO.addCategory(name);
            JOptionPane.showMessageDialog(this, "Category added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            textField.setText("");
            loadCategories();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) categoryTable.getValueAt(selectedRow, 0);
        String currentName = (String) categoryTable.getValueAt(selectedRow, 1);
        String newName = JOptionPane.showInputDialog(this, "Update Category Name:", currentName);
        if (newName != null && !newName.trim().isEmpty()) {
            try {
                categoryDAO.updateCategory(id, newName.trim());
                loadCategories();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) categoryTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categoryDAO.deleteCategory(id);
                loadCategories();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
