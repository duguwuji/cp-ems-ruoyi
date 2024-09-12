<template>
  <div class="table-container">
    <el-table class="currentTable unchanged" :data="tableData" height="100%" border style="width: 100%">
      <el-table-column label="" align="center" prop="type">
      </el-table-column>
      <el-table-column prop="chain" label="电量(kW·h)" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.data != 0">{{scope.row.data}}</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column prop="same" label="环比(%)" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.chain != 0" :class="scope.row.chain > 0 ? 'yoy-up' : 'yoy-down'">{{scope.row.chain}}</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'reportTable',
  props: {
    chainData: {
      type: Array,
    },
  },
  data() {
    return {
      tableData: [
        {
          type: "尖",
          data: 1756,
          chain: 35.23,
          same: 35.23
        },
        {
          type: '峰',
          data: 2345,
          chain: -23.42,
          same: 35.23
        },
        {
          type: "平",
          data: 343,
          chain: 34.21,
          same: 35.23
        },
        {
          type: "谷",
          data: 343,
          chain: 34.21,
          same: 35.23
        },
      ],
    }
  },
  watch: {
    'chainData': {
      handler() {
        this.tableData = this.chainData;
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    
  }
};
</script>

<style lang="scss" scoped>
.table-container {
  height: 100%;
  width: 100%;

  .currentTable {
    .yoy-down::after {
      content: "↓";
      position: relative;
      left: 5px;
      top: -2px;
      color: #49a798;
    }
    .yoy-up::after {
      content: "↑";
      position: relative;
      left: 5px;
      top: -2px;
      color: #dd65a1;
    }

    // 滚动条的宽度
    // ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
    //   width: 6px; // 横向滚动条
    //   height: 6px; // 纵向滚动条 必写
    // }
    // // 滚动条的滑块
    // ::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
    //   background-color: #ddd;
    //   border-radius: 3px;
    // }

    // 去除滚动条上方多余显示
    ::v-deep colgroup col[name='gutter']{
      // display: none;
      width: 6px !important;
    }
    ::v-deep .el-table__body{
      width: 100% !important;
    }
    // Firefox滚动条样式设置
    // ::v-deep .el-table__body-wrapper {
    //   //overflow-y: scroll;
    //   //scrollbar-color: #bebebf transparent;
    //   //scrollbar-width: thin;
    // }
  }
}
</style>
<style>

</style>
