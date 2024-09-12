import request from '@/utils/request'

// 查询项目拓扑列表
export function listItemTopology(query) {
  return request({
    url: '/system/itemTopology/list',
    method: 'get',
    params: query
  })
}

// 查询项目拓扑详细
export function getItemTopology(itemId) {
  return request({
    url: '/system/itemTopology/' + itemId,
    method: 'get'
  })
}

// 新增项目拓扑
export function addItemTopology(data) {
  return request({
    url: '/system/itemTopology',
    method: 'post',
    data: data
  })
}

// 修改项目拓扑
export function updateItemTopology(data) {
  return request({
    url: '/system/itemTopology',
    method: 'put',
    data: data
  })
}

// 删除项目拓扑
export function delItemTopology(itemId) {
  return request({
    url: '/system/itemTopology/' + itemId,
    method: 'delete'
  })
}

// 查询部门列表（排除节点）
export function listItemTopologyExcludeChild(itemId) {
  return request({
    url: '/system/itemTopology/list/exclude/' + itemId,
    method: 'get'
  })
}

// 查询拓扑下拉树结构
export function topologyTreeSelect(data) {
  return request({
    url: '/system/itemTopology/topologyTree',
    method: 'get',
    params: data
  })
}