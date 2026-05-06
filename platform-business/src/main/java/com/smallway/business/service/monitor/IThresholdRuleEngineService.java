package com.smallway.business.service.monitor;

/**
 * 阈值类规则引擎：根据 {@link com.smallway.business.domain.DeviceRuntimeSnapshot} 与阈值规则
 * 动态维护 {@code alarm_count_table}，达到连续次数后写入 {@code rules_alarm_log}。
 */
public interface IThresholdRuleEngineService {

    /** 执行一轮全量评估（供定时任务或手工触发） */
    void runEvaluationCycle();
}
