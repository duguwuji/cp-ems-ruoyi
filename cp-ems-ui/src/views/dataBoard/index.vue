<template>
  <div class="board-container">
    <div v-if="showWhich" class="back-btn" @click="backHome"><img src="@/assets/images/home.png">能源平台</div>
    <img v-else src="@/assets/images/fullscreen.png" alt="" class="fullscreen-img" @click="toDataBoard">
    <div class="time-stamp">{{ nowTime }}</div>
    <div class="board-title"><span>能源数据可视化平台</span></div>
    <div class="board-content flex-between">
      <div class="content-left flex-column-between">
        <div class="content-overview">
          <div class="box-title">项目总览</div>
          <div class="box-content flex-between">
            <div class="overview-object flex-column-center">
              <div class="object-count">3</div>
              <div class="object-name">项目总数</div>
            </div>
            <div class="overview-meter flex-column-center">
              <div class="object-count">{{ deviceTotal }}</div>
              <div class="object-name">仪表总数</div>
            </div>
            <div class="overview-alarm flex-column-center">
              <div class="object-count">{{ alarmTotal }}</div>
              <div class="object-name">报警记录</div>
            </div>
          </div>
        </div>
        <div class="content-status">
          <div class="box-title">设备状态</div>
          <div class="box-content">
            <CirclePieChartVue height="100%" :pieData="pieData"/>
          </div>
        </div>
        <div class="content-alarm">
          <div class="box-title">报警信息</div>
          <div class="box-content">
            <alarmInfo />
          </div>
        </div>
      </div>
      <div class="content-middle flex-column-between">
        <div class="content-map" id="boardMap"></div>
        <div class="content-chart">
          <div class="box-title">今日能源趋势</div>
          <div class="box-content">
            <el-tabs
              v-model="activeName"
              @tab-click="handleClick"
              class="trend-tabs"
            >
              <el-tab-pane label="综合能耗" name="total">
                <TrendLineChart
                  v-if="activeName === 'total'"
                  :height="'100%'"
                  :yName="'kgce'"
                  :xData="xData"
                  :yData="energyData"
                />
              </el-tab-pane>
              <el-tab-pane label="电" name="electricity">
                <TrendLineChart
                  v-if="activeName === 'electricity'"
                  :height="'100%'"
                  :yName="'kM·h'"
                  :xData="xData"
                  :yData="electricityData"
                />
              </el-tab-pane>
              <el-tab-pane label="水" name="water">
                <TrendLineChart
                  v-if="activeName === 'water'"
                  :height="'100%'"
                  :yName="'t'"
                  :xData="xData"
                  :yData="waterData"
                />
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>
      <div class="content-right flex-column-between">
        <div class="content-statistics">
          <div class="box-title">今日用能统计</div>
          <div class="box-content flex-between">
            <div class="statistics-item flex-column-center">
              <lightning theme="outline" size="25" fill="#00d1ff" />
              <div class="item-count">{{ electricityTotal }}</div>
              <div style="text-align: center">电(kW·h)</div>
            </div>
            <div class="statistics-item flex-column-center">
              <dashboard theme="outline" size="25" fill="#00d1ff" />
              <div class="item-count">{{ powerTotal}}</div>
              <div style="text-align: center">综合能耗(kgce)</div>
            </div>
            <div class="statistics-item flex-column-center">
              <cycle theme="outline" size="25" fill="#00d1ff" />
              <div class="item-count">{{ carbonTotal}}</div>
              <div style="text-align: center">今日碳排放量(kg)</div>
            </div>
          </div>
        </div>
        <div class="content-trend">
          <div class="box-title">日用电功率曲线</div>
          <div class="box-content">
            <LineChart height="100%" :chartData="dailyP" :yName="'kW'" xName=""/>
          </div>
        </div>
        <div class="content-carbon">
          <div class="box-title">今日碳排放量</div>
          <div class="box-content">
            <barChart
              :height="'100%'"
              :barColor="['#1c508e', '#1be5e7']"
              :yName="'kg'"
              :xData="xData"
              :yData="carbonData"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CirclePieChartVue from "@/views/dataBoard/CirclePieChart.vue";
