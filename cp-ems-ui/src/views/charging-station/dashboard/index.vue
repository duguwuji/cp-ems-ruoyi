<template>
  <div class="app-container bg-container">
    <div class="station-top">
      <el-row :gutter="12">
        <el-col :span="6">
          <div class="top-item-box item-box-one" style="display: flex;">
            <div style="flex:2;height:100%;">
              <div>充电用户（户）</div>
              <div style="text-align:center;margin-top:30px;"><span style="font-size:26px;font-weight:bold;">325</span></div>
            </div>
            <div style="flex:3;display: flex;flex-direction:column;justify-content:space-evenly">
              <div>累计用户：2344 户</div>
              <div>环比增长：13.46%</div>
              <div>同比增长：27.37%</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="top-item-box item-box-two" style="display: flex;">
            <div style="flex:2;height:100%;">
              <div>充电金额（元）</div>
              <div style="text-align:center;margin-top:30px;"><span style="font-size:26px;font-weight:bold;">4386.42</span></div>
            </div>
            <div style="flex:3;display: flex;flex-direction:column;justify-content:space-evenly">
              <div>累计金额：764351.39 元</div>
              <div>环比增长：32.0%</div>
              <div>同比增长：16.78%</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="top-item-box item-box-three" style="display: flex;">
            <div style="flex:2;height:100%;">
              <div>充电电量（kW·h）</div>
              <div style="text-align:center;margin-top:30px;"><span style="font-size:26px;font-weight:bold;">3671.58</span></div>
            </div>
            <div style="flex:3;display: flex;flex-direction:column;justify-content:space-evenly">
              <div>累计电量：52412 kW·h</div>
              <div>环比增长：41.27%</div>
              <div>同比增长：36.82%</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="top-item-box item-box-four" style="display: flex;">
            <div style="flex:2;height:100%;">
              <div>充电次数（次）</div>
              <div style="text-align:center;margin-top:30px;"><span style="font-size:26px;font-weight:bold;">167</span></div>
            </div>
            <div style="flex:3;display: flex;flex-direction:column;justify-content:space-evenly">
              <div>累计次数：2412 次</div>
              <div>环比增长：23.4%</div>
              <div>同比增长：21.67%</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="station-middle">
      <el-row :gutter="12">
        <el-col :span="6">
          <el-card class="box-card" shadow="never">
            <div class="card-title">充电终端状态</div>
            <div style="height: calc(100% - 30px);">
              <StationPieVue height="100%"></StationPieVue>
              <div></div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="18">
          <el-card class="box-card" shadow="never">
            <div style="display:flex;justify-content: space-between;align-items: center;">
              <div class="card-title">充电订单趋势</div>
              <el-radio-group v-model="tabPosition" @change="dateChange" size="mini">
                <!-- <el-radio-button label="day">当日</el-radio-button> -->
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">今年</el-radio-button>
              </el-radio-group>
            </div>
            <div style="height: calc(100% - 30px);">
              <StationBarVue height="100%" :chartData="barChartData" :xName="barXName"/>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <div class="station-bottom">
      <el-row :gutter="12">
        <el-col :span="6">
          <el-card class="box-card" shadow="never">
            <div class="card-title">近7日充电次数</div>
            <div style="height: calc(100% - 30px);">
              <StationLineVue height="100%" itemColor="#ee4368" :chartData="lineDataOne"/>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card" shadow="never">
            <div class="card-title">近7日充电时长</div>
            <div style="height: calc(100% - 30px);">
              <StationLineVue height="100%" yName="h" seriesName="充电时长" :chartData="lineDataTwo" itemColor="#5470c6"/>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card" shadow="never">
            <div class="card-title">近7日充电电量</div>
            <div style="height: calc(100% - 30px);">
              <StationLineVue height="100%" yName="kW·h" seriesName="充电电量" :chartData="lineDataThree" itemColor="#c58bea"/>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card" shadow="never">
            <div class="card-title">近7日充电金额</div>
            <div style="height: calc(100% - 30px);">
              <StationLineVue height="100%" yName="元" seriesName="充电金额" :chartData="lineDataFour" itemColor="#c7a428"/>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
