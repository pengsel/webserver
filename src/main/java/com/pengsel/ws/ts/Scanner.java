package com.pengsel.ws.ts;

import com.pengsel.ws.util.annotation.Autowired;
import com.pengsel.ws.util.annotation.RequestMapping;
import com.pengsel.ws.util.annotation.Service;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author pengsel
 * @Create 2019/7/15 15:18
 */
public class Scanner {

    private static Logger logger = Logger.getLogger(Scanner.class);


    /**
     * 实例表
     */
    public static Map<String, Object> objectMap = new HashMap<String, Object>();

    /**
     * 方法表
     */
    public static Map<String, Method> methodMap = new HashMap<String, Method>();

    /**
     * 扫描指定路径下的java文件，生成实例和方法进入实例表和方法表
     *
     * @param scanPath    路径
     * @param packageName 包名
     */
    public static void scanService(String scanPath, String packageName) {

        File file = new File(scanPath);
        getInstance(file, packageName);
        dependencyInject();
    }

    private static void getInstance(File folder, String packageName) {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getInstance(file, packageName + file.getName() + ".");
            } else {
                try {
                    String filename = file.getName();
                    String name = filename.substring(0, filename.length() - 5);
                    Class clazz = Class.forName(packageName + name);
                    Object object = clazz.newInstance();
                    //根据Service注解注册Service
                    if (clazz.isAnnotationPresent(Service.class)) {
                        objectMap.put(clazz.getName(), object);

                    }
                    String classPath = "";
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                        classPath = requestMapping.path();
                    }

                    //根据RequestMapping注解注册Method
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping methodPath = method.getAnnotation(RequestMapping.class);
                            methodMap.put(classPath + methodPath.path(), method);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Create service instance and method instance failed", e);
                }
            }
        }
    }

    private static void dependencyInject() {
        for (String entry : objectMap.keySet()) {
            Object object = objectMap.get(entry);
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        field.setAccessible(true);
                        field.set(object, objectMap.get(field.getType().getName()));
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error("Dependency injection failed", e);
            }

        }
    }
}