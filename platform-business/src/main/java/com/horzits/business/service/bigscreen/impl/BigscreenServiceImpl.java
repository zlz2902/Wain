package com.horzits.business.service.bigscreen.impl;

import com.horzits.business.mapper.bigscreen.BigscreenMapper;
import com.horzits.business.service.bigscreen.IBigscreenService;
import com.horzits.common.core.domain.BigscreenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BigscreenServiceImpl implements IBigscreenService {

    private static final DateTimeFormatter YM = DateTimeFormatter.ofPattern("yyyy-MM");

    @Autowired
    private BigscreenMapper bigscreenMapper;

    @Override
    public BigscreenResult countUserNum() {
        Map<String, Object> row = bigscreenMapper.selectUserOverview();
        if (row == null) {
            row = new HashMap<>();
        }
        long total = toLong(row.get("totalNum"));
        long online = toLong(row.get("onlineNum"));
        long lock = toLong(row.get("lockNum"));
        long offline = Math.max(0, total - online - lock);
        row.put("offlineNum", offline);
        return BigscreenResult.ok(row);
    }

    @Override
    public BigscreenResult countDeviceNum() {
        Map<String, Object> snap = bigscreenMapper.selectLatestGlobalMetricSnapshot();
        if (snap != null && snap.get("temperature") != null) {
            return BigscreenResult.ok(snap);
        }
        Map<String, Object> avg = bigscreenMapper.selectRuntimeAvgMetrics();
        if (avg == null) {
            avg = new HashMap<>();
        }
        roundMetric(avg);
        return BigscreenResult.ok(avg);
    }

    @Override
    public BigscreenResult sbtx(int limitNum) {
        int n = limitNum > 0 ? limitNum : 20;
        List<Map<String, Object>> list = bigscreenMapper.selectSbtxList(n);
        Map<String, Object> data = new HashMap<>(2);
        data.put("list", list != null ? list : new ArrayList<>());
        return BigscreenResult.ok(data);
    }

    @Override
    public BigscreenResult alarmNum() {
        List<Map<String, Object>> buckets = bigscreenMapper.selectAlarmMonthlyBuckets(6);
        Map<String, long[]> byYm = new HashMap<>();
        if (buckets != null) {
            for (Map<String, Object> b : buckets) {
                String ym = String.valueOf(b.get("ym"));
                long low = toLong(b.get("lowCnt"));
                long high = toLong(b.get("highCnt"));
                byYm.put(ym, new long[]{low, high});
            }
        }
        LocalDate now = LocalDate.now();
        List<String> dateList = new ArrayList<>(6);
        List<Integer> numList = new ArrayList<>(6);
        List<Integer> numList2 = new ArrayList<>(6);
        for (int i = 5; i >= 0; i--) {
            String ym = now.minusMonths(i).format(YM);
            dateList.add(ym);
            long[] v = byYm.getOrDefault(ym, new long[]{0, 0});
            numList.add((int) v[0]);
            numList2.add((int) v[1]);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("dateList", dateList);
        data.put("numList", numList);
        data.put("numList2", numList2);
        return BigscreenResult.ok(data);
    }

    @Override
    public BigscreenResult ssyj(int limitNum) {
        int n = limitNum > 0 ? limitNum : 50;
        List<Map<String, Object>> list = bigscreenMapper.selectSsyjList(n);
        Map<String, Object> data = new HashMap<>(2);
        data.put("list", list != null ? list : new ArrayList<>());
        return BigscreenResult.ok(data);
    }

    @Override
    public BigscreenResult topology(String stationNo) {
        String sn = stationNo != null && !stationNo.isEmpty() ? stationNo : "DH-01";
        Long sid = bigscreenMapper.selectStationIdByNo(sn);
        if (sid == null) {
            return BigscreenResult.ok(emptyTopology());
        }
        List<Map<String, Object>> nodes = bigscreenMapper.selectTopologyNodes(sn);
        List<Map<String, Object>> links = bigscreenMapper.selectTopologyLinks(sid);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("nodes", nodes != null ? nodes : new ArrayList<>());
        data.put("links", links != null ? links : new ArrayList<>());
        return BigscreenResult.ok(data);
    }

    @Override
    public BigscreenResult ranking() {
        List<Map<String, Object>> rows = bigscreenMapper.selectAlarmRankingByCity(8);
        List<Map<String, Object>> out = new ArrayList<>();
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("name", r.get("name"));
                item.put("value", toInt(r.get("value")));
                out.add(item);
            }
        }
        return BigscreenResult.ok(out);
    }

    @Override
    public BigscreenResult centermap(String regionCode) {
        String code = regionCode != null ? regionCode : "china";
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("regionCode", code);
        List<Map<String, Object>> list;
        if ("china".equalsIgnoreCase(code)) {
            list = bigscreenMapper.selectMapAggByProvince();
        } else {
            list = bigscreenMapper.selectMapAggByRegionChildren(code);
        }
        data.put("dataList", list != null ? list : new ArrayList<>());
        return BigscreenResult.ok(data);
    }

    @Override
    public BigscreenResult installationPlan() {
        List<Map<String, Object>> rows = bigscreenMapper.selectAlarmRankingByCity(50);
        List<String> category = new ArrayList<>();
        List<Integer> barData = new ArrayList<>();
        List<Integer> lineData = new ArrayList<>();
        List<String> rateData = new ArrayList<>();
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                category.add(String.valueOf(r.get("name")));
                int v = toInt(r.get("value"));
                barData.add(v);
                lineData.add((int) Math.round(v * 0.85));
                rateData.add(String.valueOf(Math.min(99, 60 + v % 40)));
            }
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("category", category);
        data.put("barData", barData);
        data.put("lineData", lineData);
        data.put("rateData", rateData);
        return BigscreenResult.ok(data);
    }

    private static Map<String, Object> emptyTopology() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("nodes", new ArrayList<>());
        data.put("links", new ArrayList<>());
        return data;
    }

    private static long toLong(Object o) {
        if (o == null) {
            return 0L;
        }
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(o));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private static int toInt(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(o));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void roundMetric(Map<String, Object> avg) {
        Object t = avg.get("temperature");
        Object h = avg.get("humidity");
        Object p = avg.get("pressure");
        if (t instanceof Number) {
            avg.put("temperature", Math.round(((Number) t).doubleValue() * 10.0) / 10.0);
        }
        if (h instanceof Number) {
            avg.put("humidity", Math.round(((Number) h).doubleValue()));
        }
        if (p instanceof Number) {
            avg.put("pressure", Math.round(((Number) p).doubleValue()));
        }
    }
}
