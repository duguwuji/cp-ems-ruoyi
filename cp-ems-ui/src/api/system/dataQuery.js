import request from "@/utils/request"

// 电力参数查询
export function queryPowerParameter(data) {
  return request({
    url: '/dataQuery/powerParameter',
    method: 'get',
    params: data
  })
}
// 电力参数极值查询
export function queryPeak(data) {
  return request({
    url: '/dataQuery/dailyExtremum',
    method: 'get',
    params: data
  })
}