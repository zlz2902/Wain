package com.horzits.business.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警日志 rules_alarm_log
 */
@ApiModel(description = "告警日志")
@Data
public class MonAlarmRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("告警ID")
    private Long alarmId;

    @ApiModelProperty("设备编号")
    private String deviceNo;

    @ApiModelProperty("站点ID")
    private Long stationId;

    @ApiModelProperty("规则ID")
    private Long ruleId;

    @ApiModelProperty("告警级别")
    private Integer alarmLevel;

    @ApiModelProperty("告警类型")
    private String alarmType;

    @ApiModelProperty("告警名称")
    private String alarmName;

    @ApiModelProperty("告警值")
    private Double alarmValue;

    @ApiModelProperty("详情")
    private String alarmDetail;

    @ApiModelProperty("相位")
    private String phase;

    @ApiModelProperty("设备运行摘要")
    private String sbInfo;

    @ApiModelProperty("告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;

    @ApiModelProperty("是否已处理 0/1")
    private Integer isHandled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private Date beginAlarmTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private Date endAlarmTime;
}
