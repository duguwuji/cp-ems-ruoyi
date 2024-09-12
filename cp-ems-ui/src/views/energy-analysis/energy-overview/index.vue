<template>
  <div class="app-container bg-container" v-loading="loading">
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="top-query-box">
          <el-form
            :model="queryParams"
            ref="queryForm"
            size="small"
            :inline="true"
            class="query-form"
          >
            <el-form-item prop="areaId">
              <treeselect
                v-model="queryParams.areaId"
                :options="areaList"
                :show-count="true"
                placeholder="请选择区域"
                style="width: 200px"
                @select="areaChange"
                :clearable="false"
              />
              <!-- <el-select v-model="queryParams.address" placeholder="请选择">
                <el-option
                  v-for="item in energyType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select> -->
            </el-form-item>
            <el-form-item prop="energyType">
              <el-select v-model="queryParams.energyType" placeholder="请选择" @change="typeChange">
                <el-option
                  v-for="item in dict.type.energy_type"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="12" class="overview-top">
      <el-col :sm="24" :lg="10">
        <div class="ratio-month">
          <div class="overview-title">
            <div class="overview-icon"></div>
            <span>环比</span>
          </div>
          <div class="ratio-content flex-column-between">
            <div class="flex-between content-row">
              <div class="ratio-item flex-column-center">
                <div>{{ chainData.now ? chainData.now : "--" }}</div>
                <span>今日用能({{showText.unit}})</span>
              </div>
              <div class="ratio-item flex-column-center">
                <div>{{ chainData.last ? chainData.last : "--" }}</div>
                <span>昨日同期({{showText.unit}})</span>
              </div>
              <div class="ratio-change flex-column-center">
                <div
                  v-if="chainData.nowPer"
                  :class="chainData.nowPer > 0 ? 'item-top' : 'item-top-down'"
                >
                  <span v-if="chainData.nowPer<=0">{{ (chainData.nowPer * 100).toFixed(2) }} </span>
                  <span v-if="chainData.nowPer>0">+{{ (chainData.nowPer * 100).toFixed(2) }} </span> %
                </div>
                <div
                  v-else
                  class="item-top empty-text"
                >
                  --
                </div>
                <div class="item-center">
                  <span>{{chainData.nowTrend > 0 ? ('+' + chainData.nowTrend) : chainData.nowTrend}}</span>
                  {{showText.unit}}
                </div>
                <div class="item-bottom">趋势</div>
              </div>
            </div>
            <div class="flex-between content-row">
              <div class="ratio-item flex-column-center">
                <div>{{ chainData.nowMonth ? chainData.nowMonth : "--" }}</div>
                <span>当月用能({{showText.unit}})</span>
              </div>
              <div class="ratio-item flex-column-center">
                <div>
                  {{ chainData.lastMonth ? chainData.lastMonth : "--" }}
                </div>
                <span>上月同期({{showText.unit}})</span>
              </div>
              <div class="ratio-change flex-column-center">
                <div
                  v-if="chainData.monthPer"
                  :class="chainData.monthPer > 0 ? 'item-top' : 'item-top-down'"
                >
                <span v-if="chainData.monthPer<=0">{{ (chainData.monthPer * 100).toFixed(2) }} </span>
                  <span v-if="chainData.monthPer>0">+{{ (chainData.monthPer * 100).toFixed(2) }} </span> %
                </div>
                <div
                  v-else
                  class="item-top empty-text"
                >
                  --
                </div>
                <div class="item-center">
                  <span>{{chainData.monthTrend > 0 ? ('+' + chainData.monthTrend) : chainData.monthTrend}}</span>
                  <!-- <span v-if="chainData.monthTrend<=0">{{
                    chainData.monthTrend ? chainData.monthTrend : "--"
                  }}</span>
                  <span v-if="chainData.monthTrend>0">{{
                    chainData.monthTrend ? ('+'+chainData.monthTrend) : "--"
                  }}</span> -->
                  {{showText.unit}}
                </div>
                <div class="item-bottom">趋势</div>
              </div>
            </div>
            <div class="flex-between content-row">
              <div class="ratio-item flex-column-center">
                <div>{{ chainData.nowYear ? chainData.nowYear : "--" }}</div>
                <span>今年用能({{showText.unit}})</span>
              </div>
              <div class="ratio-item flex-column-center">
                <div>{{ chainData.lastYear ? chainData.lastYear : "--" }}</div>
                <span>去年同期({{showText.unit}})</span>
              </div>
              <div class="ratio-change flex-column-center">
                <div
                  v-if="chainData.yearPer"
                  :class="chainData.yearPer > 0 ? 'item-top' : 'item-top-down'"
                >
                  <span v-if="chainData.yearPer<=0">{{ (chainData.yearPer * 100).toFixed(2) }} </span>
                  <span v-if="chainData.yearPer>0">+{{ (chainData.yearPer * 100).toFixed(2) }} </span> %
                </div>
                <div
                  v-else
                  class="item-top empty-text"
                >
                  --
                </div>
                <div class="item-center">
                  <span>{{chainData.yearTrend > 0 ? ('+' + chainData.yearTrend) : chainData.yearTrend}}</span>
                  <!-- <span v-if="chainData.yearTrend<=0">{{
                    chainData.yearTrend ? chainData.yearTrend : "--"
                  }}</span>
                  <span v-if="chainData.yearTrend>0">{{
                    chainData.yearTrend ? ('+'+chainData.yearTrend) : "--"
                  }}</span> -->
                  {{showText.unit}}
                </div>
                <div class="item-bottom">趋势</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :sm="24" :lg="14">
        <div class="energy-trend">
          <div class="overview-title">
            <div class="overview-icon"></div>
            <span>能耗趋势</span>
          </div>
          <div style="text-align: right">
            <el-radio-group v-model="tabPosition" @change="change">
              <el-radio-button label="day">当日</el-radio-button>
              <el-radio-button label="month">当月</el-radio-button>
              <el-radio-button label="year">当年</el-radio-button>
            </el-radio-group>
          </div>
          <div v-show="tabPosition === 'day'">
            <MinMaxBarChartVue
              v-if="tabPosition === 'day'"
              className="day-bar"
              :yName="showText.unit"
              :xName="title"
              :xData="xData"
              :yData="yData"
              height="307px"
            />
          </div>
          <div v-show="tabPosition === 'month'">
            <MinMaxBarChartVue
              v-if="tabPosition === 'month'"
              className="month-bar"
              :yName="showText.unit"
              :xName="title"
              :xData="xData"
              :yData="yData"
              height="307px"
            />
          </div>
          <div v-show="tabPosition === 'year'">
            <MinMaxBarChartVue
              v-if="tabPosition === 'year'"
              className="year-bar"
              :yName="showText.unit"
              :xName="title"
              :xData="xData"
              :yData="yData"
              height="307px"
            />
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="overview-bottom">
      <el-col :xs="24" :sm="20">
        <div class="daily-power">
          <div class="overview-title">
            <div class="overview-icon"></div>
            <span>{{showText.chartTitle}}</span>
          </div>
          <line-chart width="100%" :chartData="dailyP" :yName="showText.maxUnit" height="310px" />
        </div>
      </el-col>
      <el-col :xs="0" :sm="4">
        <div class="power-max">
          <div class="overview-title">
            <div class="overview-icon"></div>
            <span>{{showText.maxTitle}}</span>
          </div>
          <div class="max-content">
            <div class="max-top flex-column-center">
              <div>
                {{ typeof todayMax == "string" ? todayMax : todayMax.max }}
              </div>
              <span>{{
                typeof todayMax == "string" ? todayMax : todayMax.createTime
              }}</span>
            </div>
            <div class="max-bottom">当日({{showText.maxUnit}})</div>
          </div>
          <div class="max-content">
            <div class="max-top flex-column-center">
              <div>
                {{
                  typeof yesterdayMax == "string"
                    ? yesterdayMax
                    : yesterdayMax.max
                }}
              </div>
              <span>{{
                typeof yesterdayMax == "string" ? yesterdayMax : yesterdayMax.createTime
              }}</span>
            </div>
            <div class="max-bottom">昨日({{showText.maxUnit}})</div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import MinMaxBarChartVue from "@/views/dashboard/MinMaxBarChart.vue";
