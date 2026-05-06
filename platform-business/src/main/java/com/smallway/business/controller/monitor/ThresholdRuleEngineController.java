package com.smallway.business.controller.monitor;

import com.smallway.business.service.monitor.IThresholdRuleEngineService;
import com.smallway.common.annotation.Log;
import com.smallway.common.core.controller.BaseController;
import com.smallway.common.core.domain.AppRestResult;
import com.smallway.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ?????????????????????????????????????? monitor.rule-engine ???????
 */
@RestController
@RequestMapping("/business/monitor/ruleEngine")
@Api(tags = "???ù”-????????")
public class ThresholdRuleEngineController extends BaseController {

    @Autowired
    private IThresholdRuleEngineService thresholdRuleEngineService;

    @ApiOperation("?????????????????????")
    @Log(title = "????????", businessType = BusinessType.OTHER)
    @PreAuthorize("@ss.hasPermi('monitor:rule:list')")
    @PostMapping("/evaluateOnce")
    public AppRestResult evaluateOnce() {
        thresholdRuleEngineService.runEvaluationCycle();
        return AppRestResult.success();
    }
}
