package com.hehe.smartcamera.player;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.hehe.smartcamera.util.Util;

import java.io.IOException;
import java.nio.ByteBuffer;

public class NativeCodecPlayer implements VideoPlayer {

    private String TAG = "NativeCodecPlayer";

    MediaCodec mediaCodec;
    MediaFormat f;
    int videoWidth;
    int videoHeight;
    boolean isEOS = false;


    public String getDecoderType() {
        return h265()?MediaFormat.MIMETYPE_VIDEO_HEVC:MediaFormat.MIMETYPE_VIDEO_AVC;
    }
    public boolean h265(){
        return true;
    }

    private boolean asyncMode = true;

    @Override
    public void startPlay(String path, Surface s) {
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; ++i) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);

            Log.d("player","mime:"+mime+"  format "+(format==null) );

            int width = 0;
            int height = 0 ;
            int rate = 0;

            try {
                width = format.getInteger(MediaFormat.KEY_WIDTH);
                height = format.getInteger(MediaFormat.KEY_HEIGHT);
                rate = format.getInteger(MediaFormat.KEY_FRAME_RATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("player","width :"+width+"   height:"+height +"  rate:"+rate);

            if (mime.equalsIgnoreCase(getDecoderType())) {
                f = format;
                extractor.selectTrack(i);
                videoWidth = format.getInteger(MediaFormat.KEY_WIDTH);
                videoHeight = format.getInteger(MediaFormat.KEY_HEIGHT);
            }
        }


        try {
            mediaCodec = MediaCodec.createDecoderByType(getDecoderType());
        } catch (IOException e) {
            e.printStackTrace();
        }



        // async mode
        if(asyncMode) {
            mediaCodec.setCallback(new MediaCodec.Callback() {
                @Override
                public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {

                    Log.d(TAG,"--------- onInputBufferAvailable:"+index);

                    //解码器异步方法返回InputBuffer的序列值，用以填充视频数据,并且返回我们初始化的解码器。
                    ByteBuffer inputBuffer = null;
                    try {
                        inputBuffer = codec.getInputBuffer(index);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (inputBuffer != null) {
                        int sampleSize = extractor.readSampleData(inputBuffer, 0);//获取视频帧的数据大小
                        long presentationTimeUs = extractor.getSampleTime();//获取视频帧的时间戳
                        int flag = extractor.getSampleFlags();//获取视频帧的标志位，例如是否是关键帧
                        try {
                            codec.queueInputBuffer(index, 0, sampleSize, presentationTimeUs, flag);//将视频帧的相关信息送入解码器的InputBuffer队列
                            extractor.advance();
                        } catch (Exception ex) {
                        }
                    }

                }

                @Override
                public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {

                    Log.d(TAG,"onOutputBufferAvailable:"+index);

                    //解码器解码视//频帧完成回调在这里可以直接通过 codec.releaseOutputBuffer(index,true)把解码的内容显示出来，但是这样做我们达
                    //不到控制视频播放速度的目的,所以我们把解码器解码后的OutputBuffer队列存起来，用一个线程来执行codec.releaseOutputBuffer(index,true)显示;

//                mOutputtIndexQueue.add(index);//把解码器解码后的OutputBufferAvailable的序列用队列存起来
                    codec.releaseOutputBuffer(index, true);

                }

                @Override
                public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {

                }

                @Override
                public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {

                }
            });
        }

        mediaCodec.configure(f, s, null, 0);


        // sync mode ....
        /**
         *
         *    in sync mode :   decode is slower than play time .....     this can only be used to develop/debug
         *
         *
         */
        if(asyncMode){
            mediaCodec.start();
            return;
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                long startMs = System.currentTimeMillis();
                MediaCodec.BufferInfo outBufferInfo = new MediaCodec.BufferInfo();
                mediaCodec.start();
                while(true){
                    ByteBuffer[] inBuffers =  mediaCodec.getInputBuffers();
                    while(!isEOS){
                        int inIndex = mediaCodec.dequeueInputBuffer(100000);
                        if(inIndex >= 0){
                            int size = extractor.readSampleData(inBuffers[inIndex], 0);//demux get video es
                            if(size < 0){
                                Log.d("player", "mybe eos or error");
                                mediaCodec.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            }else{
                                mediaCodec.queueInputBuffer(inIndex, 0, size, extractor.getSampleTime(), 0);
                                extractor.advance();
                                inBuffers[inIndex].clear();
                            }
                        }
                        int outI = -1;
                        do{
                            int outIndex = mediaCodec.dequeueOutputBuffer(outBufferInfo, 100000);
                            outI = outIndex;
                            if((outBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0){
                                Log.d("player", "outBufferInfo flag is BUFFER_FLAG_END_OF_STREAM");
                                isEOS = true;
                            }

                            if(outIndex >= 0){
                                long realMs = System.currentTimeMillis() - startMs;
                                Log.d("player", "presentation time:"+ Util.getTime(outBufferInfo.presentationTimeUs) + " actual time:"+(realMs/1000f) );
                                // We use a very simple clock to keep the video FPS, or the video playback will be too fast
                                /**
                                 * this is not needed --> decode (at least for H265) is actually slower than playback
                                 */

//                                while (outBufferInfo.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
//                                    try {
//                                        Thread.sleep(100);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                        break;
//                                    }
//                                }
                                mediaCodec.releaseOutputBuffer(outIndex, true);  //true means output to surface
                            }
                        }while(outI >= 0);
                    }
                }
            }
        });

        t.setName("xxxx1");

        t.start();

    }






}
