<template>
  <div class="app-container bg-container" v-loading="loading">
    <div class="itemized-top">
      <div style="margin-bottom: 12px; padding: 8px;">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          class="query-form"
        >
          <el-form-item label="能源类型" prop="itemId">
            <el-select v-model="queryParams.itemId" placeholder="请选择">
              <el-option
                v-for="item in topologyTree"
                :key="item.id"
                :label="item.label"
                :value="item.id"
                :disabled="!item.children"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期" prop="dateType">
            <el-select
              v-model="queryParams.dateType"
              placeholder="请选择"
              style="width: 80px"
              @change="dateTypeChange"
            >
              <el-option
                v-for="item in dateTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="queryParams.date"
              :format="queryParams.dateType === 'week' ? 'yyyy-WW' : ''"
              value-format="yyyy-MM-dd HH:mm:ss"
              :type="queryParams.dateType"
              placeholder="选择时间"
              :picker-options="pickerOptions"
              @change="dateChange"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="search"
              >查询</el-button
            >
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="itemized-middle">
      <el-row :gutter="12">
        <el-col  :lg="8" :sm="24">
          <div class="itemized-percent">
            <div class="overview-title">
              <div class="overview-icon"></div>
              <span>能耗排行</span>
            </div>
            <div><ItemizedRankingBarVue height="400px" :barData="rankingBarData" /></div>
          </div>
        </el-col>
        <el-col  :lg="16" :sm="24">
          <div class="itemized-total">
            <div class="overview-title">
              <div class="overview-icon"></div>
              <span>分项统计</span>
            </div>
            <div><ItemizedBarVue height="400px" :barData="barData" :xName="showText.xName"/></div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="itemized-bottom">
      <el-row :gutter="12">
        <el-col :lg="12" :sm="24" v-for="(item, index) in barData" :key="index">
          <div class="itemized-bottom-item">
            <div class="overview-title">
              <div class="overview-icon"></div>
              <span>{{item.itemizeName}}</span>
            </div>
            <div class="ratio-content flex-column-between">
              <div class="flex-between content-row">
                <div class="ratio-item flex-column-center">
                  <div>{{ item.nowEnergy ? item.nowEnergy : "--" }}</div>
                  <span>今日用能({{ showText.unit }})</span>
                </div>
                <div class="ratio-item flex-column-center">
                  <div>{{ item.lastEnergy ? item.lastEnergy : "--" }}</div>
                  <span>昨日同期({{ showText.unit }})</span>
                </div>
                <div class="ratio-change flex-column-center">
                  <div
                    v-if="item.nowPer"
                    :class="item.nowPer > 0 ? 'item-top' : 'item-top-down'"
                  >
                    <span v-if="item.nowPer<=0">{{ (item.nowPer * 100).toFixed(2) }} </span>
                  <span v-if="item.nowPer>0">+{{ (item.nowPer * 100).toFixed(2) }} </span> %
                  </div>
                  <div
                    v-else
                    class="item-top empty-text"
                  >
                    --
                  </div>
                  <div class="item-center">
                    <span>{{item.nowTrend > 0 ? ('+' + item.nowTrend) : item.nowTrend}}</span>
                    <!-- <span v-if="item.nowTrend<=0">{{
                      item.nowTrend ? item.nowTrend : "--"
                    }}</span>
                    <span v-if="item.nowTrend>0">{{
                      item.nowTrend ? ('+'+item.nowTrend) : "--"
                    }}</span> -->
                    {{ showText.unit }}
                  </div>
                  <div class="item-bottom">趋势</div>
                </div>
              </div>
              <div class="flex-between content-row">
                <div class="ratio-item flex-column-center">
                  <div>
                    {{ item.nowMonthEnergy ? item.nowMonthEnergy : "--" }}
                  </div>
                  <span>当月用能({{ showText.unit }})</span>
                </div>
                <div class="ratio-item flex-column-center">
                  <div>
                    {{ item.lastMonthEnergy ? item.lastMonthEnergy : "--" }}
                  </div>
                  <span>上月同期({{ showText.unit }})</span>
                </div>
                <div class="ratio-change flex-column-center">
                  <div
                    v-if="item.monthPer"
                    :class="
                      item.monthPer > 0 ? 'item-top' : 'item-top-down'
                    "
                  >
                    <span v-if="item.monthPer<=0">{{ (item.monthPer * 100).toFixed(2) }} </span>
                  <span v-if="item.monthPer>0">+{{ (item.monthPer * 100).toFixed(2) }} </span> %
                  </div>
                  <div
                    v-else
                    class="item-top empty-text"
                  >
                    --
                  </div>
                  <div class="item-center">
                    <span>{{item.monthTrend > 0 ? ('+' + item.monthTrend) : item.monthTrend}}</span>
                    <!-- <span v-if="item.monthTrend<=0">{{
                      item.monthTrend ? item.monthTrend : "--"
                    }}</span>
                    <span v-if="item.monthTrend>0">{{
                      item.monthTrend ? ('+'+item.monthTrend) : "--"
                    }}</span> -->
                    {{ showText.unit }}
                  </div>
                  <div class="item-bottom">趋势</div>
                </div>
              </div>
              <div class="flex-between content-row">
                <div class="ratio-item flex-column-center">
                  <div>{{ item.nowYearEnergy ? item.nowYearEnergy : "--" }}</div>
                  <span>今年用能({{ showText.unit }})</span>
                </div>
                <div class="ratio-item flex-column-center">
                  <div>
                    {{ item.lastYearEnergy ? item.lastYearEnergy : "--" }}
                  </div>
                  <span>去年同期({{ showText.unit }})</span>
                </div>
                <div class="ratio-change flex-column-center">
                  <div
                    v-if="item.yearPer"
                    :class="
                      item.yearPer > 0 ? 'item-top' : 'item-top-down'
                    "
                  >
                    <span v-if="item.yearPer<=0">{{ (item.yearPer * 100).toFixed(2) }} </span>
                    <span v-if="item.yearPer>0">+{{ (item.yearPer * 100).toFixed(2) }} </span> %
                  </div>
                  <div
                    v-else
                    class="item-top empty-text"
                  >
                    --
                  </div>
                  <div class="item-center">
                    <span>{{item.yearTrend > 0 ? ('+' + item.yearTrend) : item.yearTrend}}</span>
                    <!-- <span v-if="item.yearTrend<=0">{{
                      item.yearTrend ? item.yearTrend : "--"
                    }}</span>
                    <span v-if="item.yearTrend>0">{{
                      item.yearTrend ? ('+'+item.yearTrend) : "--"
                    }}</span> -->
                    {{ showText.unit }}
                  </div>
                  <div class="item-bottom">趋势</div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import moment from "moment";