import StationBarVue from './StationBar.vue';
import StationLineVue from './StationLine.vue';
import StationPieVue from './StationPie.vue';
export default {
  components: {
    StationPieVue,
    StationLineVue,
    StationBarVue
  },
  data() {
    return {
      tabPosition: 'month',
      barChartData: {
        xData: ['10-01', '10-02', '10-03', '10-04', '10-05', '10-06', '10-07', '10-08', '10-09', '10-10', '10-12', '10-13', '10-14', '10-15'],
        yData: [79, 68, 56, 72, 51, 63, 67,71, 58, 81, 64, 77, 56, 69]
      },
      barXName: '日',
      lineDataOne: {
        yData: [79,65, 21, 67, 21, 89, 56],
        xData: ['09-22','09-23','09-24','09-25','09-26','09-27','09-28']
      },
      lineDataTwo: {
        yData: [45,72, 16, 37, 64, 28, 46],
        xData: ['09-22','09-23','09-24','09-25','09-26','09-27','09-28']
      },
      lineDataThree: {
        yData: [16,27, 37, 16, 27, 21, 11],
        xData: ['09-22','09-23','09-24','09-25','09-26','09-27','09-28']
      },
      lineDataFour: {
        yData: [134,107, 94, 173, 37, 143, 86],
        xData: ['09-22','09-23','09-24','09-25','09-26','09-27','09-28']
      },
    }
  },
  created() {
    this.initTime()
    this.dateChange('month')
  },
  methods: {
    // 时间类型选择
    dateChange(value) {
      var date = new Date()
      var month = date.getMonth() + 1
      var day = date.getDate()
      let barXData = []
      let barYData = []
      if(value == 'year') {
        for(var i = 0; i < month; i++) {
          barXData.push(moment().subtract(i, 'months').format('YYYY-MM'))
          barYData.push(Math.floor(Math.random()*(180-120+1))+120)
        }
        this.barXName = '月'
      } else {
        for(var i = 0; i < day; i++) {
          barXData.push(moment().subtract(i, 'days').format('MM-DD'))
          barYData.push(Math.floor(Math.random()*(30-15+1))+15)
        }
        this.barXName = '日'
      }
      this.barChartData = {
        xData: barXData.reverse(),
        yData: barYData
      }
    },
    // 初始化时间模拟数据
    initTime() {
      let lineXData = []
      for(var i = 0; i < 7; i++) {
        lineXData.push(moment().subtract(i, 'days').format('MM-DD'))
      }
      lineXData = lineXData.reverse()
      this.lineDataOne.xData = lineXData
      this.lineDataTwo.xData = lineXData
      this.lineDataThree.xData = lineXData
      this.lineDataFour.xData = lineXData
    }
  }
}
</script>

<style scoped>
.app-container {
  min-height: calc(100vh - 84px);
  padding: 12px 12px 0 12px;
}
.top-item-box {
  height: 160px;
  background: var(--base-item-bg);
  margin-bottom: 12px;
  border-radius: 12px;
  color: #fff;
  padding: 16px;
}
.item-box-one {
  background: linear-gradient(30deg, #1a94db, #4db1eb, #7acaf9);
  box-shadow: 0 4px 12px #8ed2fa;
}
.item-box-two {
  background: linear-gradient(30deg, #c7a327, #d5ba47, #e3cf65);
  box-shadow: 0 4px 12px #ece7cd;
}
.item-box-three {
  background: linear-gradient(30deg, #6365f7, #9177f1, #cd8ee9);
  box-shadow: 0 4px 12px #dcc9e6;
}
.item-box-four {
  background: linear-gradient(30deg, #ed3a60, #f1557a, #f67da0);
  box-shadow: 0 4px 12px #e7cfd6;
}
.box-card {
  height: 400px;
  margin-bottom: 12px;
  background-color: var(--base-item-bg);
  border-color: var(--border-color-3);
}
.box-card>>>.el-card__body {
  height: 100%;
}
.card-title {
  font-weight: bold;
  height: 30px;
  display: flex;
  align-items: center;
  color: var(--base-color-1);
}
.card-title::before {
  content: '';
  height: 70%;
  width: 5px;
  background: var(--current-color);
  margin-right: 8px;
}

</style>
