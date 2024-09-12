import request from '@/utils/request'

// 查询阶梯计费参数信息列表
export function listChargingStep(query) {
  return request({
    url: '/system/chargingStep/list',
    method: 'get',
    params: query
  })
}

// 查询阶梯计费参数信息详细
export function getChargingStep(id) {
  return request({
    url: '/system/chargingStep/' + id,
    method: 'get'
  })
}

// 新增阶梯计费参数信息
export function addChargingStep(data) {
  return request({
    url: '/system/chargingStep',
    method: 'post',
    data: data
  })
}

// 修改阶梯计费参数信息
export function updateChargingStep(data) {
  return request({
    url: '/system/chargingStep',
    method: 'put',
    data: data
  })
}

// 删除阶梯计费参数信息
export function delChargingStep(id) {
  return request({
    url: '/system/chargingStep/' + id,
    method: 'delete'
  })
}

// 更新策略的所有价格参数
export function updateParamList(data) {
    return request({
      url: '/system/chargingStep/update',
      method: 'put',
      data: data
    })
  }
