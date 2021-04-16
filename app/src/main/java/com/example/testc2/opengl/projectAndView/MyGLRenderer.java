package com.example.testc2.opengl.projectAndView;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 *
 * https://sites.google.com/site/pavelkolodin/js-javascript/webgl
 *
 *
 attribute:VERTEX	   Global variables.May change per vertex. Passed from app to vertex shader.
 uniform:ALL     Global variables. May change per primitive (pixel?). Passed from app to shaders.
 varying:ALL     Interpolating data between VERTEX and FRAGMENT shaders. Write in vertex. RO in fragment shader.
 const:ALL	   	 Compile-time constant.




 *
 *
 *
 *
 *
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }



    private Triangle mTriangle;



    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // initialize a triangle
        mTriangle = new Triangle();


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        /**
         *
         *  define projection
         *
         *  This transformation adjusts the coordinates of drawn objects based on the width and height of the GLSurfaceView where they are displayed.
         *  Without this calculation, objects drawn by OpenGL ES are skewed by the unequal proportions of the view window.
         *
         **/
         GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }



    public void setEye(float eyeX,float eyeY,float eyeZ) {
        synchronized (lock){
            this.eyeX = eyeX;
            this.eyeY = eyeY;
            this.eyeZ = eyeZ;
        }

    }
    Object lock = new Object();

    float eyeX = 0;
    float eyeY = 0;
    float eyeZ = 3;



    @Override
    public void onDrawFrame(GL10 gl) {

        // clear previous frame ...
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        //
        //  eye view direction !!!!
        //
        // different eye direction ---> gives different result !!!

        synchronized (lock){
            Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        }

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Draw shape
        mTriangle.draw(vPMatrix);

    }



}