#!/bin/bash
# NDK目录
NDK_ROOT=/Users/weiwang/Library/Android/sdk/ndk/22.0.7026061
#编译后安装位置 pwd表示当前目录
PREFIX=`pwd`/android/armeabi-v7a
#目标平台版本,我们将兼容到android-21
API=21
#编译工具链目录
TOOLCHAIN=$NDK_ROOT/toolchains/llvm/prebuilt/darwin-x86_64

#小技巧，创建一个AS的NDK工程，执行编译，
#然后在 app/.cxx/cmake/debug(release)/自己要编译的平台/ 目录下自己观察 build.ninja与 rules.ninja

#虽然x264提供了交叉编译配置:--cross-prefix，如--corss-prefix=/NDK/arm-linux-androideabi-
#那么则会使用 /NDK/arm-linux-androideabi-gcc 来编译
#然而ndk19开始gcc已经被移除，由clang替代。
# 小常识:一般的库都会使用$CC 变量来保存编译器，我们自己设置CC变量的值为clang。

export CC=$TOOLCHAIN/bin/armv7a-linux-androideabi$API-clang
export CXX=$TOOLCHAIN/bin/armv7a-linux-androideabi$API-clang++


#--extra-cflags会附加到CFLAGS 变量之后，作为传递给编译器的参数，所以就算有些库没有--extra-cflags配置，我们也可以自己创建变量cFLAGS传参
FLAGS="--target=armv7-none-linux-androideabi21 --gcc-toolchain=${TOOLCHAIN}  -g -DANDROID -fdata-sections -ffunction-sections -funwind-tables -fstack-protector-strong -no-canonical-prefixes -D_FORTIFY_SOURCE=2 -march=armv7-a -mthumb -Wformat -Werror=format-security   -Oz -DNDEBUG  -fPIC "


# echo ${FLAGS}


# prefix: 指定编译结果的保存目录 `pwd`: 当前目录
./x264/configure --prefix=${PREFIX} \
--disable-cli \
--enable-static \
--enable-pic=no \
--host=arm-linux \
--cross-prefix=${TOOLCHAIN}/bin/arm-linux-androideabi- \
--sysroot=${TOOLCHAIN}/sysroot \
--extra-cflags="$cleFLAGS"

make install
