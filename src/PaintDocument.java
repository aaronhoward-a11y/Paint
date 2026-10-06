/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;

public class PaintDocument {
    private Canvas canvas;
    private GraphicsContext gc;
    
    private Canvas previewCanvas;
    private GraphicsContext previewGc;
    
    private WritableImage image;
    private File currentFile;
    
    private boolean modified;
    
    private UndoRedo undoRedo;
    
    private double strokeWidth = 5.0;
    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private Line lineType = Line.SOLID;
    private Shape activeShape = Shape.RECTANGLE;
    private int polygonSides = 5;
    private String textFont = "Arial";
    private double textSize = 18;
    
    private double selectionStartX;
    private double selectionStartY;
    private double selectionEndX;
    private double selectionEndY;
    
    private WritableImage selectedImage;
    private WritableImage clipboardImage;
    private boolean movingSelection;
    private boolean pastingImage;
    private double moveOffsetX;
    private double moveOffsetY;
    
    private String displayName;
    
    public PaintDocument(String displayName, int width, int height){
        this.displayName = displayName;
        
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        previewCanvas = new Canvas(width, height);
        previewGc = previewCanvas.getGraphicsContext2D();
        previewCanvas.setMouseTransparent(true);
        
        image = new WritableImage(width, height);
        
        undoRedo = new UndoRedo();
        
        modified = false;
        currentFile = null;
    }
    
    /**
     * Returns the main drawing canvas.
     *
     * @return the drawing canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Returns the graphics context for the main canvas.
     *
     * @return the main graphics context
     */
    public GraphicsContext getGc() {
        return gc;
    }

    /**
     * Returns the preview canvas.
     *
     * @return the preview canvas
     */
    public Canvas getPreviewCanvas() {
        return previewCanvas;
    }

    /**
     * Returns the graphics context for the preview canvas.
     *
     * @return the preview graphics context
     */
    public GraphicsContext getPreviewGc() {
        return previewGc;
    }

    /**
     * Returns the image associated with this document.
     *
     * @return the document image
     */
    public WritableImage getImage() {
        return image;
    }

    /**
     * Sets the image associated with this document.
     *
     * @param image the document image
     */
    public void setImage(WritableImage image) {
        this.image = image;
    }

    /**
     * Returns the file associated with this document.
     *
     * @return the current file, or null if the document has not been saved
     */
    public java.io.File getCurrentFile() {
        return currentFile;
    }

