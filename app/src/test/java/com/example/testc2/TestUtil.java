package com.example.testc2;

public class TestUtil {

    public static void printArr(int[] arr,String tag){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<arr.length;i++){
            sb.append(arr[i]+"_");
        }
        System.out.println(tag+"  "+sb.toString());
    }

    public static void printArr(char[] arr,String tag){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<arr.length;i++){
            sb.append(arr[i]+"_");
        }
        System.out.println(tag+"  "+sb.toString());
    }

}
