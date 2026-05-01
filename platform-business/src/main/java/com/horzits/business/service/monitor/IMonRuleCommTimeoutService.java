package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonRuleCommTimeout;

import java.util.List;

/**
 * 通讯超时规则场景表
 *
 * @author ruoyi
 */
public interface IMonRuleCommTimeoutService {

    MonRuleCommTimeout selectMonRuleCommTimeoutById(Long timeoutId);

    List<MonRuleCommTimeout> selectMonRuleCommTimeoutList(MonRuleCommTimeout query);

    int insertMonRuleCommTimeout(MonRuleCommTimeout row);

    int updateMonRuleCommTimeout(MonRuleCommTimeout row);

    int deleteMonRuleCommTimeoutByIds(Long[] timeoutIds);
}
