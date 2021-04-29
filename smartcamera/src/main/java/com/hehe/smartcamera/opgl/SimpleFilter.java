package com.hehe.smartcamera.opgl;

import android.content.Context;
import android.opengl.GLES20;
import com.hehe.smartcamera.R;

public class SimpleFilter extends AbstractFilter {



    public SimpleFilter(Context context) {
        super(context, R.raw.camera_vert, R.raw.camera_frag);
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
    }

}
