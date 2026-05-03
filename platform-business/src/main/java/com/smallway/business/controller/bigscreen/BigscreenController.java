package com.smallway.business.controller.bigscreen;

import com.smallway.business.service.bigscreen.IBigscreenService;
import com.smallway.common.core.domain.BigscreenResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 可视化大屏 API（契约见 Database_Design_Specification）
 */
@RestController
@RequestMapping("/bigscreen")
@Api(tags = "可视化大屏")
public class BigscreenController {

    @Autowired
    private IBigscreenService bigscreenService;

    @GetMapping("/countUserNum")
    @ApiOperation("big1 用户总览")
    public BigscreenResult countUserNum() {
        return bigscreenService.countUserNum();
    }

    @GetMapping("/countDeviceNum")
    @ApiOperation("big2 温湿压")
    public BigscreenResult countDeviceNum() {
        return bigscreenService.countDeviceNum();
    }

    @GetMapping("/sbtx")
    @ApiOperation("big3 设备提醒")
    public BigscreenResult sbtx(@RequestParam(value = "limitNum", defaultValue = "20") int limitNum) {
        return bigscreenService.sbtx(limitNum);
    }

    @GetMapping("/alarmNum")
    @ApiOperation("big4 报警次数趋势")
    public BigscreenResult alarmNum() {
        return bigscreenService.alarmNum();
    }

    @GetMapping("/ssyj")
    @ApiOperation("big5 实时预警")
    public BigscreenResult ssyj(@RequestParam(value = "limitNum", defaultValue = "50") int limitNum) {
        return bigscreenService.ssyj(limitNum);
    }

    @GetMapping("/topology")
    @ApiOperation("big6 拓扑图")
    public BigscreenResult topology(@RequestParam(value = "stationNo", required = false) String stationNo) {
        return bigscreenService.topology(stationNo);
    }

    @GetMapping("/ranking")
    @ApiOperation("big7 报警排名")
    public BigscreenResult ranking() {
        return bigscreenService.ranking();
    }

    @GetMapping("/centermap")
    @ApiOperation("big8 中间地图")
    public BigscreenResult centermap(@RequestParam(value = "regionCode", defaultValue = "china") String regionCode) {
        return bigscreenService.centermap(regionCode);
    }

    @GetMapping("/installationPlan")
    @ApiOperation("兼容旧版安装计划图表")
    public BigscreenResult installationPlan() {
        return bigscreenService.installationPlan();
    }
}
