import request from '@/utils/request'

export function listMonRule(query) {
  return request({
    url: '/business/monitor/rule/list',
    method: 'get',
    params: query
  })
}

export function getMonRule(ruleId) {
  return request({
    url: '/business/monitor/rule/' + ruleId,
    method: 'get'
  })
}

export function addMonRule(data) {
  return request({
    url: '/business/monitor/rule',
    method: 'post',
    data: data
  })
}

export function updateMonRule(data) {
  return request({
    url: '/business/monitor/rule',
    method: 'put',
    data: data
  })
}

export function delMonRule(ruleIds) {
  return request({
    url: '/business/monitor/rule/' + ruleIds,
    method: 'delete'
  })
}
