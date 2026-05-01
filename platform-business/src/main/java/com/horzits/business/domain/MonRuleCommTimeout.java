package com.horzits.business.domain;

import com.horzits.common.annotation.Excel;
import com.horzits.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通讯超时规则场景表
 *
 * @author ruoyi
 */
@ApiModel(description = "通讯超时规则场景表")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonRuleCommTimeout extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long timeoutId;

    @ApiModelProperty("关联规则总表")
    @Excel(name = "规则ID")
    private Long ruleId;

    @ApiModelProperty("外部端口/链路等唯一识别符")
    private String endpointCode;

    @ApiModelProperty("超时阈值（秒）")
    private Integer timeoutSec;

    @ApiModelProperty("告警级别")
    private String alarmLevel;
}
