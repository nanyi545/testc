package com.example.testc2.opengl.camera_gl;

import android.content.Context;
import android.opengl.GLES20;

import com.example.testc2.R;

public class CameraFilter extends AbstractFboFilter {

    private float[] mtx;
    private int vMatrix;

    /**
     * AbstractFboFilter  --> does not display in surface but , only change layer in FBO ...
     * @param context
     */
    public CameraFilter(Context context) {
        super(context, R.raw.camera_vert, R.raw.camera_frag);
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
    }

    @Override
    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }


}
