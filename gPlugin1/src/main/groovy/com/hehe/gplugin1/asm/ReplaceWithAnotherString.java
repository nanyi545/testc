package com.hehe.gplugin1.asm;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


/**
 *
 * 替换指定方法
 *
 *
 */
public class ReplaceWithAnotherString extends MethodVisitor {
    private final MethodVisitor targetWriter;
    private final int newMaxLocals;

    ReplaceWithAnotherString(MethodVisitor writer, int newMaxL) {
        // now, we're not passing the writer to the superclass for our radical changes
        super(Opcodes.ASM5);
        targetWriter = writer;
        newMaxLocals = newMaxL;
    }

    // we're only override the minimum to create a code attribute with a sole RETURN

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        targetWriter.visitMaxs(0, newMaxLocals);
    }

    @Override
    public void visitCode() {
        targetWriter.visitCode();

        // return ..... this string
        targetWriter.visitLdcInsn("plugin returned string .... ");
        targetWriter.visitInsn(Opcodes.ARETURN);
    }

    @Override
    public void visitEnd() {
        targetWriter.visitEnd();
    }

    // the remaining methods just reproduce meta information,
    // annotations & parameter names

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return targetWriter.visitAnnotation(desc, visible);
    }

    @Override
    public void visitParameter(String name, int access) {
        targetWriter.visitParameter(name, access);
    }
}

