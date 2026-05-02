package com.horzits.business.domain;

import com.horzits.common.annotation.Excel;
import com.horzits.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 阈值规则明细 threshold_rule_table
 */
@ApiModel(description = "阈值规则明细")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonRuleThreshold extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long thresholdId;

    @ApiModelProperty("规则ID")
    private Long ruleId;

    @ApiModelProperty("指标 Temperature/Humidity/Pressure")
    @Excel(name = "指标")
    private String resourceType;

    @ApiModelProperty("下限")
    private Double minValue;

    @ApiModelProperty("上限")
    private Double maxValue;
}
