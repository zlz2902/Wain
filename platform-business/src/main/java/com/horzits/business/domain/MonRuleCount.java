package com.horzits.business.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报警次数累计表
 *
 * @author ruoyi
 */
@ApiModel(description = "报警次数累计表")
@Data
public class MonRuleCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long countId;

    @ApiModelProperty("关联规则总表")
    private Long ruleId;

    @ApiModelProperty("关联设备")
    private Long deviceId;

    @ApiModelProperty("当前累计异常次数 counts")
    private Integer counts;

    @ApiModelProperty("最近一次告警时间戳")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastAlarmTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
