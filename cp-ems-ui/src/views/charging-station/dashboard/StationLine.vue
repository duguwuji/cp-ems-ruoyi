<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      default: () => {
        return {
          yData: [79,65, 21, 67, 21, 89, 56],
          xData: ['09-22','09-23','09-24','09-25','09-26','09-27','09-28']
        }
      }
      // required: true
    },
    yName: {
      type: String,
      default: '次'
    },
    xName: {
      type: String,
      default: '日'
    },
    seriesName: {
      type: String,
      default: '充电次数'
    },
    itemColor: {
      type: String, 
      default: ''
    }

  },
  data() {
    return {
      chart: null
    }
  },
  computed: {
    theme() {
      return this.$store.getters.logoInfo.theme || 'theme-light'
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        if(!this.chart) return
        this.setOptions()
      }
    },
    theme: {
      deep: true,
      handler(val) {
        if (!this.chart) {
          return
        }
        this.chart.dispose()
        this.chart = null
        this.initChart()
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      let echartsTheme = this.theme == 'theme-light' ? 'lightTheme' : 'darkTheme'
      this.chart = echarts.init(this.$el, echartsTheme)
      this.setOptions()
    },
    setOptions() {
      this.chart.setOption({
        xAxis: {
          name: this.xName,
          data: this.chartData.xData,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          top: '15%',
          left: '2%',
          right: '10%',
          bottom: '0%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        dataZoom: {
          show: true,
          type: 'inside'
        },
        yAxis: {
          name: this.yName,
          axisTick: {
            show: false
          }
        },
        series: [{
          name: this.seriesName,
          itemStyle: {
            color: this.itemColor,
          },
          lineStyle: {
            // color: '#FF005A',
            width: 4
          },
          areaStyle: {
            opacity: 0.6,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: this.itemColor
            },
            {
              offset: 1,
              color: 'rgba(255, 255, 255, .255)'
            }
          ])
          },
          smooth: true,
          type: 'line',
          data: this.chartData.yData,
          animationDuration: 1500,
          animationEasing: 'cubicInOut',
        }]
      })
    }
  }
}
</script>
