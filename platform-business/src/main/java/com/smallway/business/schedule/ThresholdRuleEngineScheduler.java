package com.smallway.business.schedule;

import com.smallway.business.service.monitor.IThresholdRuleEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "monitor.rule-engine", name = "enabled", havingValue = "true")
public class ThresholdRuleEngineScheduler {

    private static final Logger log = LoggerFactory.getLogger(ThresholdRuleEngineScheduler.class);

    private final IThresholdRuleEngineService thresholdRuleEngineService;

    public ThresholdRuleEngineScheduler(IThresholdRuleEngineService thresholdRuleEngineService) {
        this.thresholdRuleEngineService = thresholdRuleEngineService;
    }

    @Scheduled(fixedDelayString = "${monitor.rule-engine.interval-ms:15000}")
    public void runCycle() {
        try {
            thresholdRuleEngineService.runEvaluationCycle();
        } catch (Exception e) {
            log.error("阈值规则引擎调度失败", e);
        }
    }
}
