import request from '@/utils/request'

// 查询例报管理列表
export function listExampleReport(query) {
  return request({
    url: '/system/exampleReport/list',
    method: 'get',
    params: query
  })
}

// 查询例报管理详细
export function getExampleReport(exampleReportId) {
  return request({
    url: '/system/exampleReport/' + exampleReportId,
    method: 'get'
  })
}

// 新增例报管理
export function addExampleReport(data) {
  return request({
    url: '/system/exampleReport',
    method: 'post',
    data: data
  })
}

// 修改例报管理
export function updateExampleReport(data) {
  return request({
    url: '/system/exampleReport',
    method: 'put',
    data: data
  })
}

// 删除例报管理
export function delExampleReport(exampleReportId) {
  return request({
    url: '/system/exampleReport/' + exampleReportId,
    method: 'delete'
  })
}
