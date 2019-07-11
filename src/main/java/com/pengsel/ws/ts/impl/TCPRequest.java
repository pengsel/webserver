package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.Conn;
import com.pengsel.ws.ts.Message;
import com.pengsel.ws.ts.Request;

/**
 * @Author pengsel
 * @Create 2019/7/11 15:20
 */
public class TCPRequest implements Request {

    private Conn conn;
    private Message message;

    public TCPRequest(Conn conn, Message message) {
        this.conn = conn;
        this.message = message;
    }

    public Conn getConn() {
        return conn;
    }

    public int getMsgId() {
        return message.getMsgId();
    }

    public byte[] getData() {
        return message.getData();
    }

}
