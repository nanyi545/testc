package com.hehe.smartcamera.opgl;

import android.content.Context;
import android.opengl.GLES20;

import com.hehe.smartcamera.R;


public class CameraFilter extends AbstractFboFilter {

    public CameraFilter(Context context) {
//        super(context, R.raw.camera_vert, R.raw.camera_frag1);
        super(context, R.raw.camera_vert, R.raw.camera_frag);
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
    }


    @Override
    public int onDraw(int texture) {
        super.onDraw(texture);
        return frameTextures[0];
    }

}
