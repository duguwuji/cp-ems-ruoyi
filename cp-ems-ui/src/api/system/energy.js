// 获取计算数据
import request from '@/utils/request'

//获取指定日期用能统计
export function getByDay(date) {
  return request({
    url: '/data/energy/getByDay/' + date,
    method: 'get'
  })
}

// 获取能源流向
export function getFlowData(query) {
  return request({
    url: '/data/energy/getFlowData',
    method: 'get',
    params: query
  })
}

//获取指定日期的用电量趋势（按小时）
export function getHourlyData(date) {
  return request({
    url: '/data/energy/getHourlyData/' + date,
    method: 'get'
  })
}

//获取指定日期所属月的用电量趋势（按日）
export function getDailyData(date) {
  return request({
    url: '/data/energy/getDailyData/' + date,
    method: 'get'
  })
}

//获取指定日期所属年的用电量趋势（按月）
export function getMonthlyData(date) {
  return request({
    url: '/data/energy/getMonthlyData/' + date,
    method: 'get'
  })
}

// 用能概况环比
export function getChainData(data) {
  return request({
    url: '/data/energy/getChain',
    method: 'get',
    params: data
  })
}

//获取指定日期的用电量趋势（按小时）
export function getDayTrend(data) {
  return request({
    url: '/data/energy/dayTrend',
    method: 'get',
    params: data
  })
}

//获取指定日期的用电量趋势（按天）
export function getMonthTrend(data) {
  return request({
    url: '/data/energy/monthTrend',
    method: 'get',
    params: data
  })
}

//获取指定日期的用电量趋势（按月）
export function getYearTrend(data) {
  return request({
    url: '/data/energy/yearTrend',
    method: 'get',
    params: data
  })
}

// 用能概况日用电功率曲率
export function getDailyP(data) {
  return request({
    url: '/data/energy/getDailyP',
    method: 'get',
    params: data
  })
}

// 用能趋势-日
export function getWTrendByDay(data) {
  return request({
    url: '/data/energy/getWTrendByDay',
    method: 'get',
    params: data
  })
}

// 用能趋势-月
export function getWTrendByMonth(data) {
  return request({
    url: '/data/energy/getWTrendByMonth',
    method: 'get',
    params: data
  })
}

// 用能趋势-年
export function getWTrendByYear(data) {
  return request({
    url: '/data/energy/getWTrendByYear',
    method: 'get',
    params: data
  })
}


// 同比分析
export function getYearAnalysis(query) {
  return request({
    url: '/data/energy/getYearAnalysis',
    method: 'get',
    params: query
  })
}

// 环比分析
export function getChainByDevice(query) {
  return request({
    url: '/data/energy/getChainByDevice',
    method: 'get',
    params: query
  })
}

// 损耗分析
export function getLossAnalysis(data) {
  return request({
    url: '/data/energy/getLossAnalysis',
    method: 'post',
    data: data
  })
}

// 碳排分析-本月，本年
export function getChain(query) {
  return request({
    url: '/data/carbon/getChain',
    method: 'get',
    params: query
  })
}

// 碳排分析-按年
export function getChainByYear(query) {
  return request({
    url: '/data/carbon/getChainByYear',
    method: 'get',
    params: query
  })
}

// 分析报告-chart
export function getReportChart(query) {
  return request({
    url: '/data/energy/getReportChart',
    method: 'get',
    params: query
  })
}

// 分析报告-电费chart
export function getReportChargingChart(query) {
  return request({
    url: '/data/energy/getReportChargingChart',
    method: 'get',
    params: query
  })
}

// 分析报告-table
export function getReportTable(query) {
  return request({
    url: '/data/energy/getReportTable',
    method: 'get',
    params: query
  })
}
// 分析报告-复费率
export function getRecurringRate(query) {
  return request({
    url: '/data/energy/getRecurringRate',
    method: 'get',
    params: query
  })
}
// 费用报表
export function getConsumptionExpenseReport(data) {
  return request({
    url: '/data/energy/getConsumptionExpenseReport',
    method: 'get',
    params: data
  })
}
// 首页用能统计
export function getConsumptionStatistics(data) {
  return request({
    url: '/data/energy/getConsumptionStatistics',
    method: 'get',
    params: data
  })
}
// 首页综合能耗和碳排放chart
export function getTrendAndCarbon(data) {
  return request({
    url: '/data/energy/getTrendAndCarbon',
    method: 'get',
    params: data
  })
}
