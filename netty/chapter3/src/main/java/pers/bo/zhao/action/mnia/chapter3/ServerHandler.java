package pers.bo.zhao.action.mnia.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server handler 新连接客户端：" + ctx.channel().remoteAddress());
//        System.out.println(ctx.channel().eventLoop().inEventLoop());
//        ctx.channel().eventLoop().schedule(
//                (Runnable) () -> ctx.writeAndFlush(Unpooled.copiedBuffer("hello!".getBytes()))
//                , 5,
//                TimeUnit.SECONDS
//        );
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//        System.out.println(ctx.channel().eventLoop().inEventLoop());
        int length = msg.readableBytes();
        byte[] bytes = new byte[length];
        msg.readBytes(bytes);
        System.out.println("Server Channel 接收消息：" + new String(bytes));
//        ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.copiedBuffer("this is test msg from server".getBytes()));
//        channelFuture.addListener((ChannelFutureListener) future -> {
//            if (future.isSuccess()) {
//                System.out.println("服务器发送消息成功");
//            } else {
//                System.out.println("服务器发送消息失败");
//                future.cause().printStackTrace();
//            }
//        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
