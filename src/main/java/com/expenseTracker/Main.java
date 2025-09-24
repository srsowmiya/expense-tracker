package com.expenseTracker;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Ensure this import matches the actual package and class name of MainGUI
import com.expenseTracker.gui.MainGUI;
import com.expenseTracker.util.DataBaseConnection;

public class Main {
    public static void main(String[] args) {
        
        DataBaseConnection dbConnection = new DataBaseConnection();
       try {
            Connection cn= dbConnection.getDBConnection();
            System.out.println("Connection Successful");
        } catch(SQLException e) {
            System.out.println("Connection Failed");
           System.exit(1); //process is terminated
        } 
            
        
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e){
           System.err.println("Could not set Look and Feel"+e.getMessage()); 
        }

        SwingUtilities.invokeLater(() -> {  //invoke later creates a new thread (eventdispatch thread)
            try {
                new MainGUI().setVisible(true); 
            } catch (Exception e) {
                System.err.println("Error starting the application"+e.getLocalizedMessage());
            }
           
           
        });

    }
}
