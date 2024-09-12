import request from '@/utils/request'

// 查询充电桩信息列表
export function listPile(query) {
  return request({
    url: '/system/pile/list',
    method: 'get',
    params: query
  })
}

// 查询充电桩信息详细
export function getPile(pileId) {
  return request({
    url: '/system/pile/' + pileId,
    method: 'get'
  })
}

// 新增充电桩信息
export function addPile(data) {
  return request({
    url: '/system/pile',
    method: 'post',
    data: data
  })
}

// 修改充电桩信息
export function updatePile(data) {
  return request({
    url: '/system/pile',
    method: 'put',
    data: data
  })
}

// 删除充电桩信息
export function delPile(pileId) {
  return request({
    url: '/system/pile/' + pileId,
    method: 'delete'
  })
}

// 启用/停用充电桩
export function openOrClosePile(data) {
  return request({
    url: '/system/pile/openOrClose',
    method: 'put',
    data: data
  })
}
