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
 * 设备信息 device_info
 */
@ApiModel(description = "监控设备信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonDevice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long deviceId;

    @ApiModelProperty("设备唯一编号 device_no")
    @Excel(name = "设备编号")
    private String deviceNo;

    @ApiModelProperty("网关号 gatewayno")
    @Excel(name = "网关号")
    private String gatewayno;

    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    @ApiModelProperty("设备类型")
    @Excel(name = "设备类型")
    private String deviceType;

    @ApiModelProperty("终端号 terminalno")
    @Excel(name = "终端号")
    private String terminalno;

    @ApiModelProperty("站点ID")
    private Long stationId;

    @ApiModelProperty("负责人")
    @Excel(name = "负责人")
    private String owner;

    @ApiModelProperty("安装日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date installDate;

    @ApiModelProperty("是否启用 0/1")
    private Integer isEnabled;
}
