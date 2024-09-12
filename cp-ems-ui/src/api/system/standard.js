import request from '@/utils/request'

// 查询作业规范列表
export function listStandard(query) {
  return request({
    url: '/system/standard/list',
    method: 'get',
    params: query
  })
}

// 查询作业规范详细
export function getStandard(standardId) {
  return request({
    url: '/system/standard/' + standardId,
    method: 'get'
  })
}

// 新增作业规范
export function addStandard(data) {
  return request({
    url: '/system/standard',
    method: 'post',
    data: data
  })
}

// 修改作业规范
export function updateStandard(data) {
  return request({
    url: '/system/standard',
    method: 'put',
    data: data
  })
}

// 删除作业规范
export function delStandard(standardId) {
  return request({
    url: '/system/standard/' + standardId,
    method: 'delete'
  })
}
