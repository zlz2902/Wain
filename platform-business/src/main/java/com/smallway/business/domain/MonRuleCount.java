package com.smallway.business.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则累计 alarm_count_table
 */
@ApiModel(description = "规则异常累计")
@Data
public class MonRuleCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long countId;

    @ApiModelProperty("设备编号")
    private String deviceNo;

    @ApiModelProperty("规则ID")
    private Long ruleId;

    @ApiModelProperty("当前连续次数")
    private Integer currentCount;

    @ApiModelProperty("最后检查时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastCheckTime;
}
