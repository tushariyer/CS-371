package graphicsassignment3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author tushariyer
 */
public class GraphicsAssignment3 {

    public static void main(String[] args) {
        Vector3f grid = new Vector3f(0, 0, 0); //Instance Vars
        create(); //Create display
        boolean s = false, //For scaling
                r = false, //For rotation
                g = false, //For movement
                goX = false,
                goY = false,
                goZ = false;
        int curPos = 0, nState = curPos; //Properties for the box
        double amtR = 0;
        BlenderMethods[] box = new BlenderMethods[15];
        box[curPos] = new BlenderMethods();
        Vector3f newR = new Vector3f(box[curPos].getRV().getX(), box[curPos].getRV().getY(), box[curPos].getRV().getZ()), //Rotating
                newS = new Vector3f(box[curPos].getS().getX(), box[curPos].getS().getY(), box[curPos].getS().getZ()), //Scaling
                newG = new Vector3f(box[curPos].getG().getX(), box[curPos].getG().getY(), box[curPos].getG().getZ()); //Moving

        while (!Display.isCloseRequested()) { //Draw
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            if (Keyboard.isKeyDown(Keyboard.KEY_R) && !r) { //Rotate
                s = false;
                g = false;
                r = true; //Set only 'R' to true

                Vector3f mCoords = getMousePositionIn3dCoords(); //Get Current Coordinates
                grid = new Vector3f(mCoords.getX() - box[curPos].getG().getX(), mCoords.getY() - box[curPos].getG().getY(), mCoords.getZ() - box[curPos].getG().getZ());
            } else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !s) { //Resize
                r = false;
                g = false;
                s = true; //Set only 'S' to true

                Vector3f mCoords = getMousePositionIn3dCoords(); //Get Current Coordinates
                grid = new Vector3f(mCoords.getX() - box[curPos].getG().getX(), mCoords.getY() - box[curPos].getG().getY(), mCoords.getZ() - box[curPos].getG().getZ());
            } else if (Keyboard.isKeyDown(Keyboard.KEY_G) && !g) { //Move
                r = false;
                s = false;
                g = true; //Set only 'G' to true

                Vector3f mCoords = getMousePositionIn3dCoords(); //Get Current Coordinates
                grid = new Vector3f(mCoords.getX(), mCoords.getY(), mCoords.getZ());
            } else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                System.exit(0);
            }

            if (Mouse.isButtonDown(0)) { //When the Mouse is clicked
                s = false; //Disable all movements
                r = false;
                g = false;
                if (curPos == box.length - 1) { //Remain in the current position
                    for (int i = 0; i < box.length - 2; i++) {
                        box[i] = box[i + 1];
                    }
                    box[box.length - 1] = null;
                    curPos--;
                }
                curPos++; //Increment the position
                nState = curPos;
                box[curPos] = new BlenderMethods(newS, amtR, newR, newG); //Re-set the Vector3f positions with the new data
            }

            if (g == true) { //When G is Pressed
                Vector3f motion = euclideanV(grid); //Get the distance between the original and current position

                newG = new Vector3f(box[curPos].getG().getX() + boolVal(!goX) * motion.getX(), box[curPos].getG().getY() + boolVal(!goY) * motion.getY(), box[curPos].getG().getY() + boolVal(!goZ) * motion.getZ()); //New Position
                glTranslatef(newG.getX(), newG.getY(), newG.getZ()); //Change to new position

            } else if (g == false) {
                glTranslatef(box[curPos].getG().getX(), box[curPos].getG().getY(), box[curPos].getG().getZ()); //Keep new position
            }

            if (s == true) { //When S is Pressed
                Vector3f mCoords = getMousePositionIn3dCoords();
                dottedLine(mCoords.getX(), mCoords.getY(), mCoords.getZ()); //Draw dotted line
                float delta = mCoords.length() - grid.length(); //Changes in length

                newS = new Vector3f(box[curPos].getS().getX() + boolVal(!goX) * delta, box[curPos].getS().getY() + boolVal(!goY) * delta, box[curPos].getS().getZ() + boolVal(!goZ) * delta); //New Scale
                glScalef(newS.getX(), newS.getY(), newS.getZ()); //Change to new scale

            } else if (s == false) {
                glScalef(box[curPos].getS().getX(), box[curPos].getS().getY(), box[curPos].getS().getZ()); //Keep new scale
            }

