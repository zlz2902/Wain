package com.horzits.business.mapper.monitor;

import com.horzits.business.domain.MonRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 告警规则总表（论文 表4.1）
 *
 * @author horzits
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
