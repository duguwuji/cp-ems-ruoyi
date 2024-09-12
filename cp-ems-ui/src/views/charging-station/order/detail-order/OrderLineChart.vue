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
          require: [79,65, 21, 67, 21, 89, 56],
          real: [48,73, 63, 82, 16, 38, 61]
        }
      }
      // required: true
    },
    yName: {
      type: String,
      default: '电压/V'
    },
    xName: {
      type: String,
      default: ''
    },
    xData: {
      type: Array,
      default: () => {
        return ['2023-10-08 08:23:52','2023-10-08 08:57:21','2023-10-08 08:16:36','2023-10-08 09:48:19','2023-10-08 10:03:51','2023-10-08 10:37:28','2023-10-08 10:55:49',]
      }
    },
    seriesName: {
      type: Array,
      required: true
    },
    itemColor: {
      type: Array, 
      default: () => {
        return ['#5470c6', '#91cc75']
      }
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
          data: this.xData,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          top: '15%',
          left: '5%',
          right: '8%',
          bottom: '2%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          right: '10%'
        },
        yAxis: {
          name: this.yName,
          axisTick: {
            show: false
          }
        },
        series: [{
          name: this.seriesName[0],
          itemStyle: {
            color: this.itemColor[0],
          },
          lineStyle: {
            // color: '#FF005A',
            width: 2
          },
          areaStyle: {
            opacity: 0.6,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: this.itemColor[0]
            },
            {
              offset: 1,
              color: 'rgba(255, 255, 255, .255)'
            }
          ])
          },
          showSymbol: false,
          smooth: true,
          type: 'line',
          data: this.chartData.require,
          animationDuration: 1500,
          animationEasing: 'cubicInOut',
        },
        {
          name: this.seriesName[1],
          itemStyle: {
            color: this.itemColor[1],
          },
          lineStyle: {
            // color: '#FF005A',
            width: 2
          },
          areaStyle: {
            opacity: 0.6,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: this.itemColor[1]
            },
            {
              offset: 1,
              color: 'rgba(255, 255, 255, .255)'
            }
          ])
          },
          showSymbol: false,
          smooth: true,
          type: 'line',
          data: this.chartData.real,
          animationDuration: 1500,
          animationEasing: 'cubicInOut',
        }]
      })
    }
  }
}
</script>
