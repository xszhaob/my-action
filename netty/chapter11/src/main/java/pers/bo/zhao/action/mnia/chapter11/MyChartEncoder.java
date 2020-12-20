package pers.bo.zhao.action.mnia.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyChartEncoder extends MessageToByteEncoder<ChartProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ChartProtocol msg, ByteBuf out) throws Exception {
        byte[] content = msg.getContent();
        int length = msg.getLength();

        out.writeInt(length);
        out.writeBytes(content);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
