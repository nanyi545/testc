package com.hehe.gplugin1.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class LifecycleClassVisitor extends ClassVisitor {
    private String className;
    private String superName;

    public LifecycleClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

//        System.out.println("ClassVisitor visitMethod name-------" + name + ", superName:" + superName +"  className:"+className+"   desc:"+desc);

        /**
         * 茶庄修改   MainActivity.mod1 的 modifier
         * */
//        if (superName.equals("androidx/appcompat/app/AppCompatActivity")) {
//            if (name.startsWith("mod1")) {
//                access = Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL;
//            }
//        }


        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

//        if (superName.equals("androidx/appcompat/app/AppCompatActivity")) {
//            if (name.startsWith("onCreate")) {
//                //处理onCreate()方法
//                return new AddLogVisitor(mv, className, name);
//            }
//        }

        /**
         * 茶庄修改   MainActivity 的静态代码块
         */
//        if(name.equals("<clinit>") && className.equals("com/example/testc2/MainActivity") ) {
//            ReplaceWithEmptyBody mv2 = new ReplaceWithEmptyBody(mv,(Type.getArgumentsAndReturnSizes(desc)>>2)-1);
//            return mv2;
//        }


        /**
         * 茶庄修改 MainActivity。testCall1方法
         */
//        if (superName.equals("androidx/appcompat/app/AppCompatActivity")) {
//            if (name.startsWith("testCall1")) {
//                ReplaceWithEmptyBody mv2 = new ReplaceWithEmptyBody(mv,(Type.getArgumentsAndReturnSizes(desc)>>2)-1);
//                return mv2;
//            }
//        }

        /**
         * 茶庄修改 MainActivity。getStr1 方法
         */
//        if (superName.equals("androidx/appcompat/app/AppCompatActivity")) {
//            if (name.startsWith("getStr1")) {
//                ReplaceWithAnotherString mv2 = new ReplaceWithAnotherString(mv,(Type.getArgumentsAndReturnSizes(desc)>>2)-1);
//                return mv2;
//            }
//        }



        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}