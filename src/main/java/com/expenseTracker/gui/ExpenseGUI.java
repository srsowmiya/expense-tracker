package com.expenseTracker.gui;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ExpenseGUI extends JFrame{
    ExpenseGUI(){
        super("Expense Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JButton addButton = new JButton("Add Expense");
        add(addButton);
        setVisible(true);
    }
    
}
