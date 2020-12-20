package pers.bo.zhao.action.mnia.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

public class ByteBufAllocatorAction {

    public static void main(String[] args) {
        ByteBufAllocator allocator = new PooledByteBufAllocator();
        ByteBuf buffer1 = allocator.buffer(1024);
        int i = buffer1.hashCode();
        buffer1.release();
        ByteBuf buffer2 = allocator.buffer(2048);
        int i1 = buffer2.hashCode();
        buffer1.writeInt(5);
        System.out.println(buffer2.readInt());
        System.out.println(i == i1);
    }
}
