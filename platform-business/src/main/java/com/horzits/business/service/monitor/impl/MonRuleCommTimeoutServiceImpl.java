package com.horzits.business.service.monitor.impl;

import com.horzits.business.domain.MonRuleCommTimeout;
import com.horzits.business.mapper.monitor.MonRuleCommTimeoutMapper;
import com.horzits.business.service.monitor.IMonRuleCommTimeoutService;
import com.horzits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通讯超时规则场景表
 *
 * @author horzits
 */
@Service
public class MonRuleCommTimeoutServiceImpl implements IMonRuleCommTimeoutService {

    @Autowired
    private MonRuleCommTimeoutMapper monRuleCommTimeoutMapper;

    @Override
    public MonRuleCommTimeout selectMonRuleCommTimeoutById(Long timeoutId) {
        return monRuleCommTimeoutMapper.selectMonRuleCommTimeoutById(timeoutId);
    }

    @Override
    public List<MonRuleCommTimeout> selectMonRuleCommTimeoutList(MonRuleCommTimeout query) {
        return monRuleCommTimeoutMapper.selectMonRuleCommTimeoutList(query);
    }

    @Override
    public int insertMonRuleCommTimeout(MonRuleCommTimeout row) {
        row.setCreateTime(DateUtils.getNowDate());
        return monRuleCommTimeoutMapper.insertMonRuleCommTimeout(row);
    }

    @Override
    public int updateMonRuleCommTimeout(MonRuleCommTimeout row) {
        row.setUpdateTime(DateUtils.getNowDate());
        return monRuleCommTimeoutMapper.updateMonRuleCommTimeout(row);
    }

    @Override
    public int deleteMonRuleCommTimeoutByIds(Long[] timeoutIds) {
        return monRuleCommTimeoutMapper.deleteMonRuleCommTimeoutByIds(timeoutIds);
    }
}
