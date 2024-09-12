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
    <div class="content-data flex-between" v-loading="loading">
      <div class="bar-box">
        <div class="flex-between">
          <div class="overview-title">
            <div class="overview-icon"></div>
            <span v-if="queryParams.quotaType == '0'">月度用量</span>
            <span v-if="queryParams.quotaType == '1'">年度用量</span>
          </div>
          <div>
            <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
              <span class="label-title">定额类型</span>
              <el-radio-group v-model="queryParams.quotaType" @change="change">
                <el-radio-button label="0">月度</el-radio-button>
                <el-radio-button label="1">年度</el-radio-button>
              </el-radio-group>
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
        <div class="data-chart" v-if="queryParams.quotaType == '0'">
          <MonthQuotaBar :x-data="date" :y-data="energyData" :legend-name="itemName"></MonthQuotaBar>
        </div>
        <div class="data-chart" v-if="queryParams.quotaType == '1'">
          <YearQuotaBar :x-data="date" :y-data="yearData" :setInfo="{ xName: '月份',yName: 'kM·h',legendName: [itemName+'定额数据', itemName+'能耗数据'],}"></YearQuotaBar>
        </div>
      </div>
      <div class="detail-box">
        <div class="overview-title">
          <div class="overview-icon"></div>
          <span v-if="queryParams.quotaType == '0'">月定额度详情</span>
          <span v-if="queryParams.quotaType == '1'">年度额度详情</span>
        </div>
        <div class="flex-between" style="height: calc(100% - 50px);">
          <div class="detail-label">
            <div style="display: flex;justify-content: flex-start;align-items:center;">
              <div class="detail-title">{{queryParams.quotaType == '0' ? '月定额度：' : '年定额度：'}}</div>
              <div v-if="queryParams.quotaType == '0'" class="data">
                <span v-if="analysisData.monthQuota">{{analysisData.monthQuota == 0.00 ? '不限' : analysisData.monthQuota + ' kW·h'}}</span>
                <span v-else>-- kW·h</span>
              </div>
              <div v-else class="data">
                <span v-if="analysisData.yearQuota">{{analysisData.yearQuota == 0.00 ? '不限' : analysisData.yearQuota + ' kW·h'}}</span>
                <span v-else>-- kW·h</span>
              </div>
            </div>
            <div style="display: flex;justify-content: flex-start;align-items:center;">
              <div class="detail-title">{{queryParams.quotaType == '0' ? '月累计能耗：': '年累计能耗：'}}</div>
              <div class="data">{{ queryParams.quotaType == '0' ? analysisData.monthTotal : analysisData.yearTotal }} kWh</div>
            </div>
            <div class="detail-title">已使用能耗占比：</div>
            <el-progress
              :text-inside="true"
              text-color="#fffff"
              :stroke-width="24"
              status="success"
              :percentage="queryParams.quotaType == '0' ? handleValue(analysisData.monthPer) : handleValue(analysisData.yearPer)"></el-progress>
            <div class="detail-title">已过时间占比：</div>
            <el-progress
              :text-inside="true"
              text-color="#fffff"
              :stroke-width="24"
              :percentage="queryParams.quotaType == '0' ? handleValue(analysisData.monthTimePer): handleValue(analysisData.yearTimePer)"></el-progress>
          </div>
          <div class="detail-bar">
            <QuotaChart height="80%" :pie-data="timePieData" item-title="时间" :residue="{ residueName: '剩余天数',residueValue: residue+'天',residueClass: '#3671e8',}"></QuotaChart>
            <div class="detail-bar-bottom">时间统计</div>
          </div>
          <div class="detail-bar">
            <QuotaChart height="80%" :pie-data="quotaPieData" item-title="定额" :residue="{ residueName: '剩余定额',residueValue: residueQuota+'kWh',residueClass: '#13ce66',}"></QuotaChart>
            <div class="detail-bar-bottom">定额统计</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { monitor } from "@/api/system/quota";
import topologicaTree from "@/components/TopologicaTree/index";
import moment from 'moment'
import YearQuotaBar from '@/views/dashboard/yearQuotaBar.vue'
import MonthQuotaBar from '@/views/dashboard/monthQuotaBar.vue'
import QuotaChart from '@/views/dashboard/quotaChart.vue'

