import request from '@/utils/request'

// 查询Blacklist列表
export function listBlacklist(query) {
  return request({
    url: '/business/blacklist/list',
    method: 'get',
    params: query
  })
}

// 查询Blacklist详细
export function getBlacklist(id) {
  return request({
    url: '/business/blacklist/' + id,
    method: 'get'
  })
}

// 新增Blacklist
export function addBlacklist(data) {
  return request({
    url: '/business/blacklist',
    method: 'post',
    data: data
  })
}

// 修改Blacklist
export function updateBlacklist(data) {
  return request({
    url: '/business/blacklist',
    method: 'put',
    data: data
  })
}

// 删除Blacklist
export function delBlacklist(id) {
  return request({
    url: '/business/blacklist/' + id,
    method: 'delete'
  })
}

// 导出Blacklist列表
export function exportBlacklist(query) {
  return request({
    url: '/business/blacklist/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}
