package com.horzits.business.service.monitor;

import com.horzits.business.domain.MonDevice;

import java.util.List;

/**
 * 监控设备信息（论文：设备管理模块）
 *
 * @author horzits
 */
public interface IMonDeviceService {

    MonDevice selectMonDeviceById(Long deviceId);

    List<MonDevice> selectMonDeviceList(MonDevice monDevice);

    int insertMonDevice(MonDevice monDevice);

    int updateMonDevice(MonDevice monDevice);

    int deleteMonDeviceByIds(Long[] deviceIds);
}
