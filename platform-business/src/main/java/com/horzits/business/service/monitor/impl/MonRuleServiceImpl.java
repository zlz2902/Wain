package com.horzits.business.service.monitor.impl;

import com.horzits.business.domain.MonRule;
import com.horzits.business.mapper.monitor.MonRuleCommTimeoutMapper;
import com.horzits.business.mapper.monitor.MonRuleCountMapper;
import com.horzits.business.mapper.monitor.MonRuleMapper;
import com.horzits.business.mapper.monitor.MonRuleThresholdMapper;
import com.horzits.business.mapper.monitor.MonRuleWorkstateMapper;
import com.horzits.business.service.monitor.IMonRuleService;
import com.horzits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 告警规则总表
 *
 * @author horzits
 */
@Service
public class MonRuleServiceImpl implements IMonRuleService {

    @Autowired
    private MonRuleMapper monRuleMapper;
    @Autowired
    private MonRuleThresholdMapper monRuleThresholdMapper;
    @Autowired
    private MonRuleWorkstateMapper monRuleWorkstateMapper;
    @Autowired
    private MonRuleCommTimeoutMapper monRuleCommTimeoutMapper;
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
        if (monRule.getDelFlag() == null) {
            monRule.setDelFlag("0");
        }
        if (monRule.getEnabled() == null) {
            monRule.setEnabled("1");
        }
        if (monRule.getIsCount() == null) {
            monRule.setIsCount("0");
        }
        monRule.setCreateTime(DateUtils.getNowDate());
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
            monRuleWorkstateMapper.deleteMonRuleWorkstateByRuleId(ruleId);
            monRuleCommTimeoutMapper.deleteMonRuleCommTimeoutByRuleId(ruleId);
            monRuleCountMapper.deleteMonRuleCountByRuleId(ruleId);
        }
        return monRuleMapper.deleteMonRuleByIds(ruleIds);
    }
}
