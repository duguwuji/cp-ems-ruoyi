import request from '@/utils/request'

// 查询设备信息列表
export function listEquipmentInfo(query) {
  return request({
    url: '/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询设备信息详细
export function getEquipmentInfo(id) {
  return request({
    url: '/equipment/info/' + id,
    method: 'get'
  })
}

// 新增设备信息
export function addEquipmentInfo(data) {
  return request({
    url: '/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备信息
export function updateEquipmentInfo(data) {
  return request({
    url: '/equipment',
    method: 'put',
    data: data
  })
}

// 删除设备信息
export function delEquipmentInfo(id) {
  return request({
    url: '/equipment/' + id,
    method: 'delete'
  })
}

// 获取全部不同状态的设备数量及占比
export function getAllStatus(query) {
  return request({
    url: '/equipment/getAllStatus',
    method: 'get',
    params: query
  })
}
