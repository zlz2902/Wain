import request from '@/utils/request'

export function listMonDevice(query) {
  return request({
    url: '/business/monitor/device/list',
    method: 'get',
    params: query
  })
}

export function getMonDevice(deviceId) {
  return request({
    url: '/business/monitor/device/' + deviceId,
    method: 'get'
  })
}

export function addMonDevice(data) {
  return request({
    url: '/business/monitor/device',
    method: 'post',
    data: data
  })
}

export function updateMonDevice(data) {
  return request({
    url: '/business/monitor/device',
    method: 'put',
    data: data
  })
}

export function delMonDevice(deviceIds) {
  return request({
    url: '/business/monitor/device/' + deviceIds,
    method: 'delete'
  })
}
