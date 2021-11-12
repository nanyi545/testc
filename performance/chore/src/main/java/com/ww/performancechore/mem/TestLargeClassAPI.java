package com.ww.performancechore.mem;

import android.util.Log;

public class TestLargeClassAPI {
    static TestLargeClass o1;
    public static void test1(){
        Log.d("aaaa","TestLargeClass");
        o1 = new TestLargeClass();
    }

    /**
     * ClassFile  412byte
     * Total 39.1mb
     * others1.5 code19.1   native11.7 java6.1
     *
     *
     * ClassFile  958kb
     * Total 44.5mb
     * others1.7 code18.7   native12.5 java6.1
     *
     *
     * 内存计数中的类别如下：
     *
     * Java：从 Java 或 Kotlin 代码分配的对象的内存。
     * Native：从 C 或 C++ 代码分配的对象的内存。即使您的应用中不使用 C++，您也可能会看到此处使用了一些原生内存，因为即使您编写的代码采用 Java 或 Kotlin 语言，Android 框架仍使用原生内存代表您处理各种任务，如处理图像资源和其他图形。
     * Graphics：图形缓冲区队列为向屏幕显示像素（包括 GL 表面、GL 纹理等等）所使用的内存。（请注意，这是与 CPU 共享的内存，不是 GPU 专用内存。）
     * Stack：您的应用中的原生堆栈和 Java 堆栈使用的内存。这通常与您的应用运行多少线程有关。
     * Code：您的应用用于处理代码和资源（如 dex 字节码、经过优化或编译的 dex 代码、.so 库和字体）的内存。
     * Others：您的应用使用的系统不确定如何分类的内存。
     * Allocated：您的应用分配的 Java/Kotlin 对象数。此数字没有计入 C 或 C++ 中分配的对象。
     *
     */
}
