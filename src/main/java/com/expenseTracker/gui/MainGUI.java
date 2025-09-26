package com.expenseTracker.gui;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    public MainGUI() {
        setTitle("Expense Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        showInitialButtons();

        setVisible(true);
    }

    public void showInitialButtons() {
        getContentPane().removeAll(); // Clear everything

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 150));

        JButton categoryButton = new JButton("Categories");
        JButton expenseButton = new JButton("Expenses");

        categoryButton.setPreferredSize(new Dimension(200, 80));
        expenseButton.setPreferredSize(new Dimension(200, 80));

        categoryButton.setFont(new Font("Arial", Font.BOLD, 24));
        expenseButton.setFont(new Font("Arial", Font.BOLD, 24));

        buttonPanel.add(categoryButton);
        buttonPanel.add(expenseButton);

        add(buttonPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        categoryButton.addActionListener(e -> showCategoryPanel());
        expenseButton.addActionListener(e -> showExpensePanel());
    }

    private void showCategoryPanel() {
        getContentPane().removeAll();
        add(new CategoryPanel(this), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showExpensePanel() {
        getContentPane().removeAll();
        add(new ExpensePanel(this), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
