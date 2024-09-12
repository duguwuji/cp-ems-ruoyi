import request from '@/utils/request'

// 分项分析-分项概览
export function itemizedOverview(data) {
  return request({
    url: '/data/itemizeAnalysis/getOverview',
    method: 'get',
    params: data
  })
}