import LineChart from "@/views/dashboard/LineChart.vue";
import {
  getChainData,
  getDayTrend,
  getMonthTrend,
  getYearTrend,
  getDailyP,
  getWTrendByDay,
} from "@/api/system/energy";
import moment from "moment/moment";
import { topologyTreeSelect } from "@/api/system/itemTopology";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {timerInterval} from "@/settings"
export default {
  dicts: ['energy_type'],
  components: {
    MinMaxBarChartVue,
    LineChart,
    Treeselect,
  },
  data() {
    return {
      tabPosition: "day",
      xData: [],
      yData: [],
      title: "时间",
      chainData: {},
      dailyP: {},
      todayMax: {},
      yesterdayMax: {},
      queryParams: {
        // type: '0',
        // address: 1,
        areaId: 1,
        energyType: '0'
      },
      energyType: [
        {
          value: 0,
          label: "电",
          unit: 'kW.h',
          maxUnit: 'kW',
          chartTitle: '日用电功率曲线',
          maxTitle: '电功率峰值'
        },
        {
          value: 1,
          label: "水",
          unit: 't',
          maxUnit: 't',
          chartTitle: '日实时水流量曲线',
          maxTitle: '水流量峰值'
        },
      ],
      areaList: [],
      showText: {
        value: 0,
        label: "电",
        unit: 'kW.h',
        maxUnit: 'kW',
        chartTitle: '日用电功率曲线',
        maxTitle: '电功率峰值'
      },
      timer: null, // 定时器
      loading: true,
    };
  },
  methods: {
    change() {
      let date = moment().format("yyyy-MM-DD HH:mm:ss");
      this.xData = [];
      this.yData = [];
      let data = {
        ...this.queryParams,
        nowTime: date,
      };
      if (this.tabPosition === "day") {
        this.title = "时间";
        getDayTrend(data).then((res) => {
          this.xData = ['00:00','01:00','02:00','03:00','04:00','05:00','06:00','07:00','08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00'];
          if (res.data.length <= 0) {
            return;
          }
          this.xData = this.xData.slice(0, res.data.length)
          this.yData = res.data;
        });
      } else if (this.tabPosition === "month") {
        this.title = "日期";
        getMonthTrend(data).then((res) => {
          if (res.data.length <= 0) {
            this.xData = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30];
            return;
          }
          res.data.forEach((item, index) => {
            this.xData.push(index + 1);
          });
          this.yData = res.data;
        });
      } else if (this.tabPosition === "year") {
        this.title = "月份";
        getYearTrend(data).then((res) => {
          if (res.data.length <= 0) {
            this.xData = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
            return;
          }
          res.data.forEach((item, index) => {
            this.xData.push(index + 1);
          });
          this.yData = res.data;
        });
      }
    },
    getChain() {
      this.loading = true
      getChainData(this.queryParams).then((res) => {
        this.chainData = res.data;
      }).finally(() => {
        this.loading = false
      });
    },
    // 获取当日-昨日电功率曲线
    getDailyPData() {
      getDailyP(this.queryParams).then((res) => {
        let result = {
          todayData: res.data.todayData,
          yesterdayData: res.data.yesterdayData,
        };
        this.dailyP = result;
        this.todayMax = res.data.todayMax;
        this.yesterdayMax = res.data.yesterdayMax;
      });
    },
    // 获取当日-昨日水流量曲线
    getDailyWater() {
        let params = {
            ...this.queryParams,
            time: moment().format("yyyy-MM-DD")
        }
        getWTrendByDay(params).then(res => {
            let result = {
                todayData: res.data.currentList,
                yesterdayData: res.data.beforeList,
            }
            this.dailyP = result
            let beforeData = [], currentData = []
            res.data.beforeList.forEach(item => {
              if(!isNaN(item)) beforeData.push(item)
            })
            res.data.currentList.forEach(item => {
              if(!isNaN(item)) currentData.push(item)
            })
            let yMax = Math.max.apply(null, beforeData)
            let tMax = Math.max.apply(null, currentData)
            let todayStart = moment().format("yyyy-MM-DD")
            let yesterdayStart = moment().subtract(1, 'days').format('yyyy-MM-DD')
            if(yMax < 0) {
              this.yesterdayMax = '--'
            } else {
              let yIndex = res.data.beforeList.findIndex(i => i == yMax)
              this.yesterdayMax = {
                  createTime: moment(yesterdayStart).add(yIndex, 'hours').format("yyyy-MM-DD HH:mm:ss"),
                  max: yMax
              }
            }
            if(tMax < 0) {
              this.todayMax = '--'
            } else {
              let tIndex = res.data.currentList.findIndex(i => i == tMax)
              this.todayMax = {
                  createTime: moment(todayStart).add(tIndex, 'hours').format("yyyy-MM-DD HH:mm:ss"),
                  max: tMax
              }
            }
        })
    },
    // 获取区域拓扑
    getAreaList() {
      this.loading = true
      topologyTreeSelect({itemType: 'building'}).then((res) => {
        this.areaList = res.data;
        this.getData()
      }).finally(() => {
        this.loading = false
      });
    },
    areaChange(value) {
      this.queryParams.areaId = value.id;
      this.getData()
    },
    typeChange(value) {
        let item = this.energyType.find(t => t.value == value)
        if(item) {
            this.showText = item
        }
        this.getData()
    },
    getData() {
      // 定时获取数据
      if(this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
      this.change();
      this.getChain();
      this.queryParams.energyType == '0' ? this.getDailyPData() : this.getDailyWater()
      this.timer = setInterval(() => {
        this.change();
        this.getChain();
        this.queryParams.energyType == '0' ? this.getDailyPData() : this.getDailyWater()
      }, timerInterval);
    },
  },
  created() {
    this.getAreaList();
  },
  destroyed() {
    clearInterval(this.timer)
    this.timer = null
  }
};
</script>

