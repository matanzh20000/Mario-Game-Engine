package jade;

import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private GameObject obj2;
    private SpriteSheet sprites;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1",
                new Transform(new Vector2f(100, 0), new Vector2f(60, 60)), -1);
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 0), new Vector2f(50, 50)), 2);
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        this.addGameObjectToScene(obj2);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"), 16, 16, 26, 0 ));
    }

    private int sprite1Index = 0;
    private int sprite2Index = 14;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void update(float dt) {
       spriteFlipTimeLeft -= 2.0f * dt;
       obj1.transform.position.x += 20.0f * dt;
       obj2.transform.position.x -= 20.0f * dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            sprite1Index++;
            sprite2Index++;
            if (sprite1Index > 3) {
                sprite1Index = 0;
            }
            if (sprite2Index > 15) {
                sprite2Index = 14;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(sprite1Index));
            obj2.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(sprite2Index));
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
