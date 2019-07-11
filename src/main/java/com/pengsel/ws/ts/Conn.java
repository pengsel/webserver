package com.pengsel.ws.ts;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executor;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:18
 */
public interface Conn {
    void start();

    void stop();

    Socket getTCPConn();

    SocketAddress getRemoteAddr();

    int getConnId();

    void sendMsg(int msgId,byte[] data);

    Executor getThreadPoolExecutor();
}
