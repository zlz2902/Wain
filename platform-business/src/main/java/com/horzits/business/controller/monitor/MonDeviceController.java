package com.horzits.business.controller.monitor;

import com.horzits.business.domain.MonDevice;
import com.horzits.business.service.monitor.IMonDeviceService;
import com.horzits.common.annotation.Log;
import com.horzits.common.core.controller.BaseController;
import com.horzits.common.core.domain.AppRestResult;
import com.horzits.common.core.domain.model.LoginUser;
import com.horzits.common.core.page.TableDataInfo;
import com.horzits.common.enums.BusinessType;
import com.horzits.common.utils.SecurityUtils;
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
 * 监控设备信息（论文：设备管理模块）
 *
 * @author horzits
 */
@RestController
@RequestMapping("/business/monitor/device")
@Api(tags = "监控告警-设备管理")
public class MonDeviceController extends BaseController {

    @Autowired
    private IMonDeviceService monDeviceService;

    @ApiOperation(value = "查询监控设备列表", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(MonDevice query) {
        startPage();
        List<MonDevice> list = monDeviceService.selectMonDeviceList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "获取监控设备详情", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:device:query')")
    @GetMapping("/{deviceId}")
    public AppRestResult getInfo(@PathVariable Long deviceId) {
        return AppRestResult.success(monDeviceService.selectMonDeviceById(deviceId));
    }

    @ApiOperation(value = "新增监控设备", httpMethod = "POST", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:device:add')")
    @Log(title = "监控设备信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AppRestResult add(@RequestBody MonDevice row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setCreateBy(loginUser.getUser().getUserName());
        monDeviceService.insertMonDevice(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "修改监控设备", httpMethod = "PUT", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:device:edit')")
    @Log(title = "监控设备信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AppRestResult edit(@RequestBody MonDevice row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setUpdateBy(loginUser.getUser().getUserName());
        monDeviceService.updateMonDevice(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "删除监控设备", httpMethod = "DELETE", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:device:remove')")
    @Log(title = "监控设备信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AppRestResult remove(@PathVariable Long[] deviceIds) {
        monDeviceService.deleteMonDeviceByIds(deviceIds);
        return AppRestResult.success();
    }
}
