package graphicsassignment4; //Package
import org.lwjgl.BufferUtils; //LWJGL Packages
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture; //Texture Packages
import org.newdawn.slick.opengl.TextureLoader;
import java.io.File; //IO for Texture
import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*; //LWJGL for Camera
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;
/**
 * @author tushariyer
 */
public class GraphicsAssignment4 {
    public static void main(String[] args) { //Let's do this ... pls
        Vector3f grid = new Vector3f(0, 0, 0); //World
        Texture texture = null; //Texture
        create(texture); //Create display
        boolean s = false, //For scaling
                r = false, //For rotation
                g = false, //For movement
                lightG = false, //For moving the light source
                camG = false, //For moving the camera
                camView = false; //For viewing through the camera
        int curPos = 0, nState = curPos; //Object properties
        float worldSpace = 15; //World space
        double amtR = 0; //Rotation 
        BlenderMethodInterface[] box = new BlenderMethodInterface[15];
        box[curPos] = new BlenderMethods();
        Vector3f newR = new Vector3f(box[curPos].getRV().getX(), box[curPos].getRV().getY(), box[curPos].getRV().getZ()), //Literally every vector we could possibly need
                newS = new Vector3f(box[curPos].getS().getX(), box[curPos].getS().getY(), box[curPos].getS().getZ()),
                newG = new Vector3f(box[curPos].getG().getX(), box[curPos].getG().getY(), box[curPos].getG().getZ()),
                setLight = new Vector3f(0, 3, 0),
                moveLight = setLight,
                setCam = new Vector3f(12, 0, 0),
                moveCam = setCam;
        while (!Display.isCloseRequested()) { //While the display is open
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glColor3f(1.0f, 1.0f, 1.0f);
            glPushMatrix();
            if (camView == true) { //Perspective settings if in camview
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                Vector3f tempG = box[curPos].getG();
                gluLookAt(setCam.getX(), setCam.getY(), setCam.getZ(), tempG.getX(), tempG.getY(), tempG.getZ(), 0, 1, 0);
                glLight(GL_LIGHT0, GL_POSITION, flipBuffer(new float[]{setLight.getX(), setLight.getY(), setLight.getZ(), 0.0f}));
                glEnable(GL_LIGHTING);
                glEnable(GL_LIGHT0);
            } else { //Perspective if not in camview
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                gluLookAt(0, 0, worldSpace, 0, 0, 0, 0, 1, 0);
            }
            if (camView == false) { //If we're not in camview
                if (Keyboard.isKeyDown(Keyboard.KEY_R) && !r) { //When R is pressed
                    g = s = lightG = camG = camView = false;
                    r = true; //Set only 'R' to true

                    Vector3f mCoords = getMousePositionIn3dCoords(); //Get Current Coordinates
                    grid = new Vector3f(mCoords.getX() - box[curPos].getG().getX(), mCoords.getY() - box[curPos].getG().getY(), mCoords.getZ() - box[curPos].getG().getZ());
                } else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !s) { //When S is pressed
                    r = g = lightG = camG = camView = false;
                    s = true; //Set only 'S' to true

                    Vector3f mCoords = getMousePositionIn3dCoords(); //Get Current Coordinates
                    grid = new Vector3f(mCoords.getX() - box[curPos].getG().getX(), mCoords.getY() - box[curPos].getG().getY(), mCoords.getZ() - box[curPos].getG().getZ());
                } else if (Keyboard.isKeyDown(Keyboard.KEY_G) && !g) { //When G is pressed
                    r = s = lightG = camG = camView = false;
                    g = true; //Set only 'G' to true

                    Vector3f mCoords = getMousePositionIn3dCoords(); //Get Current Coordinates
                    Mouse.getDWheel(); //Get Z position from wheel
                    grid = new Vector3f(mCoords.getX(), mCoords.getY(), mCoords.getZ());
                }
            }
            if (Mouse.isButtonDown(0)) { //When left-clicked
                s = r = g = camView = false; //Reset modes
                if (curPos == box.length - 1) { //Set settings to what they were prior to left-click
                    for (int i = 0; i < box.length - 2; i++) {
                        box[i] = box[i + 1];
                    }
                    box[box.length - 1] = null;
                    curPos = curPos - 1; //Go back a position
                }
                curPos += 1; //Increment position
                nState = curPos; //Set that to the new state
                box[curPos] = new BlenderMethods(newS, amtR, newR, newG); //Set the box to stay at the new orientation
            } else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) { //When Q is pressed
                System.exit(0); //Abandon everything and quit
            }
            if (g == true) { //When G is Pressed
                Vector3f motion = euclideanV(grid); //Get the distance between the original and current position

                newG = new Vector3f(box[curPos].getG().getX() + motion.getX(), box[curPos].getG().getY() + motion.getY(), box[curPos].getG().getY() + motion.getZ()); //New Position
                glTranslatef(newG.getX(), newG.getY(), newG.getZ()); //Change to new position

            } else if (g == false) {
                glTranslatef(box[curPos].getG().getX(), box[curPos].getG().getY(), box[curPos].getG().getZ()); //Keep new position
            }
            if (s == true) { //When S is Pressed
                Vector3f mCoords = getMousePositionIn3dCoords();
                dottedLine(mCoords.getX(), mCoords.getY(), mCoords.getZ()); //Draw dotted line
                float delta = mCoords.length() - grid.length(); //Changes in length

                newS = new Vector3f(box[curPos].getS().getX() + delta, box[curPos].getS().getY() + delta, box[curPos].getS().getZ() + delta); //New Scale
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
                glRotated(amtR, newR.getX(), newR.getY(), newR.getZ()); //Change to new rotation

            } else if (r == false) {
                glRotated(box[curPos].getRA(), box[curPos].getRV().getX(), box[curPos].getRV().getY(), box[curPos].getRV().getZ()); //Keep new angle
            }
            drawBox(); //Draw cube and fill it
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glPopMatrix(); //Flush and reset
            glFlush();
            glPushMatrix();
            /**
             * Lighting
             */
            if (camView == false) { //If we're not in camview
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                gluLookAt(0, 0, worldSpace, 0, 0, 0, 0, 1, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_L) && lightG == false && camView == false) { //When L is pressed
                r = s = g = camG = camView = false;
                lightG = true;
                Mouse.getDWheel();
                Vector3f mouse3d = getMousePositionIn3dCoords();
                grid = new Vector3f(mouse3d.getX(), mouse3d.getY(), mouse3d.getZ());
            } else if (lightG == true) { //If we're adjusting lighting
                Vector3f mouseDel = euclideanV(grid);
                int mouse = Mouse.getDWheel();
                moveLight = new Vector3f(setLight.getX() + mouseDel.getX(), setLight.getY() + mouseDel.getY(), moveLight.getZ() - (mouse));
                glTranslatef(moveLight.getX(), moveLight.getY(), moveLight.getZ());
            } else if (lightG == false) { //If we're not adjusting lighting
                glTranslatef(setLight.getX(), setLight.getY(), setLight.getZ());
            }
            if (lightG && Mouse.isButtonDown(0)) { //When we left-click
                s = r = g = lightG = camG = camView = false;
                setLight = new Vector3f(moveLight.getX(), moveLight.getY(), moveLight.getZ());
            } else if (lightG && Mouse.isButtonDown(1)) { //Or right-click
                s = r = g = lightG = camG = camView = false;
                moveLight = new Vector3f(setLight.getX(), setLight.getY(), setLight.getZ());
            }
            if (camView == false) { //Create an indicator for the light source only if we're not in camview
                pointSource();
            }
            glPopMatrix(); //Flush and reset
            glFlush();
            glPushMatrix();
            /**
             * Camera
             */
            if (camView == false) { //If we aren't rendering
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                gluLookAt(0, 0, worldSpace, 0, 0, 0, 0, 1, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_C) && camG == false && camView == false) { //When C is pressed
                r = s = g = lightG = camView = false;
                camG = true;
                Mouse.getDWheel();
                Vector3f mouse3d = getMousePositionIn3dCoords();
                grid = new Vector3f(mouse3d.getX(), mouse3d.getY(), mouse3d.getZ());
            } else if (camG == true) { //If we're in Camera perspective
                Vector3f mouseDel = euclideanV(grid);
                int mouse = Mouse.getDWheel();
                moveCam = new Vector3f(setCam.getX() + mouseDel.getX(), setCam.getY() + mouseDel.getY(), moveCam.getZ() - (mouse));
                glTranslatef(moveCam.getX(), moveCam.getY(), moveCam.getZ());
                glRotated(Math.toDegrees(Math.atan2(moveCam.getY() - newG.getY(), moveCam.getX() - newG.getX())), 0, 0, 1);
            } else if (camG == false) { //If we're not in Camera perspective
                glTranslatef(setCam.getX(), setCam.getY(), setCam.getZ());
                glRotated(Math.toDegrees(Math.atan2(moveCam.getY() - newG.getY(), moveCam.getX() - newG.getX())), 0, 0, 1);
            }
            if (camG && Mouse.isButtonDown(0)) { //When we left-click
                s = r = g = lightG = camG = camView = false;
                setCam = new Vector3f(moveCam.getX(), moveCam.getY(), moveCam.getZ());
            } else if (camG && (Mouse.isButtonDown(1) || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))) { //Or when we un-toggle camView
                s = r = g = lightG = camG = camView = false;
                moveCam = new Vector3f(setCam.getX(), setCam.getY(), setCam.getZ());
            }
            if (camView == false) { //Create a camera indicator only if we're not in camview
                camera();
            }
            glPopMatrix(); //Flush and Reset
            glFlush();
            if (Keyboard.isKeyDown(Keyboard.KEY_E) && camView == false) { //Toggle into camview
                s = r = g = lightG = camG = false;
                camView = true;
            }
            if (camView == true && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) { //Togger out of camview
                s = r = g = lightG = camG = camView = false;
                glDisable(GL_LIGHTING);
                glDisable(GL_LIGHT0);
                glDisable(GL_TEXTURE_2D);
            }
            if (camView == true) { //Just be
            } else { //Adjust perspective
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                gluLookAt(0, 0, worldSpace, 0, 0, 0, 0, 1, 0);
            }
            glPopMatrix(); //Flush and Reset
            glFlush();
            Display.update();
        }
    }
    private static void dottedLine(float x, float y, float z) { //Draws the dotted line when scaling or rotating from the origin to the mouse
        glColor3f(0.5f, 1.0f, 0.0f);
        glEnable(GL_LINE_STIPPLE);
        glLineStipple(24, (short) 0XAAAA);
        glBegin(GL_LINES);
        glVertex3f(0, 0, 0);
        glVertex3f(x, y, z);
        glEnd();
    }
    private static void create(Texture texture) { //Draw the display
        DisplayMode dm = new DisplayMode(1280, 720);
        try { //Initialise Display
            Display.setTitle("Graphics Assignment 4 - Tushar Iyer");
            Display.setDisplayMode(dm);
            Display.create();
            System.out.println("Controls:\n" + "R - Rotate\n" + "S - Scale\n" + "G - Move\n" + "L - Light\n" + "C - Camera\n" + "E - CamView\n" + "Esc to return\n" + "Q - Quit\n" + "\nLeft Click  - Stop Change"); //Console Controls
        } catch (LWJGLException ex) {
            Logger.getLogger(GraphicsAssignment4.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            texture = TextureLoader.getTexture("BMP", new FileInputStream(new File("GoldWeave.bmp")));
        } catch (Exception e) {
            System.out.println(e.toString());
            Display.destroy();
            System.exit(1);
        }
        glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER, GL_TRUE);
        glEnable(GL_DEPTH_TEST); //Initialise OpenGL
        glClearColor(0.3f, 0.3f, 0.5f, 0.3f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(90.0f, dm.getWidth() / (float) dm.getHeight(), 1, 20);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt(0, 0, 15, 0, 0, 0, 0, 1, 0);
    }
    private static void drawBox() { //Draws the box
        glEnable(GL_COLOR_MATERIAL);
        glColor3f(0.53f, 0.81f, 0.92f); //SkyBlue - Side One
        glNormal3f(0.0f, 0.0f, -1.0f);
        glBegin(GL_POLYGON);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glEnd();
        glColor3f(0.0f, 0.749f, 1.0f); //Deep Sky Blue - Side Two
        glNormal3f(0.0f, 0.0f, 1.0f);
        glBegin(GL_POLYGON);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glEnd();
        glColor3f(0.251f, 0.878f, 0.816f); //Turquoise - Side Three
        glNormal3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_POLYGON);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glEnd();
        glColor3f(0.118f, 0.565f, 1.0f); //Dodger Blue - Side Four
        glNormal3f(-1.0f, 0.0f, 0.0f);
        glBegin(GL_POLYGON);
        glTexCoord2f(1, 0);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glEnd();
        glColor3f(0.0f, 1.0f, 1.0f); //Cyan - Side Five
        glNormal3f(0.0f, 1.0f, 0.0f);
        glBegin(GL_POLYGON);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glEnd();
        glColor3f(0.275f, 0.51f, 0.706f); //Steel Blue - Side Six
        glNormal3f(0.0f, -1.0f, 0.0f);
        glBegin(GL_POLYGON);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glEnd();
        glDisable(GL_COLOR_MATERIAL);

    }
    private static void pointSource() { //Draws the light indicator
        glColor3f(1.0f, 1.5f, 0.8f);
        Sphere lightSource = new Sphere();
        lightSource.draw(0.5f, 20, 20);
    }
    private static void camera() { //Draws the camera indicator
        glColor3f(1.00f, 0.3f, 0.3f);
        Sphere camera = new Sphere();
        camera.draw(0.5f, 20, 20);
    }
    private static Vector3f getMousePositionIn3dCoords() { //Get Mouse position
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
    private static FloatBuffer flipBuffer(float[] inputBuffer) { //Flips given float buffer array
        FloatBuffer fBuff = BufferUtils.createFloatBuffer(inputBuffer.length);
        for (float i : inputBuffer) {
            fBuff.put(i);
        }
        fBuff.flip();
        return fBuff;
    }
}