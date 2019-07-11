package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.MsgHandler;
import com.pengsel.ws.ts.Request;
import com.pengsel.ws.ts.Router;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author pengsel
 * @Create 2019/7/11 15:51
 */
public class TCPMsgHandler implements MsgHandler {

    ConcurrentHashMap<Integer,Router> apis=new ConcurrentHashMap<Integer, Router>();

    class Handle implements Runnable{
        private Request request;
        public Handle(Request request){
            this.request=request;
        }
        public void run() {
            Router router=apis.get(request.getMsgId());
            router.handle(request);
        }
    }
    public void doMsgHandle(Request request) {
        request.getConn().getThreadPoolExecutor().execute(new Handle(request));
    }

    public void addRouter(int msgId, Router router) {
        apis.put(msgId,router);

    }

    public void startWorkerPool() {

    }
}
