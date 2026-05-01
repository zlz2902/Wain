import request from '@/utils/request'

// 查询车牌明细列表
export function listGroup(query) {
  return request({
    url: '/business/group/list',
    method: 'get',
    params: query
  })
}

// 查询车牌明细详细
export function getGroup(ID) {
  return request({
    url: '/business/group/' + ID,
    method: 'get'
  })
}

// 新增车牌明细
export function addGroup(data) {
  return request({
    url: '/business/group',
    method: 'post',
    data: data
  })
}

// 修改车牌明细
export function updateGroup(data) {
  return request({
    url: '/business/group',
    method: 'put',
    data: data
  })
}

// 删除车牌明细
export function delGroup(ID) {
  return request({
    url: '/business/group/' + ID,
    method: 'delete'
  })
}
