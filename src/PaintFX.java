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
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// Other Imports
import java.io.File;

public class PaintFX extends Application{
    
    // define variables
    private Stage stage;
    private TabPane tabPane;
    private PaintDocument currentDocument;
    
    private ColorPicker lineColorPicker;
    
    private double startX;
    private double startY;
    
    private Tool activeTool = Tool.NONE;
    
    private FileOperations fileOperations;
    private Dialog dialog;
    
    /**
     * Creates and displays the PaintFX program window.
     * 
     * @param stage The stage used to display the program.
     */
    @Override
    public void start(Stage stage){
        this.stage = stage;
        stage.setTitle("PaintFX");
        
        fileOperations = new FileOperations(this);
        dialog = new Dialog(this);
        
        // initialize menubar
        MenuBar menuBar = new MenuBar();
        
        // initialize toolbar
        ToolBar toolBar = new ToolBar();
        
        // initialize file for menubar
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu toolsMenu = new Menu("Tools");
        Menu helpMenu = new Menu("Help");
        
        // initialize items inside file
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");
        
        // initialize items inside edit
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");
        MenuItem clearItem = new MenuItem("Clear");
        
        // initializes items inside tools
        MenuItem selectItem = new MenuItem("Select");
        MenuItem lineItem = new MenuItem("Line");
        MenuItem pencilItem = new MenuItem("Pencil");
        MenuItem shapeItem = new MenuItem("Shape");
        MenuItem textItem = new MenuItem("Text");
        MenuItem colorGrabberItem = new MenuItem("Color Grabber");
        
        // initializes items inside help
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        
        // initializes labels for tools
        Label widthLabel = new Label("Line Width:");
        ComboBox<Integer> widthBox = new ComboBox<>();
        Label lineColorLabel = new Label("Line Color:");
        lineColorPicker = new ColorPicker(Color.BLACK);
        Label fillColorLabel = new Label("Fill Color:");
        ColorPicker fillColorPicker = new ColorPicker(Color.WHITE);
        Label lineTypeLabel = new Label("Line Type:");
        ComboBox<Line> lineTypeBox = new ComboBox<>();
        Label shapeLabel = new Label("Shape:");
        ComboBox<Shape> shapeBox = new ComboBox<>();
        Label sidesLabel = new Label("Sides:");
        ComboBox<Integer> sidesBox = new ComboBox<>();
        Label fontLabel = new Label("Font:");
        ComboBox<String> fontBox = new ComboBox<>();
        Label fontSizeLabel = new Label("Font Size:");
        ComboBox<Integer> fontSizeBox = new ComboBox<>();
        
        // add all items under the file button
        fileMenu.getItems().addAll(newItem, openItem, closeItem, saveItem, saveAsItem, exitItem);
        
        // add all items under the edit button
        editMenu.getItems().addAll(undoItem, redoItem, copyItem, pasteItem, clearItem);
        
        // add all tools under the tools button
        toolsMenu.getItems().addAll(selectItem, lineItem, pencilItem, shapeItem, textItem, colorGrabberItem);
        
        // add all help features under the help button
        helpMenu.getItems().addAll(helpItem, aboutItem);
        
        // put all the options in the tool bar
        toolBar.getItems().addAll(widthLabel, widthBox, lineColorLabel, lineColorPicker, fillColorLabel, fillColorPicker, lineTypeLabel, lineTypeBox, shapeLabel, shapeBox, sidesLabel, sidesBox, fontLabel, fontBox, fontSizeLabel, fontSizeBox);
        
        // add line width options to dropdown
        widthBox.getItems().addAll(1, 2, 5, 10, 15, 20, 30);
        widthBox.setValue(5);
        lineTypeBox.getItems().addAll(Line.SOLID, Line.DASHED);
        lineTypeBox.setValue(Line.SOLID);
        shapeBox.getItems().addAll(Shape.RECTANGLE, Shape.SQUARE, Shape.CIRCLE, Shape.ELLIPSE, Shape.RIGHT_TRIANGLE, Shape.TRIANGLE, Shape.POLYGON, Shape.STAR);
        shapeBox.setValue(Shape.RECTANGLE);
        sidesBox.getItems().addAll(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        sidesBox.setValue(5);
        fontBox.getItems().addAll(Font.getFamilies());
        fontBox.setValue("Arial");
        fontBox.setCellFactory(comboBox -> new ListCell<String>(){
            @Override
            protected void updateItem(String fontName, boolean empty){
                super.updateItem(fontName, empty);
                
                if(empty || fontName == null){
                    setText(null);
                    setFont(Font.getDefault());
                } else{
                    setText(fontName);
                    setFont(Font.font(fontName, 16));
                }
            }
        });
        fontBox.setButtonCell(new ListCell<String>(){
            @Override
            protected void updateItem(String fontName, boolean empty){
                super.updateItem(fontName, empty);
                
                if(empty || fontName == null){
                    setText(null);
                    setFont(Font.getDefault());
            } else{
                    setText(fontName);
                    setFont(Font.font(fontName, 16));
                }
            }
        });
        fontSizeBox.getItems().addAll(8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 48, 60, 72, 90);
        fontSizeBox.setValue(18);
        
        // add file button to the menubar
        menuBar.getMenus().addAll(fileMenu, editMenu, toolsMenu, helpMenu);
        
        // call the function when the user chooses it
        newItem.setOnAction(event -> createNewTab("untitled"));
        openItem.setOnAction(event -> fileOperations.open());
        closeItem.setOnAction(event -> closeCurrentTab());
        saveItem.setOnAction(event -> fileOperations.save());
        saveAsItem.setOnAction(event -> fileOperations.saveAs());
        exitItem.setOnAction(event -> fileOperations.exit());
        
        // add undo and redo button actions
        undoItem.setOnAction(event -> {
            if(currentDocument.getUndoRedo().undo(currentDocument.getCanvas())){
                currentDocument.setModified(true);
            }
        });
        redoItem.setOnAction(event -> {
            if(currentDocument.getUndoRedo().redo(currentDocument.getCanvas())){
                currentDocument.setModified(true);
            }
        });
        copyItem.setOnAction(event -> copy());
        pasteItem.setOnAction(event -> paste());
        clearItem.setOnAction(event -> {
            if(dialog.confirmClearCanvas()){
                currentDocument.getUndoRedo().saveState(currentDocument.getCanvas());
                ImageHelp.clearCanvas(this);
                currentDocument.setModified(true);
            }
        });
        
        // turn on the line feature when clicked
        selectItem.setOnAction(event -> {activeTool = Tool.SELECT;});
        lineItem.setOnAction(event -> {activeTool = Tool.LINE;});
        pencilItem.setOnAction(event -> {activeTool = Tool.PENCIL;});
        shapeItem.setOnAction(event -> {activeTool = Tool.SHAPE;});
        textItem.setOnAction(event -> {activeTool = Tool.TEXT;});
        colorGrabberItem.setOnAction(event -> {activeTool = Tool.COLOR_GRABBER;});
        
        // set action for clicking help features
        helpItem.setOnAction(event -> dialog.help());
        aboutItem.setOnAction(event -> dialog.about());
        
        // retrieve the option the user selects in the tool bar
        widthBox.setOnAction(event -> {currentDocument.setStrokeWidth(widthBox.getValue());});
        lineColorPicker.setOnAction(event -> {currentDocument.setStrokeColor(lineColorPicker.getValue()); currentDocument.getGc().setStroke(lineColorPicker.getValue());});
        fillColorPicker.setOnAction(event -> {currentDocument.setFillColor(fillColorPicker.getValue()); currentDocument.getGc().setFill(fillColorPicker.getValue());});
        lineTypeBox.setOnAction(event -> {currentDocument.setLineType(lineTypeBox.getValue());});
        shapeBox.setOnAction(event -> {currentDocument.setActiveShape(shapeBox.getValue());});
        sidesBox.setOnAction(event -> {currentDocument.setPolygonSides(sidesBox.getValue());});
        fontBox.setOnAction(event -> {currentDocument.setTextFont(fontBox.getValue());});
        fontSizeBox.setOnAction(event -> {currentDocument.setTextSize(fontSizeBox.getValue());});
        
        // keyboard shortcuts
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        closeItem.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveAsItem.setAccelerator(KeyCombination.keyCombination("F12"));
        exitItem.setAccelerator(KeyCombination.keyCombination("Alt+F4"));
        
        undoItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        redoItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
        copyItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        pasteItem.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        clearItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+X"));
        
        selectItem.setAccelerator(KeyCombination.keyCombination("S"));
        lineItem.setAccelerator(KeyCombination.keyCombination("L"));
        pencilItem.setAccelerator(KeyCombination.keyCombination("P"));
        shapeItem.setAccelerator(KeyCombination.keyCombination("H"));
        textItem.setAccelerator(KeyCombination.keyCombination("T"));
        colorGrabberItem.setAccelerator(KeyCombination.keyCombination("I"));
        
        helpItem.setAccelerator(KeyCombination.keyCombination("F1"));
        aboutItem.setAccelerator(KeyCombination.keyCombination("Alt+A"));
        
        // create the tab pane
        tabPane = new TabPane();
        createNewTab("untitled");
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {if(newTab != null){switchToTab(newTab);}});
        
        // combine menu bar and tool bar to be at the top of the window
        VBox top = new VBox();
        top.getChildren().addAll(menuBar, toolBar);
        
        // create border
        BorderPane root = new BorderPane();
        
        // add menubar, toolbar, and canvas to the stage
        root.setTop(top);
        root.setCenter(tabPane);
        
        // size and display the scene
        Scene scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }
    
