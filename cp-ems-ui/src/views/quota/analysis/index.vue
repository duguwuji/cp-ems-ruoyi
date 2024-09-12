<template>
  <div class="app-container flex-between bg-container">
    <div class="content-tree">
      <div>
        <el-tabs v-model="item" type="card">
          <el-tab-pane name="first">
            <span slot="label"><i class="el-icon-help"></i> 能源区域</span>
            <topologicaTree v-if="item=='first'" item-type="building" :showCheckbox="false" @selectItem="treeItem($event)"/>
          </el-tab-pane>
          <el-tab-pane name="second">
            <span slot="label"><i class="el-icon-set-up"></i> 能源分项</span>
            <topologicaTree v-if="item=='second'" item-type="energySubsections" :showCheckbox="false" @selectItem="treeItem($event)"/>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <div class="content-detail" v-loading="loading">
      <div class="month-overview">
        <div class="overview-title">
          <div class="overview-icon"></div>
          <span>月能耗概述</span>
        </div>
        <div class="mini-title">
          <div class="mb10">月累计能耗</div>
          <div class="unit"><span>{{ analysisData.monthTotal }}</span> kW.h</div>
        </div>
        <div class="mini-title">
          <div class="mb10">月总定额</div>
          <div class="unit" v-if="analysisData.monthQuota == 0.00"><span>不限</span></div>
          <div class="unit" v-else><span>{{ analysisData.monthQuota }}</span> kW.h</div>
        </div>
        <div style="display: flex">
          <div class="percent-title">已过时间占比：</div>
          <div class="unit">{{ analysisData.monthTimePer }} %</div>
        </div>
        <div>
          <el-progress v-if="analysisData.monthTimePer" :text-inside="true" text-color="#fffff" :stroke-width="24" :percentage="Number(analysisData.monthTimePer)"></el-progress>
        </div>
        <div style="display: flex;margin-top: 10px">
          <div class="percent-title">当前累计能耗占月定额比：</div>
          <div class="unit">{{ analysisData.monthPer }} %</div>
        </div>
        <div>
          <el-progress v-if="analysisData.monthPer" :text-inside="true" text-color="white" :stroke-width="24" :percentage="Number(analysisData.monthPer)" status="success"></el-progress>
        </div>
      </div>
      <div class="year-overview">
        <div class="overview-title">
          <div class="overview-icon"></div>
          <span>年能耗概述</span>
        </div>
        <div class="mini-title">
          <div class="mb10">年累计能耗</div>
          <div class="unit"><span>{{ analysisData.yearTotal }}</span> kW.h</div>
        </div>
        <div class="mini-title">
          <div class="mb10">年总定额</div>
          <div class="unit" v-if="analysisData.yearQuota == 0.00"><span>不限</span></div>
          <div class="unit" v-else><span>{{ analysisData.yearQuota }}</span> kW.h</div>
        </div>
        <div style="display: flex">
          <div class="percent-title">已过时间占比：</div>
          <div class="unit">{{ analysisData.yearTimePer }} %</div>
        </div>
        <div>
          <el-progress v-if="analysisData.yearTimePer" :text-inside="true" text-color="#fffff" :stroke-width="24" :percentage="Number(analysisData.yearTimePer)"></el-progress>
        </div>
        <div style="display: flex;margin-top: 10px">
          <div class="percent-title">当前累计能耗占年定额比：</div>
          <div class="unit">{{ analysisData.yearPer }} %</div>
        </div>
        <div>
          <el-progress v-if="analysisData.yearPer" :text-inside="true" text-color="white" :stroke-width="24" :percentage="Number(analysisData.yearPer)" status="success"></el-progress>
        </div>
      </div>
    </div>
    <div class="content-data flex-between" v-loading="loading">
      <div class="self-box">
        <div class="overview-title flex-between">
          <div style="display: flex">
            <div class="overview-icon"></div>
            <span>自标(月度)</span>
          </div>
          <div>
            <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
              <el-form-item label="时间" prop="quotaTime">
                <el-date-picker :clearable="false"
                                :editable="false"
                                v-model="queryParams.quotaTime"
                                :type="queryParams.dateType"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="请选择定额时间"
                                :picker-options="pickerOptions">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div class="flex-between carbon-box">
          <div class="carbon-item flex-column-center">
            <div class="param-title">同比</div>
            <div class="mb10">
              <span v-if="analysisData.yoyPer != '0.00'" :class="parseFloat(analysisData.yoyPer) > 0 ? 'change-red' : 'change-green'">{{ analysisData.yoyPer }}%</span>
              <span v-else>{{ analysisData.yoyPer }}%</span>
            </div>
            <div class="param-title-mini">同比能耗
              <div style="margin: 2px;color: #FF4500">{{ analysisData.monthYoy }}</div>
              <div style="margin-top: 2px">kW.h</div>
            </div>
          </div>
          <div class="carbon-item flex-column-center">
            <div class="param-title">环比</div>
            <div class="mb10">
              <span v-if="analysisData.chainPer != '0.00'" :class="parseFloat(analysisData.chainPer) > 0 ? 'change-red' : 'change-green'">{{ analysisData.chainPer }}%</span>
              <span v-else>{{ analysisData.chainPer }}%</span>
            </div>
            <div class="param-title-mini">环比能耗
              <div style="margin: 2px;color: #FF4500">{{ analysisData.monthChain }}</div>
              <div style="margin-top: 2px">kW.h</div>
            </div>
          </div>
          <div class="carbon-item flex-column-center">
            <div class="param-title">余量</div>
            <div class="mb10">
              <span v-if="analysisData.residuePer != '0.00'" style="color: #6495ed">{{ analysisData.residuePer }}%</span>
              <span v-else>{{ analysisData.residuePer }}%</span>
            </div>
            <div class="param-title-mini">剩余定额
              <div style="margin: 2px;color: #FF4500">{{ analysisData.residue }}</div>
              <div style="margin-top: 2px">kW.h</div>
            </div>
          </div>
        </div>
      </div>
      <div class="table-box">
        <div class="overview-title">
          <div class="overview-icon"></div>
          <span>能耗对标</span>
        </div>
        <div class="data-table">
          <quota-table :chain-data="chainData"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { analysis } from "@/api/system/quota";
