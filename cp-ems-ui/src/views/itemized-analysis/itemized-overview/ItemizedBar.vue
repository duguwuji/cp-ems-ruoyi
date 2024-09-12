<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
// require('echarts/theme/macarons') // echarts theme
import resize from "@/views/dashboard/mixins/resize";
import { log } from 'ezuikit-js';

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
      default: "时",
    },
    barData: {
      type: Array,
      required: true
    },
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
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
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
      let series = []
      let xData = []
      this.barData.forEach(item => {
        let sery = {
          name: item.itemizeName,
          type: "bar",
          stack: "vistors",
          emphasis: {
            focus: 'series'
          },
          barWidth: "50%",
          data: Object.values(item.itemizeCount),
          animationDuration,
        }
        series.push(sery)
        xData = Object.keys(item.itemizeCount)

        if(this.xName == '时') {
          var hours = ['00:00','01:00','02:00','03:00','04:00','05:00','06:00','07:00','08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00']
          xData = hours.slice(0, xData.length)
        }
      })

      let echartsTheme = this.theme == 'theme-light' ? 'lightTheme' : 'darkTheme'
      this.chart = echarts.init(this.$el, echartsTheme)

      this.chart.setOption({
        // color: ['#5571c7', '#91cc75', '#fac858', '#ea7ccc'],
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
          // },
          valueFormatter: (value) => value + ' ' + this.yName
        },
        grid: {
          top: "15%",
          left: "2%",
          right: "5%",
          bottom: "3%",
          containLabel: true,
        },
        dataZoom: {
          show: true,
          type: "inside",
        },
        legend: {
          show: true,
        },
        xAxis: [
          {
            type: "category",
            name: this.xName,
            data: xData,
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            name: this.yName,
            position: "left",
            alignTicks: true,
          },
        ],
        series: series,
      });
    },
  },
};
</script>
