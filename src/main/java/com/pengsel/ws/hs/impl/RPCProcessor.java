package com.pengsel.ws.hs.impl;

import com.alibaba.fastjson.JSON;
import com.pengsel.ws.hs.Processor;
import com.pengsel.ws.rpc.JsonRpc;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * RPCProcessor将HTTP请求转换成RPC请求，与TCPServer进行交互
 */
public class RPCProcessor implements Processor {

    private SelectionKey selectionKey;

    private HTTPResponse response;

    private HTTPRequest request;

    public RPCProcessor(SelectionKey key, HTTPRequest request, HTTPResponse response){

        this.selectionKey=key;
        this.request=request;
        this.response=response;
    }

    public void process() throws IOException {
        //建立和TCPServer的通道，向其中写入请求，然后向Selector注册 read 事件。
        SocketChannel socketChannel= (SocketChannel) selectionKey.channel();

        List<Object> params=new ArrayList<Object>();
        String s=JSON.toJSONString(request.getParameterMap());
        params.add(s);
        Object result = JsonRpc.call(request.getRequestURI(),params,socketChannel.socket());
        response.sendHeaders();
        //FIXME 此处要注意写入的contentLength字段。
        if (result != null) {
            response.write(result.toString().getBytes());
        }

        //唤醒selector，让它重新关注该通道的OP_WRITE信号
        selectionKey.interestOps (selectionKey.interestOps() | SelectionKey.OP_WRITE);
        selectionKey.selector().wakeup();
        selectionKey.attach(null);

    }


    public void run() {
        try {
            process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
