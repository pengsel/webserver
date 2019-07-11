package com.pengsel.ws.ts;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:40
 */
public interface MsgHandler {
    void doMsgHandle(Request request);

    void addRouter(int msgId,Router router);

    void startWorkerPool();

}
