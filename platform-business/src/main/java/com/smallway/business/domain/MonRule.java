package com.smallway.business.domain;

import com.smallway.common.annotation.Excel;
import com.smallway.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 告警规则 rules_table
 */
@ApiModel(description = "告警规则")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规则ID")
    private Long ruleId;

    @ApiModelProperty("规则名称")
    @Excel(name = "规则名称")
    private String ruleName;

    @ApiModelProperty("场景类型 1阈值 2工况 3超时")
    @Excel(name = "场景类型")
    private Integer sceneType;

    @ApiModelProperty("是否启用 0/1")
    private Integer isEnabled;

    @ApiModelProperty("告警级别")
    private Integer alarmLevel;

    @ApiModelProperty("连续异常次数")
    private Integer accumulateCount;
}
