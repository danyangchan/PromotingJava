package com.geekbang.jvm;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HomeWork2_MyHelloClassLoader extends ClassLoader {


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        final String suffix = ".xlass";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name + suffix);
        System.out.println("inputStream: " + inputStream);
        try {
            int length = inputStream.available();
            byte[] byteArr = new byte[length];
            inputStream.read(byteArr);
            byte[] classBytes = decode(byteArr);
            return defineClass(name,classBytes,0,classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }finally {
            close(inputStream);
        }
    }

    private byte[] decode(byte[] bytes){
        byte[] classBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++){
            classBytes[i] = (byte) (255 -  bytes[i]);
        }
        return classBytes;
    }

    private void close(Closeable res){
        if (res != null){
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = "Hello";
        String methodName = "hello";
        ClassLoader classLoader = new HomeWork2_MyHelloClassLoader();
        Class helloClass = classLoader.loadClass(className);
        for (Method method : helloClass.getDeclaredMethods()){
            System.out.println(method);
        }
        Object instance = helloClass.getDeclaredConstructor().newInstance();
        Method method = helloClass.getMethod(methodName);
        method.invoke(instance);

    }
}
