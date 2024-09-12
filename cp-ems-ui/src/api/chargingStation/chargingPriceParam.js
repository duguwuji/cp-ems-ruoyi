import request from '@/utils/request'

// 查询充电策略参数信息列表
export function listPriceParam(query) {
  return request({
    url: '/system/priceParam/list',
    method: 'get',
    params: query
  })
}

// 查询充电策略参数信息详细
export function getPriceParam(id) {
  return request({
    url: '/system/priceParam/' + id,
    method: 'get'
  })
}

// 新增充电策略参数信息
export function addPriceParam(data) {
  return request({
    url: '/system/priceParam',
    method: 'post',
    data: data
  })
}

// 修改充电策略参数信息
export function updatePriceParam(data) {
  return request({
    url: '/system/priceParam',
    method: 'put',
    data: data
  })
}

// 删除充电策略参数信息
export function delPriceParam(id) {
  return request({
    url: '/system/priceParam/' + id,
    method: 'delete'
  })
}

// 更新策略的所有价格参数
export function updateParamList(data) {
  return request({
    url: '/system/priceParam/update',
    method: 'put',
    data: data
  })
}