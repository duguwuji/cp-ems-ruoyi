import request from '@/utils/request'

// 查询品牌信息列表
export function listBrand(query) {
  return request({
    url: '/system/brand/list',
    method: 'get',
    params: query
  })
}

// 查询品牌信息详细
export function getBrand(id) {
  return request({
    url: '/system/brand/' + id,
    method: 'get'
  })
}

// 新增品牌信息
export function addBrand(data) {
  return request({
    url: '/system/brand',
    method: 'post',
    data: data
  })
}

// 修改品牌信息
export function updateBrand(data) {
  return request({
    url: '/system/brand',
    method: 'put',
    data: data
  })
}

// 删除品牌信息
export function delBrand(id) {
  return request({
    url: '/system/brand/' + id,
    method: 'delete'
  })
}