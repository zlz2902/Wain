package com.smallway.business.mapper.monitor;

import com.smallway.business.domain.MonAlarmRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
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

    /**
     * 冷却期内是否已有同类告警（设备 + 规则），用于抑制短时间刷屏
     */
    int countAlarmsSince(@Param("deviceNo") String deviceNo, @Param("ruleId") Long ruleId, @Param("since") Date since);

    int updateMonAlarmRecordHandle(MonAlarmRecord row);

    int deleteMonAlarmRecordByIds(Long[] alarmIds);
}
