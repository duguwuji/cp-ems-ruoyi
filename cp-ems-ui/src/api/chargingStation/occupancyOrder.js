import request from '@/utils/request'

// 查询占位订单信息列表
export function listOccupancyOrder(query) {
  return request({
    url: '/system/occupancyOrder/list',
    method: 'get',
    params: query
  })
}

// 查询占位订单信息详细
export function getOccupancyOrder(id) {
  return request({
    url: '/system/occupancyOrder/' + id,
    method: 'get'
  })
}

// 新增占位订单信息
export function addOccupancyOrder(data) {
  return request({
    url: '/system/occupancyOrder',
    method: 'post',
    data: data
  })
}

// 修改占位订单信息
export function updateOccupancyOrder(data) {
  return request({
    url: '/system/occupancyOrder',
    method: 'put',
    data: data
  })
}

// 删除占位订单信息
export function delOccupancyOrder(id) {
  return request({
    url: '/system/occupancyOrder/' + id,
    method: 'delete'
  })
}