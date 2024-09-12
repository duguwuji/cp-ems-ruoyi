<template>
  <div class="btn-container flex-between">
    <div class="alarm-item mr20">
      <el-button type="success" size="mini" @click="toAlarmDetail(0)">一般</el-button>
      <sup class="alarm-num-success">{{ general > 99 ? '99+' : general }}</sup>
    </div>
    <div class="alarm-item mr20">
      <el-button type="warning" size="mini" @click="toAlarmDetail(1)">紧急</el-button>
      <sup class="alarm-num-warning">{{ emergent > 99 ? '99+' : emergent }}</sup>
    </div>
    <div class="alarm-item">
      <el-button type="danger" size="mini" @click="toAlarmDetail(2)">严重</el-button>
      <sup class="alarm-num-danger">{{ serious > 99 ? '99+' : serious }}</sup>
    </div>
  </div>
</template>

<script>
import { getCountOfAllStatus } from '@/api/system/alarm'

export default {
  data() {
    return {
      general:0,
      emergent:0,
      serious:0,

    }
  },
  methods: {
     toAlarmDetail(level) {
      this.$router.push({path:'/alarm/realtime-alarm',query: { alarmLevel: level }})
     },
    getCount(){
      getCountOfAllStatus().then(res =>{
        res.data.forEach( item =>{
          if(item.alarmLevel === "0"){
            this.general = item.count;
          }else if(item.alarmLevel === "1"){
            this.emergent = item.count;
          }else  if(item.alarmLevel === "2"){
            this.serious = item.count;
          }
        })
      })
    }
  },
  created() {
    this.getCount();
  }
}
</script>

<style lang="scss" scoped>
.btn-container {
  height: 100%;
  width: 100%;
  .alarm-item {
    position: relative;
    // vertical-align: middle;
    display: inline-block;
    .alarm-num-success,.alarm-num-warning,.alarm-num-danger {
      position: absolute;
      color: #fff;
      font-size: 12px;
      height: 18px;
      min-width: 18px;
      padding: 0 6px;
      border-radius: 10px;
      line-height: 18px;
      box-shadow: -1px 1px 5px #fff;
      text-align: center;
      top: 0;
      right: 10px;
      -webkit-transform: translateY(20%) translateX(90%);
      transform: translateY(20%) translateX(90%);
    }
    .alarm-num-success {
      background-color: #67c23a;
    }
    .alarm-num-warning {
      background-color: #e6a23c;
    }
    .alarm-num-danger {
      background-color: #f56c6c;
    }
  }
}
</style>
