package com.smallway.business.service.monitor.impl;

import com.smallway.business.domain.DeviceRuntimeSnapshot;
import com.smallway.business.domain.MonAlarmRecord;
import com.smallway.business.domain.MonRule;
import com.smallway.business.domain.MonRuleCount;
import com.smallway.business.domain.MonRuleThreshold;
import com.smallway.business.mapper.monitor.MonDeviceMapper;
import com.smallway.business.mapper.monitor.MonRuleCountMapper;
import com.smallway.business.mapper.monitor.MonRuleMapper;
import com.smallway.business.mapper.monitor.MonRuleThresholdMapper;
import com.smallway.business.service.monitor.IMonAlarmRecordService;
import com.smallway.business.service.monitor.IThresholdRuleEngineService;
import com.smallway.common.utils.DateUtils;
import com.smallway.business.mapper.monitor.MonAlarmRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ???????? scene_type = 1 ?????????????????????????????????????????????????????
 */
@Service
public class ThresholdRuleEngineServiceImpl implements IThresholdRuleEngineService {

    private static final Logger log = LoggerFactory.getLogger(ThresholdRuleEngineServiceImpl.class);

    private static final int SCENE_THRESHOLD = 1;

    private final MonDeviceMapper monDeviceMapper;
    private final MonRuleMapper monRuleMapper;
    private final MonRuleThresholdMapper monRuleThresholdMapper;
    private final MonRuleCountMapper monRuleCountMapper;
    private final IMonAlarmRecordService monAlarmRecordService;
    private final MonAlarmRecordMapper monAlarmRecordMapper;

    /** ?????+????????????????????? THRESHOLD ?????0 ???????????? */
    @Value("${monitor.rule-engine.alarm-cooldown-seconds:0}")
    private int alarmCooldownSeconds;

