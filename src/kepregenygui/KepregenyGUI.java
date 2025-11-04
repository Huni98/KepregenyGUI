/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package kepregenygui;

/**
 *
 * @author hunor
 */
import MainDashboard.MainDashboard;
import ro.madarash.kepregeny_project.*;

public class KepregenyGUI 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        /* Set the Nimbus look and feel (Optional but recommended) */
        // This code tries to set a more modern look and feel for the application.
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            // Log an error if Nimbus isn't available
            java.util.logging.Logger.getLogger(MainDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the main dashboard */
        // This runs the GUI on the Event Dispatch Thread (EDT),
        // which is the standard, thread-safe way to start any Swing application.
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create an instance of your MainDashboard and make it visible
                new MainDashboard().setVisible(true);
            }
        });
    }
    
}
