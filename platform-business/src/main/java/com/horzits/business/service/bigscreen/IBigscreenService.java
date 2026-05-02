package com.horzits.business.service.bigscreen;

import com.horzits.common.core.domain.BigscreenResult;

/**
 * 大屏接口（返回 BigscreenResult）
 */
public interface IBigscreenService {

    BigscreenResult countUserNum();

    BigscreenResult countDeviceNum();

    BigscreenResult sbtx(int limitNum);

    BigscreenResult alarmNum();

    BigscreenResult ssyj(int limitNum);

    BigscreenResult topology(String stationNo);

    BigscreenResult ranking();

    BigscreenResult centermap(String regionCode);

    BigscreenResult installationPlan();
}
