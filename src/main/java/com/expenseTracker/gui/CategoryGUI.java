package com.expenseTracker.gui;

import javax.swing.JButton;
import javax.swing.JFrame;

public class CategoryGUI  extends JFrame{
    CategoryGUI(){
        super("Category Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JButton addButton = new JButton("Add Category");
        add(addButton);
        setVisible(true);
    }
    
}
