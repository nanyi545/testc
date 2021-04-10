package com.example.testc2.x264;

public class X264Encoder {

    native void init(int width, int height, int fps, int bitrate);
    native void encode(byte[] frame);
}
