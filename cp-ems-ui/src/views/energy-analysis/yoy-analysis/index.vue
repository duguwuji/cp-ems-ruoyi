<template>
  <div class="app-container flex-between bg-container">
    <div class="content-tree">
      <topologicaTree @selectItem="treeItem($event)" />
    </div>
    <div class="content-data" v-loading="showLoading">
      <div class="data-select">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="分类能耗" prop="energyType">
            <el-select v-model="queryParams.energyType" placeholder="请选择">
              <el-option v-for="item in dict.type.energy_type" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="年份">
            <el-date-picker v-model="queryParams.year" type="year" value-format="yyyy" placeholder="选择年份"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="data-chart">
        <MinMaxDoubleBarVue :xData="xData" :yData="yData" :setInfo="chartSetInfo" height="100%" />
      </div>
      <div class="data-table">
        <currentTable :tableData="tableData" :unit="chartSetInfo.yName"/>
      </div>
    </div>
  </div>
</template>

<script>
import topologicaTree from "@/components/TopologicaTree/index";
import MinMaxDoubleBarVue from '@/views/dashboard/MinMaxDoubleBar.vue';
import currentTable from "@/components/Tables/index"
import { getYearAnalysis } from '@/api/system/energy'
export default {
  dicts: ['energy_type'],
  components: {
    topologicaTree,
    MinMaxDoubleBarVue,
    currentTable
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      tableData: [],
      xData: [],
      yData: {
        currentData: [],
        sameData: []
      },
      queryParams: {
        energyType: '0',
        year: undefined,
        areaId: undefined,
      },
      energyType: [
        {
          value: '0',
          label: '电',
          unit: 'kW.h'
        },
        {
          value: '1',
          label: '水',
          unit: 't'
        },
      ],
      showLoading: true,
      chartSetInfo: {
        xName: '月',
        yName: 'kW.h',
        legendName: ['同期', '本期']
      }
    };
  },
  methods: {
    handleQuery() {
      // 能源单位
      let item = this.energyType.find(t => t.value == this.queryParams.energyType)
      if(item) {
        this.chartSetInfo.yName = item.unit
      }

      // this.analysisParams.year = this.queryParams.year.getFullYear().toString();
      getYearAnalysis(this.queryParams).then((res) => {
        this.xData = [];
        this.tableData = [],
          this.yData = {}
        let result = {
          currentData: [],
          sameData: []
        }
        const analy = res.data;
        this.tableData = analy;
        if (analy !== null) {
          analy.forEach((item) => {
            this.xData.push(item.month)
            result.currentData.push(item.correspondingPeriod)
            result.sameData.push(item.currentPeriod)
          });
          this.yData = result
        }
      })
      this.showLoading = true
      setTimeout(() => {
        this.showLoading = false
      }, 1000)
    },

    treeItem(value) {
      this.queryParams.areaId = value.id
      this.handleQuery()
    },
    getNowYear() {
      var year = new Date();
      this.queryParams.year = year.getFullYear().toString()
    }
  },
  created() {
    this.getNowYear();
  },
};
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 700px;
  height: calc(100vh - 84px);

  .content-tree {
    width: 25%;
    height: 100%;
    background: var(--base-item-bg);
    margin-right: 6px;
    padding: 20px;
  }

  .content-data {
    width: calc(100% - 25% - 6px);
    height: 100%;
    background: var(--base-item-bg);
    padding: 12px 20px;

    .data-chart {
      height: 40%;
      // border: 1px solid;
    }

    .data-table {
      margin-top: 6px;
      height: calc(60% - 57px);
      // border: 1px solid;
    }

    @media screen and (max-width: 1174px) {
      .data-table {
        height: calc(60% - 107px);
      }
    }

  }
}
</style>
<style lang="scss"></style>
