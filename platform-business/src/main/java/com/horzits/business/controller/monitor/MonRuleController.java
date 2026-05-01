package com.horzits.business.controller.monitor;

import com.horzits.business.domain.MonRule;
import com.horzits.business.service.monitor.IMonRuleService;
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
 * 告警规则总表（论文 表4.1 规则总表）
 *
 * @author horzits
 */
@RestController
@RequestMapping("/business/monitor/rule")
@Api(tags = "监控告警-规则总表")
public class MonRuleController extends BaseController {

    @Autowired
    private IMonRuleService monRuleService;

    @ApiOperation(value = "查询规则总表列表", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(MonRule query) {
        startPage();
        List<MonRule> list = monRuleService.selectMonRuleList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "获取规则详情", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:rule:query')")
    @GetMapping("/{ruleId}")
    public AppRestResult getInfo(@PathVariable Long ruleId) {
        return AppRestResult.success(monRuleService.selectMonRuleById(ruleId));
    }

    @ApiOperation(value = "新增规则", httpMethod = "POST", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:rule:add')")
    @Log(title = "告警规则总表", businessType = BusinessType.INSERT)
    @PostMapping
    public AppRestResult add(@RequestBody MonRule row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setCreateBy(loginUser.getUser().getUserName());
        monRuleService.insertMonRule(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "修改规则", httpMethod = "PUT", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:rule:edit')")
    @Log(title = "告警规则总表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AppRestResult edit(@RequestBody MonRule row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setUpdateBy(loginUser.getUser().getUserName());
        monRuleService.updateMonRule(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "删除规则（级联子表）", httpMethod = "DELETE", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:rule:remove')")
    @Log(title = "告警规则总表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ruleIds}")
    public AppRestResult remove(@PathVariable Long[] ruleIds) {
        monRuleService.deleteMonRuleByIds(ruleIds);
        return AppRestResult.success();
    }
}
