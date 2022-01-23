package com.example.testc2;

import org.junit.Test;

public class Test2 {


    @Test
    public void test1(){

        int N = 3;
        char max = (char)('a'+ (N-1));

        String str = "cba";
        int l = str.length();
        char[] arr = str.toCharArray();

        int changedDigit = 1;
        int least = l-1;

        while(true){

            boolean terminated = true;
            for (int k=0;k<l;k++){
                if(arr[k]!=max){
                    terminated = false;
                    break;
                }
            }

            if(terminated){
                break;
            }

            int t = 1;

            for(int i=least;i>=0;i--){
                System.out.println("i:"+i);
                arr[i] = (char)(arr[i]+1);
                if(arr[i]<=max){
                    break;
                } else {
                    t++;
                    arr[i] = 'a';
                    if(t>changedDigit){
                        changedDigit = t;
                    }
                }
            }

            int x = changedDigit*2;
            System.out.println("arr:"+new String(arr));
            boolean cbd = new String(arr).equals("cbd");
            boolean reverse = false;
            for (int j = 1;j<=x;j++){
                if(cbd)System.out.println("------------:"+j);
                reverse = (reverse || isReverse(arr,(least-j)<0?0:(least-j),least)) ;
            }
            if(cbd)System.out.println("------666--:"+reverse);
            if(!reverse){
                System.out.println(new String(arr));
                return;
            } else {

            }

        }


        System.out.println("NO");
        return;

    }

    static boolean isReverse(char[] arr,int s,int e){
        int length = e - s;
        for (int i=s;i<(e);i++){
            if(arr[i]!=arr[s+e-i]){
                return false;
            }
        }
        return true;
    }

}
