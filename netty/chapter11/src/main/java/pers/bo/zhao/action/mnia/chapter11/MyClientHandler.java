package pers.bo.zhao.action.mnia.chapter11;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class MyClientHandler extends SimpleChannelInboundHandler<ChartProtocol> {


    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接：" + ctx.channel().remoteAddress());
        System.out.println("新的客户端连接：" + ctx.channel().localAddress());
        for (int i = 0; i < 10; i++) {
            String hello = "hello from client " + i;
            byte[] bytes = hello.getBytes(StandardCharsets.UTF_8);

            ChartProtocol chartProtocol = new ChartProtocol();
            chartProtocol.setLength(bytes.length);
            chartProtocol.setContent(bytes);
            ctx.writeAndFlush(chartProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChartProtocol msg) throws Exception {
        System.out.println("从服务端收到消息：");
        System.out.println("消息长度：" + msg.getLength());
        System.out.println("消息内容：" + msg.getContentStr());
        System.out.println("共收到的消息数量：" + (++count));
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("完成该批次数据读取");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
