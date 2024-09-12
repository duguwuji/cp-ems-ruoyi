<template>
  <div class="app-container bg-container">
    <div class="chart-select">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" :rules="rules">
        <el-form-item label="起止时间" prop="dateRange">
          <el-date-picker v-model="queryParams.dateRange" style="width: 300px" value-format="yyyy-MM-dd HH:mm:ss"
            type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="getAlarmCharts">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="chart">
      <div class="content-left">
        <div class="columnar">
          <div class="overview-title">
            <div class="overview-icon"></div>
            <span>报警参数</span>
          </div>
          <MinMaxDoubleBarVue height="100%" :xData="paramX" :yData="paramY" :setInfo="{
            legendName: ['报警量'],
          }" />
        </div>
        <div class="circle-pie">
          <div class="overview-title">
            <div class="overview-icon"></div>
            <span>报警等级</span>
          </div>
          <PieChartVue height="100%" :pieData="pieData" />
        </div>
      </div>
      <div class="content-data">
        <div class="data-select">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          </el-form>
        </div>
        <div class="data-chart">
          <div class="day-chart">
            <div class="overview-title">
              <div class="overview-icon"></div>
              <span>报警数量</span>
            </div>
            <MinMaxDoubleBarVue height="100%" :xData="countX" :yData="countY" :setInfo="{
              legendName: ['本周期数据量', '上周期数据量'],
            }" class="chart-box" />
          </div>
          <div class="month-chart">
            <div class="overview-title">
              <div class="overview-icon"></div>
              <span>报警类型</span>
            </div>
            <MinMaxDoubleBarVue height="100%" :xData="typeX" :yData="typeY" :setInfo="{
              legendName: ['本周期数据量', '上周期数据量'],
            }" class="chart-box" />
          </div>
          <div class="year-chart">
            <div class="overview-title">
              <div class="overview-icon"></div>
              <span>报警区域</span>
            </div>
            <MinMaxDoubleBarVue height="100%" :xData="areaX" :yData="areaY" :setInfo="{
              legendName: ['本周期数据量', '上周期数据量'],
            }" class="chart-box" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment/moment'
import MinMaxDoubleBarVue from "@/views/dashboard/MinMaxDoubleBar.vue";
import PieChartVue from "@/views/dataBoard/CirclePieChart.vue"
import { getAlarmParameterStatistics, getAlarmLevelStatistics, getAlarmCountStatistics, getAlarmTypeStatistics, getAlarmAreaStatistics } from "@/api/system/alarm";
export default {
  components: {
    MinMaxDoubleBarVue,
    PieChartVue
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      paramX: [],
      paramY: {},
      pieData:[],
      countX:[],
      countY:{},
      typeX:[],
      typeY:{},
      areaX:[],
      areaY:{},
      queryParams: {
        dateRange: [],
      },
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() > Date.now(); // 禁用大于今天的日期
        },
      },
      rules: {
        dateRange: [
          { required: true, message: "请选择时间", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.defaultDate();
    this.$nextTick(() => {
    this.getAlarmAllStatictic();
  })
  },
  methods: {
    getAlarmCharts(){
      this.getAlarmAllStatictic();
    },
    //默认日期
    defaultDate(){
      this.queryParams.dateRange = [moment().format("yyyy-MM-01 00:00:00"),moment().format("yyyy-MM-DD 23:59:59")]
    },
    //获取报警分析所有数据
    getAlarmAllStatictic() {
      this.loading = true;
      let params = {
        startTime: this.queryParams.dateRange[0],
        endTime: this.queryParams.dateRange[1],
      }

      //获取报警参数分析
      getAlarmParameterStatistics(params).then(res => {
        this.paramX = [];
        this.paramY = {};
        let counts = [];
          res.data.forEach((item) => {
            this.paramX.push(item.paramName);
            counts.push(item.count)
          });
          this.paramY={
              currentData: counts,
            }
      });

      //获取报警等级分析
      getAlarmLevelStatistics(params).then(res => {
        this.pieData = [
          { value: res.data[0].count, name: "一般", itemStyle: {color: '#6be6c3'} },
          { value: res.data[1].count, name: "紧急", itemStyle: {color: '#e0c464'} },
          { value: res.data[2].count, name: "严重", itemStyle: {color: '#297ef8'} },
        ]
      });

      //获取报警数量分析
      getAlarmCountStatistics(params).then(res => {
        this.countX = [];
        this.countY = {};
        let current = [];
        let last = [];
          res.data.forEach((item) => {
            this.countX.push(item.name);
            current.push(item.count)
            last.push(item.lastCount)
          });
          this.countY={
              currentData: current,
              sameData: last,
            }

      });

      //获取报警类型分析
      getAlarmTypeStatistics(params).then(res => {
        this.typeX = [];
        this.typeY = {};
        let current = [];
        let last = [];
          res.data.forEach((item) => {
            this.typeX.push(item.alarmInfo);
            current.push(item.count)
            last.push(item.lastCount)
          });
          this.typeY={
              currentData: current,
              sameData: last,
            }
      });

      //获取报警区域分析
      getAlarmAreaStatistics(params).then(res => {
        this.areaX = [];
        this.areaY = {};
        let current = [];
        let last = [];
          res.data.forEach((item) => {
            this.areaX.push(item.area);
            current.push(item.count)
            last.push(item.lastCount)
          });
          this.areaY={
              currentData: current,
              sameData: last,
            }
      });
      this.loading = false;
    },
  }
};
</script>
<style lang="scss" scoped>
.app-container {
  min-height: calc(100vh - 84px);

  .chart-select {
    margin-left: 1%;
    margin-top: 12px;
  }
}

.content-left {
  width: 35%;
  height: 100%;
  background: var(--base-item-bg);
  margin-right: 6px;
  padding: 20px;
}

.content-data {
  width: calc(100% - 25% - 6px);
  height: 100%;
  background: var(--base-item-bg);
  padding: 12px 20px;

  .data-chart {
    margin-top: 1.5%;
    height: calc(100%);
    //   border: 1px solid;

    .day-chart,
    .month-chart,
    .year-chart {
      height: calc(100% / 3);

      .day-select {
        margin-bottom: 6px;

        .day-select-text {
          font-size: 14px;
          line-height: 36px;
          color: var(--base-color-2);
        }
      }

      .chart-box {
        height: calc(100% - 42px) !important;
      }
    }
  }
}

.chart {
  display: flex;
  height: 750px;
}

.overview-title {
  height: 30px;
  line-height: 30px;
  display: flex;
  color: var(--base-color-1);

  .overview-icon {
    width: 5px;
    height: 20px;
    margin: 5px 10px 0 0;
    background: var(--current-color);
  }
}

.columnar {
  height: 45%;
}

.circle-pie {
  margin-top: 5%;
  height: 45%;
}
</style>
