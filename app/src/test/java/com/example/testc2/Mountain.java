package com.example.testc2;


import org.junit.Test;

/**
 *
 * 陆伯言来到山顶观察此八卦阵，记从左往右第i堆石堆的高度为A[i]，发现任何两堆较矮的石堆都能和它们之间的一座较高的石堆形成"八卦锁"，将其中之人牢牢锁住，无从逃脱。
 *
 * 根据石堆的情况，陆伯言大致计算了“八卦锁”的数量，陆伯言大致计算了“八卦锁”的数量
 * （即 A[i] < A[j] > A[k] 且I < j < k的组合数），不禁心中一惊,对孔明惊为天人，遂放弃追击，收兵回吴。
 *
 *
 * 输入描述
 * 第一行一个整数n，表示石堆堆数。
 *
 * 接下来一行，n个整数，第i个数表示从左到右第i堆石堆的高度A[i]。
 *
 * 1<=N<=5*10^4,1<=A[i]<=32768
 *
 * 输出描述
 * 输出仅一行，表示答案
 *
 *
 * 样例输入
 * 5
 * 1 2 3 4 1
 * 样例输出
 * 6
 *
 */
public class Mountain {

    @Test
    public void case1(){
        int[] arr = {1,2,3,4,1};
        System.out.println(getMountCount(arr));
    }

    int getSmallerCount(int[] arr,int ind){
        int count = 0;
        for(int i=0;i<ind;i++){
            if(arr[i]<arr[ind]){
                count++;
            }
        }
        return count;
    }
    int getSmallerCount2(int[] arr,int ind){
        int count = 0;
        for(int i=ind+1;i<arr.length;i++){
            if(arr[i]<arr[ind]){
                count++;
            }
        }
        return count;
    }

    /**
     *
     *  this has n^3 complexity
     */
    public int getMountCount(int[] arr){
        int total = 0;
        for (int i=1;i<arr.length-1;i++){
            int l = getSmallerCount(arr,i);
            if(l!=0){
                int r = getSmallerCount2(arr,i);
                total += (l*r);
            }
        }
        return total;
    }

}
