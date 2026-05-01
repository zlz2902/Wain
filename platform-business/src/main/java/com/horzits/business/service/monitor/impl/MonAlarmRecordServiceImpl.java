package com.horzits.business.service.monitor.impl;

import com.horzits.business.domain.MonAlarmRecord;
import com.horzits.business.mapper.monitor.MonAlarmRecordMapper;
import com.horzits.business.service.monitor.IMonAlarmRecordService;
import com.horzits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警记录表
 *
 * @author horzits
 */
@Service
public class MonAlarmRecordServiceImpl implements IMonAlarmRecordService {

    @Autowired
    private MonAlarmRecordMapper monAlarmRecordMapper;

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
        if (row.getHandleStatus() == null) {
            row.setHandleStatus("0");
        }
        if (row.getAlarmTime() == null) {
            row.setAlarmTime(DateUtils.getNowDate());
        }
        row.setCreateTime(DateUtils.getNowDate());
        return monAlarmRecordMapper.insertMonAlarmRecord(row);
    }

    @Override
    public int updateHandle(MonAlarmRecord row) {
        if (row.getHandleTime() == null) {
            row.setHandleTime(DateUtils.getNowDate());
        }
        return monAlarmRecordMapper.updateMonAlarmRecordHandle(row);
    }

    @Override
    public int deleteMonAlarmRecordByIds(Long[] alarmIds) {
        return monAlarmRecordMapper.deleteMonAlarmRecordByIds(alarmIds);
    }
}