import ItemizedBarVue from "./ItemizedBar.vue";
import ItemizedRankingBarVue from './ItemizedRankingBar.vue';
import { topologyTreeSelect } from "@/api/system/itemTopology";
import {itemizedOverview} from "@/api/system/itemizedAnalysis"
export default {
  name: "itemizedOverview",
  // dicts: ["energy_type"],
  components: {
    ItemizedBarVue,
    ItemizedRankingBarVue
  },
  data() {
    return {
      queryParams: {
        itemId: "0",
        dateType: "date",
        date: moment(new Date()).format("yyyy-MM-DD HH:mm:ss"),
        dateTime: moment(new Date()).format("yyyy-MM-DD HH:mm:ss")
      },
      dateTypeList: [
        {
          value: "date",
          label: "日",
        },
        // {
        //   value: "week",
        //   label: "周",
        // },
        {
          value: "month",
          label: "月",
        },
        {
          value: "year",
          label: "年",
        },
      ],
      rankingBarData: {},
      showText: {
        unit: "kW·h",
        xName: '时'
      },
      topologyTree: [],
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() > Date.now(); // 禁用大于今天的日期
        },
      },
      barData: [],
      loading: true,
    };
  },
  created() {
    this.getTopologyTree()
  },
  methods: {
    search() {
      this.getOverView()
    },
    // 日期类型改变
    dateTypeChange(value) {
      if(value == 'date') {
        this.queryParams.date = this.queryParams.dateTime
        this.showText.xName = '时'
      }
      if(value == 'month') {
        this.showText.xName = '日'
      }
      if(value == 'year') {
        this.showText.xName = '月份'
      }
    },
    // 时间改变
    dateChange(value) {
      if(this.queryParams.dateType == 'date' || this.queryParams.dateType == 'week') {
        this.queryParams.dateTime = value
      }
      let nowDate = this.queryParams.dateTime
      if(this.queryParams.dateType == 'month') {
        this.queryParams.date = value.substr(0, 7) + nowDate.slice(7)
      }
      if(this.queryParams.dateType == 'year') {
        this.queryParams.date = value.substr(0, 4) + nowDate.slice(4)
      }
      this.queryParams.dateTime = this.queryParams.date
    },
    // 获取能源分项数
    getTopologyTree() {
      topologyTreeSelect({itemType: 'energySubsections'}).then(res => {
        this.topologyTree = res.data[0] ? res.data[0].children : []
        this.queryParams.itemId = this.topologyTree[0].id
      }).then(() => {
        this.getOverView()
      })
    },
    // 获取分项概览数据
    getOverView() {
      this.loading = true
      itemizedOverview(this.queryParams).then(res => {
        this.barData = res.data

        // 先升序
        let result = res.data
        result.sort((a, b) => { return a.itemizeTotal - b.itemizeTotal })
        let rankingData = {
          xData: [], yData: []
        }
        result.forEach((item => {
          rankingData.xData.push(item.itemizeName)
          rankingData.yData.push(item.itemizeTotal)
        }))
        this.rankingBarData = rankingData
      }).finally(() => {
        this.loading = false
      })
    }
  },
};
</script>

