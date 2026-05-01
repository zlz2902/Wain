package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonAlarmRecord;

import java.util.List;

/**
 * 告警记录表（论文：报文解析持久化）
 *
 * @author horzits
 */
public interface IMonAlarmRecordService {

    MonAlarmRecord selectMonAlarmRecordById(Long alarmId);

    List<MonAlarmRecord> selectMonAlarmRecordList(MonAlarmRecord query);

    int insertMonAlarmRecord(MonAlarmRecord row);

    int updateHandle(MonAlarmRecord row);

    int deleteMonAlarmRecordByIds(Long[] alarmIds);
}
