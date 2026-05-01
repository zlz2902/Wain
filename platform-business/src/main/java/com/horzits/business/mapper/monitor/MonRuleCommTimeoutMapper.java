package com.horzits.business.mapper.monitor;

import com.horzits.business.domain.MonRuleCommTimeout;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 通讯超时规则场景表（论文 表4.5）
 *
 * @author horzits
 */
@Mapper
public interface MonRuleCommTimeoutMapper {

    MonRuleCommTimeout selectMonRuleCommTimeoutById(Long timeoutId);

    List<MonRuleCommTimeout> selectMonRuleCommTimeoutList(MonRuleCommTimeout query);

    int insertMonRuleCommTimeout(MonRuleCommTimeout row);

    int updateMonRuleCommTimeout(MonRuleCommTimeout row);

    int deleteMonRuleCommTimeoutById(Long timeoutId);

    int deleteMonRuleCommTimeoutByIds(Long[] timeoutIds);

    int deleteMonRuleCommTimeoutByRuleId(Long ruleId);
}
