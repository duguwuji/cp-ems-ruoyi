import request from '@/utils/request'

// 查询报警规则列表
export function listAlarmRule(query) {
  return request({
    url: '/system/alarmRule/list',
    method: 'get',
    params: query
  })
}

// 查询报警规则详细
export function getAlarmRule(id) {
  return request({
    url: '/system/alarmRule/' + id,
    method: 'get'
  })
}

// 新增报警规则
export function addAlarmRule(data) {
  return request({
    url: '/system/alarmRule',
    method: 'post',
    data: data
  })
}

// 修改报警规则
export function updateAlarmRule(data) {
  return request({
    url: '/system/alarmRule',
    method: 'put',
    data: data
  })
}

// 删除报警规则
export function delAlarmRule(id) {
  return request({
    url: '/system/alarmRule/' + id,
    method: 'delete'
  })
}
