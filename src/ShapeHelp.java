/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author carri
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class ShapeHelp {
    
    /**
     * Draws a shape on the canvas using user specified settings.
     * 
     * @param app The PaintFX program
     * @param gc The graphics context to draw to
     * @param activeShape The type of shape to draw
     * @param startX The x-coordinate where the mouse is pressed
     * @param startY The y-coordinate where the mouse is pressed
     * @param endX The x-coordinate where the mouse is released
     * @param endY The y-coordinate where the mouse is released
     */
    public static void drawShape(PaintFX app, GraphicsContext gc, Shape activeShape, double startX, double startY, double endX, double endY){
        double x = startX;
        double y = startY;
        
        double width = endX-startX;
        double height = endY-startY;
        
        if(width<0){
            x = startX+width;
            width = -width;
        }
        
        if(height<0){
            y = startY+height;
            height = -height;
        }
        
        gc.setStroke(app.getStrokeColor());
        gc.setFill(app.getFillColor());
        gc.setLineWidth(app.getStrokeWidth());
        
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        
        // set the selected line type
        if(app.getLineType() == Line.DASHED){
            gc.setLineDashes(10);
        } else{
            gc.setLineDashes(0);
        }
        
        switch(activeShape){
            
            case RECTANGLE:
                drawRectangle(gc, x, y, width, height);
                break;
                
            case SQUARE:
                drawSquare(gc, x, y, width, height);
                break;
                
            case CIRCLE:
                drawCircle(gc, x, y, width, height);
                break;
                
            case ELLIPSE:
                drawEllipse(gc, x, y, width, height);
                break;
                
            case RIGHT_TRIANGLE:
                drawRightTriangle(gc, x, y, width, height);
                break;
                
            case TRIANGLE:
                drawTriangle(gc, x, y, width, height);
                break;
                
            case POLYGON:
                drawPolygon(gc, x+width/2, y+height/2, Math.min(width, height)/2, app.getPolygonSides());
                break;
                
            case STAR:
                drawStar(gc, x+width/2, y+height/2, Math.min(width, height)/2, Math.min(width, height)/4);
                break;
        }
        
        gc.setLineDashes(0);
    }
    
    /**
     * Draws a rectangle.
     * 
     * @param gc Graphics Context for drawing
     * @param x X-coordinate of the rectangle
     * @param y Y-coordinate of the rectangle
     * @param width Width of the rectangle
     * @param height Height of the rectangle
     */
    private static void drawRectangle(GraphicsContext gc, double x, double y, double width, double height){
        gc.fillRect(x, y, width, height);
        gc.strokeRect(x, y, width, height);
    }
    
    /**
     * Draws a square.
     * 
     * @param gc Graphics Context for drawing
     * @param startX X-coordinate where the mouse was pressed
     * @param startY Y-coordinate where the mouse was pressed
     * @param endX X-coordinate where the mouse was released
     * @param endY Y-coordinate where the mouse was released
     */
    private static void drawSquare(GraphicsContext gc, double x, double y, double width, double height){
        double size = Math.min(width, height);

        gc.fillRect(x, y, size, size);
        gc.strokeRect(x, y, size, size);
    }
    
    /**
     * Draws a circle.
     * 
     * @param gc Graphics Context for drawing
     * @param startX X-coordinate where the mouse was pressed
     * @param startY Y-coordinate where the mouse was pressed
     * @param endX X-coordinate where the mouse was released
     * @param endY Y-coordinate where the mouse was released
     */
    private static void drawCircle(GraphicsContext gc, double x, double y, double width, double height){
        double size = Math.min(width, height);

        gc.fillOval(x, y, size, size);
        gc.strokeOval(x, y, size, size);
    }
    
    /**
     * Draws an ellipse.
     * 
     * @param gc Graphics Context for drawing
     * @param x X-coordinate of the ellipse
     * @param y Y-coordinate of the ellipse
     * @param width Width of the ellipse
     * @param height Height of the ellipse
     */
    private static void drawEllipse(GraphicsContext gc, double x, double y, double width, double height){
        gc.fillOval(x, y, width, height);
        gc.strokeOval(x, y, width, height);
    }
    
    /**
     * Draws a right triangle.
     * 
     * @param gc Graphics Context for drawing
     * @param x X-coordinate of the triangle
     * @param y Y-coordinate of the triangle
     * @param width Width of the triangle
     * @param height Height of the triangle
     */
    private static void drawRightTriangle(GraphicsContext gc, double x, double y, double width, double height){
        double[] xPoints = {x, x, x+width};
        double[] yPoints = {y, y+height, y+height};
        
        gc.fillPolygon(xPoints, yPoints, 3);
        gc.strokePolygon(xPoints, yPoints, 3);
    }
    
    /**
     * Draws a non-right triangle.
     * 
     * @param gc Graphics Context for drawing
     * @param x X-coordinate of the triangle
     * @param y Y-coordinate of the triangle
     * @param width Width of the triangle
     * @param height Height of the triangle
     */
    private static void drawTriangle(GraphicsContext gc, double x, double y, double width, double height){
        double centerX = x+width/2;
        
        double[] xPoints = {centerX, x, x+width};
        double[] yPoints = {y, y+height, y+height};
        
        gc.fillPolygon(xPoints, yPoints, 3);
        gc.strokePolygon(xPoints, yPoints, 3);
    }
    
    /**
     * Draws a regular polygon of n sides.
     * 
     * @param gc Graphics Context for drawing
     * @param centerX X-coordinate of the polygon center
     * @param centerY Y-coordinate of the polygon center
     * @param radius Radius of the polygon
     * @param sides Number of sides of the polygon
     */
    private static void drawPolygon(GraphicsContext gc, double centerX, double centerY, double radius, int sides){
        if(sides < 3){
            return;
        }
        
        double[] xPoints = new double[sides];
        double[] yPoints = new double[sides];
        
        for(int i = 0; i < sides; i++){
            double angle = (2*Math.PI*i/sides) - Math.PI/2;
            xPoints[i] = centerX + radius*Math.cos(angle);
            yPoints[i] = centerY + radius*Math.sin(angle);
        }
        
        gc.fillPolygon(xPoints, yPoints, sides);
        gc.strokePolygon(xPoints, yPoints, sides);
    }
    
    /**
     * Draws a star.
     * 
     * @param gc Graphics Context for drawing
     * @param centerX X-coordinate of the star center
     * @param centerY Y-coordinate of the star center
     * @param outerRadius Outer radius of the star
     * @param innerRadius Inner radius of the star
     */
    private static void drawStar(GraphicsContext gc, double centerX, double centerY, double outerRadius, double innerRadius){
        int points = 5;
        int totalPoints = points*2;
        
        double[] xPoints = new double[totalPoints];
        double[] yPoints = new double[totalPoints];
        
        for(int i = 0; i < totalPoints; i++){
            double angle = (Math.PI*i/points) - Math.PI/2;
            double radius;
            
            if(i%2 == 0){
                radius = outerRadius;
            } else{
                radius = innerRadius;
            }
            
            xPoints[i] = centerX + radius*Math.cos(angle);
            yPoints[i] = centerY + radius*Math.sin(angle);
        }
        
            gc.fillPolygon(xPoints, yPoints, totalPoints);
            gc.strokePolygon(xPoints, yPoints, totalPoints);
    }
}
