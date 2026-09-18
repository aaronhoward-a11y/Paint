/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

//  JavaFX Imports
import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.SnapshotParameters;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

// Other Imports
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;

public class PaintFX extends Application{
    
    // define variables
    private File currentFile;
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage stage;
    private WritableImage image;
    private boolean modified = false;
    
    private double lineWidth = 1.0;
    private Color lineColor = Color.BLACK;
    private double startX;
    private double startY;
    private boolean lineToolSelected = false;
    
    // create stage
    @Override
    public void start(Stage stage){
        this.stage = stage;
        stage.setTitle("PaintFX");
        
        // make default white canvas as background
        canvas = new Canvas(750, 750);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // initialize menubar
        MenuBar menuBar = new MenuBar();
        
        // initialize toolbar
        ToolBar toolBar = new ToolBar();
        
        // initialize file for menubar
        Menu fileMenu = new Menu("File");
        
        Menu toolsMenu = new Menu("Tools");
        
        Menu helpMenu = new Menu("Help");
        
        // initialize items inside file
        MenuItem openItem = new MenuItem("Open");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");
        
        // initializes items inside tools
        MenuItem lineItem = new MenuItem("Line");
        
        // initializes items inside help
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        
        // initializes labels for tools
        Label widthLabel = new Label("Line Width:");
        ComboBox<Integer> widthBox = new ComboBox<>();
        Label colorLabel = new Label("Color:");
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        
        // add all items under the file button
        fileMenu.getItems().addAll(openItem, closeItem, saveItem, saveAsItem, exitItem);
        
        // add all tools under the tools button
        toolsMenu.getItems().add(lineItem);
        
        // add all help features under the help button
        helpMenu.getItems().addAll(helpItem, aboutItem);
        
        // put all the options in the tool bar
        toolBar.getItems().addAll(widthLabel, widthBox, colorLabel, colorPicker);
        
        // add line width options to dropdown
        widthBox.getItems().addAll(1, 2, 5, 10, 15, 20, 30);
        widthBox.setValue(1);
        
        // add file button to the menubar
        menuBar.getMenus().addAll(fileMenu, toolsMenu, helpMenu);
        
        // call the function when the user chooses it
        openItem.setOnAction(event -> open());
        closeItem.setOnAction(event -> close());
        saveItem.setOnAction(event -> save());
        saveAsItem.setOnAction(event -> saveAs());
        exitItem.setOnAction(event -> exit());
        
        // turn on the line feature when clicked
        lineItem.setOnAction(event -> {lineToolSelected = true;});
        
        // set action for clicking help features
        helpItem.setOnAction(event -> help());
        aboutItem.setOnAction(event -> about());
        
        // retrieve the option the user selects in the tool bar
        widthBox.setOnAction(event -> {lineWidth = widthBox.getValue();});
        colorPicker.setOnAction(event -> {lineColor = colorPicker.getValue();});
        
        // retrieve where the user clicks on the image to start the line
        canvas.setOnMousePressed(event -> {
            if(lineToolSelected && image != null){
                startX = event.getX();
                startY = event.getY();
        }});
        
        // retrieve where the user releases their mouse to end the line
        // then draw the line with the selected width and color
        canvas.setOnMouseReleased(event -> {
            if (lineToolSelected && image != null){
                double endX = event.getX();
                double endY = event.getY();
                
                gc.setStroke(lineColor);
                gc.setLineWidth(lineWidth);
                gc.strokeLine(startX, startY, endX, endY);
                
                modified = true;
            }
        });
        
        // create the window
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvas);
        scrollPane.setPannable(true);
        
        // combine menu bar and tool bar to be at the top of the window
        VBox top = new VBox();
        top.getChildren().addAll(menuBar, toolBar);
        
        // create border
        BorderPane root = new BorderPane();
        
        // add menubar, toolbar, and canvas to the stage
        root.setTop(top);
        root.setCenter(scrollPane);
        
