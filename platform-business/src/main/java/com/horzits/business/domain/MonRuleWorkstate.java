package com.horzits.business.domain;

import com.horzits.common.annotation.Excel;
import com.horzits.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 工况状态报警表（论文 表4.4）
 *
 * @author horzits
 */
@ApiModel(description = "工况状态报警表")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonRuleWorkstate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long workstateId;

    @ApiModelProperty("关联规则总表")
    @Excel(name = "规则ID")
    private Long ruleId;

    @ApiModelProperty("规则编码（与总表规则编号一致时可冗余）")
    private String ruleCode;

    @ApiModelProperty("设备检测参数标识")
    private String paramKey;

    @ApiModelProperty("正常工况阈值下限")
    private BigDecimal normalMin;

    @ApiModelProperty("正常工况阈值上限")
    private BigDecimal normalMax;

    @ApiModelProperty("告警级别")
    private String alarmLevel;
}
