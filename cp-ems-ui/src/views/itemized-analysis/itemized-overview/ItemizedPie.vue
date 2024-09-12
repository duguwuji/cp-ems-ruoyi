<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
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
      default: '300px'
    },
    unit: {
      type: String,
      default: 'kW·h'
    },
    pieData: {
      type: Array,
      default: function () {
        return [
          { value: 0, name: "正常设备" },
          { value: 0, name: "报警设备" },
          { value: 0, name: "离线设备" },
        ];
      },
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
    'pieData': {
      handler() {
        this.$nextTick(() => {
          if (!this.chart) {
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
          trigger: 'item',
          appendToBody: true,
          formatter: (params) => {
            return params.seriesName + '<br/>' + params.marker + params.data.name + '： <strong>' + params.data.value + '</strong> ' + this.unit
          }
        },
        series: [
          {
            name: '分项占比',
            type: 'pie',
            radius: ['0%', '60%'],
            center: ['50%', '50%'],
            data:this.pieData,
            animationDuration: 1500,
            label: {
              formatter: '{b}:{d}%'
            },
            emphasis: {
              label: {
                fontWeight: 'bold',
                fontSize: 12
              }
            }
          }
        ]
      })
    }
  }
}
</script>
