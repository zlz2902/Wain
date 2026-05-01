package com.horzits.business.controller.monitor;

import com.horzits.business.domain.MonRuleThreshold;
import com.horzits.business.service.monitor.IMonRuleThresholdService;
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
 * 阈值范围规则场景表（论文 表4.3）
 *
 * @author horzits
 */
@RestController
@RequestMapping("/business/monitor/ruleThreshold")
@Api(tags = "监控告警-阈值范围规则场景表")
public class MonRuleThresholdController extends BaseController {

    @Autowired
    private IMonRuleThresholdService monRuleThresholdService;

    @ApiOperation(value = "列表", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:threshold:list')")
    @GetMapping("/list")
    public TableDataInfo list(MonRuleThreshold query) {
        startPage();
        List<MonRuleThreshold> list = monRuleThresholdService.selectMonRuleThresholdList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "详情", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:threshold:query')")
    @GetMapping("/{thresholdId}")
    public AppRestResult getInfo(@PathVariable Long thresholdId) {
        return AppRestResult.success(monRuleThresholdService.selectMonRuleThresholdById(thresholdId));
    }

    @ApiOperation(value = "新增", httpMethod = "POST", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:threshold:add')")
    @Log(title = "阈值范围规则场景表", businessType = BusinessType.INSERT)
    @PostMapping
    public AppRestResult add(@RequestBody MonRuleThreshold row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setCreateBy(loginUser.getUser().getUserName());
        monRuleThresholdService.insertMonRuleThreshold(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "修改", httpMethod = "PUT", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:threshold:edit')")
    @Log(title = "阈值范围规则场景表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AppRestResult edit(@RequestBody MonRuleThreshold row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setUpdateBy(loginUser.getUser().getUserName());
        monRuleThresholdService.updateMonRuleThreshold(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "删除", httpMethod = "DELETE", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:threshold:remove')")
    @Log(title = "阈值范围规则场景表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{thresholdIds}")
    public AppRestResult remove(@PathVariable Long[] thresholdIds) {
        monRuleThresholdService.deleteMonRuleThresholdByIds(thresholdIds);
        return AppRestResult.success();
    }
}
