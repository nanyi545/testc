package com.example.testc2.encoder1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import com.example.testc2.R;
import com.example.testc2.codec2.Utils;
import com.example.testc2.util.TestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * h264 格式
 * https://zhuanlan.zhihu.com/p/71928833
 *
 *
 *
 *
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class EncoderActivity1 extends AppCompatActivity {
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaCodec mediaCodec;

    private String TAG = "xxxx_recorder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoder1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this,MyService.class));
            //  Context.startForegroundService() did not then call Service.startForeground() ???
        }

        checkPermission();

        this.mediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, 100);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            mediaProjection = mediaProjectionManager.getMediaProjection
                    (resultCode, data);
//
            initMediaCodec();

        }
    }

    @Override
    protected void onDestroy() {
        mediaCodec.stop();
        super.onDestroy();
    }

    String type = MediaFormat.MIMETYPE_VIDEO_AVC;


    private String getFileName(){
        String str = h265()?"record4.h265":"record4.h264";
        return str;
    }
    private void setDecodeType(){
        type = h265()?MediaFormat.MIMETYPE_VIDEO_HEVC:MediaFormat.MIMETYPE_VIDEO_AVC;
    }
    private boolean h265(){
        return false;
    }

    private void initMediaCodec() {
        final Size screen = TestUtil.getScreen(EncoderActivity1.this);
        Toast.makeText(this,"width:"+screen.getWidth()+"  height:"+screen.getHeight(),Toast.LENGTH_SHORT).show();


        final int width = screen.getWidth();
        final int height= screen.getHeight();
        try {
            setDecodeType();
            mediaCodec = MediaCodec.createEncoderByType(type);

            MediaFormat format= MediaFormat.createVideoFormat(type,width, height);

//            MediaFormat format= MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,
//                    368, 384);


            format.setInteger(MediaFormat.KEY_COLOR_FORMAT,MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            format.setInteger(MediaFormat.KEY_BIT_RATE, width * height);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);//2s一个I帧

            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            final Surface surface = mediaCodec.createInputSurface();


            new Thread(){
                @Override
                public void run() {
                    mediaCodec.start();
//这是MediaCodec 提供的surface

//提供的surface  与MediaProjection关联
                    mediaProjection.createVirtualDisplay("screen-codec",
                            width, height, 1,
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                            surface, null, null);


                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    while ( (!end) ) {
//                        源源不断的插叙编码好的数据
                        int index = mediaCodec.dequeueOutputBuffer(bufferInfo, 100000);

                        if (index >= 0) {

                            ByteBuffer buffer = mediaCodec.getOutputBuffer(index);
//                                byteBuffer  压缩数据

                            // 初始muxer
                            if(!muxerInited){
                                initMuxer(mediaCodec.getOutputFormat());
                            }


                            // 测试h264/265数据
//                            byte[] outData = new byte[bufferInfo.size];
//                            buffer.get(outData);
//                            writeContent(outData);  //以字符串的方式写入
//                            writeBytes(outData); //


                            // 写入mp4
                            muxer.writeSampleData(videoTrackIndex,buffer,bufferInfo);


                            mediaCodec.releaseOutputBuffer(index, false);

                            frameCount++;
                            Log.d(TAG,"frameCount:"+frameCount );
                        }
                        if(frameCount>100){
                            endRecord();
                        }

                    }


                }
            }.start();

        } catch ( Exception e) {
            e.printStackTrace();
            Log.i(TAG, "initMediaCodec fail: "+Log.getStackTraceString(e));
        }

    }


    int frameCount = 0;
    private boolean end = false;
    private void endRecord(){
        if(!end){

            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec=null;


            muxer.stop();
            muxer.release();
            muxer=null;

            end = true;
        }
    }


    MediaMuxer muxer;
    boolean muxerInited = false;
    int videoTrackIndex;

    private void initMuxer(MediaFormat format){
        try {
            if(muxer==null){
                muxerInited = true;
                String mp4FileName = Environment.getExternalStorageDirectory() + "/aaa/c1.mp4";
                muxer = new MediaMuxer(mp4FileName ,  MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                // More often, the MediaFormat will be retrieved from MediaCodec.getOutputFormat()
                // or MediaExtractor.getTrackFormat().

//        MediaFormat audioFormat = new MediaFormat(...);
//        int audioTrackIndex = muxer.addTrack(audioFormat);
                videoTrackIndex = muxer.addTrack(format);
                muxer.start();
                Log.d(TAG,"mux started:" );
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"mux fail:"+e.toString());
        }

    }




    public void writeBytes(byte[] array) {
        FileOutputStream writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            File sd = Environment.getExternalStorageDirectory();
            File folder = new File(sd,"aaa");
            if(!folder.exists()){
                folder.mkdirs();
            }
            File f = new File(folder,getFileName());
            writer = new FileOutputStream(f.getAbsolutePath(), true);
            writer.write(array);
            writer.write('\n');


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }


    public String writeContent(byte[] array) {
        char[] HEX_CHAR_TABLE = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(HEX_CHAR_TABLE[(b & 0xf0) >> 4]);
            sb.append(HEX_CHAR_TABLE[b & 0x0f]);
        }
        Log.i(TAG, "writeContent: "+sb.toString());
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(Environment.getExternalStorageDirectory()+"/aaa/codec3.txt", true);
            writer.write(sb.toString());
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
