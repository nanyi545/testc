package com.example.testc2.opengl.player_gl;

import android.content.Context;
import android.graphics.SurfaceTexture;

import com.example.testc2.R;

public class PlayerFilter extends AbstractFboPlayerFilter {


    public PlayerFilter(Context context, SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener) {
        super(context, R.raw.base_vert, R.raw.base_frag, onFrameAvailableListener);
    }


    @Override
    public Object setSize(int width, int height) {
        return super.setSize(width, height);
    }
}
