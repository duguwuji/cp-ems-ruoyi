<template>
  <div class="app-container flex-between bg-container">
    <div class="content-left">
      <el-date-picker
        v-model="dateRange"
        style="width: 100%;min-width:240px;"
        value-format="yyyy-MM-dd HH:mm:ss"
        type="daterange"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :clearable="false"
        :editable="false"
        :default-time="['00:00:00', '23:59:59']"
      ></el-date-picker>
      <div class="content-tree">
        <topologicaTree @selectItem="treeItem($event)" />
      </div>
      <div class="button-confirm">
        <el-button type="primary" @click="search">生成分析报告</el-button>
      </div>
    </div>
    <div class="content-right">
      <el-button class="print-btn" type="primary" icon="el-icon-printer" v-print="printObj">打印</el-button>
      <!-- 这里是需要打印的内容 -->
      <div id="printableContent">
        <div class="print-cover">
          <div class="report-name">分析报告</div>
          <div class="report-date" v-if="show">{{ dateRange[0].split(" ")[0] }}~{{ dateRange[1].split(" ")[0] }}</div>
        </div>
        <div class="print-content" v-if="show">
          <div class="report-title">1、用能统计</div>
          <div style="padding: 20px 20px; height: 261px;width: 100%">
            <report-table :chain-data="tableData"/>
          </div>
          <div class="report-title">2、用电量</div>
          <div class="report-content">本周期内，共计使用电力{{electricityTotal}}kW·h，最大用电量{{electricityMax}}kW·h， 最大负荷发生时间{{electricityMaxDate}}。</div>
          <div style="padding: 20px 20px;">
            <report-chart :yName="'kW.h'" :xName="title" :xData="xData" :yData="electricityData" :title="'用电量'"/>
          </div>
          <div class="report-content">建议：通过在变压器下端增多监测回路，收集末端数据，判断各个监测点的能耗情况，有无电量浪费现象。或将楼宇内照明等设备更换节能产品。降低能耗，实现节能减排。</div>
          <div class="report-title">3、电费</div>
          <div class="report-content">本周期内，共计使用电费￥{{ electricityChargingTotal }} 元</div>
          <div style="padding: 20px 20px;">
            <report-chart :yName="'￥'" :xName="title" :xData="xChargingData" :yData="electricityChargingData" :title="'电费'"/>
          </div>
          <div class="report-title">4、用水量</div>
          <div class="report-content">本周期内，共计使用水{{ waterTotal }}t，最大用水量{{waterMax}}t，最大流量发生时间{{waterMaxDate}}。</div>
          <div style="padding: 20px 20px;">
            <report-chart :yName="'t'" :xName="title" :xData="xData" :yData="waterData" :title="'用水量'"/>
          </div>
          <div class="report-title">5、水费</div>
          <div class="report-content">本周期内，共计使用水费￥{{ waterChargingTotal }} 元</div>
          <div style="padding: 20px 20px;">
            <report-chart :yName="'￥'" :xName="title" :xData="xChargingData" :yData="waterChargingData" :title="'水费'"/>
          </div>
          <div class="report-title">6、复费率</div>
          <div style="padding: 20px 20px;">
            <div style="height: 221px;width: 100%">
              <RecurringRateTableVue :chainData="recurringRateTableData"/>
            </div>
            <RecurringRatePieVue :pieData="recurringRatePie"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import topologicaTree from "@/components/TopologicaTree/index";
