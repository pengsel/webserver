package com.pengsel.ws.rpc;

import com.alibaba.fastjson.JSONObject;
import com.pengsel.ws.ts.Message;
import com.pengsel.ws.ts.impl.TCPDataPack;
import com.pengsel.ws.ts.impl.TCPMessage;

import java.util.Map;

/**
 * @Author pengsel
 * @Create 2019/7/15 19:41
 */
public class JsonRpcClient {
    public static Object call(String method, Map params) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("method",method);
        jsonObject.put("params",params);
        String data=jsonObject.toString();
        Message message=new TCPMessage(data.getBytes().length,0,data.getBytes());
        


        return null;
    }
}
