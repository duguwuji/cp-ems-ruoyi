<template>
  <div class="app-container flex-between bg-container">
    <div class="content-tree">
      <topologicaTree @selectItem="treeItem($event)"/>
    </div>
    <div class="content-data">
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="日原始数据" name="origin">
        <div class="origin-content" v-if="activeName == 'origin'" v-loading="loading">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
            <el-form-item label="起始时间">
              <el-date-picker
                v-model="queryParams.dateRange"
                style="width: 300px"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
                @change="getTimeRange"
              ></el-date-picker>
            </el-form-item>
            <!-- <el-form-item label="电力类别" prop="type">
              <el-select v-model="queryParams.type" placeholder="请选择" style="width:150px">
                <el-option
                  v-for="item in energyType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="">
              <el-radio v-for="item in configList" :key="item.energyType" v-model="queryParams.energyType" :label="item.energyType">{{item.yName}}</el-radio>
              <!-- <el-checkbox-group v-model="checkList">
                <el-checkbox label="电流"></el-checkbox>
                <el-checkbox label="电压"></el-checkbox>
                <el-checkbox label="电功率"></el-checkbox>
              </el-checkbox-group> -->
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="getOriginData">查询</el-button>
              <!-- <el-button type="primary" size="mini">重置</el-button> -->
            </el-form-item>
          </el-form>
          <div class="change-btn">
            <el-button :type="showWhich === 'chart' ? 'primary' : 'default'" icon="el-icon-data-line" size="mini" @click="changeChart">图表</el-button>
            <el-button :type="showWhich === 'table' ? 'primary' : 'default'" icon="el-icon-data-analysis" size="mini" @click="changeTable">数据</el-button>
            <!-- <el-button v-if="showWhich === 'table'" type="warning" icon="el-icon-download" size="mini" @click="changeTable">导出</el-button> -->
          </div>
          <div class="chart-box">
            <ElectricLineChart height="100%" v-if="showWhich == 'chart'" :chartData="paramData" :dateRange="queryParams.dateRange" :configData="configData"/>
            <ElectricTable v-if="showWhich == 'table'" :tableData="paramTableData" :type="activeName" :configData="configData"/>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="逐日极值数据" name="peak">
        <div class="origin-content" v-if="activeName=='peak'" v-loading="loading">
          <el-form :model="peakParams" ref="peakParams" size="small" :inline="true">
            <el-form-item label="起始时间">
              <el-date-picker
                v-model="peakParams.dateRange"
                style="width: 300px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="dateChange"
              ></el-date-picker>
            </el-form-item>
            <!-- <el-form-item label="电力类别" prop="type">
              <el-select v-model="queryParams.type" placeholder="请选择" style="width:150px">
                <el-option
                  v-for="item in energyType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item> -->
            <el-form-item label="">
              <el-radio v-for="item in configList" :key="item.energyType" v-model="peakParams.energyType" :label="item.energyType">{{item.yName}}</el-radio>
              <!-- <el-checkbox-group v-model="checkList">
                <el-checkbox label="电流"></el-checkbox>
                <el-checkbox label="电压"></el-checkbox>
                <el-checkbox label="电功率"></el-checkbox>
              </el-checkbox-group> -->
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="getPeakData">查询</el-button>
              <!-- <el-button type="primary" size="mini">重置</el-button> -->
            </el-form-item>
          </el-form>
          <div class="change-btn">
            <el-button :type="showWhich === 'chart' ? 'primary' : 'default'" icon="el-icon-data-line" size="mini" @click="changeChart">图表</el-button>
            <el-button :type="showWhich === 'table' ? 'primary' : 'default'" icon="el-icon-data-analysis" size="mini" @click="changeTable">数据</el-button>
            <!-- <el-button v-if="showWhich === 'table'" type="warning" icon="el-icon-download" size="mini" @click="changeTable">导出</el-button> -->
          </div>
          <div class="chart-box">
            <ElectricyPeakLineVue height="100%" v-if="showWhich == 'chart'" :dateRange="peakParams.dateRange" :configData="peakConfig" :chartData="peakData"/>
            <ElectricTable v-if="showWhich == 'table'" :type="activeName" :tableData="peakTableData" :configData="peakConfig"/>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
      <!-- <div class="data-select">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="分类能耗" prop="type">
            <el-select v-model="queryParams.type" placeholder="请选择">
              <el-option
                v-for="item in energyType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期" prop="dateType">
            <el-select v-model="queryParams.dateType" placeholder="请选择" style="width: 80px;">
              <el-option
                v-for="item in dateTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="queryParams.time"
              :format="queryParams.dateType === 'week' ? 'yyyy-WW' : ''"
              :type="queryParams.dateType"
              placeholder="选择年份"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="data-table">
        <mon-table />
      </div> -->
    </div>
  </div>
</template>