import ReportTable from '@/components/Tables/reportTable.vue';
import { getReportChart, getReportTable, getReportChargingChart, getRecurringRate } from '@/api/system/energy'
import ReportChart from '@/views/analysis-report/reportChart.vue'
import RecurringRatePieVue from './recurringRatePie.vue';
import RecurringRateTableVue from './recurringRateTable.vue';
export default {
  components: {
    ReportChart,
    ReportTable,
    topologicaTree,
    RecurringRatePieVue,
    RecurringRateTableVue
  },
  data() {
    return {
      printObj: {
        id: "printableContent", // 必要，打印元素的id
        // popTitle: "", // 打印配置页上方标题
        // extraHead: "", //最上方的头部文字，附加在head标签上的额外标签,使用逗号分隔
        // preview: "", // 是否启动预览模式，默认是false（开启预览模式，可以先预览后打印）
        // previewTitle: "", // 打印预览的印标题（开启预览模式后出现）,
        // previewPrintBtnLabel: "", // 打预览的标题的下方按钮文本，点击可进入打印（开启预览模式后出现）
        // zIndex: "", // 预览的窗口的z-index，默认是 20002（此值要高一些，这涉及到预览模式是否显示在最上面）
        // previewBeforeOpenCallback() {}, //预览窗口打开之前的callback（开启预览模式调用）
        // previewOpenCallback() {}, // 预览窗口打开之后的callback（开启预览模式调用）
        // beforeOpenCallback() {}, // 开启打印前的回调事件
        // openCallback() {}, // 调用打印之后的回调事件
        // closeCallback() {}, //关闭打印的回调事件（无法确定点击的是确认还是取消）
        // url: "",
        // standard: "",
        // extraCss: "",
      },
      dateRange: [],
      areaId:1,
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() > Date.now() // 禁用大于今天的日期
        },
      },
      unit: 'kW.h',
      xData: [],
      xChargingData:[],
      electricityChargingData:[],
      waterChargingData:[],
      electricityData: [],
      waterData:[],
      title: "日期",
      electricityTotal:0,
      electricityMax:0,
      electricityMaxDate:"",
      waterTotal:0,
      waterMax:0,
      waterMaxDate:"",
      electricityChargingTotal:0,
      waterChargingTotal:0,
      show:false,
      tableData:[],
      recurringRateTableData: [],
      recurringRatePie: []
    };
  },
  methods: {
    defaultDate(){
      this.dateRange = [moment().format("yyyy-MM-01 00:00:00"),moment().format("yyyy-MM-DD 23:59:59")]
    },
    treeItem(value) {
      this.areaId = value.id;
    },
    search(){
      this.getReport("0")
      this.getReport("1")
      this.getTable()
      this.getRecurringRateData()
    },
    getTable(){
      let query={
        areaId:this.areaId,
        startTime:this.dateRange[0],
        endTime:this.dateRange[1],
      }
      getReportTable(query).then(res=>{
        this.tableData = res.data;
      })
    },
    getReport(type){
      let query={
        areaId:this.areaId,
        startTime:this.dateRange[0],
        endTime:this.dateRange[1],
        energyType:type,
      }
      getReportChart(query).then(res=>{
        if (res.data.length <= 0) {
          return;
        }
        this.xData = Object.keys(res.data.chartData);
        if(type == "0"){
          this.electricityTotal = res.data.total;
          this.electricityMax = res.data.max;
          this.electricityMaxDate = res.data.maxDate;
          this.electricityData = Object.values(res.data.chartData);
        }else if(type == "1"){
          this.waterTotal = res.data.total;
          this.waterMax = res.data.max;
          this.waterMaxDate = res.data.maxDate;
          this.waterData = Object.values(res.data.chartData);
        }
        this.show = true;
      })
      getReportChargingChart(query).then(res=>{
        if (res.data.length <= 0) {
          return;
        }
        this.xChargingData = Object.keys(res.data.chartData);
        if(type == "0"){
          this.electricityChargingTotal = res.data.total;
          this.electricityChargingData = Object.values(res.data.chartData);
        }else if(type == "1"){
          this.waterChargingTotal = res.data.total;
          this.waterChargingData = Object.values(res.data.chartData);
        }
        this.show = true;
      })
    },
    // 复费率
    getRecurringRateData() {
      let query={
        areaId:this.areaId,
        startTime:this.dateRange[0],
        endTime:this.dateRange[1],
        energyType:'0',
      }
      getRecurringRate(query).then(res => {
          this.recurringRateTableData = res.data
          let pieData = []
          res.data.forEach(item => {
            pieData.push({value: item.data, name: item.type})
          })
          this.recurringRatePie = pieData
      })
    }
  },
  created() {
    this.defaultDate();
  }
};
</script>
<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 84px);
  /* padding: 6px; */
  /* background: #f6f6f6; */
}
.content-left {
  width: 25%;
  height: 100%;
  padding: 16px;
  background: var(--base-item-bg);
  overflow: auto;
}
.content-right {
  width: 75%;
  height: 100%;
  padding: 0 14px;
  overflow: auto;
}
#printableContent {
  width: 794px;
  margin: 0 auto;
  padding-top: 1%;
}
.print-btn {
  position: absolute;
}
.print-cover {
  width: 100%;
  height: 1116px;
  background-image: url("../../assets/images/cover.png");
  background-size: 100% 100%;
}
.print-content{
  width: 100%;
  background: #fff;
  /*margin-top: 2%;*/
}
/* .content-right::-webkit-scrollbar,
.content-left::-webkit-scrollbar {
  position: absolute;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: transparent;
}
.content-right::-webkit-scrollbar-thumb,
.content-left::-webkit-scrollbar-thumb {
  border-radius: 4px;
  background: #dddddd;
} */
.report-name {
  font-size: 35px;
  font-weight: 500;
  padding-top: 45%;
  text-align: center;
}
.report-date {
  font-size: 20px;
  font-weight: 500;
  padding-top: 5%;
  text-align: center;
}
.report-title{
  font-size: 18px;
  font-weight: 700;
  padding: 20px 20px;
}
.report-content{
  font-size: 16px;
  font-weight: 500;
  padding: 20px 20px;
  text-indent: 30px;
}
.button-confirm{
  padding-top: 15%;
  text-align: center;
}
.report-time {
  margin-top: 30px;
  text-align: center;
}
</style>
<style media="print">
@page {
  size: auto;
  margin: 0mm;
}

html {
  background-color: #ffffff;
  height: auto;
  margin: 0px;
}
</style>
