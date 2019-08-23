package com.pengsel.ws.hs;

import java.io.IOException;

public interface Processor extends Runnable {

    void process() throws IOException;
}
