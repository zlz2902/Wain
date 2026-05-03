package com.smallway.business.service.monitor.impl;

import com.smallway.business.domain.MonRuleThreshold;
import com.smallway.business.mapper.monitor.MonRuleThresholdMapper;
import com.smallway.business.service.monitor.IMonRuleThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 阈值范围规则场景表
 *
 * @author horzits
 */
@Service
public class MonRuleThresholdServiceImpl implements IMonRuleThresholdService {

    @Autowired
    private MonRuleThresholdMapper monRuleThresholdMapper;

    @Override
    public MonRuleThreshold selectMonRuleThresholdById(Long thresholdId) {
        return monRuleThresholdMapper.selectMonRuleThresholdById(thresholdId);
    }

    @Override
    public List<MonRuleThreshold> selectMonRuleThresholdList(MonRuleThreshold query) {
        return monRuleThresholdMapper.selectMonRuleThresholdList(query);
    }

    @Override
    public int insertMonRuleThreshold(MonRuleThreshold row) {
        return monRuleThresholdMapper.insertMonRuleThreshold(row);
    }

    @Override
    public int updateMonRuleThreshold(MonRuleThreshold row) {
        return monRuleThresholdMapper.updateMonRuleThreshold(row);
    }

    @Override
    public int deleteMonRuleThresholdByIds(Long[] thresholdIds) {
        return monRuleThresholdMapper.deleteMonRuleThresholdByIds(thresholdIds);
    }
}
