package com.pengsel.ws.ts;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:35
 */
public interface Request {
    Conn getConn();

    int getMsgId();

    byte[] getData();
}
