<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
// require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize'

const animationDuration = 1500

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
      default: '300px'
    },
    yName: {
      type: String,
      default: ''
    },
    xName: {
      type: String,
      default: '日'
    },
    barColor: {
      type: Array,
      default: () => {
        return ['#75c7f7','#3671e8']
      }
    },
    setting: {
      type: Object,
      default: () => {
        return {seriesName: '充电订单数量', yName: ''}
      }
    },
    chartData: {
      type: Object,
      default: () => {
        return {
          xData: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
          yData: [79, 52, 200, 334, 390, 330, 34,79, 52, 123, 334, 34, 56]
        }
      }
    },
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
  watch: {
    'chartData': {
      handler() {
        this.$nextTick(() => {
          if (!this.chart) {
            this.initChart();
            return;
          }
          this.chart.dispose();
          this.chart = null;
          this.initChart();
        });
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
  methods: {
    initChart() {
      let echartsTheme = this.theme == 'theme-light' ? 'lightTheme' : 'darkTheme'
      this.chart = echarts.init(this.$el, echartsTheme)

      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          },
          // backgroundColor: 'rgba(28, 37, 80, 0.9)',
          // borderColor: '#2969e8',
          // textStyle: {
          //   color: '#fff'
          // }
        },
        grid: {
          top: '15%',
          left: '2%',
          right: '4%',
          bottom: '2%',
          containLabel: true
        },
        // dataZoom: {
        //   show: true,
        //   type: 'inside'
        // },
        xAxis: [{
          name: this.xName,
          type: 'category',
          data: this.chartData.xData,
          // axisTick: {
          //   alignWithLabel: true
          // },
          // nameTextStyle: {
          //   color: '#fff'
          // },
          // splitLine: {
          //   lineStyle: {
          //     color: '#1d3666'
          //   }
          // },
          // axisLabel: {
          //   color: '#fff'
          // },
          // axisLine: {
          //   lineStyle: {
          //     color: this.barColor[0]
          //   }
          // }
        }],
        yAxis: [{
          // type: 'value',
          // name: 'kmh',
          // axisTick: {
          //   show: true
          // }
          type: 'value',
          name: this.setting.yName,
          position: 'left',
          alignTicks: true,
          // nameTextStyle: {
          //   color: '#fff'
          // },
          // splitLine: {
          //   lineStyle: {
          //     color: '#1d3666'
          //   }
          // },
          // axisLabel: {
          //   color: '#fff'
          // }
        }],
        series: [{
          name: this.setting.seriesName,
          type: 'bar',
          stack: 'vistors',
          barWidth: '50%',
          data: this.chartData.yData,
          animationDuration,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 1, color: this.barColor[0] },
              { offset: 0, color: this.barColor[1] },
            ])
          },
          markLine: {
            data: [{ type: 'average', name: 'Avg' }]
          }
        }]
      })
    }
  }
}
</script>
