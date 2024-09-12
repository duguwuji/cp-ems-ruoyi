import request from '@/utils/request'

// 查询定额配置列表
export function listQuota(query) {
  return request({
    url: '/system/quota/list',
    method: 'get',
    params: query
  })
}

// 查询定额配置详细
export function getQuota(quotaId) {
  return request({
    url: '/system/quota/' + quotaId,
    method: 'get'
  })
}

// 新增定额配置
export function addQuota(data) {
  return request({
    url: '/system/quota',
    method: 'post',
    data: data
  })
}

// 修改定额配置
export function updateQuota(data) {
  return request({
    url: '/system/quota',
    method: 'put',
    data: data
  })
}

// 删除定额配置
export function delQuota(quotaId) {
  return request({
    url: '/system/quota/' + quotaId,
    method: 'delete'
  })
}

// 定额分析
export function analysis(query) {
  return request({
    url: '/system/quota/analysis',
    method: 'get',
    params: query
  })
}

// 用量监测
export function monitor(query) {
  return request({
    url: '/system/quota/monitor',
    method: 'get',
    params: query
  })
}
