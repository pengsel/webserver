package com.pengsel.ws.rpc;

import com.alibaba.fastjson.JSONObject;
import com.pengsel.ws.hs.impl.HTTPConnector;
import com.pengsel.ws.ts.Message;
import com.pengsel.ws.ts.impl.TCPDataPack;
import com.pengsel.ws.ts.impl.TCPMessage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.pengsel.ws.util.Constants.TCP_CONN_SIZE;

/**
 * @Author pengsel
 * @Create 2019/7/15 19:41
 */
public class JsonRpcClient {

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
    public static Object call(String method, Map params) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("method",method);
        jsonObject.put("params",params);
        String data=jsonObject.toString();
        Message message=new TCPMessage(data.getBytes().length,0,data.getBytes());
        Socket socket=tcpConns.poll();
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            inputStream=socket.getInputStream();
            outputStream=socket.getOutputStream();
            outputStream.write(TCPDataPack.pack(message));
            byte[] headBuffer=new byte[TCPDataPack.getHeadLen()];
            int i=socket.getInputStream().read(headBuffer);
            if (i!=TCPDataPack.getHeadLen()){
                logger.error(String.format(" i = %d, headBuffer=%s",i, Arrays.toString(headBuffer)));
                throw new Exception();
            }

            Message ret=TCPDataPack.unpack(headBuffer);
            int datalen=ret.getDataLen();
            byte[] dataBuffer=new byte[datalen];
            i=socket.getInputStream().read(dataBuffer);
            if (i!=datalen){
                throw  new Exception();
            }
            ret.setData(dataBuffer);
            return ret;
        } catch (IOException e) {
            logger.error("Connect with TCPServer err");
        }catch (Exception e){
            logger.error("Communication with TCPServer failed");
        }finally {
            tcpConns.add(socket);
        }
        return null;
    }
}
