package com.smallway.business.ingest.netty;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.smallway.business.domain.MonAlarmRecord;
import com.smallway.business.service.monitor.IMonAlarmRecordService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
@ChannelHandler.Sharable
@ConditionalOnProperty(prefix = "ingest.netty", name = "enabled", havingValue = "true")
public class AlarmLineInboundHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger log = LoggerFactory.getLogger(AlarmLineInboundHandler.class);

    private final IMonAlarmRecordService monAlarmRecordService;
    private final Executor ingestBusinessExecutor;

    public AlarmLineInboundHandler(IMonAlarmRecordService monAlarmRecordService,
            @Qualifier("ingestBusinessExecutor") Executor ingestBusinessExecutor) {
        this.monAlarmRecordService = monAlarmRecordService;
        this.ingestBusinessExecutor = ingestBusinessExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String line) {
        final String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        ingestBusinessExecutor.execute(() -> handleLine(trimmed));
    }

    private void handleLine(String trimmed) {
        try {
            MonAlarmRecord row = JSON.parseObject(trimmed, MonAlarmRecord.class);
            if (row == null || row.getDeviceNo() == null || row.getDeviceNo().trim().isEmpty()) {
                log.warn("ingest skip: missing deviceNo, payload={}", trimmed);
                return;
            }
            row.setDeviceNo(row.getDeviceNo().trim());
            monAlarmRecordService.insertMonAlarmRecord(row);
            log.debug("ingest alarm saved deviceNo={}", row.getDeviceNo());
        } catch (JSONException e) {
            log.warn("ingest JSON parse failed: {}", trimmed, e);
        } catch (Exception e) {
            log.error("ingest persist failed: {}", trimmed, e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("ingest channel error: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
