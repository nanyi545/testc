package com.example.testc2.sort;

import com.example.testc2.TestUtil;

import org.junit.Test;

/**
 *
 *  find kth element out of 2 sorted array
 *
 */
public class Kth {

    @Test
    public void test1(){
        int[] arr1= {2,4,6,8,14,22,79};
        int[] arr2= {1,2,3,4,5,6,7};

        // test merge ...
        int[] m = merge(arr1,arr2);
        TestUtil.printArr(m,"a");
        for (int i=0;i<10;i++){
            System.out.println("-------i:"+i+"   m[i]:"+m[i]+"  getI:"+get(arr1,arr2,i+1));
        }

    }

    int getInt(int i,int[] arr){
        if(i<arr.length){ return arr[i]; }return Integer.MAX_VALUE;
    }


    /**
     *
     * method 1 , merge 2 arrays   ---->   o(m+n)
     *
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public int[] merge(int[] arr1,int[] arr2){
        int s1 = arr1.length;
        int s2 = arr2.length;
        int[] arr = new int[s1+s2];
        int i1 = 0;
        int i2 = 0;
        for(int i=0;i<s1+s2;i++){
            int v1 = getInt(i1,arr1);
            int v2 = getInt(i2,arr2);
            if(v1<v2){
                arr[i] = v1;
                i1++;
            }else {
                arr[i] = v2;
                i2++;
            }
        }
        return arr;
    }


    /**
     *
     *
     *  method 2  ln(m+n)
     *
     * @param arr1
     * @param arr2
     * @param k
     * @return
     *
     *
     * find kth element  ---->  exclude k-1 elements ...
     *
     *
     */
    public int get(int[] arr1,int[] arr2, int k){
        int need2exclude = k-1;
        int excluded = 0;
        int i1 = 0;  // items to exclude in arr1
        int i2 = 0;  // items to exclude in arr2
        while ( excluded < need2exclude ) {

            int t   = (need2exclude-excluded) / 2;
            int count =  t>0?t:1;

            //   this might has issue  ----->  might cause ---> array index out of bounds ...
            if( arr1[i1+count-1] < arr2[i2+count-1] ){
                i1+=count;
            } else {
                i2+=count;
            }
            excluded += count;
        }
        if(arr1[i1]<arr2[i2]){
            return arr1[i1];
        }
        return arr2[i2];
    }

}
