package com.pengsel.ws.ts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.pengsel.ws.util.Constants.SCAN_PATH;

/**
 * @Author pengsel
 * @Create 2019/7/15 16:46
 */
public class ScannerTest {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Scanner.scanService(SCAN_PATH,"com.pengsel.ws.ts.service.");
        Method method=Scanner.methodMap.get("/login/login");
        Object object=Scanner.objectMap.get(method.getDeclaringClass().getName());
        method.setAccessible(true);
        method.invoke(object);
    }
}
