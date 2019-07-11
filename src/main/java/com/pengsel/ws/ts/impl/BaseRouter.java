package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.Request;
import com.pengsel.ws.ts.Router;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author pengsel
 * @Create 2019/7/11 16:06
 */
public class BaseRouter implements Router {
    private static final Logger logger= Logger.getLogger(BaseRouter.class);

    public void handle(Request request) {
        Socket socket=request.getConn().getTCPConn();
        OutputStream outputStream=null;
        try {
            outputStream=socket.getOutputStream();
            outputStream.write("Test router......Test router......".getBytes());
            logger.info("Send message to "+request.getConn().getRemoteAddr().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
