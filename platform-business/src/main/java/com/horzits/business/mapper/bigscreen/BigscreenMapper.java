package com.horzits.business.mapper.bigscreen;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 可视化大屏数据查询
 */
public interface BigscreenMapper {

    Map<String, Object> selectUserOverview();

    Map<String, Object> selectLatestGlobalMetricSnapshot();

    Map<String, Object> selectRuntimeAvgMetrics();

    List<Map<String, Object>> selectSbtxList(@Param("limitNum") int limitNum);

    List<Map<String, Object>> selectAlarmMonthlyBuckets(@Param("months") int months);

    List<Map<String, Object>> selectSsyjList(@Param("limitNum") int limitNum);

    List<Map<String, Object>> selectTopologyNodes(@Param("stationNo") String stationNo);

    List<Map<String, Object>> selectTopologyLinks(@Param("stationId") Long stationId);

    Long selectStationIdByNo(@Param("stationNo") String stationNo);

    List<Map<String, Object>> selectAlarmRankingByCity(@Param("limitNum") int limitNum);

    List<Map<String, Object>> selectMapAggByProvince();

    List<Map<String, Object>> selectMapAggByRegionChildren(@Param("regionCode") String regionCode);
}
