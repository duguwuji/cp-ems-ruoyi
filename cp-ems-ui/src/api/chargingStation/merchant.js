import request from '@/utils/request'

// 查询商户信息列表
export function listMerchant(query) {
  return request({
    url: '/system/merchant/list',
    method: 'get',
    params: query
  })
}

// 查询商户信息详细
export function getMerchant(merchantId) {
  return request({
    url: '/system/merchant/' + merchantId,
    method: 'get'
  })
}

// 新增商户信息
export function addMerchant(data) {
  return request({
    url: '/system/merchant',
    method: 'post',
    data: data
  })
}

// 修改商户信息
export function updateMerchant(data) {
  return request({
    url: '/system/merchant',
    method: 'put',
    data: data
  })
}

// 删除商户信息
export function delMerchant(merchantId) {
  return request({
    url: '/system/merchant/' + merchantId,
    method: 'delete'
  })
}
