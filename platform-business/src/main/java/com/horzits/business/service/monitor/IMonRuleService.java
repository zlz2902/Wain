package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonRule;

import java.util.List;

/**
 * 告警规则总表（论文 表4.1）
 *
 * @author horzits
 */
public interface IMonRuleService {

    MonRule selectMonRuleById(Long ruleId);

    List<MonRule> selectMonRuleList(MonRule monRule);

    int insertMonRule(MonRule monRule);

    int updateMonRule(MonRule monRule);

    /**
     * 逻辑删除规则，并级联删除四类场景子表与累计表数据（论文 ER 子表）
     */
    int deleteMonRuleByIds(Long[] ruleIds);
}
