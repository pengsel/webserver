package com.pengsel.ws.hs.impl;

import com.pengsel.ws.hs.Connecter;
import com.pengsel.ws.hs.Dispatcher;
import com.pengsel.ws.hs.Processor;
import io.netty.util.internal.ConcurrentSet;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPConnector implements Connecter {
    private static Logger logger = Logger.getLogger(HTTPConnector.class);

    boolean stopped;
    private String scheme = "http";
    private Selector selector;
    private Dispatcher dispatcher;
    private ExecutorService executor= Executors.newFixedThreadPool(20);
    private ConcurrentSet<SocketChannel> httpChannels=new ConcurrentSet<SocketChannel>();
    private ConcurrentSet<SocketChannel> tcpChannels=new ConcurrentSet<SocketChannel>();

    public String getScheme() {
        return scheme;
    }


    public void run() {
        int port = 8080;
        try {

            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"),port));
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (IOException e) {
            logger.error("Init server socket channel FAILED",e);
            System.exit(1);
        }
        while (!stopped) {
            // Accept the next incoming connection from the Selector

            try {
                int n=selector.select();
                if (n==0){
                    continue;
                }
            }
            catch (IOException e) {
                logger.error("Exception happens when select",e);
                continue;
            }

            Iterator it = selector.selectedKeys().iterator();

            while (it.hasNext()){

                SelectionKey key=(SelectionKey) it.next();

                if (key.isAcceptable()){

                    ServerSocketChannel server=(ServerSocketChannel) key.channel();
                    try {
                        SocketChannel socketChannel=server.accept();
                        httpChannels.add(socketChannel);
                        SelectionKey key1 = socketChannel.register(selector,SelectionKey.OP_READ);
                        key1.attach(new HTTPProcessor(key1,dispatcher));

                        logger.info("Register OP_READ to selector");
                    } catch (IOException e) {
                        logger.error("Exception when accept",e);
                        continue;
                    }
                }

                if (key.isReadable()){

                    //免得重复调用，线程完后会自动复原
                    key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                    Processor processor=(Processor) key.attachment();
                    executor.submit(processor);
                }

                if (key.isWritable()){
                    //免得重复调用，线程完后会自动复原
                    key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                    Processor processor=(Processor) key.attachment();
                    executor.submit(processor);
                }
            }

        }
    }

    public void start() {
        try {
            selector=Selector.open();
        } catch (IOException e) {
            logger.error("Failed to open selector",e);
        }
        dispatcher=new HTTPDispatcher();
        dispatcher.init();
        dispatcher.configure(HTTPDispatcher.DispatchStratege.POLLING);
        Thread thread = new Thread(this);
        thread.start();
    }
}
