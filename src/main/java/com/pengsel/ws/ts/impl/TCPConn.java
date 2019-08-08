package com.pengsel.ws.ts.impl;

import com.pengsel.ws.hs.impl.SocketInputStream;
import com.pengsel.ws.ts.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
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

    private Handler handler;


    public TCPConn(Server server, Socket socket, int connId, Handler handler) {
        this.server = server;
        this.socket = socket;
        this.connId = connId;
        this.handler = handler;
        this.isClosed=false;
    }


    class Reader implements Runnable{

        private Conn conn;
        public Reader(Conn conn){
            this.conn=conn;
        }
        public void run() {
            try {
                SocketInputStream socketInputStream=new SocketInputStream(socket.getInputStream(),2048);
                String json=socketInputStream.readJson();
                Request tcpRequest =new TCPRequest(conn,json);
                handler.handle(tcpRequest);
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

    public Executor getThreadPoolExecutor() {
        return server.getThreadPoolExecutor();
    }
}
