import request from '@/utils/request'

// 查询计费方案信息列表
export function listCharging(query) {
  return request({
    url: '/system/charging/list',
    method: 'get',
    params: query
  })
}

// 查询计费方案信息详细
export function getCharging(chargingId) {
  return request({
    url: '/system/charging/' + chargingId,
    method: 'get'
  })
}

// 新增计费方案信息
export function addCharging(data) {
  return request({
    url: '/system/charging/insert',
    method: 'post',
    data: data
  })
}

// 修改计费方案信息
export function updateCharging(data) {
  return request({
    url: '/system/charging',
    method: 'put',
    data: data
  })
}

// 删除计费方案信息
export function delCharging(chargingId) {
  return request({
    url: '/system/charging/' + chargingId,
    method: 'delete'
  })
}
