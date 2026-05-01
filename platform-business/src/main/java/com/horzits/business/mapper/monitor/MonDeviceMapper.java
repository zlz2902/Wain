package com.horzits.business.mapper.monitor;

import com.horzits.business.domain.MonDevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 监控设备信息（论文：设备管理模块）
 *
 * @author horzits
 */
@Mapper
public interface MonDeviceMapper {

    MonDevice selectMonDeviceById(Long deviceId);

    List<MonDevice> selectMonDeviceList(MonDevice monDevice);

    int insertMonDevice(MonDevice monDevice);

    int updateMonDevice(MonDevice monDevice);

    int deleteMonDeviceById(Long deviceId);

    int deleteMonDeviceByIds(Long[] deviceIds);
}