import alarmInfo from "@/views/dataBoard/alarmInfo";
import LineChart from "@/views/dataBoard/LineChart.vue";
import barChart from "@/views/dataBoard/BarChart";
import TrendLineChart from "@/views/dataBoard/TrendLineChart";
import { Lightning, Dashboard, Cycle } from "@icon-park/vue";
import moment from "moment";
import { getAllStatus } from '@/api/system/equipmentInfo'
import { listHistory } from '@/api/system/alarmHistory'
import { topologyTreeSelect } from '@/api/system/itemTopology'
import { getChainData, getDailyP, getDayTrend, getConsumptionStatistics } from '@/api/system/energy'
export default {
  name: "dataBoard",
  components: {
    CirclePieChartVue,
    alarmInfo,
    LineChart,
    Lightning,
    Cycle,
    Dashboard,
    barChart,
    TrendLineChart
  },
  data() {
    return {
      map: null,
      activeName: "total",
      nowTime: "",
      timer: null, // 定时任务
      showWhich: false, // 全屏按钮切换
      pieData: [],
      deviceTotal: 0,
      // 历史报警总条数
      alarmTotal: 0,
      energyType: "0",
      areaId: 1,
      xData:[],
      electricityData:[],
      waterData:[],
      energyData:[],
      carbonData:[],
      electricityTotal:0,
      powerTotal:0,
      carbonTotal: 0,
      dailyP:{},
    };
  },
  methods: {
    getEquipmentData() {
      getAllStatus().then(res => {
        this.pieData = [
          { value: res.data[0].count, name: "正常", itemStyle: {color: '#6be6c3'} },
          { value: res.data[1].count, name: "报警", itemStyle: {color: '#e0c464'} },
          { value: res.data[2].count, name: "离线", itemStyle: {color: '#297ef8'} },
        ]
        res.data.forEach(item=>{
          this.deviceTotal = item.count + this.deviceTotal;
        })
      })
    },
    /** 查询历史报警列表 */
    getAlarmList() {
      listHistory().then(response => {
        this.alarmTotal = response.total;
      });
    },
    // 获取区域拓扑
    getAreaList() {
      topologyTreeSelect().then((res) => {
        if(res.data.length>0){
          this.areaId = res.data[0].id
        }
      });
    },
    //根据能源类型查询
    getEnergyType(){
      let date = moment().format("yyyy-MM-DD HH:mm:ss");
      this.xData = [];
      this.electricityData = [];
      this.waterData = [];
      let data = {
        energyType:this.energyType,
        areaId:this.areaId,
        nowTime: date,
      };
      getDayTrend(data).then((res) => {
        if (res.data.length <= 0) {
          this.xData = [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
          return;
        }
        res.data.forEach((item, index) => {
          this.xData.push(index);
        });
        if(this.energyType == "0"){
          this.electricityData = res.data;
        }else if(this.energyType == "1"){
          this.waterData = res.data;
        }
      });
    },
    // 获取总能耗
    getConsumption() {
      getConsumptionStatistics({areaId: this.areaId}).then(res => {
        this.electricityTotal = res.data.electricity
        this.powerTotal = res.data.kgce
        this.carbonTotal = res.data.kg
      })
    },
    //综合能耗
    getEnergy(){
      let date = moment().format("yyyy-MM-DD HH:mm:ss");
      this.xData = [];
      this.energyData = [];
      this.carbonData = [];
      this.electricityData = [];
      this.waterData = [];
      let data = {
        energyType:'0',
        areaId:this.areaId,
        nowTime: date,
      };
      getDayTrend(data).then((res) => {
        if (res.data.length <= 0) {
          this.xData = [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
          return;
        }
        res.data.forEach((item, index) => {
          this.xData.push(index);
        });
        this.electricityData = res.data
      }).then(() =>{
        let query = {
          energyType:'1',
          areaId:this.areaId,
          nowTime: date,
        };
        getDayTrend(query).then((request) => {
          this.waterData = request.data
        }).then(()=>{
          this.xData.forEach((item,index)=>{
            this.energyData.push((parseFloat(this.electricityData[index])/8.167 + parseFloat(this.waterData[index])*0.4857).toFixed(2));
            this.carbonData.push((parseFloat(this.electricityData[index])*0.785 + parseFloat(this.waterData[index])*0.91).toFixed(2));
          })
        });
      });
    },
    // 获取当日-昨日电功率曲线
    getDailyPData() {
      let param= {
        areaId: this.areaId,
        energyType: '0'
      }
      getDailyP(param).then((res) => {
        let result = {
          todayData: res.data.todayData,
          yesterdayData: res.data.yesterdayData,
        };
        this.dailyP = result;
      });
    },
    initMap() {
      this.map = new BMapGL.Map("boardMap");

      var myIcon = new BMapGL.Icon(
        "https://szcloudpulse.com:9000/cp-portal/2023/04/27/d85f358bb44a4cf69e843acecf7b0c2c.png",
        new BMapGL.Size(23, 25),
        {
          // 指定定位位置。
          // 当标注显示在地图上时，其所指向的地理位置距离图标左上
          // 角各偏移10像素和25像素。您可以看到在本例中该位置即是
          // 图标中央下端的尖角位置。
          // anchor: new BMapGL.Size(15, 25),
          // 设置图片偏移。
          // 当您需要从一幅较大的图片中截取某部分作为标注图标时，您
          // 需要指定大图的偏移位置，此做法与css sprites技术类似。
          // imageOffset: new BMapGL.Size(0, 0 - 25)   // 设置图片偏移
        }
      );

      // 设置中心点坐标和放大倍数
      // 以 经纬度 定位
      var point = new BMapGL.Point(120.654617, 31.268181);
      var point2 = new BMapGL.Point(120.679607, 31.529649);
      this.map.centerAndZoom(point, 10);

      //设置地图样式
      this.map.setMapStyleV2({ styleId: "d9955f6e8bd01669bfd15a998f109283" });
      // 以 地名 定位
      // this.map.centerAndZoom('苏州市', 12);

      this.map.enableScrollWheelZoom(); //启用滚轮放大缩小，默认禁用
      this.map.enableContinuousZoom(); //启用地图惯性拖拽，默认禁用

      // 标注
      var marker = new BMapGL.Marker(point, { icon: myIcon });
      this.map.addOverlay(marker);
      // 信息窗口
      var opts = {
        width: 250,
        height: 100,
        title: "武珞科技园",
      };
      // var infoWindow = new BMapGL.InfoWindow("云脉软件", opts);
      // marker.addEventListener("mouseover", function () {
      //   this.openInfoWindow(infoWindow);
      // });
      // // 鼠标移开标注点要发生的事
      // marker.addEventListener("mouseout", function () {
      //   this.closeInfoWindow(infoWindow);
      // });

      var point2 = new BMapGL.Point(120.679607, 31.529649);
      var marker2 = new BMapGL.Marker(point2, { icon: myIcon });
      this.map.addOverlay(marker2);
      var opts2 = {
        width: 250,
        height: 100,
        title: "优胜美地生产基地",
      };
      // var infoWindow2 = new BMapGL.InfoWindow("优胜美地", opts2);
      // marker2.addEventListener("mouseover", function () {
      //   this.openInfoWindow(infoWindow2);
      // });
      // // 鼠标移开标注点要发生的事
      // marker2.addEventListener("mouseout", function () {
      //   this.closeInfoWindow(infoWindow2);
      // });
    },
    handleClick(tab, event) {
      if(tab.index == "0"){
        this.getEnergy()
      } else if(tab.index == "1"){
        this.energyType = "0"
        this.getEnergyType()
      }else if(tab.index == "2"){
        this.energyType = "1"
        this.getEnergyType()
      }
    },
    getNowTime() {
      this.nowTime = moment().format("yyyy-MM-DD HH:mm:ss");
    },
    toDataBoard() {
      this.$router.push('/system/data-board')
    },
    backHome() {
      this.$router.push('/data-board')
      // this.$router.push('/dashboard')
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initMap();
    })
  },
  created() {
    let { getNowTime } = this;
    getNowTime();
    if (this.timer) {
      clearInterval(this.timer);
    }
    this.timer = setInterval(getNowTime, 1000);

    this.showWhich = this.$router.currentRoute.path == '/data-board' ? false : true
    this.getEquipmentData()
    this.getAlarmList()
    this.getAreaList()

    this.$nextTick(() => {
      this.getConsumption()
      this.getEnergy()
      this.getDailyPData();
    })
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map = null
  },
};
</script>

<style scoped>
.board-container {
  width: 100%;
  height: 100vh;
  background-image: url("../../assets/images/board-bg.png");
  background-size: 100% 100%;
  color: #fff;
  position: relative;
}
.back-btn {
  position: absolute;
  left: 2.5%;
  top: 5%;
  height: 3%;
  color: #00d0ff;
  font-size: 17px;
  display: flex;
  align-items: center;
  cursor: pointer;
}
.back-btn img {
  height: 100%;
  margin-right: 6px;
}
.fullscreen-img {
  position: absolute;
  left: 2.5%;
  top: 5%;
  height: 3%;
  cursor: pointer;
}
.time-stamp {
  position: absolute;
  right: 2.5%;
  top: 5%;
  color: #00d1ff;
  font-size: 20px;
}
.board-title {
  background-image: linear-gradient(to top, #2571e9, #00e7ff);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  font-size: 40px;
  font-weight: bold;
  letter-spacing: 8px;
  height: 9%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.board-content {
  height: 86%;
  width: 95%;
  margin: 6px auto 0;
}
.content-left {
  width: calc(25% - 12px);
  margin-right: 12px;
}
.content-overview,
.content-status,
.content-alarm,
.content-statistics,
.content-trend,
.content-carbon,
.content-chart {
  background-image: url("../../assets/images/box-bg1.png");
  background-size: 100% 100%;
}
.content-overview {
  height: 20%;
  margin-bottom: 12px;
  padding: 12px 16px;
}
.box-title {
  height: 20px;
  margin-left: 12px;
  display: flex;
  align-items: center;
  color: #01d1ff;
}
.box-title::before {
  content: " ";
  width: 6px;
  height: 100%;
  border-radius: 10px;
  display: inline-block;
  margin-right: 6px;
  background: linear-gradient(to bottom, #00d1ff, #2869e8);
}
.box-content {
  height: calc(100% - 20px);
  /* padding: 20px 10px; */
}
.overview-object,
.overview-meter,
.overview-alarm {
  width: calc(33% - 10px);
  align-items: center;
}
.object-count {
  color: #1be5e7;
  font-size: 35px;
  font-weight: bold;
  margin-bottom: 6px;
}
.object-name {
  font-size: 12px;
}
.content-status {
  height: calc(40% - 12px);
  margin-bottom: 12px;
  padding: 20px 16px;
}
.content-alarm {
  height: calc(40% - 12px);
  padding: 16px 16px;
}
.content-middle {
  width: 50%;
  margin-right: 12px;
}
.content-map {
  height: 65%;
}
.content-chart {
  height: calc(35% - 12px);
  padding: 18px 30px;
}
.content-right {
  width: calc(25% - 12px);
}
.content-statistics {
  height: 20%;
  padding: 12px 16px;
}
.content-trend {
  height: calc(40% - 12px);
  padding: 18px 16px;
}
.content-carbon {
  height: calc(40% - 12px);
  padding: 18px 16px;
}
.statistics-item {
  width: calc(33% - 8px);
  align-items: center;
  font-size: 12px;
}
.item-count {
  font-size: 28px;
  font-weight: bold;
  color: #1be5e7;
  margin: 6px 0 6px;
}
.trend-tabs {
  height: 100%;
}
</style>
<style>
.trend-tabs,
.trend-tabs .el-tabs__content .el-tab-pane {
  height: 100%;
}
.trend-tabs .el-tabs__item {
  color: #fff;
}
.trend-tabs .el-tabs__item:hover,
.trend-tabs .el-tabs__item.is-active {
  color: #00d0fe;
}
.trend-tabs .el-tabs__active-bar {
  background-color: #00d0fe;
}
.trend-tabs .el-tabs__nav-wrap::after {
  background-color: #1d3666;
}
.trend-tabs .el-tabs__content {
  height: calc(100% - 55px);
}

/* 地图信息窗口样式修改 */
#boardMap .BMap_bubble_pop {
  background-color: rgba(28, 37, 80, 0.8) !important;
  border: 1px solid #186dbf !important;
}
#boardMap .BMap_bubble_pop img {
  display: none;
}
#boardMap .BMap_bubble_pop .BMap_bubble_top .BMap_bubble_title,
#boardMap .BMap_bubble_pop .BMap_bubble_center .BMap_bubble_content {
  color: #fff !important;
}
</style>
