package com.pengsel.ws.hs;

import com.pengsel.ws.hs.impl.HTTPDispatcher;

import java.net.InetSocketAddress;

public interface Dispatcher {
    void init();
    void configure(HTTPDispatcher.DispatchStratege stratege);
    InetSocketAddress getAddr(String uri,String host);
}
