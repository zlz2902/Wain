import request from '@/utils/request'

// 查询白名单列表
export function listWhitelist(query) {
  return request({
    url: '/business/whitelist/list',
    method: 'get',
    params: query
  })
}

// 查询白名单详细
export function getWhitelist(id) {
  return request({
    url: '/business/whitelist/' + id,
    method: 'get'
  })
}
// 查询停车场信息列表
export function listInfo(query) {
  return request({
    url: '/business/whitelist/parklist',
    method: 'get',
    params: query
  })
}
