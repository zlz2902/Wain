package com.horzits.business.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horzits.common.annotation.Excel;
import com.horzits.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 告警规则总表
 *
 * @author ruoyi
 */
@ApiModel(description = "告警规则总表")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规则主键")
    private Long ruleId;

    @ApiModelProperty("站点代码")
    @Excel(name = "站点代码")
    private String siteCode;

    @ApiModelProperty("关联设备ID")
    @Excel(name = "设备ID")
    private Long deviceId;

    @ApiModelProperty("规则编号")
    @Excel(name = "规则编号")
    private String ruleCode;

    @ApiModelProperty("规则名称")
    @Excel(name = "规则名称")
    private String ruleName;

    @ApiModelProperty("规则说明")
    private String ruleDesc;

    @ApiModelProperty("场景类型（1阈值范围 2次数累计 3工况状态 4通讯超时）")
    @Excel(name = "场景类型")
    private String sceneType;

    @ApiModelProperty("是否启用（0否 1是）")
    private String enabled;

    @ApiModelProperty("生效时间起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectBegin;

    @ApiModelProperty("生效时间止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectEnd;

    @ApiModelProperty("累计异常次数上限")
    private Integer maxAccumCount;

    @ApiModelProperty("是否累计型 iscount（0非累计 1累计）")
    private String isCount;

    @ApiModelProperty("各告警级别连续触发最大限制（JSON或约定格式）")
    private String maxSerialPerLevel;

    @ApiModelProperty("删除标志")
    private String delFlag;
}
