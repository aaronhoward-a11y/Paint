/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

// Imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.SnapshotParameters;

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
        
        // initialize file for menubar
        Menu fileMenu = new Menu("File");
        
        // initialize items inside file
        MenuItem openItem = new MenuItem("Open");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");
        
        // add all items under the file button
        fileMenu.getItems().addAll(openItem, closeItem, saveItem, saveAsItem, exitItem);
        
        // add file button to the menubar
        menuBar.getMenus().add(fileMenu);
        
        // add actions when the user selects the items
        openItem.setOnAction(event -> open());
        closeItem.setOnAction(event -> close());
        saveItem.setOnAction(event -> save());
        saveAsItem.setOnAction(event -> saveAs());
        exitItem.setOnAction(event -> exit());
        
        // create border
        BorderPane root = new BorderPane();
        
        // add menubar and canvas to the stage
        root.setTop(menuBar);
        root.setCenter(canvas);
        
        // size and display the scene
        Scene scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }
    
    public void open(){
        // choose acceptable file types
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Image");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files (*.jpg, *.png, *.gif)", "*.jpg", "*.jpeg", "*.png", "*.gif");
        chooser.getExtensionFilters().add(filter);
        File selectedFile = chooser.showOpenDialog(stage);
        
        // opening the image
        if(selectedFile != null){
            try{
                // define the chosen file to image
                Image image = new Image(selectedFile.toURI().toString());
                
                // exception error
                if(image.isError()){
                    showError("Open Error", "The file could not be opened.");
                    return;
                }
                
                // make the current file the user selected file
                currentFile = selectedFile;
                
                // set the image size and draw it on the canvas
                canvas.setWidth(image.getWidth());
                canvas.setHeight(image.getHeight());
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.drawImage(image, 0, 0);
                System.out.println("Opened file: " + currentFile.getName());
                
            } catch (Exception e){
                // throw error if necessary
                showError("Open Error", "Error opening file.");
            }
        }
    }
    
    public void close(){
        // confirm the user wants to close the image
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Image");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to close the image?");
        Optional<ButtonType> result = alert.showAndWait();
        
        // reset current file and clear the canvas
        if(result.isPresent() && result.get() == ButtonType.OK){
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            currentFile = null;
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }
    
    public void save(){
        // switch to saveAs if necessary
        if(currentFile == null){
            saveAs();
            return;
        }
        
        // saving the image
        try{
            // taking a snapshot of the current canvas to save
            WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
            
            // dimensions of the image
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            
            BufferedImage buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            PixelReader reader = image.getPixelReader();
            
            // going through each pixel of the image
            for (int y=0; y < height; y++){
                for (int x=0; x < width; x++){
                    buffImage.setRGB(x, y, reader.getArgb(x,y));
                }
            }
            
            // create the output
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(buffImage, "png", output);
            
            // write over the old file
            Files.write(currentFile.toPath(), output.toByteArray());
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        
        // end save as if there is nothing to save
        if(selectedFile == null){
            return;
        }
        
        // add .png to name if necessary
        if (!selectedFile.getName().toLowerCase().endsWith(".png")){
            selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
        }
        
        // save the image
        try{
            // taking a snapshot of the current canvas to save
            WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
            
            // dimensions of the image
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            
            BufferedImage buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            PixelReader reader = image.getPixelReader();
            
            // going through each pixel of the image
            for (int y=0; y < height; y++){
                for (int x=0; x < width; x++){
                    buffImage.setRGB(x, y, reader.getArgb(x, y));
                }
            }
            
            // create the output
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(buffImage, "png", output);
            
            // save the file and make it the current file
            Files.write(selectedFile.toPath(), output.toByteArray());
            currentFile = selectedFile;
            System.out.println("File saved as: " + currentFile.getAbsolutePath());
            
        } catch (IOException e){
            // throw error if necessary
            showError("Save Error", "Error saving file.");
        }
    }
    
    public void exit(){
        // confirm if user wants to actually exit the program
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Paint");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        
        // close program if user says yes
        if(result.isPresent() && result.get() == ButtonType.OK){
            stage.close();
        }
    }
    
    // helper function to throw errors
    public void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // launch the program
    public static void main(String[] args){
        launch(args);
    }
}
