package com.expenseTracker.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.expenseTracker.dao.CategoryDAO;
import com.expenseTracker.model.Category;

public class CategoryGUI extends JFrame {
    private CategoryDAO categoryDAO;
    private JTable categoryTable;
    private JTextField textField;

    public CategoryGUI() {
        super("Category");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ---------- TOP FORM PANEL ----------
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel label = new JLabel("Category Name:");
        textField = new JTextField(15); // fixed width
        formPanel.add(label);
        formPanel.add(textField);

        // ---------- BUTTON PANEL ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton addButton = new JButton("Add Category");
        JButton updateButton = new JButton("Update Category");
        JButton deleteButton = new JButton("Delete Category");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // ---------- TABLE PANEL ----------
        categoryTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(categoryTable);

        // add padding around the table
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- ADD PANELS TO FRAME ----------
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.NORTH);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);   // input + buttons at top
        add(scrollPane, BorderLayout.CENTER);  // table fills center with padding

        // ---------- DAO ----------
        categoryDAO = new CategoryDAO();

        // ---------- BUTTON ACTIONS ----------
        addButton.addActionListener(e -> addCategories());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        loadCategories();
        setLocationRelativeTo(null);
        setVisible(true);
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

    private void addCategories() {
        String name = textField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Category name cannot be empty.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Category category = new Category(name);
            categoryDAO.addCategory(name);
            JOptionPane.showMessageDialog(this,
                    "Category added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            textField.setText("");
            loadCategories();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding category: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a category to delete.",
                    "Selection Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) categoryTable.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categoryDAO.deleteCategory(id);
                JOptionPane.showMessageDialog(this,
                        "Category deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadCategories();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting category: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCategory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
