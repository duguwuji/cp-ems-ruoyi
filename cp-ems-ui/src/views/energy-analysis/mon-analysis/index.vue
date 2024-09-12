<template>
  <div class="app-container flex-between bg-container">
    <div class="content-tree">
      <topologicaTree :showCheckbox="true" @selectAreaId="getId" @treeOptions="getTree"/>
    </div>
    <div class="content-data" v-loading="loading">
      <div class="data-select">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
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
          <el-form-item label="日期" prop="dateType">
            <el-select v-model="queryParams.dateType" placeholder="请选择" style="width: 80px;" @change="dateTypeChange">
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
              placeholder="选择时间"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="search">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="data-table">
        <mon-table :chain-data="chainData" :now-title="nowTitle" :last-title="lastTitle" :addValue="addValue"/>
      </div>
    </div>
  </div>
</template>

<script>
import topologicaTree from "@/components/TopologicaTree/index";
import MonTable from '@/components/Tables/monTable';
import moment from 'moment'
import { getChainByDevice } from '@/api/system/energy'
export default {
  dicts: ['energy_type'],
  components: {
    topologicaTree,
    MonTable
  },
  data() {
    return {
      queryParams: {
        energyType: '0',
        time: "2023-04-13",
        dateType: 'date'
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
      ],
      areaId:[],
      chainData:[],
      treeOptions:[],
      areaName:"",
      nowTitle: "当日用能(kW·h)",
      lastTitle: "昨日用能(kW·h)",
      addValue: '增加值(kW.h)',
      unit: 'kW.h',
      loading: true,
    };
  },
  methods: {
    getNowDate() {
      var dateTime = new Date()
      this.queryParams.time = dateTime
      // this.queryParams.time = dateTime.getFullYear().toString() + '-' + (dateTime.getMonth() + 1) + '-' + dateTime.getDate().toString()
    },
    getId(value){
      this.areaId = value;
      this.search();
    },
    getTree(value){
      this.treeOptions = value;
    },
    dateTypeChange(){
      this.addValue = "增加值(" + this.unit + ")"
      if(this.queryParams.dateType == "date"){
        this.nowTitle = "当日用能(" + this.unit + ")"
        this.lastTitle = "昨日用能(" + this.unit + ")"
      }else if(this.queryParams.dateType == "week"){
        this.nowTitle = "本周用能(" + this.unit + ")"
        this.lastTitle = "上周用能(" + this.unit + ")"
      }else if(this.queryParams.dateType == "month"){
        this.nowTitle = "当月用能(" + this.unit + ")"
        this.lastTitle = "上月用能(" + this.unit + ")"
      }else if(this.queryParams.dateType == "year"){
        this.nowTitle = "当年用能(" + this.unit + ")"
        this.lastTitle = "去年用能(" + this.unit + ")"
      }

      this.search()
    },
    // 能耗类型切换
    energyTypeChange(value) {
      // 能耗单位
      let item = this.energyType.find(t => t.value == value)
      if(item) {
        this.unit = item.unit
      }

      this.dateTypeChange()
    },
    search(){
      let date = ""
      if(this.queryParams.dateType ===  "week"){
        date = moment(this.queryParams.time).format("yyyy-WW");
      }else {
        date = moment(this.queryParams.time).format("yyyy-MM-DD");
      }
      let param = {
        energyType: this.queryParams.energyType,
        dateType: this.queryParams.dateType,
        date: date,
        areaId:this.areaId.join(","),
      }
      this.loading = true
      getChainByDevice(param).then( res =>{
        this.chainData = res.data;
        this.chainData.forEach((item,index) => {
          this.chainData[index].area = this.handleLabel(item.areaId,this.treeOptions)
        })
      }).finally(() => {
        this.loading = false
      })
    },
    handleLabel(id, treeList) {
      treeList.forEach(item => {
        if(item.id == id) {
          this.areaName = item.label
        }
        if(item.children) {
          this.handleLabel(id, item.children)
        }
      })
      return this.areaName
    },
  },
  created() {
    this.getNowDate();
    // this.search()
  }
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
<style lang="scss">

</style>
