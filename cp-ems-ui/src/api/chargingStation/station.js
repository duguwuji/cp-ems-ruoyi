import request from '@/utils/request'

// 查询充电站信息列表
export function listStation(query) {
  return request({
    url: '/system/station/list',
    method: 'get',
    params: query
  })
}

// 查询充电站信息详细
export function getStation(stationId) {
  return request({
    url: '/system/station/' + stationId,
    method: 'get'
  })
}

// 新增充电站信息
export function addStation(data) {
  return request({
    url: '/system/station',
    method: 'post',
    data: data
  })
}

// 修改充电站信息
export function updateStation(data) {
  return request({
    url: '/system/station',
    method: 'put',
    data: data
  })
}

// 删除充电站信息
export function delStation(stationId) {
  return request({
    url: '/system/station/' + stationId,
    method: 'delete'
  })
}
