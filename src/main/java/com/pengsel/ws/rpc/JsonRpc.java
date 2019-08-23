package com.pengsel.ws.rpc;

import com.alibaba.fastjson.JSON;
import com.pengsel.ws.hs.impl.HTTPConnector;
import com.pengsel.ws.hs.impl.SocketInputStream;
import com.pengsel.ws.rpc.bean.JsonRPCRequest;
import com.pengsel.ws.rpc.bean.JsonRPCResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.pengsel.ws.util.Constants.TCP_CONN_SIZE;

/**
 * @Author pengsel
 * @Create 2019/7/15 19:41
 */
public class JsonRpc {

    private static Logger logger = Logger.getLogger(HTTPConnector.class);

    public static BlockingQueue<Socket> tcpConns=new ArrayBlockingQueue<Socket>(TCP_CONN_SIZE);
    static {
        for (int i=0;i<TCP_CONN_SIZE;i++) {
            try {
                Socket socket = new Socket("127.0.0.1", 7777);
                tcpConns.add(socket);
            }catch (IOException ex){
                logger.error("Init TCP conns pool err",ex);
            }
        }
    }

    /**
     * 远程调用函数
     * @param method
     * @param params
     * @return
     */
    public static Object call(String method, List<Object>params, Socket socket) {
        JsonRPCRequest request=new JsonRPCRequest(method,params);
        String json= JSON.toJSONString(request);
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            inputStream=socket.getInputStream();
            outputStream=socket.getOutputStream();
            outputStream.write(json.getBytes());
            SocketInputStream socketInputStream=new SocketInputStream(inputStream,2048);
            String jsonResponse=socketInputStream.readJson();
            JsonRPCResponse response=JSON.parseObject(jsonResponse,JsonRPCResponse.class);
            return response.getResult();
        } catch (IOException e) {
            logger.error("Connect with TCPServer err");
        }catch (Exception e){
            logger.error("Communication with TCPServer failed");
        }finally {
            tcpConns.add(socket);
        }
        return null;
    }

    /**
     * 向HTTP服务器注册服务
     * @param socket
     */
    public static void register(Socket socket){



    }
}
