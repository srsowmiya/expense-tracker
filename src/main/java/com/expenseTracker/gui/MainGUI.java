package com.expenseTracker.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainGUI extends JFrame {

    public MainGUI() {
        super("Expense Tracker");

       
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // center panel
        gbc.fill = GridBagConstraints.NONE; // don’t stretch

        // Panel to hold buttons side by side
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton button1 = new JButton("Categories");
        JButton button2 = new JButton("Expenses");

        // Set preferred size to control button size
        button1.setPreferredSize(new Dimension(200, 80));
        button2.setPreferredSize(new Dimension(200, 80));

        // Increase font size
        button1.setFont(new Font("Arial", Font.BOLD, 24));
        button2.setFont(new Font("Arial", Font.BOLD, 24));

        // Add buttons to panel
        buttonPanel.add(button1);
        buttonPanel.add(button2);

        // Add button panel to frame with GridBagConstraints
        add(buttonPanel, gbc);

        // Frame settings
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window on screen
        setVisible(true);

        button1.addActionListener(e -> new CategoryGUI());
        button2.addActionListener(e -> new ExpenseGUI());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
