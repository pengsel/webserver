package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.*;
import com.pengsel.ws.util.Constants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.pengsel.ws.util.Constants.SCAN_PATH;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:48
 */
public class TCPServer implements Server {

    private static final Logger logger=Logger.getLogger(TCPServer.class);

    static {
        Scanner.scanService(SCAN_PATH,"com.pengsel.ws.ts.service");
    }

    private String name;
    private String IPVersion;
    private String IP;
    private int port;
    private Handler handler;
    private ConnManager connManager;
    private Executor threadPoolExecutor;

    public TCPServer(String name, String IPVersion, String IP, int port, Handler handler, ConnManager connManager) {
        this.name = name;
        this.IPVersion = IPVersion;
        this.IP = IP;
        this.port = port;
        this.handler = handler;
        this.connManager = connManager;
        this.threadPoolExecutor=new ThreadPoolExecutor(2,
                2,0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public Executor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void start() {
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(port,1, InetAddress.getByName(IP));
        } catch (IOException e) {
            logger.error("ServerSocket err",e);
        }

        int connId=0;

        while (true){
            Socket socket=null;
            try {
                socket=serverSocket.accept();
                if (connManager.size()>= Constants.MAX_CONN){
                    logger.error("Can't afford more conn, closing socket...");
                    socket.close();
                    continue;
                }
                Conn conn=new TCPConn(this,socket,connId, handler);
                connManager.add(conn);
                connId++;
                conn.start();
            }catch (Exception ex){
                logger.error("Accept err:",ex);
            }
        }

    }

    public void stop() {

    }

    public void serve() {
        logger.info("TCP ts is running......");
        start();
    }

    public void addRouter(int msgId, Router router) {
        handler.addRouter(msgId,router);
    }

    public ConnManager getConnManager() {
        return null;
    }
}
