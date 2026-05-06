package com.smallway.business.ingest.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@Order(100)
@ConditionalOnProperty(prefix = "ingest.netty", name = "enabled", havingValue = "true")
public class NettyAlarmServer implements ApplicationRunner, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(NettyAlarmServer.class);

    private final NettyIngestProperties properties;
    private final AlarmLineInboundHandler alarmLineInboundHandler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public NettyAlarmServer(NettyIngestProperties properties, AlarmLineInboundHandler alarmLineInboundHandler) {
        this.properties = properties;
        this.alarmLineInboundHandler = alarmLineInboundHandler;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bossGroup = new NioEventLoopGroup(properties.getBossThreads());
        int w = properties.getWorkerThreads();
        workerGroup = w <= 0 ? new NioEventLoopGroup() : new NioEventLoopGroup(w);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new AlarmIngestChannelInitializer(properties, alarmLineInboundHandler));

        InetSocketAddress address = new InetSocketAddress(properties.getBindAddress(), properties.getPort());
        ChannelFuture bindFuture = bootstrap.bind(address).sync();
        serverChannel = bindFuture.channel();
        log.info("Netty ingest listening {}:{} (line-delimited JSON -> rules_alarm_log)",
                properties.getBindAddress(), properties.getPort());
    }

    @Override
    public void destroy() {
        try {
            if (serverChannel != null) {
                serverChannel.close().syncUninterruptibly();
            }
        } finally {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
        }
        log.info("Netty ingest stopped");
    }
}
