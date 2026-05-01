import request from '@/utils/request'
export function indexUrl() {
  return request({
    url: '/system/jeecg/getReport',
    method: 'get'
  })
}
