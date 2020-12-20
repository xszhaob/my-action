package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

public class DummyChannelHandlerContext extends AbstractChannelHandlerContext {


    public static final ChannelHandlerContext DUMMY_INSTANCE = new DummyChannelHandlerContext(
            null,
            null,
            "default",
            true,
            true);

    public DummyChannelHandlerContext(DefaultChannelPipeline pipeline,
                                      EventExecutor executor,
                                      String name, boolean inbound, boolean outbound) {
        super(pipeline, executor, name, inbound, outbound);
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }
}
