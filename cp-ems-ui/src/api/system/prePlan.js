import request from '@/utils/request'

// 查询预案管理列表
export function listPrePlan(query) {
  return request({
    url: '/system/prePlan/list',
    method: 'get',
    params: query
  })
}

// 查询预案管理详细
export function getPrePlan(prePlanId) {
  return request({
    url: '/system/prePlan/' + prePlanId,
    method: 'get'
  })
}

// 新增预案管理
export function addPrePlan(data) {
  return request({
    url: '/system/prePlan',
    method: 'post',
    data: data
  })
}

// 修改预案管理
export function updatePrePlan(data) {
  return request({
    url: '/system/prePlan',
    method: 'put',
    data: data
  })
}

// 删除预案管理
export function delPrePlan(prePlanId) {
  return request({
    url: '/system/prePlan/' + prePlanId,
    method: 'delete'
  })
}
