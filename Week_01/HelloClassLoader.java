package com.java.partiii.jvm.temp.jk.c1.classloader.homework1;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @Title: HelloClassloader.java
 * @Description: 自定义ClassLoader
 * @Company: ruhua
 * 
 * @author ruhua
 * @version 1.0
 * @date 2020年10月16日 22:54:03
 */
public class HelloClassLoader extends ClassLoader {

	public static void main(String[] args) {

		// 文件路径
		String filePath = HelloClassLoader.class.getResource("Hello.xlass").getPath();
		// System.out.println(filePath);

		try {
			// 获取类实例
			Class<?> c = new HelloClassLoader().findClass(filePath);
			// Class<?> c = new HelloClassLoader().loadClass(filePath);

			// 调用方法
			Method hello = c.getDeclaredMethod("hello");
			hello.setAccessible(true);
			hello.invoke(c.newInstance());

		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Class<?> findClass(String filePath) throws ClassNotFoundException {
		byte[] bytes = convert2Bytes(filePath); // 转换为byte数组
		return defineClass("Hello", bytes, 0, bytes.length); // 将 byte数组转换为 Class类实例
	}

	/**
	 * @Desc 读取class文件并转换为byte数组:文件内容是一个 Hello.class文件所有字节（x=255-x）处理后的文件
	 * 
	 * @param filePath
	 * @return
	 */
	private byte[] convert2Bytes(String filePath) {
		byte[] resultBytes = null;
		try (FileInputStream fis = new FileInputStream(filePath);
				ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			int line;
			// 读取文件并减去255
			while ((line = fis.read()) != -1) {
				baos.write(255 - line);
			}
			resultBytes = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultBytes;
	}
}
