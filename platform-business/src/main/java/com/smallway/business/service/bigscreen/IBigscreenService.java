package com.smallway.business.service.bigscreen;

import com.smallway.common.core.domain.BigscreenResult;

/**
 * �����ӿڣ����� BigscreenResult��
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
