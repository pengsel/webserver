package com.pengsel.ws.ts.impl;

import com.alibaba.fastjson.JSON;
import com.pengsel.ws.hs.impl.SocketInputStream;
import com.pengsel.ws.rpc.bean.JsonRPCRequest;
import com.pengsel.ws.rpc.bean.JsonRPCResponse;
import com.pengsel.ws.ts.Request;
import com.pengsel.ws.ts.Router;
import com.pengsel.ws.ts.Scanner;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class JsonRouter implements Router {
    private static final Logger logger= Logger.getLogger(BaseRouter.class);

    public void handle(Request request) {
        Socket socket=request.getConn().getTCPConn();
        OutputStream outputStream=null;
        InputStream inputStream;
        String requestId = null;
        try {
            outputStream=socket.getOutputStream();
            inputStream=socket.getInputStream();
            SocketInputStream socketInputStream=new SocketInputStream(inputStream,2048);
            String jsonRequest=socketInputStream.readJson();
            JsonRPCRequest rpcRequest= JSON.parseObject(jsonRequest,JsonRPCRequest.class);
            requestId=rpcRequest.getId();
            Method method= Scanner.methodMap.get(rpcRequest.getMethod());
            Object result=method.invoke(Scanner.objectMap.get(method.getDeclaringClass().getName()),rpcRequest.getParams().toArray());

            JsonRPCResponse response=new JsonRPCResponse();
            response.setResult(result);
            response.setId(rpcRequest.getId());
            String jsonResponse=JSON.toJSONString(response);
            outputStream.write(jsonResponse.getBytes());
            logger.info("Send message to "+request.getConn().getRemoteAddr().toString());

        } catch (IOException e) {
            logger.error("IOException",e);
        } catch (Exception e) {
            JsonRPCResponse response=new JsonRPCResponse();
            JsonRPCResponse.Error error=new JsonRPCResponse.Error(-30000,"Invocation failed");
            response.setError(error);
            response.setId(requestId);
            String jsonResponse=JSON.toJSONString(response);
            try {
                outputStream.write(jsonResponse.getBytes());
            }catch (IOException ex){
                logger.error("Reply bad request failed",ex);
            }
        }
    }
}