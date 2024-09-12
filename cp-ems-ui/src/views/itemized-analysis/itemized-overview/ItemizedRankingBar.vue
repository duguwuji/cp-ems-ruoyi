<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
// require('echarts/theme/macarons') // echarts theme
import resize from "@/views/dashboard/mixins/resize";

const animationDuration = 1500;

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "300px",
    },
    yName: {
      type: String,
      default: "kM·h",
    },
    xName: {
      type: String,
      default: "能源分项",
    },
    barData: {
      type: Object,
      default: () => {
        return {
          xData: [],
          yData: []
        }
      }
    }
  },
  data() {
    return {
      chart: null,
    };
  },
  computed: {
    theme() {
      return this.$store.getters.logoInfo.theme || 'theme-light'
    }
  },
  // mounted() {
  //   this.$nextTick(() => {
  //     this.initChart();
  //   });
  // },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  watch: {
    barData: {
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
      },
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
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
          // formatter: (params) => {
          //   let result = params[0].axisValue ?? ''
          //   params.forEach(item => {
          //     result += '<br/>' + item.marker + item.seriesName + '： <strong>' + item.value + '</strong> ' + this.yName
          //   })
          //   return result
          // }
          valueFormatter: (value) => value + ' ' + this.yName
        },
        grid: {
          top: "10%",
          left: "2%",
          right: "10%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "value",
            name: this.yName,
            position: "left",
            alignTicks: true,
          },
        ],
        yAxis: [
          {
            type: "category",
            name: this.xName,
            data: this.barData.xData,
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        series: [
          {
            name: '能耗',
            type: "bar",
            stack: "vistors",
            barWidth: "50%",
            data: this.barData.yData,
            animationDuration,
            label: {
              show: true,
              position: 'insideLeft',
            }
          }
        ],
      });
    },
  },
};
</script>
