package com.horzits.business.mapper.monitor;

import com.horzits.business.domain.MonDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 监控设备信息
 *
 * @author ruoyi
 */
@Mapper
public interface MonDeviceMapper {

    MonDevice selectMonDeviceById(Long deviceId);

    Long selectStationIdByDeviceNo(@Param("deviceNo") String deviceNo);

    List<MonDevice> selectMonDeviceList(MonDevice monDevice);

    int insertMonDevice(MonDevice monDevice);

    int updateMonDevice(MonDevice monDevice);

    int deleteMonDeviceById(Long deviceId);

    int deleteMonDeviceByIds(Long[] deviceIds);
}
