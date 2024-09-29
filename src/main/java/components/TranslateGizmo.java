package components;

import editor.PropertiesWindow;
import jade.GameObject;
import jade.Prefabs;
import jade.Transform;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslateGizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisColorHover = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private Vector4f yAxisColorHover = new Vector4f(1, 0, 0, 1);

    private GameObject xAxisObject, yAxisObject;
    private SpriteRenderer xAxisSprite, yAxisSprite;
    private GameObject activeGameObject = null;
    private PropertiesWindow propertiesWindow;

    private Vector2f xAxisOffset = new Vector2f(50, -5);
    private Vector2f yAxisOffset = new Vector2f(10, 50);

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.setNotSerialize();
        this.yAxisObject.setNotSerialize();
    }


    @Override
    public void update(float dt) {
        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(xAxisOffset);
            this.yAxisObject.transform.position.add(yAxisOffset);
        }

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
        }
    }

    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }

}