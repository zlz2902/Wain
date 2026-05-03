package com.smallway.business.mapper.monitor;

import com.smallway.business.domain.MonAlarmRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 告警记录表
 *
 * @author horzits
 */
@Mapper
public interface MonAlarmRecordMapper {

    MonAlarmRecord selectMonAlarmRecordById(Long alarmId);

    List<MonAlarmRecord> selectMonAlarmRecordList(MonAlarmRecord query);

    int insertMonAlarmRecord(MonAlarmRecord row);

    int updateMonAlarmRecordHandle(MonAlarmRecord row);

    int deleteMonAlarmRecordByIds(Long[] alarmIds);
}
