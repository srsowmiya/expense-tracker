package com.expenseTracker.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import com.expenseTracker.dao.CategoryDAO;
import com.expenseTracker.model.Category;

public class CategoryGUI  extends JFrame{
    private CategoryDAO categoryDAO;
    private JTable categoryTable;
    JTextField textField = new JTextField(20);

    CategoryGUI(){
        super("Category");
        setSize(400, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.gridx=0;    
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.NORTH;
        gbc.fill=GridBagConstraints.NONE;
        gbc.weighty=1.0;
        panel.add(textField);

        gbc.gridx=1;
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.NORTH;
        gbc.fill=GridBagConstraints.NONE;
        gbc.weighty=1.0;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JButton addButton = new JButton("Add Category");
        panel.add(addButton);
        add(panel,gbc);

        categoryDAO = new CategoryDAO();
        categoryTable = new JTable();
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=2;
        gbc.anchor=GridBagConstraints.SOUTH;
        gbc.fill=GridBagConstraints.BOTH;
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        panel.add(categoryTable,gbc);

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        add(scrollPane, gbc);
        
        addButton.addActionListener(e -> {
           addCategories();
        });

        loadCategories();




        setVisible(true);
    }
    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            String[] columnNames = {"id", "name"};
            Object[][] data = new Object[categories.size()][2];
            for (int i = 0; i < categories.size(); i++) {
                data[i][0] = categories.get(i).getId();
                data[i][1] = categories.get(i).getName();
            }
            categoryTable.setModel(new DefaultTableModel(data, columnNames));
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: "+e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategories()
    {
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
        loadCategories();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error adding category: "+e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
