package pers.bo.zhao.action.mnia.chapter11;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ChartProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接：" + ctx.channel().localAddress());
        System.out.println("新的客户端连接：" + ctx.channel().remoteAddress());

        ChannelPipeline pipeline = ctx.channel().pipeline();
        pipeline.forEach(System.out::println);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChartProtocol msg) throws Exception {
        System.out.println("接收到客户端的消息：");
        System.out.println("消息长度：" + msg.getLength());
        System.out.println("消息内容：" + msg.getContentStr());
        System.out.println("已收到消息数：" + (++count));

        String s = UUID.randomUUID().toString();
        s += "," + msg.getContentStr().substring(msg.getContentStr().length() - 1);
        byte[] response = s.getBytes(StandardCharsets.UTF_8);
        int length = response.length;
        ChartProtocol chart = new ChartProtocol();
        chart.setLength(length);
        chart.setContent(response);
        ctx.write(chart);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("完成该批次数据读取");
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端：" + ctx.channel().remoteAddress());
    }
}
