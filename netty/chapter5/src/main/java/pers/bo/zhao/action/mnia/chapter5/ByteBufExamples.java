package pers.bo.zhao.action.mnia.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DummyChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufExamples {

    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DummyChannelHandlerContext.DUMMY_INSTANCE;

    /**
     * 支持数组（Backing array）
     */
    public static void heapBuffer() {
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        // 检查是否有一个支撑数组（backing array）
        if (heapBuf.hasArray()) {
            // 获取数组的引用
            byte[] array = heapBuf.array();
            // 计算第一个要读取的字节的偏移量
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            // 可读字节数
            int length = heapBuf.readableBytes();
            // 处理数据
            handleArray(array, offset, length);
        }
    }


    public static void directBuffer() {
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        // 如果没有支撑数组
        if (!directBuf.hasArray()) {
            // 获取可读字节数
            int length = directBuf.readableBytes();

            // 分配新数组来保存字节数据
            byte[] array = new byte[length];
            // 将字节复制到数组中
            directBuf.getBytes(directBuf.readerIndex(), array);
            // 处理数据
            handleArray(array, 0, length);
        }
    }

    public static void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
        // Use an array to hold the message parts
        ByteBuffer[] message = new ByteBuffer[]{header, body};

        // Create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 =
                ByteBuffer.allocate(header.remaining() + body.remaining());
        // 把header中的数据复制到message2中
        message2.put(header);
        // 把body中的数据复制到message2中
        message2.put(body);
        // 回到起点
        message2.flip();
    }


    public void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE; // can be backing or direct
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;   // can be backing or direct
        messageBuf.addComponents(headerBuf, bodyBuf);

        messageBuf.removeComponent(0);
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }
    }


    public void byteBufCompositeArray() {
        CompositeByteBuf comBuf = Unpooled.compositeBuffer();

        int length = comBuf.readableBytes();
        byte[] array = new byte[length];
        comBuf.getBytes(comBuf.readerIndex(), array);
        handleArray(array, 0, length);
    }


    public static void byteBufSlice() {
        Charset utf8 = Charset.forName("utf8");
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        ByteBuf slice = byteBuf.slice(0, 15);
        System.out.println(slice.toString(utf8));
        byteBuf.setByte(0, 'J');
        System.out.println(slice.toString(utf8));
        assert byteBuf.getByte(0) == slice.getByte(0);
    }



    public static void byteBufCopy() {
        Charset utf8 = Charset.forName("utf8");
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        ByteBuf copy = byteBuf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        byteBuf.setByte(0, 'J');
        System.out.println(copy.toString(utf8));
        assert byteBuf.getByte(0) != copy.getByte(0);
    }

    public void obtainingByteBufAllocatorReference() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        ByteBufAllocator alloc = channel.alloc();

        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        ByteBufAllocator alloc1 = ctx.alloc();
    }


    public static void referenceCounting() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBufAllocator allocator = channel.alloc();

        ByteBuf byteBuf = allocator.directBuffer();
        assert byteBuf.refCnt() == 1;
    }

    private static void handleArray(byte[] array, int offset, int len) {
    }


    public static void main(String[] args) {
//        byteBufSlice();
//        byteBufCopy();
        referenceCounting();
    }
}
