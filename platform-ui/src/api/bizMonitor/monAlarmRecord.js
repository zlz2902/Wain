import request from '@/utils/request'

export function listMonAlarmRecord(query) {
  return request({
    url: '/business/monitor/alarmRecord/list',
    method: 'get',
    params: query
  })
}

export function getMonAlarmRecord(alarmId) {
  return request({
    url: '/business/monitor/alarmRecord/' + alarmId,
    method: 'get'
  })
}

export function addMonAlarmRecord(data) {
  return request({
    url: '/business/monitor/alarmRecord',
    method: 'post',
    data: data
  })
}

/** ´¦ÖÃ£º¸üÐÂ is_handled */
export function handleMonAlarmRecord(data) {
  return request({
    url: '/business/monitor/alarmRecord/handle',
    method: 'put',
    data: data
  })
}

export function delMonAlarmRecord(alarmIds) {
  return request({
    url: '/business/monitor/alarmRecord/' + alarmIds,
    method: 'delete'
  })
}
