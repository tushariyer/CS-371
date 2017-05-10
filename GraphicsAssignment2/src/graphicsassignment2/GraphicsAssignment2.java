/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphicsassignment2;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author tushariyer
 */
public class GraphicsAssignment2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DisplayMode dm = new DisplayMode(800, 600);
        boolean pressed = false;

        //init Display
        try {
            Display.setDisplayMode(dm);
            Display.setVSyncEnabled(true);
            Display.create();

        } catch (LWJGLException ex) {
            Logger.getLogger(GraphicsAssignment2.class.getName()).log(Level.SEVERE, null, ex);
        }
        //init openGL
        glClearColor(0.3f, 0.3f, 0.5f, 0.3f);
        glClear(GL_COLOR_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, dm.getWidth(), 0, dm.getHeight(), -1, 1);

        //draw
        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT);

            newMidpoint(dm.getWidth() / 2, dm.getHeight() / 2, Mouse.getX(), Mouse.getY());

            glFlush();
            Display.update();

        }
    }

    public static void newMidpoint(int x1, int y1, int x2, int y2) {
        //all the vars
        int x; 
        int y; 
        int d; 
        int dy; 
        int dx; 
        int incrN; 
        int incrNE;
        int incrE;
        int incrS;

        //If it's on the right
        if (x2 > x1) { 
            dx = x2 - x1;
            //If it's on the top half
            if (y2 >= y1) { 
                dy = y2 - y1;
                //Octant 1
                if (dx >= dy) {
                    d = dy * 2 - dx;
                    incrE = dy * 2;
                    incrNE = (dy - dx) * 2;
                    y = y1;
                    for (x = x1; x <= x2; x++) {
                        glColor3f(0.5f, 0.8f, 1.0f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrE;
                        }
                        else {
                            d += incrNE;
                            y++;
                        }
                    }
                }
                //Octant 2 - if dy > dx [Original Octant]
                else { 
                    d = dx * 2 - dy;
                    incrN = dx * 2; 
                    incrNE = (dx - dy) * 2; 
                    x = x1;
                    for (y = y1; y <= y2; y++) {
                        glColor3f(1.5f, 0.8f, 0.5f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrN;
                        }
                        else {
                            d += incrNE;
                            x++;
                        }
                    }
                }
            }
            //Bottom half if y1 > y2
            else {
                dy = y1 - y2;
                //Octant 3
                if (dx >= dy) { 
                    d = dx * 2 - dy;
                    incrE = dy * 2; 
                    incrS = (dy-dx) * 2; 
                    y = y1;
                    for (x = x1; x <= x2; x++) {
                        glColor3f(0.5f, 1.5f, 0.5f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrE;
                        }
                        else {
                            d += incrS;
                            y--;
                        }
                    }
                }
                //Octant 4 - if dy > dx
                else { 
                    d = dx * 2 - dy;
                    incrN = dx * 2; 
                    incrNE = (dx - dy) * 2; 
                    x = x1;
                    for (y = y1; y >= y2; y--) {
                        glColor3f(1.5f, 0.5f, 0.8f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrN;
                        }
                        else {
                            d += incrNE;
                            x++;
                        }
                    }
                }
            }
        }
        //left half if x2 > x2
        else {
            dx = x1 - x2;
            //bottom half
            if (y1 >= y2) {
                dy = y1 - y2;
                //Octant 5
                if (dy >= dx) { 
                    d = dx * 2 - dy;
                    incrN = dx * 2; 
                    incrNE = (dx - dy) * 2; 
                    x = x1;
                    for (y = y1; y >= y2; y--) {
                        glColor3f(0.0f, 1.0f, 1.0f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrN;
                        }
                        else {
                            d += incrNE;
                            x--;
                        }
                    }
                }
                //Octant 6 - if dx > dy
                else {
                    d = dy * 2 - dx;
                    incrE = dy * 2;
                    incrNE = (dy - dx) * 2;
                    y = y2;
                    for (x = x2; x <= x1; x++) {
                        glColor3f(1.5f, 1.2f, 0.6f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrE;
                        }
                        else {
                            d += incrNE;
                            y++;
                        }

                    }
                }
            }
            //top half if y2 > y2
            else {
                dy = y2 - y1;
                //Octant 7
                if (dx >= dy) { 
                    d = dx * 2 - dy;
                    incrE = dy * 2; 
                    incrS = (dy-dx) * 2; 
                    y = y2;
                    for (x = x2; x <= x1; x++) {
                        glColor3f(1.5f, 0.0f, 0.0f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrE;
                        }
                        else {
                            d += incrS;
                            y--;
                        }
                    }
                }
                //Octant 8 - if dy > dx
                else {
                    d = dx * 2 - dy;
                    incrN = dx * 2;
                    incrNE = (dx - dy) * 2;
                    x = x1;
                    for (y = y1; y <= y2; y++) {
                        glColor3f(0.8f, 0.8f, 1.8f);
                        glPointSize(3.5f);
                        glBegin(GL_POINTS);
                        glVertex2d(x, y);
                        glEnd();
                        if (d <= 0) {
                            d += incrN;
                        }
                        else {
                            d += incrNE;
                            x--;
                        }
                    }
                }
            }
        }
    }
    
    /*Original Given*/
    public static void drawLineMidpoint(int x0, int y0, int x1, int y1) {
        int dy, dx, incrE, incrNE, d, x, y;

        dy = y1 - y0;
        dx = x1 - x0;
        d = dy * 2 - dx;
        incrE = dy * 2;
        incrNE = (dy - dx) * 2;
        y = y0;

        for (x = x0; x <= x1; x++) {
            glBegin(GL_POINTS);
            glVertex2d(x, y);
            glEnd();

            if (d <= 0) {
                d += incrE;
            } else {
                d += incrNE;
                y++;
            }
        }
    }
}
//Tushar Iyer