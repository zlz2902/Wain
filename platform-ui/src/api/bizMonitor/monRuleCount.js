import request from '@/utils/request'

export function listMonRuleCount(query) {
  return request({
    url: '/business/monitor/ruleCount/list',
    method: 'get',
    params: query
  })
}

export function getMonRuleCount(countId) {
  return request({
    url: '/business/monitor/ruleCount/' + countId,
    method: 'get'
  })
}

export function addMonRuleCount(data) {
  return request({
    url: '/business/monitor/ruleCount',
    method: 'post',
    data: data
  })
}

export function updateMonRuleCount(data) {
  return request({
    url: '/business/monitor/ruleCount',
    method: 'put',
    data: data
  })
}

export function delMonRuleCount(countIds) {
  return request({
    url: '/business/monitor/ruleCount/' + countIds,
    method: 'delete'
  })
}
