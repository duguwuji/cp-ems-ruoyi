<template>
  <div class="dashboard-container bg-container">
    <div id="build-model" class="center-map">
      <ThreeModel></ThreeModel>
    </div>
    <div class="app-container">
      <div class="container-left">
        <el-card class="project-overview" shadow="hover">
          <div slot="header">
            <span>项目总览</span>
          </div>
          <div class="overview-content">
            <div class="column-one">
              <!-- <img src="http://124.223.191.78:9000/cp-portal/2023/04/14/390786c6aa2a4eb995505343cd6e669f.png" alt=""> -->
              <hotel theme="filled" size="35" fill="#13a6ce"/>
              <div>3</div>
              <div>项目总数</div>
            </div>
            <div class="column-two">
              <!-- <img src="http://124.223.191.78:9000/cp-portal/2023/04/14/760a13a5cc88407581bbeb9870900dcf.png" alt=""> -->
              <dashboard-two theme="filled" size="35" fill="#48baa2"/>
              <div>{{ normalCount + alarmCount + offLineCount }}</div>
              <div>仪表总数</div>
            </div>
            <div class="column-three">
              <!-- <img src="http://124.223.191.78:9000/cp-portal/2023/04/14/abb46392f7af4559a22044102367bdc3.png" alt=""> -->
              <alarm theme="filled" size="35" fill="#f56c6c"/>
              <div>{{ alarmTotal }}</div>
              <div>报警记录</div>
            </div>
          </div>
        </el-card>
        <el-card class="device-status" shadow="hover">
          <div slot="header">
            <span>设备状态</span>
          </div>
          <div class="status-content">
            <div class="status-pie">
              <pie-chart :height="'100%'" :pieData="pieData"></pie-chart>
            </div>
            <div class="status-item flex-column-around">
              <div class="status-normal flex-column-evenly">
                <div class="flex-between">
                  <span class="text-icon">正常设备</span>
                  <span>占比</span>
                </div>
                <div class="flex-between">
                  <span>{{ normalCount }}</span>
                  <span>{{ normalProportion }}</span>
                </div>
              </div>
              <div class="status-alarm flex-column-evenly">
                <div class="flex-between">
                  <span class="text-icon">报警设备</span>
                  <span>占比</span>
                </div>
                <div class="flex-between">
                  <span>{{ alarmCount }}</span>
                  <span>{{ alarmProportion }}</span>
                </div>
              </div>
              <div class="status-offLine flex-column-evenly">
                <div class="flex-between">
                  <span class="text-icon">离线设备</span>
                  <span>占比</span>
                </div>
                <div class="flex-between">
                  <span>{{ offLineCount }}</span>
                  <span>{{ offLineProportion }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
        <el-card class="alarm-info" shadow="hover">
          <div slot="header">
            <span>报警信息</span>
            <el-button style="padding: 3px 0;position:absolute;right:12px;" type="text" @click="toAlarm">更多>></el-button>
          </div>
          <alarmInfo/>
        </el-card>
      </div>
      <div class="container-right">
        <el-card class="energy-statistic" shadow="hover">
          <div slot="header">
            <span>今日用能统计</span>
          </div>
          <div class="statistic-content">
            <div class="column-one">
              <!-- <img src="http://124.223.191.78:9000/cp-portal/2023/04/14/d7aef19078224847ad6424bffa27076a.png" alt=""> -->
              <lightning theme="outline" size="35" class="park-icon"/>
              <div>{{ electricityTotal }}</div>
              <div>电(kW·h)</div>
            </div>
            <div class="column-two">
              <!-- <img src="http://124.223.191.78:9000/cp-portal/2023/04/14/89b0349ffd704f229d90768c1f7637c8.png" alt=""> -->
              <dashboard theme="filled" size="35" class="park-icon"/>
              <div>{{ powerTotal}}</div>
              <div>综合能耗(kgce)</div>
            </div>
            <div class="column-three">
              <!-- <img src="http://124.223.191.78:9000/cp-portal/2023/04/14/6ff28b5de424410389709626fb9dec1b.png" alt=""> -->
              <cycle theme="filled" size="35" class="park-icon"/>
              <div>{{ carbonTotal}}</div>
              <div>今日碳排放量(kg)</div>
            </div>
          </div>
        </el-card>
        <el-card class="energy-trend" shadow="hover">
          <div slot="header">
            <span>今日能源趋势</span>
          </div>
          <div class="trend-content">
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-tab-pane label="综合能耗" name="total">
                <barChart v-if="activeName==='total'" :height="'100%'" :xName="'时间'" :yName="'kgce'" :xData="xData" :yData="energyData"/>
              </el-tab-pane>
              <el-tab-pane label="电" name="electricity">
                <barChart v-if="activeName==='electricity'" :height="'100%'" :xName="'时间'" :yName="'kM·h'" :xData="xData" :yData="electricityData"/>
              </el-tab-pane>
              <el-tab-pane label="水" name="water">
                <barChart v-if="activeName==='water'" :height="'100%'" :xName="'时间'" :yName="'t'" :xData="xData" :yData="waterData"/>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
        <el-card class="carbon-emissions" shadow="hover">
          <div slot="header">
            <span>今日碳排放量</span>
          </div>
          <barChart :height="'100%'" :xName="'时间'" :yName="'kg'" :xData="xData" :yData="carbonData"/>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import pieChart from "@/views/dashboard/PieChart"
import barChart from "@/views/dashboard/BarChart"
import alarmInfo from "@/views/dashboard/alarmInfo"
import ThreeModel from "@/components/ThreeModel/index"
import {Lightning, Dashboard, Cycle, Hotel, DashboardTwo, Alarm} from "@icon-park/vue"
import { getAllStatus } from '@/api/system/equipmentInfo'
import { listHistory } from '@/api/system/alarmHistory'
import { getChainData, getDayTrend, getConsumptionStatistics, getTrendAndCarbon } from '@/api/system/energy'
import moment from 'moment'
import { topologyTreeSelect } from '@/api/system/itemTopology'
export default {
  name: "dashboardIndex",
  components: {
    pieChart,
    barChart,
    alarmInfo,
    Lightning,
    Dashboard,
    Cycle,
    Hotel,
    DashboardTwo,
    Alarm,
    ThreeModel
  },
  data() {
    return {
      map: null,
      activeName: 'total',
      pieData: [],
      normalCount:"",
      alarmCount:"",
      offLineCount:"",
      normalProportion:"",
      alarmProportion:"",
      offLineProportion:"",
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
    };
  },
  methods: {
    getEquipmentData() {
      getAllStatus().then(res => {
        this.normalCount = res.data[0].count,
        this.alarmCount = res.data[1].count,
        this.offLineCount = res.data[2].count,
        this.normalProportion = res.data[0].proportion,
        this.alarmProportion = res.data[1].proportion,
        this.offLineProportion = res.data[2].proportion,
        this.pieData = [
          {value: res.data[0].count, name: '正常设备', itemStyle: {color: '#1890ff'}},
          {value: res.data[1].count, name: '报警设备', itemStyle: {color: '#ff4949'}},
          {value: res.data[2].count, name: '离线设备', itemStyle: {color: '#ffba00'}},
        ]
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
    // 获取能耗
    getConsumption() {
      getConsumptionStatistics({areaId: this.areaId}).then(res => {
        this.electricityTotal = res.data.electricity
        this.powerTotal = res.data.kgce
        this.carbonTotal = res.data.kg
      })
    },
    // 获取综合能耗、碳排放chart
    getPowerAndCarbon() {
      getTrendAndCarbon({areaId: this.areaId}).then(res => {
        if (res.data.trendResult.length <= 0) {
          this.xData = [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
          return;
        }
        this.xData = Object.keys(res.data.carbonResult)
        this.energyData = res.data.trendResult
        this.carbonData = res.data.carbonResult
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
        })
      });
    },
    initMap() {
      this.map = new BMapGL.Map("allMap");

      // 设置中心点坐标和放大倍数
      // 以 经纬度 定位
      var point = new BMapGL.Point(120.654617,31.268181);
      var point2 = new BMapGL.Point(120.679607,31.529649)
      this.map.centerAndZoom(point, 11);

      //设置地图样式
      // this.map.setMapStyleV2({styleId: 'b5a0e397126a34c8e82f06f630306392'})
      // 以 地名 定位
      // this.map.centerAndZoom('苏州市', 12);

      this.map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
      this.map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
      // this.map.addControl(new BMapGL.NavigationControl());  //添加默认缩放平移控件
      // this.map.addControl(new BMapGL.ScaleControl());          //比例尺
      // this.map.addControl(new BMapGL.OverviewMapControl()); //添加默认缩略地图控件
      // map.addControl(new BMap.OverviewMapControl({isOpen: true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT}));   //右下角，打开缩略地图
      // this.map.addControl(new BMapGL.MapTypeControl());        //地图类型 三维方式 有错误
      // this.map.setMapStyle({style:'light'});

      // 标注
      var marker = new BMapGL.Marker(point)
      this.map.addOverlay(marker)
      // 信息窗口
      var opts = {
        width: 250,
        height: 100,
        title: '武珞科技园'
      }
      // var infoWindow = new BMapGL.InfoWindow('云脉软件', opts)
      // marker.addEventListener('mouseover', function() {
      //   this.openInfoWindow(infoWindow);
      // })
      // // 鼠标移开标注点要发生的事
      // marker.addEventListener("mouseout", function () {
      //   this.closeInfoWindow(infoWindow);
      // });

      var point2 = new BMapGL.Point(120.679607,31.529649)
      var marker2 = new BMapGL.Marker(point2)
      this.map.addOverlay(marker2)
      var opts2 = {
        width: 250,
        height: 100,
        title: '优胜美地生产基地'
      }
      // var infoWindow2 = new BMapGL.InfoWindow('优胜美地', opts2)
      // marker2.addEventListener('mouseover', function() {
      //   this.openInfoWindow(infoWindow2);
      // })
      // // 鼠标移开标注点要发生的事
      // marker2.addEventListener("mouseout", function () {
      //   this.closeInfoWindow(infoWindow2);
      // });

    },

    // 跳转到报警页面
    toAlarm() {
      this.$router.push('/alarm/realtime-alarm')
    },

    handleClick(tab, event) {
      if(tab.index == "0"){
        this.getPowerAndCarbon()
      } else if(tab.index == "1"){
        this.energyType = "0"
        this.getEnergyType()
      }else if(tab.index == "2"){
        this.energyType = "1"
        this.getEnergyType()
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      // this.initMap()
    })
  },
  beforeDestroy() {
    this.map = null
  },
  created() {
    this.getEquipmentData()
    this.getConsumption()
    
    this.$nextTick(() => {
      this.getPowerAndCarbon()
    })
  }
};
</script>

<style lang="scss" scoped>
.dashboard-container {
  height: calc(100vh - 84px);
  width: 100%;
  min-height: 750px;
  position: relative;

  .center-map {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 0;
    // border-radius: 6px;
    // border: 1px solid black;
  }
}
.app-container {
  height: 100%;
  display: flex;
  justify-content: space-between;
  z-index: 0;
  background-color: transparent;

  .container-left, .container-right {
    z-index: 1;
    width: 304px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .project-overview, .energy-statistic {
      min-height: 165px;
      height: 22%;
      // margin-bottom: 10px;

      .overview-content, .statistic-content {
        width: 100%;
        display: flex;
        justify-content: space-between;

        .column-one, .column-two, .column-three {
          flex: 1;
          text-align: center;
          padding: 5px 0;

          .park-icon {
            color: var(--current-color);
          }

          div {
            margin-top: 5px;
          }

          // &:hover {
          //   background: var(--hover-bg);
          // }
        }
      }
    }

    .device-status, .alarm-info, .energy-trend, .carbon-emissions {
      height: 38%;

      .status-content {
        width: 100%;
        height: 100%;
        padding: 12px;
        display: flex;

        .status-pie {
          width: 45%;
        }
        .status-item {
          width: 55%;

          .status-normal, .status-alarm, .status-offLine {
            border-bottom: 1px solid var(--hover-bg);
            font-size: 14px;
            height: 25%;

            .text-icon::before {
              content: ' ';
              width: 12px;
              height: 8px;
              display: inline-block;
              background: var(--normal-bg);
              margin-right: 6px;
            }
          }

          .status-alarm .text-icon::before {
            background: var(--alarm-bg);
          }
          .status-offLine .text-icon::before {
            background: var(--offLine-bg);
          }
        }
      }

      .trend-content {
        height: 100%;
        width: 100%;
        padding: 0 12px;
      }
    }
  }
}
</style>
<style lang="scss">
.project-overview, .device-status, .alarm-info, .energy-statistic, .energy-trend, .carbon-emissions {
  .el-card__header {
    padding: 6px 12px;
    min-height: 0;
    display: flex;
    position: relative;

    &::before {
      content: ' ';
      width: 5px;
      height: 22px;
      background-color: var(--current-color);
      display: inline-block;
      margin-right: 10px;
    }
  }

  .el-card__body {
    padding: 0;
    height: calc(100% - 34px);
    display: flex;
    align-items: center;
  }

  .el-tabs {
    height: 100%;

    .el-tabs__content {
      height: calc(100% - 55px);
      .el-tab-pane {
        height: 100%;
      }
    }
  }
}
</style>
