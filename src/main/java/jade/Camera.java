package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Settings;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, invereseProjection, inverseView;
    public Vector2f position;
    private Vector2f projectionSize = new Vector2f(Settings.GRID_WIDTH * 40.0f, Settings.GRID_HEIGHT * 21.0f);
    private float zoom = 1.0f;


    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.invereseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
        projectionMatrix.invert(invereseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                        cameraFront.add(position.x, position.y, 0.0f),
                                        cameraUp);
        this.viewMatrix.invert(inverseView);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return this.invereseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }

    public Vector2f getProjectionSize() {
        return this.projectionSize;
    }

    public float getZoom() {
        return this.zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        adjustProjection();
    }

    public void addZoom(float value) {
        this.zoom += value;
    }
}