<style lang='scss' scoped>
.app-container {
  min-height: calc(100vh - 84px);
  // height: auto;
  // height: calc(100vh - 84px);

  .top-query-box {
    background: var(--base-item-bg);
    margin-bottom: 12px;
    padding: 8px;
  }

  .overview-top,
  .overview-bottom {
    width: 100%;
    // height: 49%;

    .ratio-month,
    .energy-trend,
    .daily-power,
    .power-max {
      min-height: 350px;
      //   margin-bottom: 12px;
      background: var(--base-item-bg);
      padding: 12px;

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

      .ratio-content {
        height: calc(100% - 32px);
        // padding-top: 12px;

        .content-row {
          // height: 32%;
          font-size: 14px;
          .ratio-item {
            width: 32.5%;
            padding: 24px 0;
            border: 1px solid var(--border-color-1);
            align-items: center;
            margin-top: 6px;

            div {
              font-size: 28px;
              color: var(--base-color-2);
              margin-bottom: 6px;
            }

            span {
              color: var(--base-color-3);
            }
          }

          .ratio-change {
            width: 32.5%;
            // padding: 12px 0;
            border: 1px solid var(--border-color-1);
            align-items: center;
            margin-top: 6px;

            .item-top,
            .item-center,
            .item-bottom,
            .item-top-down {
              padding: 2px 0;
              width: 75%;
              text-align: center;
            }

            .item-top {
              color: #dd65a1;
              border-bottom: 1px solid var(--border-color-2);
              span {
                font-size: 22px;
              }
            }
            .item-top-down {
              color: #91cc75;
              border-bottom: 1px solid var(--border-color-2);
              span {
                font-size: 22px;
              }
            }

            .empty-text {
              color: var(--base-color-4);
              font-size: 22px
            }

            .item-center,
            .item-bottom {
              color: var(--base-color-3);

              span {
                font-size: 22px;
                color: var(--base-color-2);
              }
            }
          }
        }
      }

      .max-content {
        height: 120px;
        width: 90%;
        // min-width: 125px;
        margin: 35px auto 0;
        border: 1px solid var(--current-color);

        .max-top {
          height: 82px;
          align-items: center;
          font-size: 14px;
          color: var(--base-color-2);

          div {
            font-size: 22px;
          }
        }

        .max-bottom {
          height: 38px;
          color: #fff;
          text-align: center;
          line-height: 38px;
          background: var(--current-color);
        }
      }
    }

    .ratio-month,
    .energy-trend {
      margin-bottom: 12px;
    }
  }

  .query-form .el-form-item {
    margin-bottom: 0px;
  }
}
</style>
<style>
.app-container .energy-trend .el-radio-button--medium .el-radio-button__inner {
  padding: 8px 14px;
}
/* .app-container .el-row {
  margin-left: 0 !important;
  margin-right: 0 !important;
} */
</style>
