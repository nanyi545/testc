package com.example.testc2.codec1;

import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

public class FormatUtil {

    public static void getOutFormat(MediaCodec mCodec) {
        MediaFormat mediaFormat = mCodec.getOutputFormat();
        Log.d("yuvData","output      KEY_COLOR_FORMAT:"+mediaFormat.getInteger(MediaFormat.KEY_COLOR_FORMAT)+" " +
                "width:" + mediaFormat.getInteger(MediaFormat.KEY_WIDTH) +
                "height:"+ mediaFormat.getInteger(MediaFormat.KEY_HEIGHT) +
                "" +
                "");

        switch (mediaFormat.getInteger(MediaFormat.KEY_COLOR_FORMAT)) {
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV411Planar:
                break;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV411PackedPlanar:
                break;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
                break;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
//                yuvData = yuv420spToYuv420P(yuvData, mediaFormat.getInteger(MediaFormat.KEY_WIDTH), mediaFormat.getInteger(MediaFormat.KEY_HEIGHT));
                break;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
                break;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            default:
                break;
        }
    }


    private static byte[] yuv420spToYuv420P(byte[] yuv420spData, int width, int height) {
        byte[] yuv420pData = new byte[width * height * 3 / 2];
        int ySize = width * height;
        System.arraycopy(yuv420spData, 0, yuv420pData, 0, ySize);   //拷贝 Y 分量

        for (int j = 0, i = 0; j < ySize / 2; j += 2, i++) {
            yuv420pData[ySize + i] = yuv420spData[ySize + j];   //U 分量
            yuv420pData[ySize * 5 / 4 + i] = yuv420spData[ySize + j + 1];   //V 分量
        }
        return yuv420pData;
    }


    /**
     *  https://www.jianshu.com/p/da10007797b1
     *  https://software.intel.com/en-us/android/articles/trusted-tools-in-the-new-android-world-optimization-techniques-from-intel-sse-intrinsics-to
     **
     * @param data
     * @param w
     * @param h
     * @return
     */


    public static Bitmap nv12ToBitmap(byte[] data, int w, int h) {
        return spToBitmap(data, w, h, 0, 1);
    }

    public static Bitmap nv21ToBitmap(byte[] data, int w, int h) {
        return spToBitmap(data, w, h, 1, 0);
    }

    private static Bitmap spToBitmap(byte[] data, int w, int h, int uOff, int vOff) {
        int plane = w * h;
        int[] colors = new int[plane];
        int yPos = 0, uvPos = plane;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                // YUV byte to RGB int
                final int y1 = data[yPos] & 0xff;
                final int u = (data[uvPos + uOff] & 0xff) - 128;
                final int v = (data[uvPos + vOff] & 0xff) - 128;
                final int y1192 = 1192 * y1;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                r = (r < 0) ? 0 : ((r > 262143) ? 262143 : r);
                g = (g < 0) ? 0 : ((g > 262143) ? 262143 : g);
                b = (b < 0) ? 0 : ((b > 262143) ? 262143 : b);
                colors[yPos] = ((r << 6) & 0xff0000) |
                        ((g >> 2) & 0xff00) |
                        ((b >> 10) & 0xff);

                if((yPos++ & 1) == 1) uvPos += 2;
            }
            if((j & 1) == 0) uvPos -= w;
        }
        return Bitmap.createBitmap(colors, w, h, Bitmap.Config.RGB_565);
    }

    public static Bitmap i420ToBitmap(byte[] data, int w, int h) {
        return pToBitmap(data, w, h, true);
    }

    public static Bitmap yv12ToBitmap(byte[] data, int w, int h) {
        return pToBitmap(data, w, h, false);
    }

    private static Bitmap pToBitmap(byte[] data, int w, int h, boolean uv) {
        int plane = w * h;
        int[] colors = new int[plane];
        int off = plane >> 2;
        int yPos = 0, uPos = plane + (uv ? 0 : off), vPos = plane + (uv ? off : 0);
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                // YUV byte to RGB int
                final int y1 = data[yPos] & 0xff;
                final int u = (data[uPos] & 0xff) - 128;
                final int v = (data[vPos] & 0xff) - 128;
                final int y1192 = 1192 * y1;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                r = (r < 0) ? 0 : ((r > 262143) ? 262143 : r);
                g = (g < 0) ? 0 : ((g > 262143) ? 262143 : g);
                b = (b < 0) ? 0 : ((b > 262143) ? 262143 : b);
                colors[yPos] = ((r << 6) & 0xff0000) |
                        ((g >> 2) & 0xff00) |
                        ((b >> 10) & 0xff);

                if((yPos++ & 1) == 1) {
                    uPos++;
                    vPos++;
                }
            }
            if((j & 1) == 0) {
                uPos -= (w >> 1);
                vPos -= (w >> 1);
            }
        }
        return Bitmap.createBitmap(colors, w, h, Bitmap.Config.RGB_565);
    }



}