        // size and display the scene
        Scene scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }
    
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
        File selectedFile = chooser.showOpenDialog(stage);
        
        // opening the image
        if(selectedFile != null){
            try{
                // define the chosen file to image
                Image loadedImage = new Image(selectedFile.toURI().toString());
                
                // exception error
                if(loadedImage.isError()){
                    showError("Open Error", "The file could not be opened.");
                    return;
                }
                
                // store image at its original dimensions
                image = new WritableImage((int) loadedImage.getWidth(), (int) loadedImage.getHeight());
                
                // copy loaded image into the WritableImage
                image.getPixelWriter().setPixels(0, 0, (int) loadedImage.getWidth(), (int) loadedImage.getHeight(), loadedImage.getPixelReader(), 0, 0);
                
                // make the current file the user selected file
                currentFile = selectedFile;
                
                // make the canvas the same size as the original image
                canvas.setWidth(image.getWidth());
                canvas.setHeight(image.getHeight());
                
                // draw the image on the canvas while preserving scale
                drawImageToCanvas();
                
                modified = false;
                
                System.out.println("Opened file: " + currentFile.getName());
                System.out.println("Image size: " + (int) image.getWidth() + " x " + (int) image.getHeight());
                
            } catch (Exception e){
                // throw error if necessary
                showError("Open Error", "Error opening file.");
            }
        }
    }
    
    public void close(){
        // no unsaved changes
        if(!confirmSaveChanges("closing the image")){
            return;
        }
        
        // clear the image
        clearImage();
    }
    
    public void save(){
        // switch to saveAs if necessary
        if(currentFile == null){
            saveAs();
            return;
        }
        
        // saving the image
        try{
            // get the file type
            String format = getImageFormat(currentFile);
            if(format == null){
                showError("Save Error", "Unsupported image format.");
                return;
            }
            
            // create snapshot to write to
            WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(new SnapshotParameters(), snapshot);
            
            // dimensions of the image
            int width = (int) snapshot.getWidth();
            int height = (int) snapshot.getHeight();
            
            // use argb for png, rgb for others
            int imageType;
            if(format.equals("png")){
                imageType = BufferedImage.TYPE_INT_ARGB;
            } else{
                imageType = BufferedImage.TYPE_INT_RGB;
            }
            
            BufferedImage buffImage = new BufferedImage(width, height, imageType);
            
            PixelReader reader = snapshot.getPixelReader();
            
            // copy pixels from WritableImage to BufferedImage
            for (int y=0; y < height; y++){
                for (int x=0; x < width; x++){
                    // use argb for png images
                    int argb = reader.getArgb(x, y);
                    if(format.equals("png")){
                        buffImage.setRGB(x, y, argb);
                    } else{
                        // need rgb for jpg and bmp images
                        int red = (argb >> 16) & 0xff;
                        int green = (argb >> 8) & 0xff;
                        int blue = argb & 0xff;
                        
                        int rgb = (red << 16) | (green << 8) | blue;
                        buffImage.setRGB(x, y, rgb);
                    }
                }
            }
            
            // create the output
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(buffImage, format, output);
            
            // write over the old file
            Files.write(currentFile.toPath(), output.toByteArray());
            
            modified = false;
            
            System.out.println("File saved: " + currentFile.getAbsolutePath());
            
        } catch (IOException e){
            // throw error if necessary
            showError("Save Error", "Error saving file.");
        }
    }
    
    public void saveAs(){
        // choose acceptable file types
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG Images (*.jpg)", "*.jpg"), new FileChooser.ExtensionFilter("JPEG Images (*.jpeg)", "*.jpeg"), new FileChooser.ExtensionFilter("PNG Images (*.png)", "*.png"), new FileChooser.ExtensionFilter("BMP Images (*.bmp)", "*.bmp"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        
        // end save as if there is nothing to save
        if(selectedFile == null){
            return;
        }
        
        // save the image
        try{
            String format = getImageFormat(selectedFile);
            
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
            
            selectedFile = new File(selectedFile.getAbsolutePath() + getExtension(format));
            
            WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(new SnapshotParameters(), snapshot);

            // dimensions of the image
            int width = (int) snapshot.getWidth();
            int height = (int) snapshot.getHeight();
            
            int imageType;
            
            if(format.equals("png")){
                imageType = BufferedImage.TYPE_INT_ARGB;
            } else{
                imageType = BufferedImage.TYPE_INT_RGB;
            }
            
            BufferedImage buffImage = new BufferedImage(width, height, imageType);
            
            PixelReader reader = snapshot.getPixelReader();
            
            // going through each pixel of the image
            for (int y=0; y < height; y++){
                for (int x=0; x < width; x++){
                    int argb = reader.getArgb(x, y);
                    
                    if(format.equals("png")){
                        buffImage.setRGB(x, y, argb);
                    } else{
                        int red = (argb >> 16) & 0xff;
                        int green = (argb >> 8) & 0xff;
                        int blue = argb & 0xff;
                        
                        int rgb = (red << 16) | (green << 8) | blue;
                        
                        buffImage.setRGB(x, y, rgb);
                    }
                }
            }
            
            // create the output
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(buffImage, format, output);
            
            // save the file and make it the current file
            Files.write(selectedFile.toPath(), output.toByteArray());
            currentFile = selectedFile;
            
            modified = false;
            
            System.out.println("File saved as: " + currentFile.getAbsolutePath());
            
        } catch (IOException e){
            // throw error if necessary
            showError("Save Error", "Error saving file.");
        }
    }
    
    // exit the program
    public void exit(){
        // check smart save, then exit
        if(!confirmSaveChanges("exiting the program")){
            return;
        }
        
        // exit paint
        Platform.exit();
    }
    
    // display some help options
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
    
    // display a message about the program
    public void about(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About PaintFX");
        alert.setHeaderText("PaintFX");
        alert.setContentText("Version 1.1.0\n\n" + "An image editing program using JavaFX.\n\n" + "Made by Aaron Howard\n\n" + "9/16/2026");
        alert.showAndWait();
    }
    
    // helper function to throw an error
    public void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // helper function to draw the image on the canvas
    private void drawImageToCanvas(){
        if(image == null){
            return;
        }
        
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());
        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        gc.drawImage(image, 0, 0);
    }
    
    // helper function to get the file type of the image
    private String getImageFormat(File file){
        String name = file.getName().toLowerCase();
        if(name.endsWith(".jpg") || name.endsWith(".jpeg")){
            return "jpg";
        } else if(name.endsWith(".bmp")){
            return "bmp";
        } else if(name.endsWith("png")){
            return "png";
        }
        
        return null;
    }
    
    // helper function to get the file type extension
    private String getExtension(String format){
        if(format.equals("jpg")){
            return ".jpg";
        } else if(format.equals("bmp")){
            return ".bmp";
        } else if(format.equals("png")){
            return ".png";
        }
        
        return null;
    }
    
    // helper method to clear the canvas
    private void clearImage(){
        image = null;
        currentFile = null;
        
        canvas.setWidth(750);
        canvas.setHeight(750);
        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        lineToolSelected = false;
        modified = false;
    }
    
    // helper smart save method
    private boolean confirmSaveChanges(String action){
        if(!modified){
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
            
            // save the changes
            if(result.get() == saveButton){
                save();
                
                // check if save succeeded
                if(!modified){
                    return true;
                }
                
                // save was cancelled/failed
                return false;
                
            // dont save the changes    
            } else if(result.get() == dontSaveButton){
                return true;
                
            // cancel
            } else{
                return false;
                
            }
        }
        return false;
    }
    
    // launch the program
    public static void main(String[] args){
        launch(args);
    }
}
