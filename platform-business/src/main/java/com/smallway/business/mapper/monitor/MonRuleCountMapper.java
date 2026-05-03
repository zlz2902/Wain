package com.smallway.business.mapper.monitor;

import com.smallway.business.domain.MonRuleCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ?????????????????? ??4.2??
 *
 * @author horzits
 */
@Mapper
public interface MonRuleCountMapper {

    MonRuleCount selectMonRuleCountById(Long countId);

    List<MonRuleCount> selectMonRuleCountList(MonRuleCount query);

    MonRuleCount selectByRuleIdAndDeviceNo(@Param("ruleId") Long ruleId, @Param("deviceNo") String deviceNo);

    int insertMonRuleCount(MonRuleCount row);

    int updateMonRuleCount(MonRuleCount row);

    int deleteMonRuleCountById(Long countId);

    int deleteMonRuleCountByIds(Long[] countIds);

    int deleteMonRuleCountByRuleId(Long ruleId);
}