<script>
import topologicaTree from "@/components/TopologicaTree/index";
import ElectricTable from '@/components/Tables/ElectricTable';
import ElectricLineChart from '@/views/dashboard/ElectricLineChart';
import {queryPowerParameter, queryPeak} from "@/api/system/dataQuery"
import moment from 'moment'
import ElectricyPeakLineVue from '../../dashboard/ElectricyPeakLine.vue';
export default {
  components: {
    topologicaTree,
    ElectricTable,
    ElectricLineChart,
    ElectricyPeakLineVue
  },
  data() {
    return {
      queryParams: {
        areaId: '',
        dateRange:[],
        startTime: '',
        endTime: '',
        energyType: 'electricityi'
      },
      energyType: [
        {
          value: 0,
          label: '有功功率'
        },
        {
          value: 1,
          label: '电流'
        },
        {
          value: 2,
          label: '相电压'
        },
        {
          value: 3,
          label: '线电压'
        },
        {
          value: 4,
          label: '频率'
        },
        {
          value: 5,
          label: '功率因数'
        },
        {
          value: 6,
          label: '无功功率'
        },
        {
          value: 7,
          label: '视在功率'
        },
        {
          value: 8,
          label: '三相不平衡度'
        },
        {
          value: 9,
          label: '负载率'
        },
      ],
      activeName: 'origin',
      showWhich: 'chart',
      paramData: [],
      paramTableData: [],
      configList: [
        {yName: '电流', unit: 'A' ,energyType: 'electricityi'},
        {yName: '电压', unit: 'V' ,energyType: 'electricityu'},
        {yName: '电功率', unit: 'kW' ,energyType: 'electricityp'},
      ],
      configData: {
        yName: '电流',
        unit: 'A',
        energyType: 'electricityi'
      },
      loading: true,
      peakParams: {
        areaId: '',
        dateRange:[],
        startTime: '',
        endTime: '',
        energyType: 'electricityi'
      },
      peakConfig: {
        yName: '电流',
        unit: 'A',
        energyType: 'electricityi'
      },
      peakData: {},
      peakTableData: []
    };
  },
  created() {
    this.initTime()

  },
  methods: {
    handleClick(tab, event) {
      // console.log(tab);
      tab.name == 'origin' ? this.getOriginData() : this.getPeakData()
    },
    getTimeRange() {
      // console.log(this.queryParams);
    },
    changeChart() {
      this.showWhich = 'chart'
    },
    changeTable() {
      this.showWhich = 'table'
    },
    // 获取拓扑书点击的值
    treeItem(value) {
      this.queryParams.areaId = value.id
      this.peakParams.areaId = value.id
      this.activeName == 'origin' ? this.getOriginData() : this.getPeakData()
      // this.getOriginData()
      // this.getPeakData()
    },
    // 初始化时间
    initTime() {
      let endTime = moment().format('yyyy-MM-DD 23:59:59')
      let startTime = moment().subtract(2, 'days').format('yyyy-MM-DD 00:00:00')
      this.queryParams.dateRange = [startTime, endTime]
      this.peakParams.dateRange = [moment().subtract(1, 'months').format('yyyy-MM-DD'), moment().format('yyyy-MM-DD')]
    },
    // 查询原始数据
    getOriginData() {
      this.loading = true
      this.configData = this.configList.find(c => c.energyType == this.queryParams.energyType)
      let params = {
        ... this.queryParams,
        startTime: this.queryParams.dateRange[0],
        endTime: this.queryParams.dateRange[1],
      }
      queryPowerParameter(params).then(res => {
        let data = []
        res.data.forEach(item => {
          data.push([item.ts, item.val])
        })
        this.paramData = data
        this.paramTableData = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    // 日期修改
    dateChange(value) {
      console.log(value);
    },
    // 查询极值数据
    getPeakData() {
      this.loading = true
      this.peakConfig = this.configList.find(c => c.energyType == this.peakParams.energyType)
      let params = {
        ...this.peakParams,
        startTime: this.peakParams.dateRange[0],
        endTime: this.peakParams.dateRange[1],
      }
      queryPeak(params).then(res => {
        this.peakTableData = res.data
        let data = {
          xData: [],
          minData: [],
          maxData: [],
          avgData: []
        }
        res.data.forEach(item => {
          data.xData.push(item.date)
          data.minData.push(item.min)
          data.maxData.push(item.max)
          data.avgData.push(item.ave)
        })
        this.peakData = data
      }).finally(() => {
        this.loading = false
      })
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 700px;
  height: calc(100vh - 84px);

  .content-tree {
    width: 20%;
    height: 100%;
    background: var(--base-item-bg);
    margin-right: 6px;
    padding: 20px;
  }
  .content-data {
    width: calc(100% - 20% - 10px);
    height: 100%;
    background: var(--base-item-bg);
    // padding: 12px 20px;

    .origin-content {
      height: 100%;
      padding: 0 10px;
      .el-checkbox {
        margin-right: 15px;
      }
    }

    .chart-box {
      height: calc(100% - 75px);
      padding: 10px 0;
    }

    @media screen and (max-width: 1180px) {
      .chart-box {
        height: calc(100% - 118px);
      }
    }
    @media screen and (max-width: 1056px) {
      .chart-box {
        height: calc(100% - 159px);
      }
    }
    @media screen and (max-width: 521px) {
      .chart-box {
        height: calc(100% - 234px);
      }
    }

  }
}
</style>
<style lang="scss">
.app-container {
  .content-data {
    .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
      margin-bottom: 10px;
    }
    .el-tabs {
      height: 100%;
      .el-tabs__content {
        height: calc(100% - 58px);
        .el-tab-pane {
          height: 100%;
        }
      }
    }
    .el-tabs--card>.el-tabs__header {
      border-bottom: 4px solid var(--current-color);

      .el-tabs__nav {
        border: none;
        border-radius: 0px;
      }

      .el-tabs__item {
        border-left: none;
      }
    }
    .el-tabs--card>.el-tabs__header .el-tabs__item.is-active {
      color: #fff;
      background-color: var(--current-color);
      border-bottom: none;
    }
  }
}
</style>
