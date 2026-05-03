package com.smallway.business.service.monitor.impl;

import com.smallway.business.domain.MonRule;
import com.smallway.business.mapper.monitor.MonRuleCountMapper;
import com.smallway.business.mapper.monitor.MonRuleMapper;
import com.smallway.business.mapper.monitor.MonRuleThresholdMapper;
import com.smallway.business.service.monitor.IMonRuleService;
import com.smallway.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 告警规则 rules_table
 */
@Service
public class MonRuleServiceImpl implements IMonRuleService {

    @Autowired
    private MonRuleMapper monRuleMapper;
    @Autowired
    private MonRuleThresholdMapper monRuleThresholdMapper;
    @Autowired
    private MonRuleCountMapper monRuleCountMapper;

    @Override
    public MonRule selectMonRuleById(Long ruleId) {
        return monRuleMapper.selectMonRuleById(ruleId);
    }

    @Override
    public List<MonRule> selectMonRuleList(MonRule monRule) {
        return monRuleMapper.selectMonRuleList(monRule);
    }

    @Override
    public int insertMonRule(MonRule monRule) {
        if (monRule.getIsEnabled() == null) {
            monRule.setIsEnabled(1);
        }
        if (monRule.getAlarmLevel() == null) {
            monRule.setAlarmLevel(1);
        }
        if (monRule.getAccumulateCount() == null) {
            monRule.setAccumulateCount(1);
        }
        if (monRule.getSceneType() == null) {
            monRule.setSceneType(1);
        }
        monRule.setCreateTime(DateUtils.getNowDate());
        monRule.setUpdateTime(monRule.getCreateTime());
        return monRuleMapper.insertMonRule(monRule);
    }

    @Override
    public int updateMonRule(MonRule monRule) {
        monRule.setUpdateTime(DateUtils.getNowDate());
        return monRuleMapper.updateMonRule(monRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMonRuleByIds(Long[] ruleIds) {
        for (Long ruleId : ruleIds) {
            monRuleThresholdMapper.deleteMonRuleThresholdByRuleId(ruleId);
            monRuleCountMapper.deleteMonRuleCountByRuleId(ruleId);
        }
        return monRuleMapper.deleteMonRuleByIds(ruleIds);
    }
}
