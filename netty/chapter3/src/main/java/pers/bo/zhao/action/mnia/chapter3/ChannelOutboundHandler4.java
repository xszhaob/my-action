package pers.bo.zhao.action.mnia.chapter3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class ChannelOutboundHandler4 extends ChannelOutboundHandlerAdapter {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exception in ChannelOutboundHandler4");
        cause.printStackTrace();
        ctx.close();
    }
}
