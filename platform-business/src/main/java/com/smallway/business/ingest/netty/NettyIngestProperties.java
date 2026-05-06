package com.smallway.business.ingest.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Netty �澯���Ľ��루���� UTF-8 �ı���ͨ��Ϊ���� JSON����
 * <p>
 * JSON ʾ����{@code {"deviceNo":"DEV001","alarmName":"�¶ȹ���","alarmValue":85.5,"alarmLevel":2,"alarmDetail":"..."}}
 * �ֶ����� {@link com.smallway.business.domain.MonAlarmRecord} һ�£�camelCase����
 */
@Data
@ConfigurationProperties(prefix = "ingest.netty")
public class NettyIngestProperties {

    /** �Ƿ����� Netty ������Ĭ�Ϲرգ����������󿪶˿ڣ� */
    private boolean enabled = false;

    /** ������ַ��0.0.0.0 ��ʾ�������� */
    private String bindAddress = "0.0.0.0";

    /** TCP �˿ڣ����� server.port ��ͬ */
    private int port = 9010;

    private int bossThreads = 1;

    /** &lt;=0 ʱʹ�� Netty Ĭ���߳��� */
    private int workerThreads = 0;

    /** ��������ֽڳ��ȣ�������ǰ�����ݣ� */
    private int maxFrameLength = 65536;

    private int businessCorePoolSize = 4;

    private int businessMaxPoolSize = 32;

    private int businessQueueCapacity = 1000;
}
