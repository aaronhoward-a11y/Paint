/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

public class ImageHelp {
    
    /**
     * Displays the image on the program canvas.
     * 
     * Canvas is resized to match the image dimensions.
     * 
     * @param app The PaintFX program that has the image and canvas
     */
    public static void drawImageToCanvas(PaintFX app){
        WritableImage image = app.getImage();
        if(image == null){
            return;
        }
        
        Canvas canvas = app.getCanvas();
        Canvas previewCanvas = app.getPreviewCanvas();
        GraphicsContext gc = app.getGc();
        
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());
        
        previewCanvas.setWidth(image.getWidth());
        previewCanvas.setHeight(image.getHeight());
        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(image, 0, 0);
    }
    
    /**
     * Returns the format of the image from its file extension.
     * 
     * @param file The image file whose format is found
     * @return File extension such as "jpg", "bmp", "png"
     */
    public static String getImageFormat(File file){
        String name = file.getName().toLowerCase();
        
        if(name.endsWith(".jpg") || name.endsWith(".jpeg")){
            return "jpg";
        }else if(name.endsWith(".bmp")){
            return "bmp";
        } else if(name.endsWith(".png")){
            return "png";
        }
        return null;
    }
    
    /**
     * Returns the file extension of an image format.
     * 
     * @param format The image format
     * @return The matching file extension
     */
    public static String getExtension(String format){
        if(format.equals("jpg")){
            return ".jpg";
        }else if(format.equals("bmp")){
            return ".bmp";
        } else{
            return ".png";
        }
    }
    
    /**
     * Clears the image and resets the canvas to default.
     * 
     * @param app The PaintFX program
     */
    public static void clearImage(PaintFX app){
        app.setImage(null);
        app.setCurrentFile(null);
        
        Canvas canvas = app.getCanvas();
        GraphicsContext gc = app.getGc();
        
        canvas.setWidth(1000);
        canvas.setHeight(1000);
        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        app.setActiveTool(Tool.NONE);
        app.setModified(false);
    }
    
    /**
     * Gets the color of a specific pixel on the canvas.
     * 
     * @param app The PaintFX program with the canvas
     * @param x The x-coordinate on the canvas
     * @param y The y-coordinate on the canvas
     * @return The color at the specified (x, y) coordinates on the canvas, or null if the coordinates are not on the canvas
     */
    public static Color grabColor(PaintFX app, int x, int y){
        Canvas canvas = app.getCanvas();
        
        if(x < 0 || x >= canvas.getWidth() || y < 0 || y >= canvas.getHeight()){
            return null;
        }
        
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        
        PixelReader reader = snapshot.getPixelReader();
        
        return reader.getColor(x, y);
    }
    
    /**
     * Draws user text onto the canvas.
     * 
     * @param app The PaintFX program with the canvas
     * @param text The text to draw
     * @param x X-coordinate on the canvas
     * @param y Y-coordinate on the canvas
     */
    public static void drawText(PaintFX app, String text, double x, double y){
        GraphicsContext gc = app.getGc();
        
        gc.setFill(app.getStrokeColor());
        gc.setFont(Font.font(app.getTextFont(), app.getTextSize()));
        
        gc.fillText(text, x, y);
    }
    
    public static void createBlankImage(PaintDocument doc, int width, int height){
        WritableImage blank = new WritableImage(width, height);
        PixelWriter writer = blank.getPixelWriter();
        
        for (int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                writer.setColor(x, y, Color.WHITE);
            }
        }
        
        doc.setImage(blank);
        
        Canvas canvas = doc.getCanvas();
        canvas.setWidth(width);
        canvas.setHeight(height);
        
        GraphicsContext gc = doc.getGc();
        gc.clearRect(0, 0, width, height);
        gc.drawImage(blank, 0, 0);
        
        Canvas previewCanvas = doc.getPreviewCanvas();
        previewCanvas.setWidth(width);
        previewCanvas.setHeight(height);
        doc.getPreviewGc().clearRect(0, 0, width, height);
    }
    
    public static void clearCanvas(PaintFX app){
        Canvas canvas = app.getCanvas();
        GraphicsContext gc = app.getGc();
        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    public static void clearPreviewCanvas(PaintFX app){
        Canvas previewCanvas = app.getPreviewCanvas();
        GraphicsContext previewGc = app.getPreviewGc();
        
        previewGc.clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
    }
    
    public static void resizePreviewCanvas(PaintFX app){
        Canvas canvas = app.getCanvas();
        Canvas previewCanvas = app.getPreviewCanvas();
        
        previewCanvas.setWidth(canvas.getWidth());
        previewCanvas.setHeight(canvas.getHeight());
    }
    
    public static void drawLine(PaintFX app, GraphicsContext gc, double startX, double startY, double endX, double endY){
        gc.setStroke(app.getStrokeColor());
        gc.setLineWidth(app.getStrokeWidth());
        
        if(app.getLineType() == Line.DASHED){
            gc.setLineDashes(10);
        }else{
            gc.setLineDashes(0);
        }
        
        gc.strokeLine(startX, startY, endX, endY);
        gc.setLineDashes(0);
    }
    
    public static WritableImage captureSelection(PaintFX app, double x, double y, double width, double height){
        if(width <= 0 || height <= 0){
            return null;
        }
        
        SnapshotParameters params = new SnapshotParameters();
        params.setViewport(new Rectangle2D(x, y, width, height));
        WritableImage selection = new WritableImage((int) Math.ceil(width), (int) Math.ceil(height));
        app.getCanvas().snapshot(params, selection);
        
        return selection;
    }
    
    public static void drawSelectionPreview(PaintFX app, double x, double y, double width, double height){
        GraphicsContext previewGc = app.getPreviewGc();
        previewGc.setStroke(Color.BLACK);
        previewGc.setLineWidth(1);
        previewGc.setLineDashes(5);
        previewGc.strokeRect(x, y, width, height);
        previewGc.setLineDashes(0);
    }
    
    public static void drawImagePreview(PaintFX app, WritableImage image, double x, double y){
        if(image == null){
            return;
        }
        
        GraphicsContext previewGc = app.getPreviewGc();
        
        previewGc.drawImage(image, x, y);
    }
}
