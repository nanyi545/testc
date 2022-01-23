package framework;

/**
 *
 * https://linguanghua.github.io/Programming-Tech-Books/advanced/init进程到Zygote进程.html
 *
 *
 */

public class Memo {


    /**
     *
     * https://linguanghua.github.io/Programming-Tech-Books/advanced/Android设备启动流程.html
     *
     *
     * Android设备启动流程
     *
     * 上电。任何电子设备，启动的第一步都是上电，Android自然不例外。上电之后，引导芯片在预定义的地方找到执行代码开始执行。这部分代码主要是加载引导程序（Bootloader）到RAM，然后执行引导程序。
     *
     * Bootloader。引导程序是Android操作系统开始运行前需要执行的一个小程序，主要作用是检查硬件资源有没有问题，是否满足操作系统运行，检查通过之后就把Android操作系统拉起来运行。首先是Android内核的启动。
     *
     * kernel（内核）。内核是操作系统最基本的部分，是一个操作系统的核心，是基于硬件的第一层软件扩充，是操作系统工作的基础，提供操作系统最基本的功能。负责管理系统的进程、内存、内核体系结构、设备驱动程序、文件和网络系统，决定系统的性能和稳定。Android内核基于Linux内核，不过Android的内核跟Linux的内核差别也比较大。Android内核去掉了部分Linux内核的功能，也加入了自身特有的功能。在这一步，会进行加载驱动等操作。当Android内核设置完成之后，就会启动第一个进程：init进程
     *
     * init进程。用户空间的第一个进程，进程号是：1。Android操作系统基于Linux操作系统，所以也有内核空间和用户空间之分。当内核启动之后，会在系统文件中寻找init.rc文件，启动init进程。所以，在这里就从内核空间转到了用户空间。init进程启动之后，会创建一些目录，包括/dev、/porc、/sysfc等。紧接着就是启动Zygote进程。
     *
     * Zygote进程。zygote进程在Android系统中很重要，它也是Android中Java世界的第一个进程，后续所有的Java进程（如应用程序进程）都是从Zygote孵化出来。Zygote进程创建进程的模式：a、Zyogte模式，初始化Zygote进程。b、application模式，孵化启动普通应用程序。Zygote进程启动之后，紧接着会启动System-Server。
     *
     * System-Server。System-Server启动之后，就开始启动Android的各种系统服务，最后启动Launcher界面，整个Android系统就启动起来了。
     *
     *
     *
     * keys
     * *  Android操作系统基于Linux操作系统，所以也有内核空间和用户空间之分。
     * *  当内核启动之后，会在系统文件中寻找init.rc文件，启动init进程。所以，在这里就从内核空间转到了用户空间。
     * *  Zygote进程。它也是Android中Java世界的第一个进程，后续所有的Java进程（如应用程序进程）都是从Zygote孵化出来。
     * *  System-Server。System-Server启动之后，就开始启动Android的各种系统服务，最后启动Launcher界面，整个Android系统就启动起来了。
     *
     */


    /**
     *  init进程到Zygote进程
     *
     * https://linguanghua.github.io/Programming-Tech-Books/advanced/init进程到Zygote进程.html
     *
     *
     * 总结
     * ​ init进程是怎么被拉起的，那是内核的事情，太底层了看不到。而init进程被拉起来之后，做了些什么事情呢？如下：
     *
     * 读取、解析、执行init.rc文件
     * 解析init.rc到zygote start的时候， 程序运行到app_process.cpp的main方法(app_process.cpp的main方法的参数来源与init.zygote64.rc)
     * app_process.cpp的main方法，解析参数，将参数一一准备好，放到一个String类型的列表中。
     * 启动虚拟机、注册Android JNI方法
     * 通过JNI，调用Java层的com.android.internal.os.ZygoteInit.java的main方法
     */


    /**
     *https://linguanghua.github.io/Programming-Tech-Books/advanced/SystemServer进程的启动.html
     *
     *
     *
     * 总结
     *
     * System Server进程不愧是Android系统在Java世界的大管家，做的事情真多。哈哈哈哈，看一下主要做了什么。
     *
     * 记录启动的信息
     * 时区设置
     * 语言设置
     * 确保在系统服务内，对所有传入的Bundle进行隔离。
     * 确保在系统中的binder调用总是在前台运行
     * 设置 stem_server中的binder线程数量值是31
     * 创建stem_server进程中主线程的looper
     * 调用 createSystemContext 创建系统上下文环境
     * 创建SystemServiceManager的对象，设置创建时间，然后放入LocalServices里面
     * 准备需要用的线程池，没有接收返回，应该相当于初始化
     * 启动各种服务，包括：引导程序服务、核心服务、其他服务三大类
     * 系统启动快慢监测
     * 开启主线程的looper
     *
     *
     */

}
