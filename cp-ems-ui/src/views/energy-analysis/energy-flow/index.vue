<template>
  <div class="app-container bg-container" v-loading="loading">
    <div class="chart-select">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" :rules="rules">
        <el-form-item label="分类能耗" prop="energyType">
          <el-select v-model="queryParams.energyType" placeholder="请选择" @change="energyTypeChange">
            <el-option
              v-for="item in dict.type.energy_type"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="起止时间" prop="dateRange">
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
          <el-button type="primary" icon="el-icon-search" size="mini" @click="getTopologyList"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <el-empty v-if="this.value == 0" image="" style="height: 700px; width: 100%"></el-empty>
    <div v-else id="chart-sankey" style="height: 700px; width: 100%" />
  </div>
</template>

<script>
import * as echarts from "echarts";
import resize from "@/views/dashboard/mixins/resize";
import {
  topologyTreeSelect,
  listItemTopology,
} from "@/api/system/itemTopology";
import { getFlowData } from '@/api/system/energy'
import moment from 'moment/moment'

export default {
  mixins: [resize],
  dicts: ['energy_type'],
  props: {},
  data() {
    return {
      queryParams: {
        energyType: '0',
        dateRange: [],
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
      chart: null,
      nodes: [
        {
          name: "一层",
          itemStyle: {
            color: "#ccebc5",
          },
        },
        {
          name: "云脉软件",
          itemStyle: {
            color: "#b3cde3",
          },
        },
        {
          name: "101",
          itemStyle: {
            color: "#d48265",
          },
        },
        {
          name: "102",
          itemStyle: {
            color: "#d7e0e8",
          },
        },
      ],
      links: [
        { source: "一层", target: "102", value: 1500 },
        { source: "云脉软件", target: "一层", value: 2000 },
        { source: "一层", target: "101", value: 500 },
      ],
      rules: {
        dateRange: [
          { required: true, message: "请选择时间", trigger: "change" },
        ],
      },
      value:0,
      unit: 'kW.h',
      loading: true,
    };
  },
  mounted() {
    // this.$nextTick(() => {
    //   this.initChart();
    // });
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    defaultDate(){
      this.queryParams.dateRange = [moment().format("yyyy-MM-01 00:00:00"),moment().format("yyyy-MM-DD 23:59:59")]
    },
    initChart() {
      this.chart = echarts.init(document.getElementById("chart-sankey"));
      // this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: "item",
          triggerOn: "mousemove",
          valueFormatter: (value) => value + this.unit
        },
        // color: ['#fbb4ae', '#b3cde3', '#ccebc5', '#decbe4'],
        series: [
          {
            type: "sankey",
            data: this.nodes,
            links: this.links,
            height: "80%",
            // emphasis: {
            //   focus: "adjacency",
            // },
            levels: [
              {
                depth: 0,
                itemStyle: {
                  color: "#fbb4ae",
                },
                lineStyle: {
                  color: "source",
                  opacity: 0.6,
                },
              },
              {
                depth: 1,
                itemStyle: {
                  color: "#b3cde3",
                },
                lineStyle: {
                  color: "source",
                  opacity: 0.6,
                },
              },
              {
                depth: 2,
                itemStyle: {
                  color: "#ccebc5",
                },
                lineStyle: {
                  color: "source",
                  opacity: 0.6,
                },
              },
              {
                depth: 3,
                itemStyle: {
                  color: "#decbe4",
                },
                lineStyle: {
                  color: "source",
                  opacity: 0.6,
                },
              },
            ],
            lineStyle: {
              color: 'gradient',
              curveness: 0.5,
            },
          },
        ],
      });
    },
    getTimeRange() {
      console.log(this.queryParams.dateRange);
    },
    // getTopologyTree() {
    //   topologyTreeSelect().then((response) => {
    //     this.topologyOptions = response.data;
    //   });
    // },
    // 能耗类型切换
    energyTypeChange(value) {
      let item = this.energyType.find(t => t.value == this.queryParams.energyType)
      if(item) {
        this.unit = item.unit
      }

      this.getTopologyList()
    },
    getTopologyList() {
      this.$refs["queryForm"].validate((valid) => {
        if (valid) {
          this.nodes = [];
          this.links = [];
          let params = {
            startTime:this.queryParams.dateRange[0],
            endTime:this.queryParams.dateRange[1],
            energyType: this.queryParams.energyType,
            status: '0'
          }
          this.loading = true
          getFlowData(params).then((res) => {
            if (res.data == [] || res.data == null) {
              this.nodes = res.data;
              return;
            }

            let result = res.data;
            res.data.forEach((item) => {
              let object = {
                name: item.itemName,
              };
              this.nodes.push(object);

              let index = result.find((obj) => obj.itemId == item.parentId);
              if (index) {
                let link = {
                  source: index.itemName,
                  target: item.itemName,
                  value: item.value,
                };
                this.links.push(link);
              }
            });

            this.value = 0
            this.links.forEach(item =>{
              if(item.value != null){
                this.value += parseInt(item.value)
              }
            })
            if(this.value == 0){
              return
            }
            if (this.chart != null) {
              this.chart.dispose();
              this.chart = null;
            }
            this.$nextTick(() => {
              this.initChart();
            })
          }).finally(() => {
            this.loading = false
          });
        }
      });
    },
  },
  created() {
    this.defaultDate();
    // this.getTopologyTree()
    this.$nextTick(() => {
      this.getTopologyList()
    })
  },
};
</script>
<style lang="scss" scoped>
.app-container {
  min-height: calc(100vh - 84px);
  .chart-select {
    margin-top: 12px;
  }
}
</style>
