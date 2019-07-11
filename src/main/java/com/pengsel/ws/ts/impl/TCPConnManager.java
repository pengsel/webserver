package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.Conn;
import com.pengsel.ws.ts.ConnManager;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author pengsel
 * @Create 2019/7/11 16:08
 */
public class TCPConnManager implements ConnManager {
    ConcurrentHashMap<Integer,Conn> conns=new ConcurrentHashMap<Integer, Conn>();

    public void add(Conn conn) {
        conns.put(conn.getConnId(),conn);
    }

    public void remove(Conn conn) {
        conns.remove(conn.getConnId());
    }

    public Conn get(int connId) {
        return conns.get(connId);
    }

    public int size() {
        return conns.size();
    }

    public void clear() {
    }
}
