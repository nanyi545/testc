package com.ww.performancechore;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void writeFile() {
        File f = new File("/Users/weiwang/AndroidStudioProjects/TestC2/performance/chore/src/main/java/com/ww/performancechore/mem/TestLargeClass.java");
        try {
            PrintWriter out = new PrintWriter(f);
            out.println("package com.ww.performancechore.mem;");
            out.println("public class TestLargeClass {");
            for (int i=0;i<1;i++){
                out.println("long a"+i+";");
                out.println("public long getA"+i+"(){return "+i+";}");
            }
            out.println("}");
            out.flush();
            out.close();
            System.out.println("11");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("22");
        }

    }

}