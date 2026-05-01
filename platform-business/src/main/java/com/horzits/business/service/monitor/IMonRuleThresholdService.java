package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonRuleThreshold;

import java.util.List;

/**
 * 阈值范围规则场景表（论文 表4.3）
 *
 * @author horzits
 */
public interface IMonRuleThresholdService {

    MonRuleThreshold selectMonRuleThresholdById(Long thresholdId);

    List<MonRuleThreshold> selectMonRuleThresholdList(MonRuleThreshold query);

    int insertMonRuleThreshold(MonRuleThreshold row);

    int updateMonRuleThreshold(MonRuleThreshold row);

    int deleteMonRuleThresholdByIds(Long[] thresholdIds);
}
