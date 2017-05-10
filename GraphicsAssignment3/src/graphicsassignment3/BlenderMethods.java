package graphicsassignment3;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author tushariyer
 */
public class BlenderMethods {

    private Vector3f curBox; //Instance Vars
    private double angle;
    private Vector3f rVector;
    private Vector3f curPos;

    public BlenderMethods() { //Original Constructor
        curBox = new Vector3f(1, 1, 1);
        angle = 0;
        rVector = new Vector3f(1, 1, 1);
        curPos = new Vector3f(0, 0, 0);
    }

    public BlenderMethods(Vector3f nS, double nR, Vector3f nRV, Vector3f nG) { //Constructor for when vectors have changed
        curBox = new Vector3f(nS.getX(), nS.getY(), nS.getZ());
        angle = nR;
        rVector = new Vector3f(nRV.getX(), nRV.getY(), nRV.getZ());
        curPos = new Vector3f(nG.getX(), nG.getY(), nG.getZ());
    }

    Vector3f getS() {
        return curBox;
    } //Get Scale

    public void setS(float x, float y, float z) {
        curBox = new Vector3f(x, y, z);
    } //Set Scale

    public void setS(Vector3f newSize) {
        curBox = new Vector3f(newSize.getX(), newSize.getY(), newSize.getZ());
    } //Set Scale (Called when resizing)

    public double getRA() {
        return angle;
    } //Get Rotation Angle

    public void setRA(double nR) {
        angle = nR;
    } //Set Rotation Angle

    public Vector3f getRV() {
        return rVector;
    } //Get Rotation Vector

    public void setRV(Vector3f newRot) {
        rVector = new Vector3f(newRot.getX(), newRot.getY(), newRot.getZ());
    } //Set Rotation Vector

    public void setRV(float x, float y, float z) {
        rVector = new Vector3f(x, y, z);
    } //Set Rotation Vector (Called after rotated)

    public Vector3f getG() {
        return curPos;
    } //Get Movement

    public void setG(Vector3f newMove) {
        curPos = new Vector3f(newMove.getX(), newMove.getY(), newMove.getZ());
    } //Set Movement

    public void setG(float x, float y, float z) {
        curPos = new Vector3f(x, y, z);
    } //Set Movement (Called after moved)
}
