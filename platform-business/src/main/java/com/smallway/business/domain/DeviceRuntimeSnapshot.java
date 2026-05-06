package com.smallway.business.domain;

import lombok.Data;

/**
 * 规则引擎评估用的设备实时指标（device_info + device_runtime_status）
 */
@Data
public class DeviceRuntimeSnapshot {

    private String deviceNo;

    private Long stationId;

    private Double currentTemp;

    private Double currentHumi;

    private Double currentPres;
}
