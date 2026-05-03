import request from '@/utils/request'

export function listMonRuleThreshold(query) {
  return request({
    url: '/business/monitor/ruleThreshold/list',
    method: 'get',
    params: query
  })
}

export function getMonRuleThreshold(thresholdId) {
  return request({
    url: '/business/monitor/ruleThreshold/' + thresholdId,
    method: 'get'
  })
}

export function addMonRuleThreshold(data) {
  return request({
    url: '/business/monitor/ruleThreshold',
    method: 'post',
    data: data
  })
}

export function updateMonRuleThreshold(data) {
  return request({
    url: '/business/monitor/ruleThreshold',
    method: 'put',
    data: data
  })
}

export function delMonRuleThreshold(thresholdIds) {
  return request({
    url: '/business/monitor/ruleThreshold/' + thresholdIds,
    method: 'delete'
  })
}
