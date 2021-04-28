package com.hehe.smartcamera.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import com.hehe.smartcamera.camera2.Camera2Helper;
import com.hehe.smartcamera.camera2.ImageUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class MediaEncoder {

    static class State {
        static final int STATE_READY  = 1;
        static final int STATE_RECORDING  = 2;
        static final int STATE_STOP = 3;

        int state = STATE_READY;

        synchronized void resetState(int newState){
            state = newState;
        }

        synchronized boolean isState(int target){
            return state == target;
        }

    }


    State state;


    public void stop(){
        state.resetState(State.STATE_STOP);
    }

    public MediaEncoder() {
        state = new State();
    }

    private MediaCodec mediaCodec;
    int videoTrackIndex;

    private void initCodec(Size size) {
        setDecodeType();
        try {
            mediaCodec = MediaCodec.createEncoderByType(getDecoderType());
            MediaFormat format = MediaFormat.createVideoFormat(getDecoderType(), size.getHeight(), size.getWidth());
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 20);//
            format.setInteger(MediaFormat.KEY_BIT_RATE, size.getWidth() * size.getHeight() * 2 );
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String type;
    public String getDecoderType() {
        return type;
    }
    public void setDecodeType(){
        type = h265()?MediaFormat.MIMETYPE_VIDEO_HEVC:MediaFormat.MIMETYPE_VIDEO_AVC;
    }
    public boolean h265(){
        return true;
    }
    public String getFileName(){
        String str = h265()?"record4.h265":"record4.h264";
        return str;
    }


    //   先转成nv21   再转成  yuv420    n21 横着   1   竖着
    private byte[] nv21;//width  height
    byte[] nv21_rotated;
    byte[] nv12;


    public Camera2Helper.Camera2Listener getCamera2Listener() {
        return camera2Listener;
    }

    Camera2Helper.Camera2Listener camera2Listener = new Camera2Helper.Camera2Listener() {
        @Override
        public void onPreview(byte[] y, byte[] u, byte[] v, Size previewSize, int stride, long presentationTimeUs) {

            if(Looper.getMainLooper()==Looper.myLooper()){
                throw new RuntimeException("encoder should not work on main thread !");
            }

            if(state.isState(State.STATE_READY)){
                initCodec(previewSize);
                state.resetState(State.STATE_RECORDING);
            }
            if(state.isState(State.STATE_STOP)){
                if(muxer!=null){

                    mediaCodec.stop();
                    mediaCodec.release();
                    mediaCodec=null;


                    muxer.stop();
                    muxer.release();
                    muxer=null;
                }
                return;
            }



            if (nv21 == null) {
//            实例化了一次
                nv21 = new byte[stride * previewSize.getHeight() * 3 / 2];
                nv21_rotated = new byte[stride * previewSize.getHeight() * 3 / 2];
            }


            ImageUtil.yuvToNv21(y, u, v, nv21, stride, previewSize.getHeight());
            ImageUtil.nv21_rotate_to_90(nv21, nv21_rotated, stride, previewSize.getHeight());
            byte[] temp = ImageUtil.nv21toNV12(nv21_rotated, nv12);

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            Log.d("cammm"," *************  dequeueInputBuffer:");
            int inIndex = mediaCodec.dequeueInputBuffer(100000);
            Log.d("cammm"," *************  inIndex:"+inIndex);
            if (inIndex >= 0) {
                ByteBuffer byteBuffer = mediaCodec.getInputBuffer(inIndex);
                byteBuffer.clear();
                byteBuffer.put(temp, 0, temp.length);
                mediaCodec.queueInputBuffer(inIndex, 0, temp.length, presentationTimeUs, 0);
            }

            Log.d("cammm","OMX   *************  dequeueOutputBuffer:");
            int outIndex = mediaCodec.dequeueOutputBuffer(info, 100000);
            Log.d("cammm","OMX   *************  outIndex:"+outIndex);
            if (outIndex >= 0) {
                ByteBuffer byteBuffer = mediaCodec.getOutputBuffer(outIndex);
                int bufferSize = byteBuffer.remaining();

                if(!muxerInited){
                    initMuxer(mediaCodec.getOutputFormat());
                }

                // write h264 stream ....
//                byte[] ba = new byte[bufferSize];
//                byteBuffer.get(ba);
//                writeBytes(ba);

                // write to mp4 ...
//                MediaCodec.BufferInfo metaInfo = new MediaCodec.BufferInfo();
//                // Associate this metadata with the video frame by setting
//                // the same timestamp as the video frame.
//                metaInfo.presentationTimeUs = presentationTimeUs;
//                metaInfo.offset = 0;
//                metaInfo.flags = 0;
//                metaInfo.size = bufferSize;



                muxer.writeSampleData(videoTrackIndex,byteBuffer,info);


                mediaCodec.releaseOutputBuffer(outIndex, false);
            }

            Log.d("ccc","muxer:"+getState(muxer) );


        }
    };


    private int getState(MediaMuxer muxer){
        Field f = null;
        try {
            f = muxer.getClass().getDeclaredField("mState");
            f.setAccessible(true);
            int state = (int) f.get(muxer); //IllegalAccessException
            return state;
        } catch (Exception e) {
            Log.d("mux","e:"+Log.getStackTraceString(e));
            e.printStackTrace();
            return -999;
        }
    }


    MediaMuxer muxer;
    boolean muxerInited = false;

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
                Log.d("ccc","mux started:" );
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ccc","mux fail:"+e.toString());
        }

    }




    private void writeBytes(byte[] array) {
        FileOutputStream writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileOutputStream(Environment.getExternalStorageDirectory() + "/aaa/"+getFileName(), true);
            writer.write(array);
            writer.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
