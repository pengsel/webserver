package com.pengsel.ws.ts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pengsel.ws.rpc.User;
import com.pengsel.ws.rpc.bean.JsonRPCRequest;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectMethodTest {
    public static void main(String[] args) throws NoSuchMethodException {

        List<Object> list =new ArrayList<Object>();
        list.add("hello");
        User user=new User("sssssss",55);
        list.add(user);
        JsonRPCRequest request=new JsonRPCRequest("hello",list);
        String json= JSON.toJSONString(request);

        JSONObject jsonObject=JSON.parseObject(json);
        List params= (List) jsonObject.get("params");
        Method method=ReflectMethodTest.class.getMethod("hello", String.class, User.class);
//        Class[] classes=method.getParameterTypes();
//        Object[] objects=new Object[classes.length];
//        for (int i=0;i<objects.length;i++){
//            String str=(String) list.get(i);
//            Class clazz=classes[i];
//            objects[i]=JSON.parseObject(str,clazz);
//        }

        try {
            String ret=(String) method.invoke(new ReflectMethodTest(),list.toArray());
            System.out.println(ret);
            System.out.println(Arrays.toString(method.getParameterTypes()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(method.toString());
    }

    public  String hello(String first, User last){
        return "hello:"+first+last.getName()+last.getAge();
    }
}
