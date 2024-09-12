<template>
  <div class="alarmInfo-container">
    <VueSeamlessScroll :data="alarmList" :class-option="classOption" style="height: 100%;overflow:hidden;">
      <div class="alarm-item" v-for="(item, index) in alarmList" :key="index">
        <div class="flex-between mb5">
          <span class="alarm-name">{{item.area}}</span>
          <span>{{item.alarmTime}}</span>
        </div>
        <div class="flex-between">
          <span>{{item.alarmInfo}}</span>
          <div class="item-level" :class="changeAlarmBg(item.alarmLevel)">{{getLevel(item.alarmLevel)}}</div>
        </div>
      </div>
    </VueSeamlessScroll>
  </div>
</template>

<script>
import VueSeamlessScroll from "vue-seamless-scroll";
import { getLatestAlarmsByCount } from '@/api/system/alarm'
export default {
  components: {
    VueSeamlessScroll,
  },
  data() {
    return {
      alarmList:[],
    }
  },
  computed: {
    classOption() {
      return {
        step: 0.4, // 数值越大速度滚动越快
        limitMoveNum: 4, // 开启无缝滚动的数据量 设置(数值<页面展示数据条数不滚)(超过页面展示条数滚动)
        openWatch: true, // 开启数据实时监控刷新dom
        // singleHeight: 30,
        // waitTime: 2500, // 单行停顿时间(singleHeight,waitTime)
      };
    },
  },
  methods: {
    changeAlarmBg(level) {
      switch (level) {
        case '0':
          return 'alarm-normal'
        case '1':
          return 'alarm-warning'
        case '2':
          return 'alarm-danger'
        default:
          break;
      }
    },
    getLevel(level) {
      switch (level) {
        case '0':
          return '一般'
        case '1':
          return '紧急'
        case '2':
          return '严重'
        default:
          break;
      }
    },
    getAlarmList(){
      let params = {
        count: 10
      }
      getLatestAlarmsByCount(params).then( res =>{
        if(!res.data) {
          return
        }
        this.alarmList = res.data;
      })
    }
  },
  created() {
    this.getAlarmList()
  }

}
</script>

<style lang='scss' scoped>
.alarmInfo-container {
  width: 100%;
  height: 100%;
  padding: 12px 12px 0;
  font-size: 14px;

  .alarm-item {
    padding: 12px 0;
    border-bottom: 1px solid;
    border-image: linear-gradient(90deg, #00d0fe 0%, #286be9 50%, #00d0fe 100%) 2 2 2 2;;

    .alarm-name {
      // color: var(--current-color);
      font-weight: bold;
      color: #00d0fe;
    }

    .item-level {
      padding: 2px 8px;
      font-size: 13px;
      color: #fff;
      // background: #f56c6c;
      border-radius: 10px 0 10px 0;
    }
    .alarm-normal {
      background: #67c23a;
    }
    .alarm-warning {
      background: #e6a23c;
    }
    .alarm-danger {
      background: #f56c6c;
    }
  }
}
</style>