    /**
     * Sets the file associated with this document.
     *
     * @param currentFile the current file
     */
    public void setCurrentFile(java.io.File currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * Returns whether the document has unsaved changes.
     *
     * @return true if the document has been modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Sets whether the document has unsaved changes.
     *
     * @param modified true if the document has been modified
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * Returns the undo/redo manager for this document.
     *
     * @return the undo/redo manager
     */
    public UndoRedo getUndoRedo() {
        return undoRedo;
    }

    /**
     * Returns the stroke width.
     *
     * @return the stroke width
     */
    public double getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * Sets the stroke width.
     *
     * @param strokeWidth the stroke width
     */
    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * Returns the stroke color.
     *
     * @return the stroke color
     */
    public Color getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the stroke color.
     *
     * @param strokeColor the stroke color
     */
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * Returns the fill color.
     *
     * @return the fill color
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Sets the fill color.
     *
     * @param fillColor the fill color
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the selected line type.
     *
     * @return the line type
     */
    public Line getLineType() {
        return lineType;
    }

    /**
     * Sets the selected line type.
     *
     * @param lineType the line type
     */
    public void setLineType(Line lineType) {
        this.lineType = lineType;
    }

    /**
     * Returns the active shape.
     *
     * @return the active shape
     */
    public Shape getActiveShape() {
        return activeShape;
    }

    /**
     * Sets the active shape.
     *
     * @param activeShape the active shape
     */
    public void setActiveShape(Shape activeShape) {
        this.activeShape = activeShape;
    }

    /**
     * Returns the number of polygon sides.
     *
     * @return the polygon side count
     */
    public int getPolygonSides() {
        return polygonSides;
    }

    /**
     * Sets the number of polygon sides.
     *
     * @param polygonSides the polygon side count
     */
    public void setPolygonSides(int polygonSides) {
        this.polygonSides = polygonSides;
    }

    /**
     * Returns the selected text font.
     *
     * @return the text font
     */
    public String getTextFont() {
        return textFont;
    }

    /**
     * Sets the selected text font.
     *
     * @param textFont the text font
     */
    public void setTextFont(String textFont) {
        this.textFont = textFont;
    }

    /**
     * Returns the selected text size.
     *
     * @return the text size
     */
    public double getTextSize() {
        return textSize;
    }

    /**
     * Sets the selected text size.
     *
     * @param textSize the text size
     */
    public void setTextSize(double textSize) {
        this.textSize = textSize;
    }

    /**
     * Returns the selection start X-coordinate.
     *
     * @return the selection start X-coordinate
     */
    public double getSelectionStartX() {
        return selectionStartX;
    }

    /**
     * Sets the selection start X-coordinate.
     *
     * @param value the selection start X-coordinate
     */
    public void setSelectionStartX(double value) {
        selectionStartX = value;
    }

    /**
     * Returns the selection start Y-coordinate.
     *
     * @return the selection start Y-coordinate
     */
    public double getSelectionStartY() {
        return selectionStartY;
    }

    /**
     * Sets the selection start Y-coordinate.
     *
     * @param value the selection start Y-coordinate
     */
    public void setSelectionStartY(double value) {
        selectionStartY = value;
    }

    /**
     * Returns the selection end X-coordinate.
     *
     * @return the selection end X-coordinate
     */
    public double getSelectionEndX() {
        return selectionEndX;
    }

    /**
     * Sets the selection end X-coordinate.
     *
     * @param value the selection end X-coordinate
     */
    public void setSelectionEndX(double value) {
        selectionEndX = value;
    }

    /**
     * Returns the selection end Y-coordinate.
     *
     * @return the selection end Y-coordinate
     */
    public double getSelectionEndY() {
        return selectionEndY;
    }

    /**
     * Sets the selection end Y-coordinate.
     *
     * @param value the selection end Y-coordinate
     */
    public void setSelectionEndY(double value) {
        selectionEndY = value;
    }

    /**
     * Returns the currently selected image.
     *
     * @return the selected image
     */
    public WritableImage getSelectedImage() {
        return selectedImage;
    }

    /**
     * Sets the currently selected image.
     *
     * @param selectedImage the selected image
     */
    public void setSelectedImage(WritableImage selectedImage) {
        this.selectedImage = selectedImage;
    }

    /**
     * Returns the clipboard image.
     *
     * @return the clipboard image
     */
    public WritableImage getClipboardImage() {
        return clipboardImage;
    }

    /**
     * Sets the clipboard image.
     *
     * @param clipboardImage the clipboard image
     */
    public void setClipboardImage(WritableImage clipboardImage) {
        this.clipboardImage = clipboardImage;
    }

    /**
     * Returns whether a selection is currently being moved.
     *
     * @return true if the selection is being moved
     */
    public boolean isMovingSelection() {
        return movingSelection;
    }

    /**
     * Sets whether a selection is being moved.
     *
     * @param movingSelection true if the selection is being moved
     */
    public void setMovingSelection(boolean movingSelection) {
        this.movingSelection = movingSelection;
    }

    /**
     * Returns whether an image is currently being pasted.
     *
     * @return true if an image is being pasted
     */
    public boolean isPastingImage() {
        return pastingImage;
    }

    /**
     * Sets whether an image is being pasted.
     *
     * @param pastingImage true if an image is being pasted
     */
    public void setPastingImage(boolean pastingImage) {
        this.pastingImage = pastingImage;
    }

    /**
     * Returns the X-coordinate offset used while moving a selection.
     *
     * @return the X offset
     */
    public double getMoveOffsetX() {
        return moveOffsetX;
    }

    /**
     * Sets the X-coordinate offset used while moving a selection.
     *
     * @param moveOffsetX the X offset
     */
    public void setMoveOffsetX(double moveOffsetX) {
        this.moveOffsetX = moveOffsetX;
    }

    /**
     * Returns the Y-coordinate offset used while moving a selection.
     *
     * @return the Y offset
     */
    public double getMoveOffsetY() {
        return moveOffsetY;
    }

    /**
     * Sets the Y-coordinate offset used while moving a selection.
     *
     * @param moveOffsetY the Y offset
     */
    public void setMoveOffsetY(double moveOffsetY) {
        this.moveOffsetY = moveOffsetY;
    }

    /**
     * Returns the name displayed on the document tab.
     *
     * @return the document display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the name displayed on the document tab.
     *
     * @param displayName the document display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
