/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphicsassignment1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
//import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author tushariyer
 * 
 * @assignment CS 371 - Assignment 1 - X Marks the Spot!
 */
public class GraphicsAssignment1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initialize();
        render();
    }
    
    /**
     * method initialize 
     * 
     * initializes the display and the error logger. It then starts OpenGL and 
     * sets the window title
     */
    private static void initialize(){
        try {
            Display.setDisplayMode(new DisplayMode(800,600));
            Display.create();
            //Keyboard.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(GraphicsAssignment1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Initialise OpenGL
        glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION); // Another possibility is (GL_MODELVIEW);
        glLoadIdentity();
        glOrtho(0, 800, 600, 0, -1.0, 1.0);
        
        Display.setTitle("X Marks the Spot! - Tushar Iyer"); //Window title
    }
    
    /**
     * method render
     * 
     * this method states that until the user requests the window to be closed, 
     * render the following items using their respective GL shapes
     */
    private static void render(){
        while (!Display.isCloseRequested()){
            pointsOnly(); //Draws using GL_POINTS
            
            linesOnly(); //Draws using GL_LINES
            
            lineStrip(); //Draws using GL_LINE_STRIP
            
            lineLoop(); //Draws using GL_LINE_LOOP
            
            trianglesOnly(); //Draws using GL_TRIANGLES
            
            triangleStrip(); //Draws using GL_TRIANGLE_STRIP
            
            triangleFan(); //Draws using GL_TRIANGLE_FAN
            
            quadsOnly(); //Draws using GL_QUADS
            
            quadStrip(); //Draws using GL_QUAD_STRIP
            
            polygonOnly(); //Draws using GL_POLYGON
            
            glFlush(); //Flush everything
            Display.update(); //Refresh display
        }
    }
    
    /**
     * method pointsOnly
     * 
     * renders a box with an 'x' using only points
     */
    private static void pointsOnly(){
        glColor3f (0.2f, 0.5f, 1.0f); //Sky blue
        int j = 150; //opposing coordinate for the positive diagonal
        /**
         * GL_LINE_POINTS
         */
        
        for (int i = 50; i <= 150; i++){
            glBegin(GL_POINTS);
                glVertex3f (i, 50.0f, 0.0f); //x-axis bottom
                glVertex3f (50.0f, i, 0.0f); //y-axis left
                glVertex3f (150, i, 0.0f); //x-axis top
                glVertex3f (i, i, 0.0f); //negative diagonal

                glVertex3f(i, j, 0.0f); //positive diagonal
                j--;

                glVertex3f (150, i, 0.0f); //x-axis top
                glVertex3f (i, 150, 0.0f); //y-axis right
            glEnd();
        }
    }
    
    /**
     * method linesOnly
     * 
     * renders a box with an 'x' using only lines
     */
    private static void linesOnly(){
        glColor3f (0.14f, 0.55f, 0.42f); //Sea green
        
        /**
         * GL_LINES
         */
        glBegin(GL_LINES);
            glVertex3f (200, 50, 0.0f); 
            glVertex3f (300, 50, 0.0f); //x-axis top
            glVertex3f (200, 150, 0.0f); 
            glVertex3f (300, 150, 0.0f); //x-axis bottom
            glVertex3f (200, 50, 0.0f); 
            glVertex3f (200, 150, 0.0f); //y-axis left
            glVertex3f (300, 50, 0.0f); 
            glVertex3f (300, 150, 0.0f); //y-axis right
            glVertex3f (200, 50, 0.0f); 
            glVertex3f (300, 150, 0.0f); //negative diagonal
            glVertex3f (300, 50, 0.0f); 
            glVertex3f (200, 150, 0.0f); //positive diagonal
        glEnd();
    }
    
    /**
     * method linesStrip
     * 
     * renders a box with an 'x' using line strips
     */
    private static void lineStrip(){
        glColor3f (0.6f, 0.86f, 0.44f); //neon green
        
        /**
         * GL_LINE_STRIP
         */
        glBegin(GL_LINE_STRIP);
            glVertex3f (350, 50, 0.0f); 
            glVertex3f (450, 50, 0.0f); //x-axis top
            glVertex3f (350, 150, 0.0f); 
            glVertex3f (450, 150, 0.0f); //x-axis bottom
            glVertex3f (350, 50, 0.0f); 
            glVertex3f (350, 150, 0.0f); //y-axis left
            glVertex3f (450, 50, 0.0f); 
            glVertex3f (450, 150, 0.0f); //y-axis right
            glVertex3f (350, 50, 0.0f); 
            glVertex3f (450, 150, 0.0f); //negative diagonal
            glVertex3f (450, 50, 0.0f); 
            glVertex3f (350, 150, 0.0f); //positive diagonal
        glEnd();
    }
    
    /**
     * method linesLoop
     * 
     * renders a box with an 'x' using line loops
     */
    private static void lineLoop(){
        glColor3f (1.0f, 0.5f, 0.2f); //gold
        
        /**
         * GL_LINE_LOOP
         */
        glBegin(GL_LINE_LOOP);
            glVertex3f (500, 50, 0.0f); 
            glVertex3f (600, 50, 0.0f); //x-axis top
            glVertex3f (500, 150, 0.0f); 
            glVertex3f (600, 150, 0.0f); //x-axis bottom (connects to top to form hourglass)
            glVertex3f (500, 50, 0.0f); 
            glVertex3f (500, 150, 0.0f); //y-axis left
            glVertex3f (600, 50, 0.0f); 
            glVertex3f (600, 150, 0.0f); //y-axis right
        glEnd();
    }
    
    /**
     * method trianglesOnly
     * 
     * renders a box with an 'x' using only triangles
     */
    private static void trianglesOnly(){
        glColor3f (1.0f, 0.1f, 0.2f); //orange
        
        /**
         * The following line ensures that the following shapes only have their
         * outlines drawn. Triangles, quadrilaterals & Polygons are filled in by default,
         * so we have to specify that we only want the outlines to be drawn.
         */
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); 
        
        /**
         * GL_TRIANGLES
         */
        glBegin(GL_TRIANGLES);
            glVertex3f (650, 50, 0.0f); 
            glVertex3f (750, 50, 0.0f);
            glVertex3f (650, 150, 0.0f); // isosceles from top left
            glVertex3f (750, 150, 0.0f); 
            glVertex3f (650, 50, 0.0f); 
            glVertex3f (650, 150, 0.0f); // isosceles from bottom left
            glVertex3f (750, 50, 0.0f); 
            glVertex3f (750, 150, 0.0f); 
            glVertex3f (650, 50, 0.0f); // isosceles from top right
        glEnd();
        
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }
    
    /**
     * method triangleStrip
     * 
     * renders a box with an 'x' using triangle strips
     */
    private static void triangleStrip(){
        glColor3f (1.0f, 0.0f, 0.0f); //red
        
        /**
         * GL_TRIANGLE_STRIP
         */
        glBegin(GL_TRIANGLE_STRIP);
            glVertex3f (50, 450, 0.0f); 
            glVertex3f (150, 450, 0.0f); 
            glVertex3f (50, 550, 0.0f); // isosceles from top left
            glVertex3f (150, 550, 0.0f); 
            glVertex3f (50, 450, 0.0f); 
            glVertex3f (50, 550, 0.0f); // isosceles from bottom left
            glVertex3f (150, 450, 0.0f); 
            glVertex3f (150, 550, 0.0f); 
            glVertex3f (50, 450, 0.0f); // isosceles from top right
        glEnd();
    }
    
    /**
     * method triangleFan
     * 
     * renders a box with an 'x' using triangle fans
     */
    private static void triangleFan(){
        glColor3f (0.86f, 0.6f, 0.44f); //tan
        
        /**
         * GL_TRIANGLE_FAN
         */
        glBegin(GL_TRIANGLE_FAN);
            glVertex3f (200, 450, 0.0f); 
            glVertex3f (300, 450, 0.0f); 
            glVertex3f (200, 550, 0.0f); // isosceles from top left
            glVertex3f (300, 550, 0.0f); 
            glVertex3f (200, 450, 0.0f); 
            glVertex3f (200, 550, 0.0f); // isosceles from bottom left
            glVertex3f (300, 450, 0.0f); 
            glVertex3f (300, 550, 0.0f); 
            glVertex3f (200, 450, 0.0f); // isosceles from top right
        glEnd();
    }
    
    /**
     * method quadsOnly
     * 
     * renders a box with an 'x' using only quadrilaterals
     */
    private static void quadsOnly(){
        glColor3f (0.65f, 0.16f, 0.16f); //brown
        
        /**
         * GL_QUADS
         */
        glBegin(GL_QUADS);
            glVertex3f (350, 450, 0.0f); 
            glVertex3f (450, 450, 0.0f); 
            glVertex3f (350, 550, 0.0f); 
            glVertex3f (450, 550, 0.0f); // hourglass completed
            glVertex3f (350, 450, 0.0f); 
            glVertex3f (350, 550, 0.0f); 
            glVertex3f (450, 450, 0.0f); 
            glVertex3f (450, 550, 0.0f); // y-axis lines added
        glEnd();
    }
    
    /**
     * method quadStrip
     * 
     * renders a box with an 'x' using quad strips
     */
    private static void quadStrip(){
        glColor3f (0.35f, 0.16f, 0.14f); //dark brown
        
        /**
         * GL_QUAD_STRIP
         */
        glBegin(GL_QUAD_STRIP);
            glVertex3f (500, 450, 0.0f); 
            glVertex3f (600, 450, 0.0f); 
            glVertex3f (500, 450, 0.0f); 
            glVertex3f (600, 550, 0.0f); // isosceles from top right
            glVertex3f (500, 550, 0.0f); 
            glVertex3f (600, 450, 0.0f); // isosceles from top left
            glVertex3f (500, 550, 0.0f); 
            glVertex3f (600, 550, 0.0f); // x-axis bottom added
        glEnd();
    }
    
    /**
     * method polygonOnly
     * 
     * renders a box with an 'x' using only polygons
     */
    private static void polygonOnly(){
        glColor3f (0.32f, 0.32f, 0.32f); //grey
        
        /**
         * GL_POLYGON
         */
        glBegin(GL_POLYGON);
            glVertex2i(650,450);
            glVertex2i(750,450);
            glVertex2i(650,550); // isosceles from top left
            glVertex2i(750,550); // turns into an hourglass
            glVertex2i(650,450);
            glVertex2i(750,450);
            glVertex2i(750,550); //y-axis on right added
            glVertex2i(650,550); //y-axis on left added
        glEnd();

        /**
         * The following line ends the clause by which multi-vertex shapes
         * need to only have their outlines drawn instead of the shape filled 
         * by default.
         */
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINES);
    }
}
//Tushar Iyer