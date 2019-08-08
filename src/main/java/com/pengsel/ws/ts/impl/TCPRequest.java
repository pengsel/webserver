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
    private String json;

    public TCPRequest(Conn conn, String json) {
        this.conn = conn;
        this.json = json;
    }

    public Conn getConn() {
        return conn;
    }

    public String getJson() {
        return json;
    }


}
