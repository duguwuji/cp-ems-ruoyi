<template>
  <div class="app-container flex-column-between bg-container">
    <div class="content-top flex-between">
      <div class="month-change">
        <div class="change-title">
          <div class="change-icon"></div>
          <span>本月碳排放</span>
        </div>
        <div class="flex-between carbon-box">
          <div class="carbon-item flex-column-center">
            <div class="mb10"><span>{{ chainData.nowMonth ? chainData.nowMonth : "--" }}</span> t</div>
            <div>当月</div>
          </div>
          <div class="carbon-item flex-column-center">
            <div class="mb10"><span>{{ chainData.lastMonth ? chainData.lastMonth : "--" }}</span> t</div>
            <div>上月同期</div>
          </div>
          <div class="carbon-item flex-column-center">
            <div class="mb10">
              <span v-if="chainData.yearTrend" :class="chainData.monthTrend > 0 ? 'change-red' : 'change-green'">{{ chainData.monthTrend }}</span>
              <span v-else>--</span>
            </div>
            <div>趋势</div>
          </div>
        </div>
      </div>
      <div class="year-change">
        <div class="change-title">
          <div class="change-icon"></div>
          <span>本年碳排放</span>
        </div>
        <div class="flex-between carbon-box">
          <div class="carbon-item flex-column-center">
            <div class="mb10"><span>{{ chainData.nowYear ? chainData.nowYear : "--" }}</span> t</div>
            <div>当年</div>
          </div>
          <div class="carbon-item flex-column-center">
            <div class="mb10"><span>{{ chainData.lastYear ? chainData.lastYear : "--" }}</span> t</div>
            <div>去年同期</div>
          </div>
          <div class="carbon-item flex-column-center">
            <div class="mb10">
              <span v-if="chainData.yearTrend" :class="chainData.yearTrend > 0 ? 'change-red' : 'change-green'">{{ chainData.yearTrend }}</span>
              <span v-else>--</span>
            </div>
            <div>趋势</div>
          </div>
        </div>
      </div>
    </div>
    <div class="content-bottom">
      <div class="data-select">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="分类能耗" prop="type">
            <el-select v-model="queryParams.type" placeholder="请选择">
              <el-option
                v-for="item in dict.type.energy_type"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="年份">
            <el-date-picker
              v-model="queryParams.year"
              type="year"
              placeholder="选择年份"
              value-format="yyyy"
              :clearable="false"
              :editable="false"
              :picker-options="pickerOptions"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="getMonthData">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="data-chart">
        <BarChart height="100%" :yName="unit" :xName="title" :xData="xData" :yData="yData"/>
      </div>
    </div>
  </div>
</template>

<script>
import BarChart from '@/views/dashboard/BarChart';
import moment from 'moment'
import { getChain , getChainByYear } from '@/api/system/energy'
export default {
  dicts: ['energy_type'],
  components: {
    BarChart
  },
  data() {
    return {
      queryParams: {
        type: '0',
        year: "",
      },
      energyType: [
        {
          value: 0,
          label: '电'
        },
        {
          value: 1,
          label: '水'
        },
      ],
      chainData: {},
      xData: [],
      yData: [],
      title: "月份",
      unit: "t",
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() > Date.now(); // 禁用大于今天的日期
        },
      },
    }
  },
  created() {
    this.queryParams.year = moment().format("yyyy");
    this.getChain();
    this.getMonthData();
  },
  methods: {
    getChain() {
      getChain().then(res=>{
        this.chainData = res.data;
      })
    },
    getMonthData(){
      this.xData = [];
      this.yData = [];
      let data = {
        energyType: this.queryParams.type,
        date: this.queryParams.year,
      };
      getChainByYear(data).then(res=>{
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
};
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 700px;
  height: calc(100vh - 84px);

  .content-top {
    width: 100%;
    height: 30%;

    .month-change {
      width: calc((100% - 20px)/2);
      margin-right: 20px;
    }
    .year-change {
      width: calc((100% - 20px)/2);
    }

    .month-change,
    .year-change {
      height: 100%;
      background: var(--base-item-bg);
      padding: 12px;

      .change-title {
        height: 30px;
        line-height: 30px;
        display: flex;
        color: var(--base-color-1);

        .change-icon {
          width: 5px;
          height: 20px;
          margin: 5px 10px 0 0;
          background: var(--current-color);
        }
      }

      .carbon-box {
        height: calc(100% - 30px);
        padding: 24px;
        .carbon-item {
          height: 100%;
          // border: 1px solid;
          width: 28%;
          align-items: center;
          color: var(--base-color-3);
          span {
            font-size: 35px;
            font-weight: bold;
            color: var(--base-color-2);
          }
          @media screen and (max-width: 664px) {
            span {
              font-size: 20px;
            }
          }

          .change-green {
            color: #49a798;
            &::after {
              content: "↓";
              position: relative;
              left: 5px;
              top: -3px;
            }
          }
          .change-red {
            color: #dd65a1;
            &::after {
              content: "↑";
              position: relative;
              left: 5px;
              top: -3px;
            }
          }
        }
      }
    }
  }

  .content-bottom {
    width: 100%;
    height: 67%;
    background: var(--base-item-bg);
    padding: 12px;

    .data-chart {
      height: calc(100% - 51px);
    }
    @media screen and (max-width: 689px) {
      .data-chart {
        height: calc(100% - 101px);
      }
    }
  }
}
</style>
