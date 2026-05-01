package com.horzits.business.domain;

import com.horzits.common.annotation.Excel;
import com.horzits.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 阈值范围规则场景表（论文 表4.3）
 *
 * @author horzits
 */
@ApiModel(description = "阈值范围规则场景表")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonRuleThreshold extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long thresholdId;

    @ApiModelProperty("关联规则总表")
    @Excel(name = "规则ID")
    private Long ruleId;

    @ApiModelProperty("监测指标编码")
    private String metricCode;

    @ApiModelProperty("监测指标名称")
    @Excel(name = "指标名称")
    private String metricName;

    @ApiModelProperty("阈值下限")
    private BigDecimal minValue;

    @ApiModelProperty("阈值上限")
    private BigDecimal maxValue;

    @ApiModelProperty("告警级别")
    private String alarmLevel;
}
