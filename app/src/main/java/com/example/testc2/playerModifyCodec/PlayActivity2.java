package com.example.testc2.playerModifyCodec;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.testc2.R;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


/**
 *
 *
 * MediaCodec 高效解码得到标准 YUV420P 格式帧
 *
 *
 * https://blog.csdn.net/u010029439/article/details/91525262
 *
 *
 */
public class PlayActivity2 extends AppCompatActivity {

    private static final long DEFAULT_TIMEOUT_US = 1000 * 10;
    private static final String MIME_TYPE = "video/avc";
    private static final int VIDEO_WIDTH = 368;
    private static final int VIDEO_HEIGHT = 384;

    private MediaCodec mCodec;
    private MediaCodec.BufferInfo bufferInfo;

    public void initCodec() {
        try {
            mCodec = MediaCodec.createDecoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferInfo = new MediaCodec.BufferInfo();
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT);
        mCodec.configure(mediaFormat, null, null, 0);
        mCodec.start();
    }

    public void release() {
        if (null != mCodec) {
            mCodec.stop();
            mCodec.release();
            mCodec = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play2);
        initCodec();

        onDecodeCallback = new OnDecoderCallback() {
            @Override
            public void onFrame(byte[] yuvData) {
                Log.d("yuvData","yuvData:"+yuvData.length);
            }
        };


        decode();
    }


    public void decode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File sd = Environment.getExternalStorageDirectory();
                File folder = new File(sd, "aaa");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
//                final File f = new File(folder, "out.h264");
                final File f = new File(folder,"record1.h264");
                byte[] bytes = null;
                try {
//            偷懒   文件  加载内存
                    bytes = getBytes(f.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                decode(bytes);

            }
        }).start();
    }

    public byte[] getBytes(String path) throws IOException {
        InputStream is = new DataInputStream(new FileInputStream(new File(path)));
        int len;
        int size = 1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = is.read(buf, 0, size)) != -1)
            bos.write(buf, 0, len);
        buf = bos.toByteArray();
        return buf;
    }


    public void decode(byte[] h264Data) {
        int inputBufferIndex = mCodec.dequeueInputBuffer(DEFAULT_TIMEOUT_US);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                inputBuffer = mCodec.getInputBuffer(inputBufferIndex);
            } else {
                inputBuffer = mCodec.getInputBuffers()[inputBufferIndex];
            }
            if (inputBuffer != null) {
                inputBuffer.clear();
                inputBuffer.put(h264Data, 0, h264Data.length/10);
                mCodec.queueInputBuffer(inputBufferIndex, 0, h264Data.length/10, 0, 0);
            }
        }
        int outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, DEFAULT_TIMEOUT_US);
        ByteBuffer outputBuffer;
        while (outputBufferIndex > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                outputBuffer = mCodec.getOutputBuffer(outputBufferIndex);
            } else {
                outputBuffer = mCodec.getOutputBuffers()[outputBufferIndex];
            }
            if (outputBuffer != null) {
                outputBuffer.position(0);
                outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                byte[] yuvData = new byte[outputBuffer.remaining()];
                outputBuffer.get(yuvData);

                if (null != onDecodeCallback) {
                    onDecodeCallback.onFrame(yuvData);
                }
                mCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBuffer.clear();
            }
            outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, DEFAULT_TIMEOUT_US);
        }
    }

    OnDecoderCallback onDecodeCallback;

    public interface OnDecoderCallback {
        void onFrame(byte[] yuvData);
    }


    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }
}