    public ThresholdRuleEngineServiceImpl(MonDeviceMapper monDeviceMapper,
            MonRuleMapper monRuleMapper,
            MonRuleThresholdMapper monRuleThresholdMapper,
            MonRuleCountMapper monRuleCountMapper,
            IMonAlarmRecordService monAlarmRecordService,
            MonAlarmRecordMapper monAlarmRecordMapper) {
        this.monDeviceMapper = monDeviceMapper;
        this.monRuleMapper = monRuleMapper;
        this.monRuleThresholdMapper = monRuleThresholdMapper;
        this.monRuleCountMapper = monRuleCountMapper;
        this.monAlarmRecordService = monAlarmRecordService;
        this.monAlarmRecordMapper = monAlarmRecordMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void runEvaluationCycle() {
        List<DeviceRuntimeSnapshot> devices = monDeviceMapper.selectRuntimeSnapshots();
        if (devices.isEmpty()) {
            return;
        }

        MonRule ruleQuery = new MonRule();
        ruleQuery.setIsEnabled(1);
        ruleQuery.setSceneType(SCENE_THRESHOLD);
        List<MonRule> rules = monRuleMapper.selectMonRuleList(ruleQuery);
        if (rules.isEmpty()) {
            return;
        }

        Date now = DateUtils.getNowDate();
        int fires = 0;
        for (MonRule rule : rules) {
            MonRuleThreshold thQuery = new MonRuleThreshold();
            thQuery.setRuleId(rule.getRuleId());
            List<MonRuleThreshold> thresholds = monRuleThresholdMapper.selectMonRuleThresholdList(thQuery);
            if (thresholds.isEmpty()) {
                continue;
            }

            int need = rule.getAccumulateCount() == null || rule.getAccumulateCount() < 1
                    ? 1
                    : rule.getAccumulateCount();

            for (DeviceRuntimeSnapshot snap : devices) {
                Violation v = evaluateViolation(snap, thresholds);
                MonRuleCount existing = monRuleCountMapper.selectByRuleIdAndDeviceNo(rule.getRuleId(), snap.getDeviceNo());

                if (v.abnormal) {
                    int next = existing == null ? 1 : (existing.getCurrentCount() == null ? 1 : existing.getCurrentCount() + 1);
                    upsertCount(existing, rule.getRuleId(), snap.getDeviceNo(), next, now);

                    if (next >= need) {
                        boolean inserted = tryInsertAlarmWithCooldown(rule, snap, v, now);
                        if (inserted) {
                            fires++;
                        }
                        upsertCount(monRuleCountMapper.selectByRuleIdAndDeviceNo(rule.getRuleId(), snap.getDeviceNo()),
                                rule.getRuleId(), snap.getDeviceNo(), 0, now);
                        log.debug("rule engine reached threshold ruleId={} deviceNo={} inserted={} detail={}",
                                rule.getRuleId(), snap.getDeviceNo(), inserted, v.detail);
                    }
                } else {
                    if (existing != null && existing.getCurrentCount() != null && existing.getCurrentCount() > 0) {
                        MonRuleCount row = new MonRuleCount();
                        row.setCountId(existing.getCountId());
                        row.setCurrentCount(0);
                        row.setLastCheckTime(now);
                        monRuleCountMapper.updateMonRuleCount(row);
                    }
                }
            }
        }
        if (fires > 0) {
            log.info("threshold rule engine cycle: alarmsInserted={}", fires);
        }
    }

    private void upsertCount(MonRuleCount existing, Long ruleId, String deviceNo, int count, Date now) {
        if (existing == null) {
            MonRuleCount row = new MonRuleCount();
            row.setRuleId(ruleId);
            row.setDeviceNo(deviceNo);
            row.setCurrentCount(count);
            row.setLastCheckTime(now);
            monRuleCountMapper.insertMonRuleCount(row);
        } else {
            MonRuleCount row = new MonRuleCount();
            row.setCountId(existing.getCountId());
            row.setCurrentCount(count);
            row.setLastCheckTime(now);
            monRuleCountMapper.updateMonRuleCount(row);
        }
    }

    /**
     * @return true 若实际写入了一条告警；冷却期内跳过则 false（仍会由调用方将累计次数清零）
     */
    private boolean tryInsertAlarmWithCooldown(MonRule rule, DeviceRuntimeSnapshot snap, Violation v, Date now) {
        if (alarmCooldownSeconds > 0 && rule.getRuleId() != null) {
            Date since = new Date(now.getTime() - alarmCooldownSeconds * 1000L);
            int recent = monAlarmRecordMapper.countAlarmsSince(snap.getDeviceNo(), rule.getRuleId(), since);
            if (recent > 0) {
                return false;
            }
        }
        insertAlarm(rule, snap, v, now);
        return true;
    }

    private void insertAlarm(MonRule rule, DeviceRuntimeSnapshot snap, Violation v, Date now) {
        MonAlarmRecord alarm = new MonAlarmRecord();
        alarm.setDeviceNo(snap.getDeviceNo());
        alarm.setStationId(snap.getStationId());
        alarm.setRuleId(rule.getRuleId());
        alarm.setAlarmLevel(rule.getAlarmLevel());
        alarm.setAlarmType("THRESHOLD");
        alarm.setAlarmName(rule.getRuleName());
        alarm.setAlarmValue(v.value);
        alarm.setAlarmDetail(v.detail);
        alarm.setAlarmTime(now);
        alarm.setIsHandled(0);
        monAlarmRecordService.insertMonAlarmRecord(alarm);
    }

    private Violation evaluateViolation(DeviceRuntimeSnapshot snap, List<MonRuleThreshold> thresholds) {
        for (MonRuleThreshold t : thresholds) {
            Double value = metric(snap, t.getResourceType());
            if (value == null) {
                continue;
            }
            Double min = t.getMinValue();
            Double max = t.getMaxValue();
            boolean belowMin = min != null && value < min;
            boolean aboveMax = max != null && value > max;
            if (belowMin || aboveMax) {
                String metricCn = metricLabel(t.getResourceType());
                String lo = min == null ? "\u65e0\u4e0b\u9650" : String.format(Locale.US, "%.2f", min);
                String hi = max == null ? "\u65e0\u4e0a\u9650" : String.format(Locale.US, "%.2f", max);
                // ?????????? Unicode ????????????????????? GBK ???????????
                String detail = String.format(Locale.CHINA,
                        "%s\u5f53\u524d\u503c %.2f\uff0c\u8d85\u51fa\u9608\u503c\u8303\u56f4 [%s, %s]",
                        metricCn, value, lo, hi);
                return new Violation(true, detail, value);
            }
        }
        return Violation.ok();
    }

    private static Double metric(DeviceRuntimeSnapshot s, String resourceType) {
        if (resourceType == null) {
            return null;
        }
        String rt = resourceType.trim();
        if (rt.equalsIgnoreCase("Temperature")) {
            return s.getCurrentTemp();
        }
        if (rt.equalsIgnoreCase("Humidity")) {
            return s.getCurrentHumi();
        }
        if (rt.equalsIgnoreCase("Pressure")) {
            return s.getCurrentPres();
        }
        return null;
    }

    private static String metricLabel(String resourceType) {
        if (resourceType == null) {
            return "\u6307\u6807";
        }
        String rt = resourceType.trim();
        if (rt.equalsIgnoreCase("Temperature")) {
            return "\u6e29\u5ea6";
        }
        if (rt.equalsIgnoreCase("Humidity")) {
            return "\u6e7f\u5ea6";
        }
        if (rt.equalsIgnoreCase("Pressure")) {
            return "\u6c14\u538b";
        }
        return resourceType;
    }

    private static final class Violation {
        final boolean abnormal;
        final String detail;
        final Double value;

        private Violation(boolean abnormal, String detail, Double value) {
            this.abnormal = abnormal;
            this.detail = detail;
            this.value = value;
        }

        static Violation ok() {
            return new Violation(false, null, null);
        }
    }
}
