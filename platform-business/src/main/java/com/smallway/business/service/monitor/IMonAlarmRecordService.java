package com.smallway.business.service.monitor;

import com.smallway.business.domain.MonAlarmRecord;

import java.util.List;

/**
 * 告警记录表
 *
 * @author ruoyi
 */
public interface IMonAlarmRecordService {

    MonAlarmRecord selectMonAlarmRecordById(Long alarmId);

    List<MonAlarmRecord> selectMonAlarmRecordList(MonAlarmRecord query);

    int insertMonAlarmRecord(MonAlarmRecord row);

    int updateHandle(MonAlarmRecord row);

    int deleteMonAlarmRecordByIds(Long[] alarmIds);
}
