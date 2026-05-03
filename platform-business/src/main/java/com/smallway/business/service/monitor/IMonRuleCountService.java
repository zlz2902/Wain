package com.smallway.business.service.monitor;

import com.smallway.business.domain.MonRuleCount;

import java.util.List;

/**
 * 报警次数累计表
 *
 * @author ruoyi
 */
public interface IMonRuleCountService {

    MonRuleCount selectMonRuleCountById(Long countId);

    MonRuleCount selectByRuleIdAndDeviceNo(Long ruleId, String deviceNo);

    List<MonRuleCount> selectMonRuleCountList(MonRuleCount query);

    int insertMonRuleCount(MonRuleCount row);

    int updateMonRuleCount(MonRuleCount row);

    int deleteMonRuleCountByIds(Long[] countIds);
}
