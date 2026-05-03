package com.smallway.business.mapper.monitor;

import com.smallway.business.domain.MonRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 告警规则总表
 *
 * @author ruoyi
 */
@Mapper
public interface MonRuleMapper {

    MonRule selectMonRuleById(Long ruleId);

    List<MonRule> selectMonRuleList(MonRule monRule);

    int insertMonRule(MonRule monRule);

    int updateMonRule(MonRule monRule);

    int deleteMonRuleById(Long ruleId);

    int deleteMonRuleByIds(Long[] ruleIds);
}
