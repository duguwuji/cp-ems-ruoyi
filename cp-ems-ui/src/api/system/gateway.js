import request from '@/utils/request'

// 查询网关信息列表
export function listGateway(query) {
  return request({
    url: '/system/gateway/list',
    method: 'get',
    params: query
  })
}

// 查询网关信息详细
export function getGateway(id) {
  return request({
    url: '/system/gateway/' + id,
    method: 'get'
  })
}

// 新增网关信息
export function addGateway(data) {
  return request({
    url: '/system/gateway',
    method: 'post',
    data: data
  })
}

// 修改网关信息
export function updateGateway(data) {
  return request({
    url: '/system/gateway',
    method: 'put',
    data: data
  })
}

// 删除网关信息
export function delGateway(id) {
  return request({
    url: '/system/gateway/' + id,
    method: 'delete'
  })
}
