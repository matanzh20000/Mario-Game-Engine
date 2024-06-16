package Jade;

import org.lwjgl.BufferUtils;

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
            "    gl_Position = vec4(aPos, 1.0);\n" +
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
            0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // bottom right
            -0.5f, 0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // top left
            0.5f, 0.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,  // top right
            -0.5f, -0.5f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f // bottom left
    };

    // MUST BE IN COUNTER CLOCKWISE ORDER
    private int[] elementArray = {
            2 , 1, 0, // top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;


    public LevelEditorScene() {

    }

    @Override
    public void init() {
        // first load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // pass the shader source to the GPU
        glShaderSource(vertexID, vertexShaderSource);
        glCompileShader(vertexID);

        //check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl' compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // first load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentShaderSource);
        glCompileShader(fragmentID);

        //check for errors in compilation
        int success2 = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success2 == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl' compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // link the vertex and fragment shader into a shader program
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // check for linking errors
        int success3 = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success3 == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }
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
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt) {
        // bind the shader program
        glUseProgram(shaderProgram);
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
        glUseProgram(0);
    }
}
