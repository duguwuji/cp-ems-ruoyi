<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
// require('echarts/theme/macarons') // echarts theme
import resize from '../dashboard/mixins/resize'

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
      default: 'kM·h'
    },
    barColor: {
      type: Array,
      default: () => {
        return ['#00d0ff','#2969e8']
      }
    },
    xData: {
      type: Array,
      default: () => {
        return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12']
      }
    },
    yData: {
      type: Array,
      default: () => {
        return [79, 52, 200, 334, 390, 330, 34,79, 52, 123, 334, 34, 56]
      }
    },
  },
  data() {
    return {
      chart: null
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
    'yData': {
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
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el)
      // this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          },
          backgroundColor: 'rgba(28, 37, 80, 0.9)',
          borderColor: '#2969e8',
          textStyle: {
            color: '#fff'
          }
        },
        grid: {
          top: '15%',
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        dataZoom: {
          show: true,
          type: 'inside'
        },
        xAxis: [{
          type: 'category',
          data: this.xData,
          axisTick: {
            alignWithLabel: true
          },
          nameTextStyle: {
            color: '#fff'
          },
          splitLine: {
            lineStyle: {
              color: '#1d3666'
            }
          },
          axisLabel: {
            color: '#fff'
          },
          axisLine: {
            lineStyle: {
              color: this.barColor[0]
            }
          }
        }],
        yAxis: [{
          // type: 'value',
          // name: 'kmh',
          // axisTick: {
          //   show: true
          // }
          type: 'value',
          name: this.yName,
          position: 'left',
          alignTicks: true,
          nameTextStyle: {
            color: '#fff'
          },
          splitLine: {
            lineStyle: {
              color: '#1d3666'
            }
          },
          axisLabel: {
            color: '#fff'
          }
        }],
        series: [{
          // name: 'pageA',
          type: 'bar',
          stack: 'vistors',
          barWidth: '50%',
          data: this.yData,
          animationDuration,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 1, color: this.barColor[0] },
              { offset: 0, color: this.barColor[1] },
            ])
          }
        }]
      })
    }
  }
}
</script>
