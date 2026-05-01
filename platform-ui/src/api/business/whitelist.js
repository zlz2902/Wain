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

// 新增白名单
export function addWhitelist(data) {
  return request({
    url: '/business/whitelist',
    method: 'post',
    data: data
  })
}

// 修改白名单
export function updateWhitelist(data) {
  return request({
    url: '/business/whitelist',
    method: 'put',
    data: data
  })
}

// 删除白名单
export function delWhitelist(id) {
  return request({
    url: '/business/whitelist/' + id,
    method: 'delete'
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
