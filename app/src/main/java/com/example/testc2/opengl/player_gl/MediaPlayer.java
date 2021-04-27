package com.example.testc2.opengl.player_gl;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaPlayer {

    MediaCodec mediaCodec;
    MediaFormat f;
    int videoWidth;
    int videoHeight;
    boolean isEOS = false;

    public void play(String path, Surface s){
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

            try {
                width = format.getInteger(MediaFormat.KEY_WIDTH);
                height = format.getInteger(MediaFormat.KEY_HEIGHT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("player","width :"+width+"   height:"+height );

            if (mime.equalsIgnoreCase("video/avc")) {
                f = format;
                extractor.selectTrack(i);
                videoWidth = format.getInteger(MediaFormat.KEY_WIDTH);
                videoHeight = format.getInteger(MediaFormat.KEY_HEIGHT);
            }
        }

        try {
            mediaCodec = MediaCodec.createDecoderByType("video/avc");
            mediaCodec.configure(f, s, null, 0);



        } catch (IOException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
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
                                // We use a very simple clock to keep the video FPS, or the video
                                // playback will be too fast
                                while (outBufferInfo.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        break;
                                    }
                                }
                                mediaCodec.releaseOutputBuffer(outIndex, true);  //true means output to surface
                            }
                        }while(outI >= 0);

                    }




                }
            }
        }).start();

    }



}
