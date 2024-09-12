<template>
  <div class="app-container flex-between bg-container">
    <div class="content-tree">
      <topologicaTree :showCheckbox="true" @selectAreaId="getItemIds"/>
    </div>
    <div class="content-data" v-loading="loading">
      <el-form :model="queryParams" size="small" :inline="true">
        <el-form-item label="分类能耗" prop="energyType">
          <el-select v-model="queryParams.energyType" placeholder="请选择" @change="energyTypeChange">
            <el-option
              v-for="item in dict.type.energy_type"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-select v-model="queryParams.dateType" placeholder="请选择" style="width: 90px;" @change="dateTypeChange">
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
            value-format="yyyy-MM-dd"
            :type="queryParams.dateType"
            placeholder="选择时间"
            :key="queryParams.dateType"
            @change="dateChange"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" icon="el-icon-search" @click="queryData">查询</el-button>
          <!-- <el-button type="primary" size="mini" disabled icon="el-icon-s-data">图表</el-button> -->
        </el-form-item>
      </el-form>
      <div class="content-box">
        <ExpenseTableVue :testData="tableData" :unit="unit"/>
      </div>
    </div>
  </div>
</template>

<script>
import topologicaTree from "@/components/TopologicaTree/index";
import moment from 'moment'
import ExpenseTableVue from './expenseTable.vue';
import {getConsumptionExpenseReport} from "@/api/system/energy"
export default {
  components: {
    topologicaTree,
    ExpenseTableVue,
  },
  dicts: ['energy_type'],
  data() {
    return {
      queryParams: {
        areaIds: [],
        dateType: 'date',
        startTime: '',
        endTime: '',
        energyType: '0',
        time: ''
      },
      time: '2023-11-06',
      dateRange: [],
      loading: true,
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
      dateTypeList: [
        {
          value: 'date',
          label: '日'
        },
        {
          value: 'week',
          label: '周'
        },
        {
          value: 'month',
          label: '月'
        },
        {
          value: 'year',
          label: '年'
        },
        {
          value: 'daterange',
          label: '自定义'
        },
      ],
      tableData: [],
      unit: 'kW·h'
    }
  },
  created() {
    this.initTime()
  },
  methods: {
    // 初始化时间
    initTime() {
      this.queryParams.time = moment().format('yyyy-MM-DD')
      this.time = moment().format('yyyy-MM-DD')
      this.dateRange = [moment().format('yyyy-MM-01'), moment().format('yyyy-MM-DD')]
    },
    // 获取区域选中值
    getItemIds(value) {
      // let ids = ''
      // value.forEach(item => {
      //   ids += item + ','
      // })
      this.queryParams.areaIds = value.join(',')
      this.queryData()
    },
    // 能耗类型切换
    energyTypeChange(value) {
      // 能耗单位
      let item = this.energyType.find(t => t.value == value)
      if(item) {
        this.unit = item.unit
      }
    },
    // 日期类型切换
    dateTypeChange(value) {
      if(value == 'daterange') {
        this.queryParams.time = this.dateRange
        return
      }
      this.queryParams.time = this.time
    },
    // 日期修改
    dateChange(value) {
      if(this.queryParams.dateType == 'daterange') {
        this.dateRange = value
        return
      }
      this.time = value
    },
    // 查询按钮
    queryData() {
      this.handleDate()
      this.loading = true
      getConsumptionExpenseReport(this.queryParams).then(res => {
        this.tableData = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    // 处理时间，获取开始结束时间
    handleDate() {
      switch (this.queryParams.dateType) {
        case 'date':
          this.queryParams.startTime = moment(this.time).format('yyyy-MM-DD 00:00:00')
          this.queryParams.endTime = moment(this.time).format('yyyy-MM-DD 23:59:59')
          break;
        case 'week':
          this.queryParams.startTime = moment(this.time).startOf('week').format('yyyy-MM-DD 00:00:00')
          this.queryParams.endTime = moment(this.time).endOf('week').format('yyyy-MM-DD 23:59:59')
          break;
        case 'month':
          this.queryParams.startTime = moment(this.time).startOf('month').format('yyyy-MM-DD 00:00:00')
          this.queryParams.endTime = moment(this.time).endOf('month').format('yyyy-MM-DD 23:59:59')
          break;
        case 'year':
          this.queryParams.startTime = moment(this.time).startOf('year').format('yyyy-MM-DD 00:00:00')
          this.queryParams.endTime = moment(this.time).endOf('year').format('yyyy-MM-DD 23:59:59')
          break;
        case 'daterange':
          this.queryParams.startTime = moment(this.dateRange[0]).format('yyyy-MM-DD 00:00:00')
          this.queryParams.endTime = moment(this.dateRange[1]).format('yyyy-MM-DD 23:59:59')
          break;
        default:
          break;
      }
    },
    getQuarterDate(value) {
      console.log(value);
    }
  }
}
</script>

<style scoped>
.app-container {
  height: calc(100vh - 84px);
}
.content-tree {
  width: 25%;
  height: 100%;
  background: var(--base-item-bg);
  margin-right: 6px;
  padding: 20px;
}
.content-data {
  width: calc(75% - 10px);
  height: 100%;
  background: var(--base-item-bg);
  padding: 12px 20px;
}
.content-box {
  height: calc(100% - 51px);
  /* border: 1px solid #fff; */
}
@media (max-width: 1615px) {
  .content-box {
    height: calc(100% - 102px);
  }
}
@media (max-width: 1037px) {
  .content-box {
    height: calc(100% - 153px);
  }
}
@media (max-width: 663px) {
  .content-box {
    height: calc(100% - 204px);
  }
}
</style>