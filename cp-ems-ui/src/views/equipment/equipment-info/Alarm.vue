<template>
  <div style="padding-bottom: 20px;">
    <el-table v-loading="loading" :data="alarmList">
            <!-- <el-table-column type="index" width="55" align="center" /> -->
            <!-- <el-table-column label="参数名称" align="center" prop="paramName" /> -->
            <el-table-column label="参数名称" align="center" prop="paramName" width="80">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.electric_type" :value="scope.row.paramName" />
                </template>
            </el-table-column>
            <el-table-column label="报警时间" align="center" prop="alarmTime" width="150">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.alarmTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="报警信息" align="center" prop="alarmInfo" show-overflow-tooltip/>
            <el-table-column label="报警等级" align="center" prop="alarmLevel">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.alarm_level" :value="scope.row.alarmLevel" />
                </template>
            </el-table-column>
            <el-table-column label="报警区域" align="center" prop="area" />
            <el-table-column label="报警值" align="center" prop="alarmVal" />
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getAlarmList" :pagerCount="5"/>
  </div>
</template>

<script>
import { listAlarm } from "@/api/system/alarm";
export default {
  dicts: ['alarm_level', "electric_type"],
  props: {
    equipmentInfo: {
      type: Object,
      required: true
    },
  },
  data() {
    return {
      alarmList: [],
        queryParams: {
        pageSize: 10,
        pageNum: 1,
        equipment: ''
      },
      total: 0,
      loading: false,
    }
  },
  created() {
    this.getAlarmList()
  },
  methods: {
    // 获取报警列表
    getAlarmList() {
      this.loading = true
      this.queryParams.equipment = this.equipmentInfo.sn
      listAlarm(this.queryParams).then(res => {
        this.alarmList = res.rows
        this.total = res.total
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>

</style>