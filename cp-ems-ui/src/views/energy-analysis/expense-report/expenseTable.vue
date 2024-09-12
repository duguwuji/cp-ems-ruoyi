<template>
  <div style="height: 100%;width: 100%">
    <el-table ref="table" :data="tableData" height="100%" style="width: 100%" border :show-summary="true" :summary-method="getTableSummary">
      <el-table-column label="名称" prop="node" fixed="left"></el-table-column>
      <el-table-column v-for="(item) in timeColumns" :label="item" :key="item" align="center">
        <el-table-column :label="'用能(' + unit + ')'" prop="energy" align="center" min-width="110">
          <template slot-scope="scope">
            {{handleEnergy(scope.row, item)}}
          </template>
        </el-table-column>
        <el-table-column label="费用(元)" prop="cost" align="center" min-width="110">
          <template slot-scope="scope">
            {{handleCost(scope.row, item)}}
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="合计" align="center">
        <el-table-column :label="'用能(' + unit + ')'" prop="totalEnergy" align="center" min-width="110"></el-table-column>
        <el-table-column label="费用(元)" prop="totalCost" align="center" min-width="110"></el-table-column>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    testData: {
      type: Array,
      default: () => {
        return [
          {
            node:'test', 
            list: [{date: '01', energy: 100, cost: 21.9},{date: '02', energy: 100, cost: 21.9},{date: '03', energy: 100, cost: 21.9},{date: '04', energy: 100, cost: 21.9},],
            totalEnergy: 400,
            totalCost: 243.3
          },
          {
            node:'test2', 
            list: [{date: '01', energy: 200, cost: 21.9},{date: '02', energy: 200, cost: 21.9},{date: '03', energy: 200, cost: 21.9},{date: '04', energy: 300, cost: 21.9},],
            totalEnergy: 400,
            totalCost: 243.3
          },
        ]
      }
    },
    unit: {
      type: String,
      default: 'kW·h'
    }
  },

  data() {
    return {
      timeColumns: [],
    }
  },
  watch: {
    'testData': {
      deep: true,
      handler() {
        this.$nextTick(() => {
          this.$refs.table.doLayout()
        })
      }
    }
  },
  created() {},
  computed: {
    tableData() {
      const columns = [];
      if(!this.testData[0]) return columns
      this.testData[0].list.forEach(subItem => {
        if (!columns.includes(subItem.date)) {
          columns.push(subItem.date);
        }
      });
      this.timeColumns = columns;
      // const dateColumns = new Set();
      // this.testData.forEach(item => {
      //   item.list.forEach(subItem => {
      //     dateColumns.add(subItem.date);
      //   });
      // });
      return this.testData.map(item => {
        const row = {
          node: item.node,
          totalEnergy: item.totalEnergy,
          totalCost: item.totalCost
        };
        item.list.forEach(subItem => {
          row[subItem.date] = {
            energy: subItem.energy,
            cost: subItem.cost
          };
        });
        return row;
      });
    },
    // timeColumns() {
    //   const columns = [];
    //   if(!this.testData[0]) return columns
    //   this.testData[0].list.forEach(subItem => {
    //     if (!columns.includes(subItem.date)) {
    //       columns.push(subItem.date);
    //     }
    //   });
    //   return columns;
    // }
  },
  methods: {
    getTableSummary({ columns, data }) {
      const sums = [];
      const columnNames = this.timeColumns
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        let values = ''
        if(index > 0 && index < columns.length - 2) {
          if(index % 2 == 1) {
            // 奇数 (n-1)/2
            let cloumnName = columnNames[(index - 1) / 2]
            values = data.map(item => Number(item[cloumnName].energy))
          }
          if(index % 2 == 0) {
            // 偶数 (n-2)/2
            let cloumnName2 = columnNames[(index - 2) / 2]
            values = data.map(item => Number(item[cloumnName2].cost))
          }
        } else {
          values = data.map(item => Number(item[column.property]));
        }
        if (!values.every(energy => isNaN(energy))) {
          sums[index] = values.reduce((prev, curr) => {
            const energy = Number(curr);
            if (!isNaN(energy)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
          sums[index] = sums[index].toFixed(2)
        } else {
          sums[index] = '--';
        }
      });
      return sums;
    },
    // 处理数据
    handleEnergy(row, item) {
      return row[item].energy
    },
    // 处理费用
    handleCost(row, item) {
      return row[item].cost
    }
  }
}
</script>

<style scoped>

</style>