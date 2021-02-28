package com.example.testc2.codec2;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * https://www.cardinalpeak.com/the-h-264-sequence-parameter-set
 * H264 format
 *
 *
 *
 * https://zhuanlan.zhihu.com/p/27896239
 *
 */
public class SpsMediaCodec {

    static final String TAG = "SPS";
    private  String path;

    public SpsMediaCodec(String path) {
        this.path = path;
    }



    /**
     *
     *
     * example 1 :
     * 00 00 00 01 67 42 C0 29 8D 68 08 81 E7 BE 01 E1 10 8D 40
     *            |67-->  01100111 --->  0(0) 11(3)  00111(7)    forbidden_zero_bit + nal_ref_idc + nal_unit_type
     *               |42-->  01000010 --->  66   profile_idc
     *                  |C0-->  11000000 --->  1(1)1(1)0(0)0(0)0000(0)
     *                     |29-->  00101001 --->  41   level_idc
     *                        |8D-->  10001101   ----> 1(0)0001101(12)    seq_parameter_set_id / log2_max_frame_num_minus4
     *                            |68-->  01101000  ---> 011(2) 010(1)  pic_order_cnt_type
     *
     *
     */


    public void startCodec() {
        byte[] bytes = null;
        try {
            bytes = getBytes(path);
            Log.d(TAG, "bytes:"+bytes.length);
        } catch ( Exception e) {
            Log.d(TAG, "bytes FAIL:"+Log.getStackTraceString(e));
            e.printStackTrace();
        }
        int totalSize = bytes.length;
        int startIndex = 0;
        while (true) {

            if (totalSize == 0 ||startIndex >= totalSize) {
                break;
            }
            int nextFrameStart = findByFrame(  bytes, startIndex+2 , totalSize);
            byte[] h264 = splitByte(bytes, startIndex, nextFrameStart - startIndex);

            Log.d(TAG, "startIndex:"+startIndex+"   nextFrameStart:"+nextFrameStart+"   f:"+Utils.print(h264));

            //   00000001  --> 4*8=32 bit
            nStartBit = 4*8;
            int forbidden_zero_bit=u(1, h264);
            int nal_ref_idc       =u(2, h264);
            int nal_unit_type     =u(5, h264);

            Log.d(TAG, "forbidden_zero_bit:"+forbidden_zero_bit+"   nal_ref_idc:"+nal_ref_idc+"   nal_unit_type:"+nal_unit_type  );

            switch (nal_unit_type) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
//                    进入sps解码过程
                    parseSps(h264);

                    break;
                case 8:
                    break;
            }

            // show more frames...
//            startIndex = nextFrameStart;

            //  show only SPS
            break;

        }


    }


    private void parseSps(byte[] h264) {

//            编码等级   Baseline Main Extended High   High 10   High 4:2:2
        /**
         * (1) profile_idc：
         *
         * 标识当前H.264码流的profile。我们知道，H.264中定义了三种常用的档次profile：
         *
         * 基准档次：baseline profile;
         * 主要档次：main profile;
         * 扩展档次：extended profile;
         *
         * 在H.264的SPS中，第一个字节表示profile_idc，根据profile_idc的值可以确定码流符合哪一种档次。判断规律为：
         *
         * profile_idc = 66 → baseline profile;
         * profile_idc = 77 → main profile;
         * profile_idc = 88 → extended profile;
         *
         * 在新版的标准中，还包括了High、High 10、High 4:2:2、High 4:4:4、High 10 Intra、High
         * 4:2:2 Intra、High 4:4:4 Intra、CAVLC 4:4:4 Intra等，每一种都由不同的profile_idc表示。
         */
        int profile_idc = u(8, h264);





//            当constrained_set0_flag值为1的时候，就说明码流应该遵循基线profile(Baseline profile)的所有约束.constrained_set0_flag值为0时，说明码流不一定要遵循基线profile的所有约束。
        int constraint_set0_flag = u(1, h264);//(h264[1] & 0x80)>>7;
        //            当constrained_set1_flag值为1的时候，就说明码流应该遵循主profile(Main profile)的所有约束.constrained_set1_flag值为0时，说明码流不一定要遵
        int constraint_set1_flag = u(1, h264);//(h264[1] & 0x40)>>6;
        //当constrained_set2_flag值为1的时候，就说明码流应该遵循扩展profile(Extended profile)的所有约束.constrained_set2_flag值为0时，说明码流不一定要遵循扩展profile的所有约束。
        int constraint_set2_flag = u(1, h264);//(h264[1] & 0x20)>>5;
//            注意：当constraint_set0_flag,constraint_set1_flag或constraint_set2_flag中不只一个值为1的话，那么码流必须满足所有相应指明的profile约束。
        int constraint_set3_flag = u(1, h264);//(h264[1] & 0x10)>>4;
//            4个零位
        int reserved_zero_4bits = u(4, h264);

        Log.d(TAG, "profile_idc:"+profile_idc+"   constraint_set0_flag:"+constraint_set0_flag+"   constraint_set1_flag:"+constraint_set1_flag +"  reserved_zero_4bits:"+reserved_zero_4bits );


//            它指的是码流对应的level级
        /**d
         * (2) level_ic
         * 标识当前码流的Level。编码的Level定义了某种条件下的最大视频分辨率、最大视频帧率等参数，码流所遵从的level由level_idc指定。
         */
        int level_idc = u(8, h264);
        Log.d(TAG, "level_idc:"+level_idc);


//            是否是哥伦布编码  0 是 1 不是
        int seq_parameter_set_id = Ue(h264);
        Log.d(TAG, "seq_parameter_set_id:"+seq_parameter_set_id);


        if (profile_idc == 100) {
//                颜色位数
            int chroma_format_idc=Ue(h264);
            int bit_depth_luma_minus8   =Ue(h264);
            int bit_depth_chroma_minus8  =Ue(h264);
            int qpprime_y_zero_transform_bypass_flag=u(1, h264);
            int seq_scaling_matrix_present_flag     =u(1, h264);


        }
        int log2_max_frame_num_minus4=Ue(h264);
        Log.d(TAG, "log2_max_frame_num_minus4:"+log2_max_frame_num_minus4);


        int pic_order_cnt_type       =Ue(h264);
        Log.d(TAG, "pic_order_cnt_type:"+pic_order_cnt_type);


        /**
         * pic_order_cnt_type
         *
         * 表示解码picture order count(POC)的方法。POC是另一种计量图像序号的方式，与frame_num有着不同的计算方法。该语法元素的取值为0、1或2。
         */

        if(pic_order_cnt_type == 0){
            int log2_max_pic_order_cnt_lsb_minus4=Ue(h264);
            Log.d(TAG, "log2_max_pic_order_cnt_lsb_minus4:"+log2_max_pic_order_cnt_lsb_minus4);
        } else if (pic_order_cnt_type == 1){
            //......

        }

        int num_ref_frames                      =Ue(h264);
        Log.d(TAG, "num_ref_frames:"+num_ref_frames);

        int gaps_in_frame_num_value_allowed_flag=u(1,     h264);


        int pic_width_in_mbs_minus1             =Ue(h264);
        int pic_height_in_map_units_minus1      =Ue(h264);
        int width=(pic_width_in_mbs_minus1       +1)*16;
        int height=(pic_height_in_map_units_minus1+1)*16;
        Log.i(TAG, "width: "+width+"  height: "+height);
    }

    public byte[] splitByte(byte[] array, int start, int lenght) {
        byte[] newArray = new byte[lenght];
        for (int i = start; i < start + lenght; i++) {
            newArray[i - start] = array[i];
        }
        return newArray;
    }


    private int findByFrame( byte[] bytes, int start, int totalSize) {
        int j = 0;  // Number of chars matched in pattern
        for (int i = start; i < totalSize; i++) {
            if ((bytes[i] == 0x00 && bytes[i + 1] == 0x00 && bytes[i + 2] == 0x00
                    && bytes[i + 3] == 0x01)||(bytes[i] == 0x00 && bytes[i + 1] == 0x00
                    && bytes[i + 2] == 0x01)) {
                return i;
            }
        }
        return -1;  // Not found
    }
    public   byte[] getBytes(String path) throws IOException {
        InputStream is =   new DataInputStream(new FileInputStream(new File(path)));
        int len;
        int size = 1024*1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        len=is.read(buf, 0, size);
         bos.write(buf, 0, len);
        buf = bos.toByteArray();
        return buf;
    }


    /**
     * 取bitIndex个bit位
     *
     * @param bitIndex
     * @param h264
     * @return
     */
    private static int u(int bitIndex, byte[] h264)
    {
        int dwRet = 0;
        for (int i=0; i<bitIndex; i++)
        {
            dwRet <<= 1;
            if ((h264[nStartBit / 8] & (0x80 >> (nStartBit % 8))) != 0)
            {
                dwRet += 1;
            }
            nStartBit++;
        }
        return dwRet;
    }

    private static int nStartBit = 0;


    private static int Ue(byte[] pBuff)
    {
        int nZeroNum = 0;
        while (nStartBit < pBuff.length * 8)
        {
            if ((pBuff[nStartBit / 8] & (0x80 >> (nStartBit % 8))) != 0)
            {
                break;
            }
            nZeroNum++;
            nStartBit++;
        }
        nStartBit ++;

        int dwRet = 0;
        for (int i=0; i<nZeroNum; i++)
        {
            dwRet <<= 1;
            if ((pBuff[nStartBit / 8] & (0x80 >> (nStartBit % 8))) != 0)
            {
                dwRet += 1;
            }
            nStartBit++;
        }
        return (1 << nZeroNum) - 1 + dwRet;
    }

}
