package com.horzits.business.controller.monitor;

import com.horzits.business.domain.MonRuleCount;
import com.horzits.business.service.monitor.IMonRuleCountService;
import com.horzits.common.annotation.Log;
import com.horzits.common.core.controller.BaseController;
import com.horzits.common.core.domain.AppRestResult;
import com.horzits.common.core.page.TableDataInfo;
import com.horzits.common.enums.BusinessType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报警次数累计表
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/monitor/ruleCount")
@Api(tags = "监控告警-报警次数累计表")
public class MonRuleCountController extends BaseController {

    @Autowired
    private IMonRuleCountService monRuleCountService;

    @ApiOperation(value = "列表", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:ruleCount:list')")
    @GetMapping("/list")
    public TableDataInfo list(MonRuleCount query) {
        startPage();
        List<MonRuleCount> list = monRuleCountService.selectMonRuleCountList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "按规则与设备查询累计行", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:ruleCount:query')")
    @GetMapping("/byRuleDevice")
    public AppRestResult byRuleDevice(@RequestParam Long ruleId, @RequestParam Long deviceId) {
        return AppRestResult.success(monRuleCountService.selectByRuleIdAndDeviceId(ruleId, deviceId));
    }

    @ApiOperation(value = "详情", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:ruleCount:query')")
    @GetMapping("/{countId}")
    public AppRestResult getInfo(@PathVariable Long countId) {
        return AppRestResult.success(monRuleCountService.selectMonRuleCountById(countId));
    }

    @ApiOperation(value = "新增", httpMethod = "POST", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:ruleCount:add')")
    @Log(title = "报警次数累计表", businessType = BusinessType.INSERT)
    @PostMapping
    public AppRestResult add(@RequestBody MonRuleCount row) {
        monRuleCountService.insertMonRuleCount(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "修改", httpMethod = "PUT", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:ruleCount:edit')")
    @Log(title = "报警次数累计表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AppRestResult edit(@RequestBody MonRuleCount row) {
        monRuleCountService.updateMonRuleCount(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "删除", httpMethod = "DELETE", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:ruleCount:remove')")
    @Log(title = "报警次数累计表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{countIds}")
    public AppRestResult remove(@PathVariable Long[] countIds) {
        monRuleCountService.deleteMonRuleCountByIds(countIds);
        return AppRestResult.success();
    }
}
