<template>
  <el-table class="equipment-table" :data="equipmentList" max-height="400" width="100%" :row-class-name="handleRowStyle" cell-class-name="cellStyle" @row-click="handleSelect">
    <el-table-column prop="name" label="设备名" align="center"> </el-table-column>
    <el-table-column prop="sn" label="设备编号" align="center"> </el-table-column>
    <el-table-column prop="type" label="设备类型" align="center" width="80">
      <template slot-scope="scope">
        <dict-tag :options="dict.type.energy_type" :value="scope.row.type"/>
      </template>
    </el-table-column>
    <el-table-column prop="model" label="设备型号" align="center"> </el-table-column>
  </el-table>
</template>

<script>
import {listEquipmentInfo} from "@/api/system/equipmentInfo"
export default {
  dicts: ["energy_type"],
  props: {
    selectIds: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      equipmentList: [],
      selectList: []
    };
  },
  watch: {
    selectIds: {
      handler() {
        this.selectList = this.selectIds
      },
      deep: true,
      immediate: true
    }
  },
  created() {
    this.getEquipment()
  },
  methods: {
    handleRowStyle(data) {
      var selected = this.selectList.find(e => e == data.row.sn)
      if(selected) return 'rowActive'
      return 'rowNormal'
    },
    handleSelect(row) {
      var selected = this.selectList.find(e => e == row.sn)
      if(selected) {
        this.selectList = this.selectList.filter(e => e !== selected)
        this.$emit('selectChange', this.selectList)
        return
      }
      this.selectList.push(row.sn)
      this.$emit('selectChange', this.selectList)
    },

    // 获取设备列表
    getEquipment() {
      listEquipmentInfo().then(res => {
        this.equipmentList = res.rows
      })
    }
  }
};
</script>

<style scoped>
/* 全局滚动条样式 */
.equipment-table>>>.el-table__body-wrapper::-webkit-scrollbar {
  width: 8px;
  height: 8px;
  background: transparent;
}
.equipment-table>>>.el-table__body-wrapper::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 4px;
}
.equipment-table>>>.el-table__body-wrapper::-webkit-scrollbar-thumb {
  background: #c6c7c9;
  border-radius: 4px;
}
.equipment-table>>>.el-table__body-wrapper::-webkit-scrollbar-thumb:hover {
  background: #a2a3a5;
}
.equipment-table>>>.el-table__body-wrapper::-webkit-scrollbar-corner {
  background: transparent;
}
.equipment-table>>>.el-table__header colgroup col[name='gutter'] {
  width: 0px !important;
  display: none;
}
.equipment-table>>>.el-table__cell.gutter {
  width: 0px !important;
}
.equipment-table>>>.el-table__body {
  width: 100% !important;
  table-layout: inherit !important;
}

.equipment-table>>>.rowActive {
  background-color: rgba(54, 113, 232, .2) !important;
  font-weight: bold;
  cursor: pointer;
}
.equipment-table>>>.rowNormal {
  cursor: pointer;
}
.equipment-table>>>.rowNormal:hover {
  background-color: var(--base-color-9);
}
.equipment-table>>>.cellStyle {
  background-color: transparent !important;
  padding: 6px 0;
}
</style>