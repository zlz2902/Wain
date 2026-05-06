package com.smallway.business.ingest.netty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ConditionalOnProperty(prefix = "ingest.netty", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(NettyIngestProperties.class)
public class NettyIngestConfiguration {

    @Bean(name = "ingestBusinessExecutor")
    public Executor ingestBusinessExecutor(NettyIngestProperties p) {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setThreadNamePrefix("ingest-netty-");
        ex.setCorePoolSize(p.getBusinessCorePoolSize());
        ex.setMaxPoolSize(p.getBusinessMaxPoolSize());
        ex.setQueueCapacity(p.getBusinessQueueCapacity());
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        ex.initialize();
        return ex;
    }
}
