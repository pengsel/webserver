package com.pengsel.ws.hs;

import com.pengsel.ws.hs.impl.HTTPConnector;

public class HTTPServerTest {
    public static void main(String[] args) {
        HTTPConnector connector=new HTTPConnector();
        connector.start();
    }
}
