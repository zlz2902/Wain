package com.horzits.business.service.monitor.impl;

import com.horzits.business.domain.MonRuleWorkstate;
import com.horzits.business.mapper.monitor.MonRuleWorkstateMapper;
import com.horzits.business.service.monitor.IMonRuleWorkstateService;
import com.horzits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工况状态报警表
 *
 * @author horzits
 */
@Service
public class MonRuleWorkstateServiceImpl implements IMonRuleWorkstateService {

    @Autowired
    private MonRuleWorkstateMapper monRuleWorkstateMapper;

    @Override
    public MonRuleWorkstate selectMonRuleWorkstateById(Long workstateId) {
        return monRuleWorkstateMapper.selectMonRuleWorkstateById(workstateId);
    }

    @Override
    public List<MonRuleWorkstate> selectMonRuleWorkstateList(MonRuleWorkstate query) {
        return monRuleWorkstateMapper.selectMonRuleWorkstateList(query);
    }

    @Override
    public int insertMonRuleWorkstate(MonRuleWorkstate row) {
        row.setCreateTime(DateUtils.getNowDate());
        return monRuleWorkstateMapper.insertMonRuleWorkstate(row);
    }

    @Override
    public int updateMonRuleWorkstate(MonRuleWorkstate row) {
        row.setUpdateTime(DateUtils.getNowDate());
        return monRuleWorkstateMapper.updateMonRuleWorkstate(row);
    }

    @Override
    public int deleteMonRuleWorkstateByIds(Long[] workstateIds) {
        return monRuleWorkstateMapper.deleteMonRuleWorkstateByIds(workstateIds);
    }
}
