import request from '@/utils/request'

// 查询值班管理列表
export function listDuty(query) {
  return request({
    url: '/system/duty/list',
    method: 'get',
    params: query
  })
}

// 查询值班管理详细
export function getDuty(dutyId) {
  return request({
    url: '/system/duty/' + dutyId,
    method: 'get'
  })
}

// 新增值班管理
export function addDuty(data) {
  return request({
    url: '/system/duty',
    method: 'post',
    data: data
  })
}

// 修改值班管理
export function updateDuty(data) {
  return request({
    url: '/system/duty',
    method: 'put',
    data: data
  })
}

// 删除值班管理
export function delDuty(dutyId) {
  return request({
    url: '/system/duty/' + dutyId,
    method: 'delete'
  })
}
