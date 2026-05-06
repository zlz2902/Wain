package com.smallway.business.ingest.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

public class AlarmIngestChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyIngestProperties properties;
    private final AlarmLineInboundHandler alarmHandler;

    public AlarmIngestChannelInitializer(NettyIngestProperties properties, AlarmLineInboundHandler alarmHandler) {
        this.properties = properties;
        this.alarmHandler = alarmHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new LineBasedFrameDecoder(properties.getMaxFrameLength(), true, true));
        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
        ch.pipeline().addLast(alarmHandler);
    }
}
