package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * @Author pengsel
 * @Create 2019/7/11 13:38
 */
public class TCPConn implements Conn {
    private static final Logger logger= Logger.getLogger(TCPConn.class);

    private Server server;

    private Socket socket;

    private int connId;

    private boolean isClosed;

    private MsgHandler msgHandler;


    public TCPConn(Server server, Socket socket, int connId, MsgHandler msgHandler) {
        this.server = server;
        this.socket = socket;
        this.connId = connId;
        this.msgHandler = msgHandler;
        this.isClosed=false;
    }


    class Reader implements Runnable{

        private Conn conn;
        public Reader(Conn conn){
            this.conn=conn;
        }
        public void run() {

            byte[] headBuffer=new byte[TCPDataPack.getHeadLen()];
            try {
                int i=socket.getInputStream().read(headBuffer);
                if (i!=TCPDataPack.getHeadLen()){
                    logger.error(String.format(" i = %d, headBuffer=%s",i, Arrays.toString(headBuffer)));
                    throw new Exception();
                }

                Message message=TCPDataPack.unpack(headBuffer);
                int datalen=message.getDataLen();
                byte[] dataBuffer=new byte[datalen];
                i=socket.getInputStream().read(dataBuffer);
                if (i!=datalen){
                    throw  new Exception();
                }
                message.setData(dataBuffer);

                Request tcpRequest =new TCPRequest(conn,message);

                msgHandler.doMsgHandle(tcpRequest);

            } catch (Exception e) {
                logger.error("Unpack socket input stream failed",e);
            }
        }
    }

    public void start() {
        server.getThreadPoolExecutor().execute(new Reader(this));

    }

    public void stop() {
        isClosed=true;
        try {
            socket.close();
        } catch (IOException e) {
            logger.error("close socket err",e);
        }

    }

    public Socket getTCPConn() {
        return socket;
    }

    public SocketAddress getRemoteAddr() {
        return socket.getRemoteSocketAddress();
    }

    public int getConnId() {
        return connId;
    }

    public void sendMsg(int msgId, byte[] data) {
        Message message=new TCPMessage(data.length,msgId,data);
        byte[] bytes=TCPDataPack.pack(message);
        OutputStream outputStream=null;
        try {
            outputStream=socket.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            logger.error("send msg err",e);
        }
    }

    public Executor getThreadPoolExecutor() {
        return server.getThreadPoolExecutor();
    }
}
