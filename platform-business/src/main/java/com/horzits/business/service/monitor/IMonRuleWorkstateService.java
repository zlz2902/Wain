package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonRuleWorkstate;

import java.util.List;

/**
 * 工况状态报警表（论文 表4.4）
 *
 * @author horzits
 */
public interface IMonRuleWorkstateService {

    MonRuleWorkstate selectMonRuleWorkstateById(Long workstateId);

    List<MonRuleWorkstate> selectMonRuleWorkstateList(MonRuleWorkstate query);

    int insertMonRuleWorkstate(MonRuleWorkstate row);

    int updateMonRuleWorkstate(MonRuleWorkstate row);

    int deleteMonRuleWorkstateByIds(Long[] workstateIds);
}
