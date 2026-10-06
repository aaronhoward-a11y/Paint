/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

import javafx.application.Platform;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

public class FileOperations {
    
    private PaintFX app;
    private UndoRedo undoRedo;
    
    /**
     * Creates a FileOperations object for the PaintFX program.
     * 
     * @param app The PaintFX program
     */
    public FileOperations(PaintFX app){
        this.app = app;
    }
    
    /**
     * Opens an image file selected by the user.
     * 
     * Supported image formats: JPEG, BMP, PNG.
     * Includes smart save functionality.
     */
    public void open(){
        // smart save check
        if(!confirmSaveChanges("opening a different image")){
            return;
        }
        
        // choose acceptable file types
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Image");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files (*.jpg, *.jpeg, *.png, *.bmp)", "*.jpg", "*.jpeg", "*.png", "*.bmp");
        chooser.getExtensionFilters().add(filter);
        File selectedFile = chooser.showOpenDialog(app.getStage());
        
        // opening the image
        if(selectedFile != null){
            try{
                app.openImageInNewTab(selectedFile);
                
            } catch (Exception e){
                // throw error if necessary
                app.getDialog().showError("Open Error", "Error opening file.");
            }
        }
    }
    
    /**
     * Saves the current image to its file.
     * 
     * Saved using the same format as the original file.
     * Save As used if there is no existing file.
     */
    public void save(){
        // check if the image exists
        if(app.getImage() == null){
            app.getDialog().showError("Save Error", "There is no image to save.");
        }

        // switch to saveAs if necessary
        if(app.getCurrentFile() == null){
            saveAs();
            return;
        }
        
        // saving the image
        try{
            // get the file type
            String format = ImageHelp.getImageFormat(app.getCurrentFile());
            if(format == null){
                app.getDialog().showError("Save Error", "Unsupported image format.");
                return;
            }
            
            // runs helper method to write the file
            saveCanvasToFile(app.getCurrentFile(), format);

            app.setModified(false);
            
        } catch (Exception e){
            // throw error if necessary
            app.getDialog().showError("Save Error", "Error saving file.");
        }
    }
    
    /**
     * Saves the current image to a new user selected file.
     * 
     * Supports JPEG, BMP, and PNG formats.
     */
    public void saveAs(){
        // check if there is an image to save
        if(app.getImage() == null){
            app.getDialog().showError("Save Error", "There is no image to save.");
        }

        // choose acceptable file types
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image As");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG Images (*.jpg)", "*.jpg"), new FileChooser.ExtensionFilter("JPEG Images (*.jpeg)", "*.jpeg"), new FileChooser.ExtensionFilter("PNG Images (*.png)", "*.png"), new FileChooser.ExtensionFilter("BMP Images (*.bmp)", "*.bmp"));
        File selectedFile = fileChooser.showSaveDialog(app.getStage());
        
        // end save as if there is nothing to save
        if(selectedFile == null){
            return;
        }
        
        // save the image
        try{
            String format = ImageHelp.getImageFormat(selectedFile);
            
            if(format == null){
                String selectedExtension = fileChooser.getSelectedExtensionFilter().getDescription();
                if(selectedExtension.contains("JPEG")){
                    format = "jpeg";
                } else if(selectedExtension.contains("JPG")){
                    format = "jpg";
                } else if(selectedExtension.contains("png")){
                    format = "png";
                } else if(selectedExtension.contains("bmp")){
                    format = "bmp";
                }
            }
            
            String extension = ImageHelp.getExtension(format);
            
            selectedFile = new File(selectedFile.getAbsolutePath() + extension);
            
            saveCanvasToFile(selectedFile, format);
                    
            app.setCurrentFile(selectedFile);
  
            app.setModified(false);
            
            app.updateCurrentTabTitle();
            
            System.out.println("File saved as: " + app.getCurrentFile().getAbsolutePath());
            
        } catch (Exception e){
            // throw error if necessary
            app.getDialog().showError("Save Error", "Error saving file.");
        }
    }
    
    /**
     * Converts the canvas to an image and writes it to a file.
     * 
     * @param file The file where the image is saved to
     * @param format The image format
     * @throws Exception If the image cannot be written
     */
    private void saveCanvasToFile(File file, String format) throws Exception{
        Canvas canvas = app.getCanvas();
        
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        SnapshotParameters parameters = new SnapshotParameters();
        canvas.snapshot(parameters, snapshot);
        
        PixelReader reader = snapshot.getPixelReader();
        
        int width = (int) snapshot.getWidth();
        int height = (int) snapshot.getHeight();
        
        int imageType;
        
        if(format.equals("png")){
            imageType = BufferedImage.TYPE_INT_ARGB;
        } else{
            imageType = BufferedImage.TYPE_INT_RGB;
        }
        
        BufferedImage buffImage = new BufferedImage(width, height, imageType);
        
        for(int y = 0; y <  height; y++){
            for(int x = 0; x < width; x++){
                Color color = reader.getColor(x, y);
                int red = (int) (color.getRed()*255);
                int green = (int) (color.getGreen()*255);
                int blue = (int) (color.getBlue()*255);
                int alpha = (int) (color.getOpacity()*255);
                int rgb;
                
                if(format.equals("png")){
                    rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                } else{
                    rgb = (red << 16) | (green << 8) | blue;
                }
                
                buffImage.setRGB(x, y, rgb);
            }
        }
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(buffImage, format, output);
        Files.write(file.toPath(), output.toByteArray());
    }
    
    /**
     * Closes the current opened image without closing the program.
     * 
     * Includes smart save functionality.
     */
    public void close(){
        if(!confirmSaveChanges("closing the image")){
            return;
        }
        undoRedo.clear();
        ImageHelp.clearImage(app);
    }
    
    /**
     * Exits the PaintFX program.
     * 
     * Includes smart save functionality.
     */
    public void exit(){
        if(!confirmSaveChanges("exiting the program")){
            return;
        }
        Platform.exit();
    }
    
    /**
     * Asks the user if they want to save unsaved changes before an action.
     * 
     * @param action The action about to be performed
     * @return True if the action should continue, false otherwise
     */
    public boolean confirmSaveChanges(String action){
        if(!app.isModified()){
            return true;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved changes");
        alert.setHeaderText("You have unsaved changes");
        alert.setContentText("Do you want to save your changes before " + action + "?");
        
        ButtonType saveButton = new ButtonType("Save");
        ButtonType dontSaveButton = new ButtonType("Don't Save");
        ButtonType cancelButton = new ButtonType("Cancel");
        
        alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent()){
            if(result.get() == saveButton){
                save();
                if(!app.isModified()){
                    return true;
                }
                return false;
            } else if(result.get() == dontSaveButton){
                return true;
            } else{
                return false;
            }
        }
        return false;
    }

}
