package com.pengsel.ws.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonTest {
    public static void main(String[] args) {
        Integer name=4;
        String str="hello";
        List<String> list=new ArrayList<String>();
        list.add("hello");
        list.add("ppppp");
        System.out.println(JSON.toJSONString(str));
        System.out.println(JSON.toJSONString(name));
        System.out.println(JSON.toJSONString(list));
        String s=JSON.toJSONString(list);
        List<String> list1=JSON.parseArray(s,String.class);

        List<Object> list2=new ArrayList<Object>();
        list2.add("asdasdasd");
        list2.add(1);
        list2.add(true);
        list2.add(new User("dp",18));
        Object object= JSON.toJSON(new User("dp",18));
        list2.add(object);
        String json2=JSON.toJSONString(list2);
        System.out.println(json2);

        List json2toList=JSON.parseObject(json2,List.class);
        System.out.println(json2toList);


        User user =new User("duanpeng",24);
        Object object2=JSON.toJSON(user);
        String s1=JSON.toJSONString(user);
        System.out.println(s1);


    }


}