            if (r == true) { //When R is Pressed
                Vector3f mCoords = getMousePositionIn3dCoords();
                dottedLine(mCoords.getX(), mCoords.getY(), mCoords.getZ()); //Draw dotted line

                double xProd = mCoords.getX() * grid.getY() - mCoords.getY() * grid.getX(); //Calculate the arctangent
                double scaProd = Vector3f.dot(mCoords, grid);
                amtR = box[curPos].getRA() - Math.toDegrees(Math.atan2(xProd, scaProd));

                newR = new Vector3f(boolVal(!goX), boolVal(!goY), boolVal(!goZ)); //New Rotation
                glRotated(amtR, newR.getX(), newR.getY(), newR.getZ()); //Change to new rotation

            } else if (r == false) {
                glRotated(box[curPos].getRA(), box[curPos].getRV().getX(), box[curPos].getRV().getY(), box[curPos].getRV().getZ()); //Keep new angle
            }

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); //Fill borders
            drawBox(); //Draw the box
            glPopMatrix();
            glFlush();
            Display.update();
        }
    }

    private static void dottedLine(float x, float y, float z) { //Draws the dotted line from the origin to the mouse
        glColor3f(0.5f, 1.0f, 0.0f);
        glEnable(GL_LINE_STIPPLE);
        glLineStipple(24, (short) 0XAAAA);
        glBegin(GL_LINES);
        glVertex3f(0, 0, 0);
        glVertex3f(x, y, z);
        glEnd();
    }

    private static void create() {
        DisplayMode dm = new DisplayMode(1280, 720);
        try { //Initialise Display
            Display.setTitle("Graphics Assignment 3 - Tushar Iyer");
            Display.setDisplayMode(dm);
            Display.create();
            System.out.println("Keys:\n" + "R - Rotate\n" + "S - Scale\n" + "G - Move\n" + "Q - Quit\n" + "\nLeft Click  - Stop Change"); //Console Controls
        } catch (LWJGLException ex) {
            Logger.getLogger(GraphicsAssignment3.class.getName()).log(Level.SEVERE, null, ex);
        }
        glEnable(GL_DEPTH_TEST); //Initialise OpenGL
        glClearColor(0.3f, 0.3f, 0.5f, 0.3f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(90.0f, dm.getWidth() / (float) dm.getHeight(), 1, 20);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt(0, 0, 15, 0, 0, 0, 0, 1, 0);
    }

    private static void drawBox() {
        glColor3f(0.53f, 0.81f, 0.92f); //SkyBlue
        glBegin(GL_POLYGON);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glEnd();

        glColor3f(0.0f, 0.749f, 1.0f); //Deep Sky Blue
        glBegin(GL_POLYGON);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glEnd();

        glColor3f(0.251f, 0.878f, 0.816f); //Turquoise
        glBegin(GL_POLYGON);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glEnd();

        glColor3f(0.118f, 0.565f, 1.0f); //Dodger Blue
        glBegin(GL_POLYGON);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glEnd();

        glColor3f(0.0f, 1.0f, 1.0f); //Cyan
        glBegin(GL_POLYGON);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glEnd();

        glColor3f(0.275f, 0.51f, 0.706f); //Steel Blue
        glBegin(GL_POLYGON);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glEnd();
    }

    private static Vector3f getMousePositionIn3dCoords() {
        FloatBuffer model = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer position = FloatBuffer.allocate(3);
        FloatBuffer winZ = BufferUtils.createFloatBuffer(1);

        viewport.clear();
        model.clear();
        projection.clear();
        winZ.clear();
        position.clear();

        glGetFloat(GL_MODELVIEW_MATRIX, model);
        glGetFloat(GL_PROJECTION_MATRIX, projection);
        glGetInteger(GL_VIEWPORT, viewport);

        glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, winZ);
        GLU.gluUnProject((float) Mouse.getX(), (float) Mouse.getY(), winZ.get(), model, projection, viewport, position);
        return new Vector3f(position.get(0), position.get(1), position.get(2));
    }

    private static Vector3f euclideanV(Vector3f grid) { //Euclidean distance between original & current vectors
        Vector3f a = getMousePositionIn3dCoords();
        return new Vector3f(-(grid.getX() - a.getX()), -(grid.getY() - a.getY()), -(grid.getZ() - a.getZ()));
    }

    private static int boolVal(boolean v) {
        return (v) ? 1 : 0;
    }
}
