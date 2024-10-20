package physics2d.components;

import components.Component;
import jade.GameObject;
import jade.Window;
import org.joml.Vector2f;

public class PillboxCollider extends Component {
    private transient CircleCollider topCircle = new CircleCollider();
    private transient CircleCollider bottomCircle = new CircleCollider();
    private transient Box2DCollider boxCollider = new Box2DCollider();
    private transient boolean resetFixureNextFrame = false;
    public float width = 0.1f;
    public float height = 0.2f;
    public Vector2f offset = new Vector2f();


    public CircleCollider getTopCircle() {
        return topCircle;
    }

    public CircleCollider getBottomCircle() {
        return bottomCircle;
    }

    public Box2DCollider getBoxCollider() {
        return boxCollider;
    }

    public void setWidth(float width) {
        this.width = width;
        recalculateColliders();
        resetFixture();
    }

    public void setHeight(float height) {
        this.height = height;
        recalculateColliders();
        resetFixture();
    }

    public void resetFixture() {
        if (Window.getPhysics().isLocked()) {
            resetFixureNextFrame = true;
            return;
        }
        resetFixureNextFrame = false;

        if (gameObject != null) {
            Rigidbody2D rb = gameObject.getComponent(Rigidbody2D.class);
            if (rb != null) {
                Window.getPhysics().resetPillboxCollider(rb, this);
            }
        }
    }

    @Override
    public void update (float dt) {
        if (resetFixureNextFrame) {
            resetFixture();
        }
    }

    @Override
    public void editorUpdate(float dt) {
        topCircle.editorUpdate(dt);
        bottomCircle.editorUpdate(dt);
        boxCollider.editorUpdate(dt);

        if (resetFixureNextFrame) {
            resetFixture();
        }
    }

    @Override
    public void start() {
        this.topCircle.gameObject = this.gameObject;
        this.bottomCircle.gameObject = this.gameObject;
        this.boxCollider.gameObject = this.gameObject;
        recalculateColliders();
    }

    public void recalculateColliders() {
        float circleRadius = width/ 4.0f;
        float boxHeight = height - circleRadius * 2.0f;
        topCircle.setRadius(circleRadius);
        bottomCircle.setRadius(circleRadius);
        topCircle.setOffset(new Vector2f(offset).add(0, boxHeight / 4.0f));
        bottomCircle.setOffset(new Vector2f(offset).sub(0, boxHeight / 4.0f));
        boxCollider.setHalfSize(new Vector2f(width / 2.0f, boxHeight / 2.0f));
        boxCollider.setOffset(offset);

    }
}
