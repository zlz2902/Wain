package com.horzits.business.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警记录表
 *
 * @author ruoyi
 */
@ApiModel(description = "告警记录表")
@Data
public class MonAlarmRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("告警主键")
    private Long alarmId;

    @ApiModelProperty("触发规则ID")
    private Long ruleId;

    @ApiModelProperty("关联设备ID")
    private Long deviceId;

    @ApiModelProperty("场景类型")
    private String sceneType;

    @ApiModelProperty("异常类型")
    private String alarmType;

    @ApiModelProperty("告警级别")
    private String alarmLevel;

    @ApiModelProperty("告警标题")
    private String alarmTitle;

    @ApiModelProperty("告警详情")
    private String alarmContent;

    @ApiModelProperty("触发时指标快照")
    private String metricSnapshot;

    @ApiModelProperty("告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;

    @ApiModelProperty("处理状态（0未处理 1处理中 2已关闭）")
    private String handleStatus;

    @ApiModelProperty("处理说明")
    private String handleRemark;

    @ApiModelProperty("处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private Date beginAlarmTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private Date endAlarmTime;
}