    public void copy(){
        WritableImage selectedImage = currentDocument.getSelectedImage();
        
        if(selectedImage == null){
            return;
        }
        
        WritableImage clipboardImage = new WritableImage((int) selectedImage.getWidth(), (int) selectedImage.getHeight());
        
        PixelReader reader = selectedImage.getPixelReader();
        PixelWriter writer = clipboardImage.getPixelWriter();
        
        for(int y = 0; y < selectedImage.getHeight(); y++){
            for(int x = 0; x < selectedImage.getWidth(); x++){
                writer.setColor(x, y, reader.getColor(x, y));
            }
        }
        
        currentDocument.setClipboardImage(clipboardImage);
    }
    
    public void paste(){
        WritableImage clipboardImage = currentDocument.getClipboardImage();
        
        if(clipboardImage == null){
            return;
        }
        
        WritableImage selectedImage = new WritableImage((int) clipboardImage.getWidth(), (int) clipboardImage.getHeight());
        
        PixelReader reader = clipboardImage.getPixelReader();
        PixelWriter writer = selectedImage.getPixelWriter();
        
        for(int y = 0; y < clipboardImage.getHeight(); y++){
            for(int x = 0; x < clipboardImage.getWidth(); x++){
                writer.setColor(x, y, reader.getColor(x, y));
            }
        }
        
        currentDocument.setSelectedImage(selectedImage);
        
        currentDocument.setPastingImage(true);
        currentDocument.setMovingSelection(false);
        
        activeTool = Tool.SELECT;
        
        ImageHelp.clearPreviewCanvas(this);
    }
    