<style scoped>
.app-container {
  min-height: calc(100vh - 84px);
}
.itemized-top {
  background: var(--base-item-bg);
}
.query-form .el-form-item {
  margin-bottom: 0px;
}
.itemized-middle {
  margin-bottom: 12px;
}
.itemized-percent,
.itemized-total {
  padding: 12px;
  background: var(--base-item-bg);
}
.overview-title {
  height: 30px;
  line-height: 30px;
  display: flex;
  color: var(--base-color-1);
}
.overview-icon {
  width: 5px;
  height: 20px;
  margin: 5px 10px 0 0;
  background: var(--current-color);
}
.itemized-bottom {
  background: var(--base-item-bg);
  /* padding: 12px; */
}
.itemized-bottom-item {
  /* border: 1px solid; */
  background: var(--base-item-bg);
  padding: 12px;
  min-height: 400px;
  /* margin-bottom: 12px; */
}
.ratio-content {
  height: calc(100% - 32px);
}
.content-row {
  font-size: 14px;
}
.ratio-item {
  width: 32.5%;
  padding: 24px 0;
  border: 1px solid var(--border-color-1);
  align-items: center;
  margin-top: 6px;
}
.ratio-item div {
  font-size: 28px;
  color: var(--base-color-2);
  margin-bottom: 6px;
}

.ratio-item span {
  color: var(--base-color-3);
}
.ratio-change {
  width: 32.5%;
  border: 1px solid var(--border-color-1);
  align-items: center;
  margin-top: 6px;
}
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
}
.item-top span {
  font-size: 22px;
}
.item-top-down {
  color: #91cc75;
  border-bottom: 1px solid var(--border-color-2);
}
.item-top-down span {
  font-size: 22px;
}
.empty-text {
  color: var(--base-color-4);
  font-size: 22px
}

.item-center,
.item-bottom {
  color: var(--base-color-3);
}
.item-center span,
.item-bottom span {
  font-size: 22px;
  color: var(--base-color-2);
}
</style>