export default {
  name: "Quota",
  dicts: ['quota_type'],
  components: {
    QuotaChart,
    MonthQuotaBar,
    YearQuotaBar,
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
      itemName:"",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dateType:"month",
        quotaType: "0",
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
      date:[],
      energyData:[],
      quotaData:[],
      yearData:{},
      //统计
      timePieData:[],
      quotaPieData:[],
      residue:0,
      residueQuota:0,
      loading: true, // 加载遮罩层
    };
  },
  created() {
    this.queryParams.quotaTime = moment().format("yyyy-MM-01 00:00:00");
    // this.search();
  },
  methods: {
    treeItem(value) {
      this.itemName = value.label;
      this.queryParams.itemId = value.id;
      this.search();
    },
    getTree(value){
      this.treeOptions = value;
    },
    change(){
      if(this.queryParams.quotaType == "0"){
        this.queryParams.dateType = "month"
        this.queryParams.quotaTime = moment().format("yyyy-MM-01 00:00:00");
      }if(this.queryParams.quotaType == "1"){
        this.queryParams.dateType = "year"
        this.queryParams.quotaTime = moment().format("yyyy-01-01 00:00:00");
      }
      this.search();
    },
    /** 查询按钮操作 */
    search() {
      this.loading = true
      if(this.queryParams.quotaType == "0"){
        this.queryParams.quotaTime = moment(this.queryParams.quotaTime).format("yyyy-MM-01 00:00:00")
      }if(this.queryParams.quotaType == "1"){
        this.queryParams.quotaTime = moment(this.queryParams.quotaTime).format("yyyy-01-01 00:00:00")
      }
      this.date = [];
      this.energyData = [];
      this.quotaData = [];
      this.timePieData = [];
      this.quotaPieData = [];
      monitor(this.queryParams).then(response => {
        this.analysisData = response.data;
        //统计
        this.residue = response.data.totalDay - response.data.date;
        this.timePieData.push({ value: response.data.date, name: "已过天数" },
          { value: this.residue, name: "剩余天数" })
        let totalQuota = 0;
        if(this.queryParams.quotaType == "0"){
          totalQuota = response.data.monthTotal;
          this.residueQuota = response.data.monthQuota - totalQuota;
        }if(this.queryParams.quotaType == "1"){
          totalQuota = response.data.yearTotal;
          this.residueQuota = response.data.yearQuota - totalQuota;
        }
        this.quotaPieData.push({ value: totalQuota, name: "累计能耗" },
          { value: this.residueQuota, name: "剩余定额" })
        response.data.dosageVoList.forEach(e => {
          this.date.push(e.date);
          this.energyData.push(e.energyData);
          this.quotaData.push(e.quotaData);
        });
        this.yearData = {
          quotaData: this.quotaData,
          energyData: this.energyData,
        }
      }).finally(() => { this.loading = false });
    },
    handleValue(value) {
      if(value) return Number(value)
      return 0
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 800px;
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

  .label-title{
    font-size: 14px;
    color:var(--base-color-1);
    font-weight: bold;
    margin-right: 12px
  }

  .content-tree {
    width: 25%;
    height: 100%;
    background: var(--base-item-bg);
    margin-right: 6px;
    padding: 20px;
  }

  .content-detail {
    width: 30%;
    height: 100%;
    background: var(--base-item-bg);
    margin-right: 6px;

    .year-overview,
    .month-overview{
      height: 50%;

      .percent-title{
        font-size: 13px;
        font-weight: bold;
        color: var(--base-color-2);
        margin-bottom: 10px;
      }
      .mini-title{
        height: 25%;
        width: 28%;
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
    width: calc(100% - 25% - 10px);
    height: 100%;
    flex-direction: column;

    .bar-box{
      height: 60%;
      background: var(--base-item-bg);
      padding: 20px;
    }

    .detail-box{
      // margin-top: 10px;
      height: calc(40% - 10px);
      background: var(--base-item-bg);
      padding: 20px;

      .detail-label{
        width: 40%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .detail-title{
          font-size: 20px;
          font-weight: bold;
          color: var(--base-color-1);
        }
        .data{
          font-size: 20px;
          font-weight: bold;
          color: var(--base-color-7);
        }
      }
      .detail-bar{
        width: 30%;
        height: 100%;

        .detail-bar-bottom{
          padding: 10px 0;
          height: 20%;
          font-size: 20px;
          font-weight: bold;
          color: var(--base-color-1);
          text-align: center;
        }
      }
    }

    .data-chart {
      height: calc(100% - 51px);
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
