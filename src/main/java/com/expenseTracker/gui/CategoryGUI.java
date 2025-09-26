package com.expenseTracker.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

import com.expenseTracker.dao.CategoryDAO;
import com.expenseTracker.model.Category;

public class CategoryGUI extends JFrame {
    private CategoryDAO categoryDAO;
    private JTable categoryTable;
    private JTextField textField;

    public CategoryGUI() {
        super("Category");
        setSize(500, 400);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(new GridBagLayout());

        // Label + Text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel("Category Name:");
        panel.add(label, gbc);

        textField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(textField, gbc);

        // Add Button
        JButton addButton = new JButton("Add Category");
        gbc.gridx = 2;
        panel.add(addButton, gbc);

        // Update Button
        JButton updateButton = new JButton("Update Category");
        gbc.gridx = 3;
        panel.add(updateButton, gbc);

        // Delete Button
        JButton deleteButton = new JButton("Delete Category");
        gbc.gridx = 4;
        panel.add(deleteButton, gbc);

        // Table
        categoryTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(categoryTable);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        add(panel, gbc);

        // DAO init
        categoryDAO = new CategoryDAO();

        // Button actions
        addButton.addActionListener(e -> addCategories());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        loadCategories();
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
        JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE); 
        return;
     } 
     try{
         Category category = new Category(name); 
         categoryDAO.addCategory(name); 
         JOptionPane.showMessageDialog(this, "Category added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE); 
         textField.setText(""); 
         loadCategories(); }
         catch(Exception e){
             JOptionPane.showMessageDialog(this, "Error adding category: "+e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
             } }

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
