package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonDevice;

import java.util.List;

/**
 * 监控设备信息
 *
 * @author ruoyi
 */
public interface IMonDeviceService {

    MonDevice selectMonDeviceById(Long deviceId);

    List<MonDevice> selectMonDeviceList(MonDevice monDevice);

    int insertMonDevice(MonDevice monDevice);

    int updateMonDevice(MonDevice monDevice);

    int deleteMonDeviceByIds(Long[] deviceIds);
}
