package com.pengsel.ws.hs;

import java.net.Socket;

public interface Processor {

    void process(Socket socket);
}
