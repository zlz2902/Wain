package com.smallway.business.controller.monitor;

import com.smallway.business.domain.MonAlarmRecord;
import com.smallway.business.service.monitor.IMonAlarmRecordService;
import com.smallway.common.annotation.Log;
import com.smallway.common.core.controller.BaseController;
import com.smallway.common.core.domain.AppRestResult;
import com.smallway.common.core.page.TableDataInfo;
import com.smallway.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 告警记录表
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/monitor/alarmRecord")
@Api(tags = "监控告警-告警记录表")
public class MonAlarmRecordController extends BaseController {

    @Autowired
    private IMonAlarmRecordService monAlarmRecordService;

    @ApiOperation(value = "列表", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(MonAlarmRecord query) {
        startPage();
        List<MonAlarmRecord> list = monAlarmRecordService.selectMonAlarmRecordList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "详情", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:alarm:query')")
    @GetMapping("/{alarmId}")
    public AppRestResult getInfo(@PathVariable Long alarmId) {
        return AppRestResult.success(monAlarmRecordService.selectMonAlarmRecordById(alarmId));
    }

    @ApiOperation(value = "新增告警记录", httpMethod = "POST", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:alarm:add')")
    @Log(title = "告警记录表", businessType = BusinessType.INSERT)
    @PostMapping
    public AppRestResult add(@RequestBody MonAlarmRecord row) {
        monAlarmRecordService.insertMonAlarmRecord(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "处理告警", httpMethod = "PUT", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:alarm:edit')")
    @Log(title = "告警记录表", businessType = BusinessType.UPDATE)
    @PutMapping("/handle")
    public AppRestResult handle(@RequestBody MonAlarmRecord row) {
        monAlarmRecordService.updateHandle(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "删除", httpMethod = "DELETE", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:alarm:remove')")
    @Log(title = "告警记录表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{alarmIds}")
    public AppRestResult remove(@PathVariable Long[] alarmIds) {
        monAlarmRecordService.deleteMonAlarmRecordByIds(alarmIds);
        return AppRestResult.success();
    }
}