import topologicaTree from "@/components/TopologicaTree/index";
import moment from 'moment'
import QuotaTable from '@/components/Tables/quotaTable.vue'

export default {
  name: "Quota",
  dicts: ['quota_type'],
  components: {
    QuotaTable,
    topologicaTree,
  },
  data() {
    return {
      // 定额配置表格数据
      chainData: [],
      //选项卡
      item:"first",
      //括朴类型
      itemType:"building",
      treeOptions:[],
      itemId:undefined,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateType:"month",
        quotaType: undefined,
        itemId: undefined,
        quotaTime: undefined,
        quotaValue: undefined,
        realEnergy: undefined,
        critical: undefined,
        overMedian: undefined,
      },
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() > Date.now(); // 禁用大于今天的日期
        },
      },
      //定额分析数据
      analysisData:{},
      loading: true, // 加载遮罩层
    };
  },
  created() {
    this.queryParams.quotaTime = moment().format("yyyy-MM-01 00:00:00");
    // this.search();
  },
  methods: {
    treeItem(value) {
      this.itemId = value.id;
      this.queryParams.itemId = value.id;
      this.search();
    },
    getTree(value){
      this.treeOptions = value;
    },
    /** 查询按钮操作 */
    search() {
      this.loading = true
      analysis(this.queryParams).then(response => {
        this.analysisData = response.data;
        this.chainData = response.data.quotaList;
      }).finally(() => {
        this.loading = false
      });
    },
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 700px;
  height: calc(100vh - 84px);

  .overview-title {
    height: 30px;
    line-height: 30px;
    display: flex;
    margin-bottom: 20px;
    color: var(--base-color-1);

    .overview-icon {
      width: 5px;
      height: 20px;
      margin: 5px 10px 0 0;
      background: var(--current-color);
    }
  }

  .content-tree {
    width: 25%;
    height: 100%;
    background: var(--base-item-bg);
    margin-right: 6px;
    padding: 20px;
  }
  .content-detail {
    width: 25%;
    height: 100%;
    background: var(--base-item-bg);
    margin-right: 6px;
    padding: 20px;

    .year-overview,
    .month-overview{
      height: 50%;

      .percent-title{
        font-size: 13px;
        font-weight: bold;
        color: var(--base-color-2);
        margin-bottom: 10px;
      }
      .unit {
        color: var(--base-color-1);
      }
      .mini-title{
        height: 25%;
        width: 100%;
        font-weight: bold;
        align-items: center;
        font-size: 13px;
        color: var(--base-color-2);
        .unit{
          font-size: 20px;
          font-weight: bold;
          color: var(--base-color-2);
        }
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
      }
    }

  }
  .content-data {
    width: calc(100% - 50% - 6px);
    height: 100%;
    flex-direction: column;

    .self-box{
      height: 40%;
      background: var(--base-item-bg);
      padding: 20px;

      .carbon-box {
        height: calc(100% - 30px);
        //padding: 10px;
        .carbon-item {
          height: 100%;
          // border: 1px solid;
          width: 28%;
          align-items: center;
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

          .param-title{
            margin-bottom: 10px;
            color: var(--base-color-1);
            font-size: 20px;
            font-weight: bold;
            margin-right: 5px;
          }
          .param-title-mini{
            font-weight: bold;
            display: flex;
            color: var(--base-color-2);
            font-size: 14px;
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

    .table-box{
      margin-top: 6px;
      height: calc(100% - 40% - 6px);
      background: var(--base-item-bg);
      padding: 20px;
    }

    .data-chart {
      height: 40%;
      // border: 1px solid;
    }

    .data-table {
      height: calc(100% - 51px);
      // border: 1px solid;
    }
    @media screen and (max-width: 1262px) {
      .data-table {
        height: calc(100% - 101px);
      }
    }
    @media screen and (max-width: 645px) {
      .data-table {
        height: calc(100% - 151px);
      }
    }

  }
}
</style>
