package pers.bo.zhao.action.mnia.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接成功！");
        ctx.writeAndFlush(Unpooled.copiedBuffer("this is".getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int length = msg.readableBytes();
        byte[] bytes = new byte[length];
        msg.readBytes(bytes);
        System.out.println("客户端接收消息：" + new String(bytes));
        ByteBuf buf = Unpooled.copiedBuffer("this is".getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("失败----");
        cause.printStackTrace();
        ctx.close();
    }
}
