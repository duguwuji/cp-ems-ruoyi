import request from '@/utils/request'

// 查询视频配置列表
export function listCameraConfig(query) {
  return request({
    url: '/system/cameraConfig/list',
    method: 'get',
    params: query
  })
}

// 查询视频配置详细
export function getCameraConfig(id) {
  return request({
    url: '/system/cameraConfig/' + id,
    method: 'get'
  })
}

// 新增视频配置
export function addCameraConfig(data) {
  return request({
    url: '/system/cameraConfig',
    method: 'post',
    data: data
  })
}

// 修改视频配置
export function updateCameraConfig(data) {
  return request({
    url: '/system/cameraConfig',
    method: 'put',
    data: data
  })
}

// 删除视频配置
export function delCameraConfig(id) {
  return request({
    url: '/system/cameraConfig/' + id,
    method: 'delete'
  })
}

//根据序列号获取播放地址
export function getUrlBySerialNumber(serialNumber) {
  return request({
    url: '/system/cameraConfig/getUrlBySerialNumber?serialNumber='+serialNumber,
    method: 'get',
  })
}
