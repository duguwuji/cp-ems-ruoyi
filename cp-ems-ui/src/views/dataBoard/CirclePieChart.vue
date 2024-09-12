<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
// require('echarts/theme/macarons') // echarts theme
import resize from "../dashboard/mixins/resize";

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
    pieData: {
      type: Array,
      default: function () {
        return [
          { value: 0, name: "正常" },
          { value: 0, name: "报警" },
          { value: 0, name: "离线" },
        ];
      },
    },
  },
  data() {
    return {
      chart: null,
    };
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
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el);
      // this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          // formatter: '{a} <br/>{b} : {c} ({d}%)',
          appendToBody: true,
          backgroundColor: 'rgba(28, 37, 80, 0.8)',
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          top: "bottom",
          textStyle: {
            color: '#ff'
          },
        },
        grid: {
          left: 5,
          right: 5,
          bottom: '10%',
        },
        series: [
          {
            name: "设备状态",
            type: "pie",
            radius: [0, '65%'],
            center: ["50%", "40%"],
            roseType: "area",
            itemStyle: {
            },
            label: {
              show: true,
              position: "outside",
              color: '#1be5e7',
              formatter: '{b}({d}%)'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                color: 'data'
              },
            },
            // labelLine: {
            //   show: true,
            // },
            // data: [
            //   { value: 40, name: "正常", itemStyle: {color: '#6be6c3'} },
            //   { value: 38, name: "报警", itemStyle: {color: '#e0c464'} },
            //   { value: 32, name: "离线", itemStyle: {color: '#297ef8'} },
            // ],
            data:this.pieData,
            animationDuration: 1500,
          },
        ],
      });
    },
  },
};
</script>
