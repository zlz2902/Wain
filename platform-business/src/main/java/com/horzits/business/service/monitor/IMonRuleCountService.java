package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonRuleCount;

import java.util.List;

/**
 * 报警次数累计表（论文 表4.2）
 *
 * @author horzits
 */
public interface IMonRuleCountService {

    MonRuleCount selectMonRuleCountById(Long countId);

    MonRuleCount selectByRuleIdAndDeviceId(Long ruleId, Long deviceId);

    List<MonRuleCount> selectMonRuleCountList(MonRuleCount query);

    int insertMonRuleCount(MonRuleCount row);

    int updateMonRuleCount(MonRuleCount row);

    int deleteMonRuleCountByIds(Long[] countIds);
}
