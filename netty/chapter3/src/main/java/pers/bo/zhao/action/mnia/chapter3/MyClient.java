package pers.bo.zhao.action.mnia.chapter3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class MyClient {
    private String host;

    private int port;

    public MyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new ChannelOutboundHandler4());
//                            ch.pipeline().addLast(new ChannelOutboundHandler1());
//                            ch.pipeline().addLast(new ChannelOutboundHandler2());
//                            ch.pipeline().addLast(new ChannelOutboundHandler3());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    System.out.println("成功");
                } else {
                    System.out.println("失败！");
                    future1.cause().printStackTrace();
                }
            });
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            MyClient client1 = new MyClient("localhost", 9091);
            try {
                client1.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        MyClient client2 = new MyClient("localhost", 9091);
        client2.start();
    }
}
