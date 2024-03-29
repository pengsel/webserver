package com.pengsel.ws.ts;

//import com.pengsel.ws.rpc.JsonRpcClient;
import com.pengsel.ws.ts.impl.TCPDataPack;
import com.pengsel.ws.ts.impl.TCPMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author pengsel
 * @Create 2019/7/11 16:56
 */
public class Client {
    public static void main(String[] args) {
        Socket socket=null;
        OutputStream outputStream=null;
        InputStream inputStream = null;
        try {
            socket=new Socket("127.0.0.1",7777);
            outputStream=socket.getOutputStream();
            inputStream=socket.getInputStream();
//            JsonRpcClient client=new JsonRpcClient();
//            client.call("/hello/time",null);
            byte[] buffer=new byte[2048];
            int i=inputStream.read(buffer);
            if (i!=-1) {
                System.out.println(new String(buffer));
            }else {
                System.out.println("err");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
