package com.example.testc2.math;

/**
 * 描述
 *
 *
 *    有这样一道智力题：“某商店规定：三个空汽水瓶可以换一瓶汽水。小张手上有十个空汽水瓶，她最多可以换多少瓶汽水喝？
 *    ”答案是 5 瓶，方法如下：先用 9 个空瓶子换3瓶汽水，喝掉 3 瓶满的，喝完以后 4 个空瓶子，用 3 个再换一瓶，喝掉这瓶满的，
 *    这时候剩 2 个空瓶子。然后你让老板先借给你一瓶汽水，喝掉这瓶满的，喝完以后用 3 个空瓶子换一瓶满的还给老板。如果小张手上有 n 个空汽水瓶，最多可以换多少瓶汽水喝？
 *
 */
public class Bottles {


    public void test1(){
        int empty = 100;
        int b = 5;
        int count = 0;

        while ( empty>=b ) {
            int t = empty/b;
            empty = empty%b;
            count+= t;
            empty+= t;
        }
        if(empty==(b-1)){
            count++;
        }
        System.out.println(count);
    }
}
