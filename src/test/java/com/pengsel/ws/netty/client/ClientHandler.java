package com.pengsel.ws.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext){
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks!", CharsetUtil.UTF_8));
    }

//    @Override
//    public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) {
//        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        System.out.println("Client received: " + ((ByteBuf) o).toString(CharsetUtil.UTF_8));
    }
}
