import request from '@/utils/request'

// 查询特殊车辆管理列表
export function listCar(query) {
  return request({
    url: '/business/car/list',
    method: 'get',
    params: query
  })
}

// 查询特殊车辆管理详细
export function getCar(id) {
  return request({
    url: '/business/car/' + id,
    method: 'get'
  })
}

// 新增特殊车辆管理
export function addCar(data) {
  return request({
    url: '/business/car',
    method: 'post',
    data: data
  })
}

// 修改特殊车辆管理
export function updateCar(data) {
  return request({
    url: '/business/car',
    method: 'put',
    data: data
  })
}

// 删除特殊车辆管理
export function delCar(id) {
  return request({
    url: '/business/car/' + id,
    method: 'delete'
  })
}
