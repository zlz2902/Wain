package com.smallway.business.service.monitor.impl;

import com.smallway.business.domain.MonAlarmRecord;
import com.smallway.business.mapper.monitor.MonAlarmRecordMapper;
import com.smallway.business.mapper.monitor.MonDeviceMapper;
import com.smallway.business.service.monitor.IMonAlarmRecordService;
import com.smallway.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警日志 rules_alarm_log
 */
@Service
public class MonAlarmRecordServiceImpl implements IMonAlarmRecordService {

    @Autowired
    private MonAlarmRecordMapper monAlarmRecordMapper;

    @Autowired
    private MonDeviceMapper monDeviceMapper;

    @Override
    public MonAlarmRecord selectMonAlarmRecordById(Long alarmId) {
        return monAlarmRecordMapper.selectMonAlarmRecordById(alarmId);
    }

    @Override
    public List<MonAlarmRecord> selectMonAlarmRecordList(MonAlarmRecord query) {
        return monAlarmRecordMapper.selectMonAlarmRecordList(query);
    }

    @Override
    public int insertMonAlarmRecord(MonAlarmRecord row) {
        if (row.getIsHandled() == null) {
            row.setIsHandled(0);
        }
        if (row.getAlarmTime() == null) {
            row.setAlarmTime(DateUtils.getNowDate());
        }
        if (row.getStationId() == null && row.getDeviceNo() != null) {
            Long sid = monDeviceMapper.selectStationIdByDeviceNo(row.getDeviceNo());
            row.setStationId(sid);
        }
        return monAlarmRecordMapper.insertMonAlarmRecord(row);
    }

    @Override
    public int updateHandle(MonAlarmRecord row) {
        return monAlarmRecordMapper.updateMonAlarmRecordHandle(row);
    }

    @Override
    public int deleteMonAlarmRecordByIds(Long[] alarmIds) {
        return monAlarmRecordMapper.deleteMonAlarmRecordByIds(alarmIds);
    }
}
