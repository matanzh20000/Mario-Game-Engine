package Jade;

import components.SpriteRender;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private String vertexShaderSource = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = uProjection * uView * vec4(aPos, 1.0);" +
            "}";

    private String fragmentShaderSource = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main() {\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            // positions          // colors
            100.5f, 0.5f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f,    1, 1, // bottom right
            0.5f, 100.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,    0, 0, // top left
            100.5f, 100.5f, 0.0f,  0.0f, 0.0f, 1.0f, 1.0f,    1, 0, // top right
            0.5f, 0.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f,    0, 1, // bottom left
    };

    // MUST BE IN COUNTER CLOCKWISE ORDER
    private int[] elementArray = {
            2, 1, 0, // top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    GameObject testObj;
    private boolean firstTime = false;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        System.out.println("Creating : Test Object");
        this.testObj = new GameObject("Test Object");
        this.testObj.addComponent(new SpriteRender());
        this.addGameObjectToScene(this.testObj);
        this.camera = new Camera(new Vector2f(0, 0));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/mario_pixel.png");

        // link the vertex and fragment shader into a shader program

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // create a FloatBuffer with the vertex data
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // create the VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // create indices buffer
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        // create the EBO and upload the indices buffer
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSizeBytes = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSizeBytes) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSizeBytes, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        float dx = 100.0f * dt;
        float dy = 100.0f * dt;
        camera.position.x -= dx;
        camera.position.y -= dy;

        defaultShader.use();

        // upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // bind the VAO
        glBindVertexArray(vaoID);

        // enable the vertex array
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // draw the vertices
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();

        if (!firstTime) {
            System.out.println("Creating : Test Object 2");
            GameObject go = new GameObject("Test Object 2");
            go.addComponent(new SpriteRender());
            this.addGameObjectToScene(go);
            firstTime = true;
        }


        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
}
