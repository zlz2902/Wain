package com.horzits.business.domain;

import com.horzits.common.annotation.Excel;
import com.horzits.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监控设备信息
 *
 * @author horzits
 */
@ApiModel(description = "监控设备信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonDevice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备主键")
    private Long deviceId;

    @ApiModelProperty("站点代码")
    @Excel(name = "站点代码")
    private String siteCode;

    @ApiModelProperty("设备唯一标识")
    @Excel(name = "设备编码")
    private String deviceCode;

    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    @ApiModelProperty("品牌")
    @Excel(name = "品牌")
    private String brand;

    @ApiModelProperty("型号")
    @Excel(name = "型号")
    private String model;

    @ApiModelProperty("运行状况（0正常 1故障 2离线 3维护）")
    @Excel(name = "运行状况")
    private String runStatus;

    @ApiModelProperty("启用状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("删除标志（0存在 2删除）")
    private String delFlag;
}
