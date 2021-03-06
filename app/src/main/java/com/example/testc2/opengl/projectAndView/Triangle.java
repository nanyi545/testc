package com.example.testc2.opengl.projectAndView;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    /**
     * vertex shader with projection/view
     */
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    /**
     * vertex shader without projection/view
     */
//    private final String vertexShaderCode =
//            "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    "  gl_Position = vPosition;" +
//                    "}";



    // Use to access and set the view transformation
    private int vPMatrixHandle;

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    static float triangleCoords[] = {   // in counterclockwise order  ????
            0.5f,  0.5f, 0.0f, // top   ,  first triangle
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f,  // bottom right
    };
//    static float triangleCoords[] = {   // in counterclockwise order:
//            0.0f,  0.622008459f, 0.0f, // top
//            -0.5f, -0.311004243f, 0.0f, // bottom left
//            0.5f, -0.311004243f, 0.0f  // bottom right
//    };


    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.5f };





    private final int mProgram;




    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);


        /**
         *
         * In order to draw your shape, you must compile the shader code, add them to a OpenGL ES program object and then link the program.
         *
         * Do this in your drawn object’s constructor, so it is only done once.
         */


        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

    }


    /**
     *
     * flow
     *  ----------init------------
     *  0
     *      // number of coordinates per vertex in this array
     *     static final int COORDS_PER_VERTEX = 3;
     *
     *      // vertex count
     *      private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
     *
     *      // 4 bytes per vertex  ---> total bytes per vertex
     *      private final int vertexStride = COORDS_PER_VERTEX * 4;
     *
     *
     *
     *   1 load shader
     *   2 create program:    int mProgram = GLES20.glCreateProgram();
     *   3 attach shader:     GLES20.glAttachShader(mProgram, vertexShader);
     *                        GLES20.glAttachShader(mProgram, fragmentShader);
     *
     *   4 creates OpenGL ES program executables:
     *
     *                        GLES20.glLinkProgram(mProgram);
     *  ----------end of init------------
     *
     *  ----------draw---------------
     *   4.1  viewport
     *                  GLES20.glViewport(0, 0, mWidth, mHeight);
     *
     *   5  Add program to OpenGL ES environment :
     *
     *                  GLES20.glUseProgram(mProgram);
     *
     *   6  pass in params of shader programs:
     *
     *                  6.1  attribute :
     *
     *                          // get handle to vertex shader's vPosition member
     *                             int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
     *
     *                          // Enable a handle to the triangle vertices
     *                             GLES20.glEnableVertexAttribArray(positionHandle);
     *
     *                          // Prepare the triangle coordinate data
     *                             GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
     *
     *                  6.2   uniform :
     *                          // get handle to uniform member
     *                             int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     *
     *                          // Set color for drawing the triangle
     *                             GLES20.glUniform4fv(colorHandle, 1, color, 0);
     *
     *
     *                        uniform matrix:
     *                          // get handle to shape's transformation matrix
     *                             int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
     *
     *                          // Pass the projection and view transformation to the shader
     *                             GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
     *
     *
     *
     *     7   final step
     *
     *         // Draw the triangle
     *              GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
     *
     *                  GL_TRIANGLES、GL_TRIANGLE_STRIP、GL_TRIANGLE_FAN ----->
     *                  https://blog.csdn.net/u013749540/article/details/91826613
     *
     *
     *
     *         // Disable vertex array
     *              GLES20.glDisableVertexAttribArray(positionHandle);
     *
     *
     *
     *  ----------end of draw---------
     *
     *  ****** 生成一个采样  ：
     *     FragmentShader:    uniform samplerExternalOES vTexture;
     *     java:              GLES20.glUniform1i(vTexture, 0);
     *
     *         GLES20.glActiveTexture(GL_TEXTURE0);
     *         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
     *         GLES20.glUniform1i(vTexture, 0);
     *
     */




    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex



    // pass in the calculated transformation matrix
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);




        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);


        if(vertexShaderCode.contains("uMVPMatrix")){

            // get handle to shape's transformation matrix
            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        }

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);



    }


}