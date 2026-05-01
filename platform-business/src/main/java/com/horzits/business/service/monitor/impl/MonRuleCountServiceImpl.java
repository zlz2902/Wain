package com.horzits.business.service.monitor.impl;

import com.horzits.business.domain.MonRuleCount;
import com.horzits.business.mapper.monitor.MonRuleCountMapper;
import com.horzits.business.service.monitor.IMonRuleCountService;
import com.horzits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报警次数累计表
 *
 * @author horzits
 */
@Service
public class MonRuleCountServiceImpl implements IMonRuleCountService {

    @Autowired
    private MonRuleCountMapper monRuleCountMapper;

    @Override
    public MonRuleCount selectMonRuleCountById(Long countId) {
        return monRuleCountMapper.selectMonRuleCountById(countId);
    }

    @Override
    public MonRuleCount selectByRuleIdAndDeviceId(Long ruleId, Long deviceId) {
        return monRuleCountMapper.selectByRuleIdAndDeviceId(ruleId, deviceId);
    }

    @Override
    public List<MonRuleCount> selectMonRuleCountList(MonRuleCount query) {
        return monRuleCountMapper.selectMonRuleCountList(query);
    }

    @Override
    public int insertMonRuleCount(MonRuleCount row) {
        if (row.getCounts() == null) {
            row.setCounts(0);
        }
        row.setCreateTime(DateUtils.getNowDate());
        row.setUpdateTime(row.getCreateTime());
        return monRuleCountMapper.insertMonRuleCount(row);
    }

    @Override
    public int updateMonRuleCount(MonRuleCount row) {
        row.setUpdateTime(DateUtils.getNowDate());
        return monRuleCountMapper.updateMonRuleCount(row);
    }

    @Override
    public int deleteMonRuleCountByIds(Long[] countIds) {
        int n = 0;
        for (Long id : countIds) {
            n += monRuleCountMapper.deleteMonRuleCountById(id);
        }
        return n;
    }
}
