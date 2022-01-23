package com.example.testc2.string;

import org.junit.Test;

import java.util.regex.Pattern;


/**
 * Method	Description
 *
 * s.matches("regex")  Evaluates if "regex" matches s. Returns only true if the WHOLE string can be matched.
 *
 * s.split("regex") Creates an array with substrings of s divided at occurrence of "regex". "regex" is not included in the result.
 *
 * s.replaceFirst("regex"), "replacement" Replaces first occurance of "regex" with "replacement.
 *
 * s.replaceAll("regex"), "replacement"  Replaces all occurances of "regex" with "replacement.
 *
 */
public class RegTest {


    @Test
    public void test1(){
        String text="This is the text to be searched " +
                        "for occurrences of the http:// pattern.";
        String regex = ".*http://.*";
        boolean matches = Pattern.matches(regex, text);
        System.out.println("matches 1 = " + matches);
        System.out.println("matches 2 = " + text.matches(regex));
    }


    @Test
    public void test2(){
        String text="aaa";
        String regex = "aaa";  // will match all strings that are exactly the same as the regular expression. There can be no characters before or after
        String regex2 = "aa";
        System.out.println("matches 1 = " + text.matches(regex));
        System.out.println("matches 2 = " + text.matches(regex2));
    }

    //  "[0-9A-Za-z]{1}"

    @Test
    public void test3(){
        String text="a";
        String text2="ll";
        String regex = "[0-9A-Za-z]{1}";
        System.out.println("matches 1 = " + text.matches(regex));
        System.out.println("matches 2 = " + text2.matches(regex));
    }

    @Test
    public void test4(){
        String text="a";
        String text2="ll";
        String text3="abcdefg";
        String regex = "[0-9A-Za-z]{0,}";
        System.out.println("matches 1 = " + text.matches(regex));
        System.out.println("matches 2 = " + text2.matches(regex));
        System.out.println("matches 3 = " + text3.matches(regex));
    }


    @Test
    public void test5(){
        String text="9a9";
        String text2="9abc9";
        String text3="9abc8";
        String regex = "9[0-9A-Za-z]{0,}9";
        System.out.println("matches 1 = " + text.matches(regex));
        System.out.println("matches 2 = " + text2.matches(regex));
        System.out.println("matches 3 = " + text3.matches(regex));
    }

    @Test
    public void test6(){
        String text="aaa";
        String text2="aa1a";
        String text3="aa23a";
        String regex = "a[0-9A-Za-z]{3}a";
        System.out.println("matches 1 = " + text.matches(regex));
        System.out.println("matches 2 = " + text2.matches(regex));
        System.out.println("matches 3 = " + text3.matches(regex));
    }


    @Test
    public void test7(){
        String text="a a";
        String text2="a  a";
        String text3="a   a";
        String regex = "a[\\s]{3}a";
        System.out.println("matches 1 = " + text.matches(regex));
        System.out.println("matches 2 = " + text2.matches(regex));
        System.out.println("matches 3 = " + text3.matches(regex));

        String regex2 = "a[\\s]{1,2}a";
        System.out.println("matches 1 = " + text.matches(regex2));
        System.out.println("matches 2 = " + text2.matches(regex2));
        System.out.println("matches 3 = " + text3.matches(regex2));

    }


    @Test
    public void test8(){
        String text="te?t*.*";
        System.out.println("replace1: " + text.replace("*","9"));     //  replace takes a string
        System.out.println("replace2: " + text.replaceAll("\\*","9")); //  replaceAll takes a regex
        System.out.println("replace3: " + text.replace(".","9"));     //
    }


}
