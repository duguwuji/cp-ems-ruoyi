<template>
  <div class="content-container flex-between">
    <div class="content-tree">
      <topologicaTree @selectItem="treeItem($event)" />
    </div>
    <div class="content-data" v-loading="loading">
      <div class="data-select">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
        >
          <el-form-item label="分类能耗" prop="energyType">
            <el-select v-model="queryParams.energyType" placeholder="请选择" @change="energyTypeChange">
              <el-option
                v-for="item in dict.type.energy_type"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="年份">
            <el-date-picker
              v-model="queryParams.year"
              type="year"
              placeholder="选择年份"
            ></el-date-picker>
          </el-form-item> -->
          <!-- <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini">查询</el-button>
          </el-form-item> -->
        </el-form>
      </div>
      <div class="data-chart">
        <div class="day-chart">
          <div class="day-select flex-between">
            <el-date-picker
              v-model="queryParams.dayTime"
              type="date"
              placeholder="选择日期"
              style="width: 140px"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
              :clearable="false"
              :editable="false"
              @change="changeDayTime"
            ></el-date-picker>
            <div class="day-select-text">当日能耗总值：{{dayTotal}}kW·h</div>
          </div>
          <MinMaxDoubleBarVue
            height="100%"
            :xData="[
              '00:00',
              '01:00',
              '02:00',
              '03:00',
              '04:00',
              '05:00',
              '06:00',
              '07:00',
              '08:00',
              '09:00',
              '10:00',
              '11:00',
              '12:00',
              '13:00',
              '14:00',
              '15:00',
              '16:00',
              '17:00',
              '18:00',
              '19:00',
              '20:00',
              '21:00',
              '22:00',
              '23:00',
            ]"
            :yData="dayDataY"
            :setInfo="{
              xName: '时',
              yName: unit,
              legendName: ['当日', '昨日'],
            }"
            class="chart-box"
          />
        </div>
        <div class="month-chart">
          <div class="day-select flex-between">
            <el-date-picker
              v-model="queryParams.monthTime"
              type="month"
              placeholder="选择日期"
              style="width: 140px"
              value-format="yyyy-MM"
              :clearable="false"
              :editable="false"
              :picker-options="pickerOptions"
              @change="changeMonthTime"
            ></el-date-picker>
            <div class="day-select-text">当月能耗总值：{{monthTotal}}kW·h</div>
          </div>
          <MinMaxDoubleBarVue
            height="100%"
            :xData="[
              '01',
              '02',
              '03',
              '04',
              '05',
              '06',
              '07',
              '08',
              '09',
              '10',
              '11',
              '12',
              '13',
              '14',
              '15',
              '16',
              '17',
              '18',
              '19',
              '20',
              '21',
              '22',
              '23',
              '24',
              '25',
              '26',
              '27',
              '28',
              '28',
              '30',
              '31',
            ]"
            :yData="monthDataY"
            :setInfo="{
              xName: '日',
              yName: unit,
              legendName: ['当月', '上月'],
            }"
            class="chart-box"
          />
        </div>
        <div class="year-chart">
          <div class="day-select flex-between">
            <el-date-picker
              v-model="queryParams.yearTime"
              type="year"
              placeholder="选择日期"
              style="width: 140px"
              value-format="yyyy"
              :clearable="false"
              :editable="false"
              :picker-options="pickerOptions"
              @change="changeYearTime"
            ></el-date-picker>
            <div class="day-select-text">当年能耗总值：{{yearTotal}}kW·h</div>
          </div>
          <MinMaxDoubleBarVue
            height="100%"
            :yData="yearDataY"
            :setInfo="{
              xName: '月',
              yName: unit,
              legendName: ['当年', '去年'],
            }"
            class="chart-box"
          />
        </div>
      </div>
      <!-- <div class="data-chart">
        <MinMaxDoubleBarVue height="100%"/>
      </div>
      <div class="data-table">
        <currentTable/>
      </div> -->
    </div>
  </div>
</template>

<script>
import topologicaTree from "@/components/TopologicaTree/index";
// import LineChart from '@/views/dashboard/LineChart';
import MinMaxDoubleBarVue from "@/views/dashboard/MinMaxDoubleBar.vue";
import moment from "moment/moment";
import { getWTrendByDay, getWTrendByMonth, getWTrendByYear } from "@/api/system/energy";
export default {
  dicts: ['energy_type'],
  components: {
    topologicaTree,
    MinMaxDoubleBarVue,
  },
  data() {
    return {
      queryParams: {
        energyType: '0',
        dayTime: "2023-04-13",
        monthTime: "",
        yearTime: "",
        areaId: '',
      },
      energyType: [
        {
          value: '0',
          label: "电",
          unit: 'kW.h'
        },
        {
          value: '1',
          label: "水",
          unit: 't'
        },
      ],
      dayDataY: {},
      dayTotal: null,
      monthDataY: {},
      monthTotal: null,
      yearDataY: {},
      yearTotal: null,
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() > Date.now(); // 禁用大于今天的日期
        },
      },
      unit: 'kW.h',
      loading: true,
    };
  },
  created() {
    this.queryParams.dayTime = moment().format("yyyy-MM-DD");
    this.queryParams.monthTime = moment().format("yyyy-MM");
    this.queryParams.yearTime = moment().format("yyyy");
  },
  methods: {
    treeItem(value) {
      this.queryParams.areaId = value.id
      this.getDayTrend();
      this.getMonthTrend()
      this.getYearTrend()
    },
    getDayTrend() {
      let data = {
        areaId: this.queryParams.areaId,
        time: this.queryParams.dayTime,
        energyType: this.queryParams.energyType
      };
      getWTrendByDay(data).then((res) => {
        this.dayDataY = {
          currentData: res.data.currentList,
          sameData: res.data.beforeList,
        };
        this.dayTotal = res.data.currentTotal
      });
    },
    getMonthTrend() {
      let data = {
        areaId: this.queryParams.areaId,
        time: this.queryParams.monthTime,
        energyType: this.queryParams.energyType
      };
      getWTrendByMonth(data).then((res) => {
        this.monthDataY = {
          currentData: res.data.currentList,
          sameData: res.data.beforeList,
        };
        this.monthTotal = res.data.currentTotal
      });
    },
    getYearTrend() {
      let data = {
        areaId: this.queryParams.areaId,
        time: this.queryParams.yearTime,
        energyType: this.queryParams.energyType
      };
      this.loading = true
      getWTrendByYear(data).then((res) => {
        this.yearDataY = {
          currentData: res.data.currentList,
          sameData: res.data.beforeList,
        };
        this.yearTotal = res.data.currentTotal
      }).finally(() => {
        this.loading = false
      });
    },
    // 能耗类型切换
    energyTypeChange(value) {
      let item = this.energyType.find(t => t.value == value)
      if(item) {
        this.unit = item.unit
      }

      this.getDayTrend()
      this.getMonthTrend()
      this.getYearTrend()
    },
    changeDayTime(value) {
      this.getDayTrend()
    },
    changeMonthTime(value) {
      this.getMonthTrend()
    },
    changeYearTime(value) {
      this.getYearTrend()
    },
  },
};
</script>

<style lang="scss" scoped>
.content-container {
  min-height: 700px;
  height: calc(100vh - 84px);

  .content-tree {
    width: 25%;
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
      height: calc(100% - 51px);
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
}
</style>
<style lang="scss">
</style>
