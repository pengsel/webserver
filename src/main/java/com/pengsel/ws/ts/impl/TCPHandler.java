package com.pengsel.ws.ts.impl;

import com.alibaba.fastjson.JSON;
import com.pengsel.ws.rpc.bean.JsonRPCRequest;
import com.pengsel.ws.rpc.bean.JsonRPCResponse;
import com.pengsel.ws.ts.Handler;
import com.pengsel.ws.ts.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import static com.pengsel.ws.ts.Scanner.APIS;
import static com.pengsel.ws.ts.Scanner.OBJS;

/**
 * @Author pengsel
 * @Create 2019/7/11 15:51
 */
public class TCPHandler implements Handler {

    private static final Logger logger = Logger.getLogger(TCPHandler.class);
    class Handle implements Runnable{
        private Request request;
        public Handle(Request request){
            this.request=request;
        }
        public void run() {
            JsonRPCRequest jsonReq= JSON.parseObject(request.getJson(), JsonRPCRequest.class);
            Method method=APIS.get(jsonReq.getMethod());
            Socket socket=null;
            try {
                Object ret=method.invoke(OBJS.get(method.getDeclaringClass().getName()),jsonReq.getParams());
                JsonRPCResponse response=new JsonRPCResponse();
                response.setId(request.getJson());
                response.setResult(ret);
                socket=request.getConn().getTCPConn();
                OutputStream outputStream=socket.getOutputStream();
                outputStream.write(JSON.toJSONBytes(response));

            } catch (IllegalAccessException e) {
                logger.error("invoke method"+method.getName()+"failed, illeagal access",e);
            } catch (InvocationTargetException e) {
                logger.error("invoke method"+method.getName()+"failed",e);
            } catch (IOException e) {
                logger.error("failed to send response",e);
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("close socket err",e);
                }
            }
        }
    }
    public void handle(Request request) {
        request.getConn().getThreadPoolExecutor().execute(new Handle(request));
    }

}
