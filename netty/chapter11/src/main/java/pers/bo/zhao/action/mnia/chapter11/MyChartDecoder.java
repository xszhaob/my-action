package pers.bo.zhao.action.mnia.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyChartDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(in.writerIndex());
        System.out.println(in.readerIndex());
        System.out.println(in.readableBytes());
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);

        ChartProtocol chart = new ChartProtocol();
        chart.setLength(length);
        chart.setContent(content);

        out.add(chart);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
