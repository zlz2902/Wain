package com.horzits.business.controller.monitor;

import com.horzits.business.domain.MonRuleCommTimeout;
import com.horzits.business.service.monitor.IMonRuleCommTimeoutService;
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
 * 通讯超时规则场景表（论文 表4.5 通讯超时报警表）
 *
 * @author horzits
 */
@RestController
@RequestMapping("/business/monitor/ruleCommTimeout")
@Api(tags = "监控告警-通讯超时规则场景表")
public class MonRuleCommTimeoutController extends BaseController {

    @Autowired
    private IMonRuleCommTimeoutService monRuleCommTimeoutService;

    @ApiOperation(value = "列表", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:commTimeout:list')")
    @GetMapping("/list")
    public TableDataInfo list(MonRuleCommTimeout query) {
        startPage();
        List<MonRuleCommTimeout> list = monRuleCommTimeoutService.selectMonRuleCommTimeoutList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "详情", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:commTimeout:query')")
    @GetMapping("/{timeoutId}")
    public AppRestResult getInfo(@PathVariable Long timeoutId) {
        return AppRestResult.success(monRuleCommTimeoutService.selectMonRuleCommTimeoutById(timeoutId));
    }

    @ApiOperation(value = "新增", httpMethod = "POST", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:commTimeout:add')")
    @Log(title = "通讯超时规则场景表", businessType = BusinessType.INSERT)
    @PostMapping
    public AppRestResult add(@RequestBody MonRuleCommTimeout row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setCreateBy(loginUser.getUser().getUserName());
        monRuleCommTimeoutService.insertMonRuleCommTimeout(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "修改", httpMethod = "PUT", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:commTimeout:edit')")
    @Log(title = "通讯超时规则场景表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AppRestResult edit(@RequestBody MonRuleCommTimeout row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setUpdateBy(loginUser.getUser().getUserName());
        monRuleCommTimeoutService.updateMonRuleCommTimeout(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "删除", httpMethod = "DELETE", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:commTimeout:remove')")
    @Log(title = "通讯超时规则场景表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{timeoutIds}")
    public AppRestResult remove(@PathVariable Long[] timeoutIds) {
        monRuleCommTimeoutService.deleteMonRuleCommTimeoutByIds(timeoutIds);
        return AppRestResult.success();
    }
}
