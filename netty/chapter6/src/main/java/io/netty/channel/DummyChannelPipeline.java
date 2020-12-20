package io.netty.channel;

import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DummyChannelPipeline extends DefaultChannelPipeline {
    public static final ChannelPipeline DUMMY_INSTANCE =
            new DummyChannelPipeline(new NioServerSocketChannel());

    protected DummyChannelPipeline(Channel channel) {
        super(channel);
    }
}
