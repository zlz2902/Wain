package com.horzits.business.service.monitor.impl;

import com.horzits.business.domain.MonDevice;
import com.horzits.business.mapper.monitor.MonDeviceMapper;
import com.horzits.business.service.monitor.IMonDeviceService;
import com.horzits.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备信息 device_info
 */
@Service
public class MonDeviceServiceImpl implements IMonDeviceService {

    @Autowired
    private MonDeviceMapper monDeviceMapper;

    @Override
    public MonDevice selectMonDeviceById(Long deviceId) {
        return monDeviceMapper.selectMonDeviceById(deviceId);
    }

    @Override
    public List<MonDevice> selectMonDeviceList(MonDevice monDevice) {
        return monDeviceMapper.selectMonDeviceList(monDevice);
    }

    @Override
    public int insertMonDevice(MonDevice monDevice) {
        if (monDevice.getIsEnabled() == null) {
            monDevice.setIsEnabled(1);
        }
        monDevice.setCreateTime(DateUtils.getNowDate());
        monDevice.setUpdateTime(monDevice.getCreateTime());
        return monDeviceMapper.insertMonDevice(monDevice);
    }

    @Override
    public int updateMonDevice(MonDevice monDevice) {
        monDevice.setUpdateTime(DateUtils.getNowDate());
        return monDeviceMapper.updateMonDevice(monDevice);
    }

    @Override
    public int deleteMonDeviceByIds(Long[] deviceIds) {
        int rows = 0;
        for (Long deviceId : deviceIds) {
            rows += monDeviceMapper.deleteMonDeviceById(deviceId);
        }
        return rows;
    }
}
