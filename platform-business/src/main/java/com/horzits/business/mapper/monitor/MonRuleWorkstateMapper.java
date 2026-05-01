package com.horzits.business.mapper.monitor;

import com.horzits.business.domain.MonRuleWorkstate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 工况状态报警表
 *
 * @author ruoyi
 */
@Mapper
public interface MonRuleWorkstateMapper {

    MonRuleWorkstate selectMonRuleWorkstateById(Long workstateId);

    List<MonRuleWorkstate> selectMonRuleWorkstateList(MonRuleWorkstate query);

    int insertMonRuleWorkstate(MonRuleWorkstate row);

    int updateMonRuleWorkstate(MonRuleWorkstate row);

    int deleteMonRuleWorkstateById(Long workstateId);

    int deleteMonRuleWorkstateByIds(Long[] workstateIds);

    int deleteMonRuleWorkstateByRuleId(Long ruleId);
}
