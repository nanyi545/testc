package com.hehe.smartcamera.ui.cam2record;

public class ComputeUtil {

    public static float getSad(byte[] arr1, byte[] arr2){
        long sad = 0;
        long t = 0;
        int length = arr1.length;
        for (int i=0;i<length;i++){
            int n1 = arr1[i];
            int n2 = arr2[i];
            int abs = Math.abs(n1 - n2 );
            int inc = (abs > 30)?abs:0;
            sad +=inc;
            t = t+ ((n1+n2)/2);
        }
        double d1 = t+0.01d;
        return (float) (sad/(d1));
    }

}
