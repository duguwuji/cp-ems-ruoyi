import request from '@/utils/request'

// 查询充电价格策略信息列表
export function listPriceStrategy(query) {
  return request({
    url: '/system/priceStrategy/list',
    method: 'get',
    params: query
  })
}

// 查询充电价格策略信息详细
export function getPriceStrategy(id) {
  return request({
    url: '/system/priceStrategy/' + id,
    method: 'get'
  })
}

// 新增充电价格策略信息
export function addPriceStrategy(data) {
  return request({
    url: '/system/priceStrategy/insert',
    method: 'post',
    data: data
  })
}

// 修改充电价格策略信息
export function updatePriceStrategy(data) {
  return request({
    url: '/system/priceStrategy',
    method: 'put',
    data: data
  })
}

// 删除充电价格策略信息
export function delPriceStrategy(id) {
  return request({
    url: '/system/priceStrategy/' + id,
    method: 'delete'
  })
}