/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Dialog {
    private PaintFX app;
    
    /**
     * Creates a Dialog object for the PaintFX program.
     * 
     * @param app The PaintFX program
     */
    public Dialog(PaintFX app){
        this.app = app;
    }
    
    /**
     * Displays an error message to the user.
     * 
     * @param title The title of the error displayed
     * @param message The message of the error displayed
     */
    public void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Displays instructions for options in the PaintFX program.
     */
    public void help(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PaintFX Help");
        alert.setHeaderText("How to use PaintFX");
        alert.setContentText(
            "Open an image:\n" + "File -> Open\n\n"
            + "Save an image:\n" + "File -> Save\n\n"
            + "Save an image with new format or name:\n" + "File -> Save As\n\n"
            + "Close the current image:\n" + "File -> Close\n\n"
            + "Draw a line:\n" + "Tools -> Line, then click, drag, and release on the canvas\n\n"
            + "Change line width:\n" + "Use the Line Width dropdown on the toolbar\n\n"
            + "Change line color:\n" + "Use the Color grid in the toolbar"
        );
        alert.showAndWait();
    }
    
    /**
     * Displays information about the PaintFX program.
    */
    public void about(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About PaintFX");
        alert.setHeaderText("PaintFX");
        alert.setContentText("Version 1.1.0\n\n" + "An image editing program using JavaFX.\n\n" + "Made by Aaron Howard\n\n" + "9/16/2026");
        alert.showAndWait();
    }  
    
    /**
     * Displays dialog for the user to enter text.
     * 
     * @return Text entered buy the user, or null if cancelled
     */
    public String textDialog(){
        TextInputDialog dialog = new TextInputDialog();
        
        dialog.setTitle("Add text");
        dialog.setHeaderText("Enter text");
        dialog.setContentText("Text:");
        
        Optional<String> result = dialog.showAndWait();
        
        return result.orElse(null);
    }
    
    public boolean confirmClearCanvas(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear canvas");
        alert.setHeaderText("Clear the canvas?");
        alert.setContentText("The canvas is about to be cleared.");
        Optional<ButtonType> result = alert.showAndWait();
        
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
