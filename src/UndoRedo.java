/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.Stack;

public class UndoRedo {
    private final Stack<WritableImage> undoStack;
    private final Stack<WritableImage> redoStack;
    
    public UndoRedo(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }
    
    public void saveState(Canvas canvas){
        WritableImage snapshot = takeSnapshot(canvas);
        undoStack.push(snapshot);
        redoStack.clear();
    }
    
    public boolean undo(Canvas canvas){
        if(undoStack.isEmpty()){
            return false;
        }
        
        WritableImage current = takeSnapshot(canvas);
        redoStack.push(current);
        
        WritableImage previous = undoStack.pop();
        restoreSnapshot(canvas, previous);
        
        return true;
    }
    
    public boolean redo(Canvas canvas){
        if(redoStack.isEmpty()){
            return false;
        }
        
        WritableImage current = takeSnapshot(canvas);
        undoStack.push(current);
        
        WritableImage next = redoStack.pop();
        restoreSnapshot(canvas, next);
        
        return true;
    }
    
    public void clear(){
        undoStack.clear();
        redoStack.clear();
    }
    
    public boolean canUndo(){
        return !undoStack.isEmpty();
    }
    
    public boolean canRedo(){
        return !redoStack.isEmpty();
    }
    
    private WritableImage takeSnapshot(Canvas canvas){
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        return snapshot;
    }
    
    private void restoreSnapshot(Canvas canvas, WritableImage snapshot){
        PixelReader reader = snapshot.getPixelReader();
        PixelWriter writer = canvas.getGraphicsContext2D().getPixelWriter();
        
        writer.setPixels(0, 0, (int) snapshot.getWidth(), (int) snapshot.getHeight(), reader, 0, 0);
    }
}
