package com.pengsel.ws.server;

import com.pengsel.ws.ts.Server;
import com.pengsel.ws.ts.impl.BaseRouter;
import com.pengsel.ws.ts.impl.TCPConnManager;
import com.pengsel.ws.ts.impl.TCPMsgHandler;
import com.pengsel.ws.ts.impl.TCPServer;

/**
 * @Author pengsel
 * @Create 2019/7/11 16:51
 */
public class ServerTest {
    public static void main(String[] args) {
        Server server=new TCPServer("MyServer",
                "IPv4",
                "127.0.0.1",
                7777,
                new TCPMsgHandler(),
                new TCPConnManager());
        server.addRouter(1, new BaseRouter());
        server.serve();
    }
}
