package com.pengsel.ws.hs.impl;

import com.pengsel.ws.hs.Connecter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPConnector implements Connecter {
    private static Logger logger = Logger.getLogger(HTTPConnector.class);

    boolean stopped;
    private String scheme = "http";

    public String getScheme() {
        return scheme;
    }


    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stopped) {
            // Accept the next incoming connection from the ts socket
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            }
            catch (Exception e) {
                continue;
            }
            // Hand this socket off to an HTTPProcessor
            HTTPProcessor processor = new HTTPProcessor(this);
            processor.process(socket);
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
