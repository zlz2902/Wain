package com.smallway.business.mapper.monitor;

import com.smallway.business.domain.MonRuleThreshold;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 阈值范围规则场景表
 *
 * @author ruoyi
 */
@Mapper
public interface MonRuleThresholdMapper {

    MonRuleThreshold selectMonRuleThresholdById(Long thresholdId);

    List<MonRuleThreshold> selectMonRuleThresholdList(MonRuleThreshold query);

    int insertMonRuleThreshold(MonRuleThreshold row);

    int updateMonRuleThreshold(MonRuleThreshold row);

    int deleteMonRuleThresholdById(Long thresholdId);

    int deleteMonRuleThresholdByIds(Long[] thresholdIds);

    int deleteMonRuleThresholdByRuleId(Long ruleId);
}
