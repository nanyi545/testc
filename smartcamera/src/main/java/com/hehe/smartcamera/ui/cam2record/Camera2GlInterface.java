package com.hehe.smartcamera.ui.cam2record;

import android.graphics.SurfaceTexture;
import android.view.Surface;

public interface Camera2GlInterface {

    void init(Camera2GlRender render);
    void start(SurfaceTexture st);

}
