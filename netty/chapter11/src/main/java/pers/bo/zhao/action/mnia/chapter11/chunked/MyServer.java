package pers.bo.zhao.action.mnia.chapter11.chunked;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.net.InetSocketAddress;

public class MyServer {

    private final File file;

    private final int port;

    public MyServer(int port, String filePath) {
        this.port = port;
        this.file = new File(filePath);
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChunkedWriteHandlerInitializer(file));
            ChannelFuture future = server.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new MyServer(9092, "C:\\Users\\azhao\\Desktop\\test.txt").start();
    }
}
