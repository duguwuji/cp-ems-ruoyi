import request from '@/utils/request'

// 查询实时报警列表
export function listAlarm(query) {
  return request({
    url: '/alarm/list',
    method: 'get',
    params: query
  })
}

// 查询实时报警详细
export function getAlarm(id) {
  return request({
    url: '/alarm/' + id,
    method: 'get'
  })
}

// 新增实时报警
export function addAlarm(data) {
  return request({
    url: '/alarm',
    method: 'post',
    data: data
  })
}

// 修改实时报警
export function updateAlarm(data) {
  return request({
    url: '/alarm',
    method: 'put',
    data: data
  })
}

// 删除实时报警
export function delAlarm(id) {
  return request({
    url: '/alarm/' + id,
    method: 'delete'
  })
}

// 获取全部不同报警等级的数量及占比
export function getCountOfAllStatus(query) {
  return request({
    url: '/alarm/getCountOfAllStatus',
    method: 'get',
    params: query
  })
}

// 获取最新若干条报警数据
export function getLatestAlarmsByCount(query) {
  return request({
    url: '/alarm/getLatestAlarmsByCount',
    method: 'get',
    params: query
  })
}

// 获取报警参数统计
export function getAlarmParameterStatistics(query) {
  return request({
    url: '/alarm/getAlarmParameterStatistics',
    method: 'get',
    params: query
  })
}

// 获取报警等级统计
export function getAlarmLevelStatistics(query) {
  return request({
    url: '/alarm/getAlarmLevelStatistics',
    method: 'get',
    params: query
  })
}

// 获取报警数量统计
export function getAlarmCountStatistics(query) {
  return request({
    url: '/alarm/getAlarmCountStatistics',
    method: 'get',
    params: query
  })
}

// 获取报警类型统计
export function getAlarmTypeStatistics(query) {
  return request({
    url: '/alarm/getAlarmTypeStatistics',
    method: 'get',
    params: query
  })
}

// 获取报警区域统计
export function getAlarmAreaStatistics(query) {
  return request({
    url: '/alarm/getAlarmAreaStatistics',
    method: 'get',
    params: query
  })
}
