package graphicsassignment4;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author tushariyer
 */
public interface BlenderMethodInterface {

    void setS(float x, float y, float z);

    void setS(Vector3f updateScale);

    void setRA(double angle);

    void setRV(Vector3f updateRotation);

    void setRV(float x, float y, float z);

    void setG(Vector3f updateTranslation);

    void setG(float x, float y, float z);

    Vector3f getS();

    double getRA();

    Vector3f getRV();

    Vector3f getG();

    String toString();
}
