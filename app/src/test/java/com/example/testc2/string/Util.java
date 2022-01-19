package com.example.testc2.string;

import org.junit.Test;

import java.text.DecimalFormat;

public class Util {

    @Test
    public void test1(){
        System.out.println(removeDots("122.09"));
        System.out.println(removeDots("122.10"));
        System.out.println(removeDots("122.0002"));
        System.out.println(removeDots("1.20"));
        System.out.println(removeDots("2.00"));
        System.out.println(removeDots("122"));
    }

    @Test
    public void test2(){
        System.out.println(get("122.09"));
        System.out.println(get("122.10"));
        System.out.println(get("122.002"));
        System.out.println(get("1.20"));
        System.out.println(get("2.00"));
        System.out.println(get("122"));
    }


    private String get(String str){
        float f = 0;
        try {
            f = Float.parseFloat(str);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return format.format(f);
    }

    private static DecimalFormat format = new DecimalFormat("#.##");

    public static String removeDots(String str){
        float f = 0;
        try {
            f = Float.parseFloat(str);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        int rounded = Math.round(f);
        float diff =  Math.abs(f - rounded);
        if(diff>0.001f){
            return format.format(f);
        }
        return rounded+"";
    }


}
