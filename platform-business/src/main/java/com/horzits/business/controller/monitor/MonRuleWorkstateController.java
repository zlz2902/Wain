package com.horzits.business.controller.monitor;

import com.horzits.business.domain.MonRuleWorkstate;
import com.horzits.business.service.monitor.IMonRuleWorkstateService;
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
 * 工况状态报警表（论文 表4.4）
 *
 * @author horzits
 */
@RestController
@RequestMapping("/business/monitor/ruleWorkstate")
@Api(tags = "监控告警-工况状态报警表")
public class MonRuleWorkstateController extends BaseController {

    @Autowired
    private IMonRuleWorkstateService monRuleWorkstateService;

    @ApiOperation(value = "列表", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:workstate:list')")
    @GetMapping("/list")
    public TableDataInfo list(MonRuleWorkstate query) {
        startPage();
        List<MonRuleWorkstate> list = monRuleWorkstateService.selectMonRuleWorkstateList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "详情", httpMethod = "GET", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:workstate:query')")
    @GetMapping("/{workstateId}")
    public AppRestResult getInfo(@PathVariable Long workstateId) {
        return AppRestResult.success(monRuleWorkstateService.selectMonRuleWorkstateById(workstateId));
    }

    @ApiOperation(value = "新增", httpMethod = "POST", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:workstate:add')")
    @Log(title = "工况状态报警表", businessType = BusinessType.INSERT)
    @PostMapping
    public AppRestResult add(@RequestBody MonRuleWorkstate row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setCreateBy(loginUser.getUser().getUserName());
        monRuleWorkstateService.insertMonRuleWorkstate(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "修改", httpMethod = "PUT", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:workstate:edit')")
    @Log(title = "工况状态报警表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AppRestResult edit(@RequestBody MonRuleWorkstate row) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        row.setUpdateBy(loginUser.getUser().getUserName());
        monRuleWorkstateService.updateMonRuleWorkstate(row);
        return AppRestResult.success();
    }

    @ApiOperation(value = "删除", httpMethod = "DELETE", response = AppRestResult.class)
    @PreAuthorize("@ss.hasPermi('monitor:workstate:remove')")
    @Log(title = "工况状态报警表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{workstateIds}")
    public AppRestResult remove(@PathVariable Long[] workstateIds) {
        monRuleWorkstateService.deleteMonRuleWorkstateByIds(workstateIds);
        return AppRestResult.success();
    }
}