    private void closeCurrentTab(){
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        
        if(selectedTab == null){
            return;
        }
        
        PaintDocument doc = (PaintDocument) selectedTab.getUserData();
        
        if(doc == null){
            return;
        }

        if(currentDocument.isModified()){
            if(!fileOperations.confirmSaveChanges("closing the tab")){
                return;
            }
        }
        
        tabPane.getTabs().remove(selectedTab);
        
        if(tabPane.getTabs().isEmpty()){
            createNewTab("untitled");
        }
    }
    
    private void createNewTab(String displayName){
        PaintDocument doc = new PaintDocument(displayName, 750, 750);
        ImageHelp.createBlankImage(doc, 750, 750);
        
        Tab tab = new Tab(displayName);
        StackPane canvasStack = new StackPane();
        canvasStack.getChildren().addAll(doc.getCanvas(), doc.getPreviewCanvas());
        
        ScrollPane scrollPane = new ScrollPane(canvasStack);
        scrollPane.setPannable(true);
        
        tab.setContent(scrollPane);
        tab.setUserData(doc);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        
        switchToTab(tab);
    }
    
    private void switchToTab(Tab tab){
        if(tab == null){
            return;
        }
        
        PaintDocument doc = (PaintDocument) tab.getUserData();
        
        if(doc == null){
            return;
        }
        
        currentDocument = doc;
        
        attachMouseHandlers();
    }
    
    public void updateCurrentTabTitle(){
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        
        if(tab == null || currentDocument == null){
            return;
        }
        
        String title;
        
        if(currentDocument.getCurrentFile() == null){
            title = "untitled";
        } else{
            title = currentDocument.getCurrentFile().getName();
        }
        
        tab.setText(title);
        currentDocument.setDisplayName(title);
    }
    
