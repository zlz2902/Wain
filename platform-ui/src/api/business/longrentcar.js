import request from '@/utils/request'

// 查询长租车辆列表
export function listLongrentcar(query) {
  return request({
    url: '/business/longrentcar/list',
    method: 'get',
    params: query
  })
}

// 查询长租车辆详细
export function getLongrentcar(id) {
  return request({
    url: '/business/longrentcar/' + id,
    method: 'get'
  })
}

// 新增长租车辆
export function addLongrentcar(data) {
  return request({
    url: '/business/longrentcar',
    method: 'post',
    data: data
  })
}

// 修改长租车辆
export function updateLongrentcar(data) {
  return request({
    url: '/business/longrentcar',
    method: 'put',
    data: data
  })
}

// 删除长租车辆
export function delLongrentcar(id) {
  return request({
    url: '/business/longrentcar/' + id,
    method: 'delete'
  })
}
