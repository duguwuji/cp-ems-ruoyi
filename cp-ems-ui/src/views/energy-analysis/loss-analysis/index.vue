<template>
  <div class="app-container bg-container">
    <div class="loss-content">
      <div class="data-select">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
        >
          <el-form-item label="分类能耗" prop="energyType">
            <el-select v-model="queryParams.energyType" placeholder="请选择" >
              <el-option
                v-for="item in dict.type.energy_type"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
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
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini"
            @click="handleQuery">查询</el-button
            >
          </el-form-item>
        </el-form>
      </div>
      <div class="data-table" v-loading="loading">
        <lossTable :tableData="tableData" :unit="unit"/>
      </div>
    </div>
  </div>
</template>

<script>
import lossTable from '@/components/Tables/lossTable';
import { getLossAnalysis } from '@/api/system/energy'
import moment from "moment/moment";
export default {
  dicts: ['energy_type'],
  components: {
    lossTable,
  },
  data() {
    return {
      tableData: [],
      queryParams: {
        energyType: '0',
        dateRange:[]
      },
      energyType: [
        {
          value: 0,
          label: "电",
          unit: 'kW.h'
        },
        {
          value: 1,
          label: "水",
          unit: 't'
        },
      ],
      unit: 'kW.h',
      loading: true,
    }
  },
  methods: {
    getDefaultTime(){
      this.queryParams.dateRange[0] = moment().format("yyyy-MM-01 00:00:00")
      this.queryParams.dateRange[1] = moment().format("yyyy-MM-DD HH:mm:ss")
    },
    getTimeRange() {},
    handleQuery() {
      let _this = this

      // 能耗类型
      let item = this.energyType.find(t => t.value == this.queryParams.energyType)
      if(item) {
        this.unit = item.unit
      }

      _this.queryParams.startTime = _this.queryParams.dateRange[0];
      _this.queryParams.endTime = _this.queryParams.dateRange[1];
      _this.loading = true
      getLossAnalysis(_this.queryParams).then((res) => {
        _this.tableData = res.data
      }).finally(() => {
        _this.loading = false
      })
    },
},
  created() {
    this.getDefaultTime();
    this.handleQuery();
  },
};
</script>

<style lang="scss" scoped>
.app-container {
  height: calc(100vh - 84px);

  .loss-content {
    height: 100%;
    width: 100%;
    background: var(--base-item-bg);
    padding: 12px;
  }

  .data-table {
    height: calc(100% - 51px);
    // border: 1px solid;
  }

  @media screen and (max-width: 997px) {
    .data-table {
      height: calc(100% - 101px);
    }
  }
}
</style>