    public void openImageInNewTab(File selectedFile){
        if(selectedFile == null){
            return;
        }
        
        PaintDocument doc = new PaintDocument(selectedFile.getName(), 750, 750);
        
        Image image = new Image(selectedFile.toURI().toString());
        
        if(image.isError()){
            dialog.showError("Open Error", "Could not open the image");
            return;
        }
        
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();
        
        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                writer.setColor(x, y, reader.getColor(x, y));
            }
        }
        
        doc.setImage(writableImage);
        doc.setCurrentFile(selectedFile);
        doc.setModified(false);
        doc.getCanvas().setWidth(writableImage.getWidth());
        doc.getCanvas().setHeight(writableImage.getHeight());
        doc.getPreviewCanvas().setWidth(writableImage.getWidth());
        doc.getPreviewCanvas().setHeight(writableImage.getHeight());
        doc.getGc().drawImage(writableImage, 0, 0);
        
        Tab tab = new Tab(selectedFile.getName());
        StackPane canvasStack = new StackPane();
        canvasStack.getChildren().addAll(doc.getCanvas(), doc.getPreviewCanvas());
        ScrollPane scrollPane = new ScrollPane(canvasStack);
        scrollPane.setPannable(true);
        tab.setContent(scrollPane);
        tab.setUserData(doc);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        switchToTab(tab);
    }
    
    private void attachMouseHandlers(){
        Canvas currentCanvas = currentDocument.getCanvas();
        
        currentCanvas.setOnMousePressed(event -> {handleMousePressed(event);});
        
        currentCanvas.setOnMouseDragged(event -> {handleMouseDragged(event);});
        
        currentCanvas.setOnMouseReleased(event -> {handleMouseReleased(event);});
    }
    
    private void handleMousePressed(MouseEvent event){
        if(activeTool == Tool.LINE || activeTool == Tool.SHAPE){
            startX = event.getX();
            startY = event.getY();
                
        } else if(activeTool == Tool.COLOR_GRABBER){
            int x = (int) event.getX();
            int y = (int) event.getY();
                
            Color selectedColor = ImageHelp.grabColor(this, x, y);
                
            if(selectedColor != null){
                lineColorPicker.setValue(selectedColor);
                currentDocument.setStrokeColor(selectedColor);
            }
                
            activeTool = Tool.NONE;   
        } else if(activeTool == Tool.TEXT){
            currentDocument.getUndoRedo().saveState(currentDocument.getCanvas());
                
            double textX = event.getX();
            double textY = event.getY();
            
            String text = dialog.textDialog();
                
            if(text != null && !text.isEmpty()){
                ImageHelp.drawText(this, text, textX, textY);
            }
                
            activeTool = Tool.NONE;
            currentDocument.setModified(true);
        } else if(activeTool == Tool.PENCIL){
            currentDocument.getUndoRedo().saveState(currentDocument.getCanvas());
                
            startX = event.getX();
            startY = event.getY();
        } else if(activeTool == Tool.SELECT){
            double mouseX = event.getX();
            double mouseY = event.getY();
                
            if(currentDocument.isPastingImage()){
                startX = mouseX;
                startY = mouseY;
                return;
            }
            
            WritableImage selectedImage = currentDocument.getSelectedImage();
                
            if(selectedImage != null && mouseX >= currentDocument.getSelectionStartX() && mouseX <= currentDocument.getSelectionEndX() && mouseY >= currentDocument.getSelectionStartY() && mouseY <= currentDocument.getSelectionEndY()){
                currentDocument.setMovingSelection(true);
                currentDocument.setMoveOffsetX(mouseX-currentDocument.getSelectionStartX());
                currentDocument.setMoveOffsetY(mouseY-currentDocument.getSelectionStartY());
                    
                currentDocument.getGc().clearRect(currentDocument.getSelectionStartX(), currentDocument.getSelectionStartY(), selectedImage.getWidth(), selectedImage.getHeight());
                    
                ImageHelp.clearPreviewCanvas(this);
                    
                ImageHelp.drawImagePreview(this, selectedImage, currentDocument.getSelectionStartX(), currentDocument.getSelectionStartY());
                
                return;
            }
                
            currentDocument.setSelectionStartX(mouseX);
            currentDocument.setSelectionStartY(mouseY);
            currentDocument.setSelectionEndX(mouseX);
            currentDocument.setSelectionEndY(mouseY);
            }
    }
    
    private void handleMouseDragged(MouseEvent event){
        if(activeTool == Tool.PENCIL){
            double x = event.getX();
            double y = event.getY();
               
            currentDocument.getGc().setStroke(currentDocument.getStrokeColor());
            currentDocument.getGc().setLineWidth(currentDocument.getStrokeWidth());
            currentDocument.getGc().strokeLine(startX, startY, x, y);
               
            startX = x;
            startY = y;
               
            currentDocument.setModified(true);
        } else if(activeTool == Tool.LINE){
            ImageHelp.clearPreviewCanvas(this);
            ImageHelp.drawLine(this, currentDocument.getPreviewGc(), startX, startY, event.getX(), event.getY());
        } else if(activeTool == Tool.SHAPE){
            ImageHelp.clearPreviewCanvas(this);
            ShapeHelp.drawShape(this, currentDocument.getPreviewGc(), currentDocument.getActiveShape(), startX, startY, event.getX(), event.getY());
        } else if(activeTool == Tool.SELECT){
            double mouseX = event.getX();
            double mouseY = event.getY();
            
            WritableImage selectedImage = currentDocument.getSelectedImage();
               
            if(currentDocument.isMovingSelection() && selectedImage != null){
                double newX = mouseX-currentDocument.getMoveOffsetX();
                double newY = mouseY-currentDocument.getMoveOffsetY();
                   
                ImageHelp.clearPreviewCanvas(this);
                   
                ImageHelp.drawImagePreview(this, selectedImage, newX, newY);
               
                return;
               }
               
            if(currentDocument.isPastingImage() && selectedImage != null){
                double newX = mouseX-selectedImage.getWidth()/2.0;
                double newY = mouseY-selectedImage.getHeight()/2.0;
                   
                ImageHelp.clearPreviewCanvas(this);
                   
                ImageHelp.drawImagePreview(this, selectedImage, newX, newY);
                   
                return;
            }
            
            currentDocument.setSelectionEndX(mouseX);
            currentDocument.setSelectionEndY(mouseY);
               
            double x = Math.min(currentDocument.getSelectionStartX(), currentDocument.getSelectionEndX());
            double y = Math.min(currentDocument.getSelectionStartY(), currentDocument.getSelectionEndY());
               
            double width = Math.abs(currentDocument.getSelectionEndX() - currentDocument.getSelectionStartX());
            double height = Math.abs(currentDocument.getSelectionEndY() - currentDocument.getSelectionStartY());
               
            ImageHelp.clearPreviewCanvas(this);
               
            ImageHelp.drawSelectionPreview(this, x, y, width, height);
           } 
    }
    
    private void handleMouseReleased(MouseEvent event){
        if (activeTool == Tool.LINE){
            currentDocument.getUndoRedo().saveState(currentDocument.getCanvas());
            ImageHelp.drawLine(this, currentDocument.getGc(), startX, startY, event.getX(), event.getY());
            ImageHelp.clearPreviewCanvas(this);
            currentDocument.setModified(true);
        } else if(activeTool == Tool.SHAPE){
            ImageHelp.clearPreviewCanvas(this);
            currentDocument.getUndoRedo().saveState(currentDocument.getCanvas());
            ShapeHelp.drawShape(this, currentDocument.getGc(), currentDocument.getActiveShape(), startX, startY, event.getX(), event.getY());
            currentDocument.setModified(true);
        } else if(activeTool == Tool.SELECT){
            double mouseX = event.getX();
            double mouseY = event.getY();
            
            WritableImage selectedImage = currentDocument.getSelectedImage();
                
            if(currentDocument.isMovingSelection() && selectedImage != null){
                double newX = mouseX - currentDocument.getMoveOffsetX();
                double newY = mouseY - currentDocument.getMoveOffsetY();
                double width = selectedImage.getWidth();
                double height = selectedImage.getHeight();
                    
                currentDocument.getUndoRedo().saveState(currentDocument.getCanvas());
                    
                currentDocument.getGc().drawImage(selectedImage, newX, newY);
                    
                currentDocument.setSelectionStartX(newX);
                currentDocument.setSelectionStartY(newY);
                currentDocument.setSelectionEndX(newX+width);
                currentDocument.setSelectionEndY(newY+width);
                    
                ImageHelp.clearPreviewCanvas(this);
                ImageHelp.drawSelectionPreview(this, currentDocument.getSelectionStartX(), currentDocument.getSelectionStartY(), currentDocument.getSelectionEndX()-currentDocument.getSelectionStartX(), currentDocument.getSelectionEndY()-currentDocument.getSelectionStartX());
                    
                currentDocument.setMovingSelection(false);
                currentDocument.setModified(true);
                    
                return;
            }
                
            if(currentDocument.isPastingImage() && selectedImage != null){
                double pasteX = mouseX - selectedImage.getWidth()/2.0;
                double pasteY = mouseY - selectedImage.getHeight()/2.0;
                    
                currentDocument.getUndoRedo().saveState(currentDocument.getCanvas());
                    
                currentDocument.getGc().drawImage(selectedImage, pasteX, pasteY);
                    
                currentDocument.setSelectionStartX(pasteX);
                currentDocument.setSelectionStartY(pasteY);
                currentDocument.setSelectionEndX(pasteX+selectedImage.getWidth());
                currentDocument.setSelectionEndY(pasteY+selectedImage.getHeight());
                    
                ImageHelp.clearPreviewCanvas(this);
                ImageHelp.drawSelectionPreview(this, currentDocument.getSelectionStartX(), currentDocument.getSelectionStartY(), currentDocument.getSelectionEndX()-currentDocument.getSelectionStartX(), currentDocument.getSelectionEndY()-currentDocument.getSelectionStartY());
                    
                currentDocument.setPastingImage(false);
                currentDocument.setModified(true);
                    
                return;
            }
                
            currentDocument.setSelectionEndX(mouseX);
            currentDocument.setSelectionEndY(mouseY);
                
            double x = Math.min(currentDocument.getSelectionStartX(), currentDocument.getSelectionEndX());
            double y = Math.min(currentDocument.getSelectionStartY(), currentDocument.getSelectionEndY());
            double width = Math.abs(currentDocument.getSelectionEndX() - currentDocument.getSelectionStartX());
            double height = Math.abs(currentDocument.getSelectionEndY() - currentDocument.getSelectionStartY());
                
            if(width > 0 && height > 0){
                selectedImage = ImageHelp.captureSelection(this, x, y, width, height);
                
                currentDocument.setSelectedImage(selectedImage);
                
                currentDocument.setSelectionStartX(x);
                currentDocument.setSelectionStartY(y);
                currentDocument.setSelectionEndX(x+width);
                currentDocument.setSelectionEndY(y+height);
            }
                
            ImageHelp.clearPreviewCanvas(this);
            ImageHelp.drawSelectionPreview(this, currentDocument.getSelectionStartX(), currentDocument.getSelectionStartY(), currentDocument.getSelectionEndX()-currentDocument.getSelectionStartX(), currentDocument.getSelectionEndY()-currentDocument.getSelectionStartY());
        }
    }
    
    // getters and setters
    /**
     * Returns the program stage.
     * 
     * @return The program stage
     */
    public Stage getStage(){
        return stage;
    }
    
    public TabPane getTabPane(){
        return tabPane;
    }
    
    public PaintDocument getCurrentDocument(){
        return currentDocument;
    }
    
    /**
     * Returns the canvas used to display and edit the image.
     * 
     * @return The program canvas
     */
    public Canvas getCanvas(){
        return currentDocument.getCanvas();
    }
    
    /**
     * Returns the preview canvas used when drawing.
     * 
     * @return The preview canvas
     */
    public Canvas getPreviewCanvas(){
        return currentDocument.getPreviewCanvas();
    }
    
    /**
     * Returns the graphics context used to draw on the canvas.
     * 
     * @return The graphics context
     */
    public GraphicsContext getGc(){
        return currentDocument.getGc();
    }
    
    /**
     * Returns the graphics context of the preview canvas.
     * 
     * @return The preview graphics context
     */
    public GraphicsContext getPreviewGc(){
        return currentDocument.getPreviewGc();
    }
    
    /**
     * Returns the current image, or null if no image is open.
     * 
     * @return The current image
     */
    public WritableImage getImage(){
        return currentDocument.getImage();
    }
    
    public WritableImage getSelectedImage(){
        return currentDocument.getSelectedImage();
    }
            
    /**
     * Sets the current image to be edited.
     * 
     * @param image The image to be set as current
     */
    public void setImage(WritableImage image){
        currentDocument.setImage(image);
    }
    
    /**
     * Returns the file of the current open image, or null if no image is open.
     * 
     * @return The current image file
     */
    public File getCurrentFile(){
        return currentDocument.getCurrentFile();
    }
    
    /**
     * Sets the file of the current open image.
     * 
     * @param currentFile The file of the current image
     */
    public void setCurrentFile(File currentFile){
        currentDocument.setCurrentFile(currentFile);
    }
    
    /**
     * Checks to see if the current image has unsaved changes.
     * 
     * @return True if the image has been modified, false otherwise
     */
    public boolean isModified(){
        return currentDocument.isModified();
    }
    
    /**
     * Sets the modified status of the current image.
     * 
     * @param modified True if the image has unsaved changes, false otherwise
     */
    public void setModified(boolean modified){
        currentDocument.setModified(modified);
    }
    
    /**
     * Returns the current selected tool.
     * 
     * @return The active tool
     */
    public Tool getActiveTool(){
        return activeTool;
    }
    
    /**
     * Sets the current selected tool.
     * 
     * @param activeTool The tool to become active
     */
    public void setActiveTool(Tool activeTool){
        this.activeTool = activeTool;
    }
    
    /**
     * Returns the current selected shape.
     * 
     * @return The active shape
     */
    public Shape getActiveShape(){
        return currentDocument.getActiveShape();
    }
    
    /**
     * Sets the current selected shape.
     * 
     * @param activeShape The shape to become active
     */
    public void setActiveShape(Shape activeShape){
        currentDocument.setActiveShape(activeShape);
    }
    
    /**
     * Returns the current selected line type.
     * 
     * @return The active line type
     */
    public Line getLineType(){
        return currentDocument.getLineType();
    }
    
    /**
     * Sets the current selected line type.
     * 
     * @param lineType The line type to become active
     */
    public void setLineType(Line lineType){
        currentDocument.setLineType(lineType);
    }
    
    /**
     * Returns the current selected stroke color.
     * 
     * @return The active stroke color
     */
    public Color getStrokeColor(){
        return currentDocument.getStrokeColor();
    }
    
    /**
     * Sets the current selected stroke color.
     * 
     * @param strokeColor The color to become active
     */
    public void setStrokeColor(Color strokeColor){
        currentDocument.setStrokeColor(strokeColor);
    }
    
    /**
     * Returns the current selected fill color.
     * 
     * @return The active fill color
     */
    public Color getFillColor(){
        return currentDocument.getFillColor();
    }
    
    /**
     * Sets the current selected fill color.
     * 
     * @param fillColor The fill color to become active
     */
    public void setFillColor(Color fillColor){
        currentDocument.setFillColor(fillColor);
    }
    
    /**
     * Returns the current selected stroke width.
     * 
     * @return The active stroke width
     */
    public double getStrokeWidth(){
        return currentDocument.getStrokeWidth();
    }
    
    /**
     * Sets the current selected stroke width.
     * 
     * @param strokeWidth The width to become active
     */
    public void setStrokeWidth(double strokeWidth){
        currentDocument.setStrokeWidth(strokeWidth);
    }
    
    /**
     * Returns the dialog manager of the program.
     * 
     * @return The Dialog object
     */
    public Dialog getDialog(){
        return dialog;
    }
    
    /**
     * Returns the number of sides for a regular polygon.
     * 
     * @return The sides of a regular polygon
     */
    public int getPolygonSides(){
        return currentDocument.getPolygonSides();
    }
    
    /**
     * Sets the current number of sides for a regular polygon.
     * 
     * @param polygonSides The number of sides to become active
     */
    public void setPolygonSides(int polygonSides){
        currentDocument.setPolygonSides(polygonSides);
    }
    
    /**
     * Returns the current font.
     * 
     * @return Selected font
     */
    public String getTextFont(){
        return currentDocument.getTextFont();
    }
    
    /**
     * Sets the current font.
     * 
     * @param textFont Font name
     */
    public void setTextFont(String textFont){
        currentDocument.setTextFont(textFont);
    }
    
    /**
     * Returns the current font size.
     * 
     * @return Selected font size
     */
    public double getTextSize(){
        return currentDocument.getTextSize();
    }
    
    /**
     * Sets the current font size.
     * 
     * @param textSize Font size
     */
    public void setTextSize(double textSize){
        currentDocument.setTextSize(textSize);
    }
    
    public UndoRedo getUndoRedo(){
        return currentDocument.getUndoRedo();
    }
    
    /**
     * Starts the PaintFX program.
     * 
     * @param args Command line arguments passed to the program
     */
    public static void main(String[] args){
        launch(args);
    }
}
