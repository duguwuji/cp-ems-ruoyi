<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="充电站" prop="stationId">
        <el-select v-model="queryParams.stationId" placeholder="请选择充电站" clearable>
          <el-option
            v-for="station in stationList"
            :key="station.stationId"
            :label="station.name"
            :value="station.stationId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="价格策略名称" prop="strategyName">
        <el-input
          v-model="queryParams.strategyName"
          placeholder="请输入策略名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计费模式" prop="billModel">
        <el-select v-model="queryParams.billModel" placeholder="请选择计费模式" clearable>
          <el-option
            v-for="dict in dict.type.bill_model"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="策略状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.price_strategy_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:priceStrategy:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:priceStrategy:edit']"
        >修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:priceStrategy:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:priceStrategy:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="priceStrategyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="充电价格策略编号" align="center" prop="id" v-if="true" width="180"/>
      <el-table-column label="策略名称" align="center" prop="strategyName" width="180" show-overflow-tooltip/>
      <el-table-column label="充电站名称" align="center" prop="stationName" width="180" show-overflow-tooltip/>
      <el-table-column label="计费模式" align="center" prop="billModel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bill_model" :value="scope.row.billModel"/>
        </template>
      </el-table-column>
      <el-table-column label="充电策略说明" align="center" prop="description" width="180" show-overflow-tooltip>
        <template slot-scope="scope">
          {{scope.row.description ? scope.row.description : '--'}}
        </template>
      </el-table-column>
      <el-table-column label="策略状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.price_strategy_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:priceStrategy:edit']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-position"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:priceStrategy:edit']"
          >下发</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:priceStrategy:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改充电价格策略信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <!-- <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item label="充电价格策略编号" prop="id">
                <el-input v-model="form.id" placeholder="创建后自动生成" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              
            </el-col>
          </el-row> -->
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item label="策略名称" prop="strategyName">
                <el-input v-model="form.strategyName" placeholder="请输入策略名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="充电站名称" prop="stationId">
                <el-select v-model="form.stationId" placeholder="请选择充电站" clearable style="width:100%;" @change="stationChange">
                  <el-option
                    v-for="station in stationList"
                    :key="station.stationId"
                    :label="station.name"
                    :value="station.stationId">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item label="计费模式" prop="billModel">
                <el-radio-group v-model="form.billModel">
                  <el-radio
                    v-for="(dict, index) in dict.type.bill_model"
                    :key="index"
                    :label="dict.value"
                  >{{dict.label}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio
                    v-for="dict in dict.type.price_strategy_status"
                    :key="dict.value"
                    :label="dict.value"
                  >{{dict.label}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="12">
            <el-col :span="24">
              <el-form-item label="策略说明">
                <el-input v-model="form.description" placeholder="请输入策略说明" type="textarea" :rows="1"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div>
          <div class="title-text">价格参数</div>
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            style="margin-bottom: 8px;"
            @click="addParam"
            v-hasPermi="['system:priceStrategy:add']"
          >新增</el-button>
          <div>
            <el-table v-loading="paramLoading" :data="paramList" @row-click="rowClick">
              <el-table-column label="序号" align="center" type="index" width="50"></el-table-column>
              <el-table-column label="开始时间" prop="startTime" align="center"></el-table-column>
              <el-table-column label="结束时间" prop="endTime" align="center">
                <template slot-scope="scope">
                  <el-time-select
                    class="select-time"
                    placeholder="结束时间"
                    :clearable="false"
                    :editable="false"
                    size="mini"
                    @change="endTimeChange"
                    v-model="scope.row.endTime"
                    :disabled="scope.$index + 1 >= paramList.length"
                    :picker-options="{
                      start: '00:00',
                      step: '00:30',
                      end: '23:00'
                    }">
                  </el-time-select>
                </template>
              </el-table-column>
              <el-table-column label="时段标识" prop="mark" align="center">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.mark" placeholder="请选择" size="mini">
                    <el-option 
                      v-for="dict in dict.type.price_param_mark"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="电费单价(元)" prop="elecPrice" align="center">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.elecPrice" placeholder="请输入" size="mini" :controls="false" :precision="4" style="width: 100%;"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="服务费单价(元)" prop="servicePrice" align="center">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.servicePrice" placeholder="请输入" size="mini" :controls="false" :precision="4" style="width: 100%;"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="总价(元)" align="center">
                <template slot-scope="scope">{{totalPrice(scope.row)}}</template>
              </el-table-column>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    v-show="scope.$index + 1 < paramList.length"
                    @click="delectParam(scope.row)"
                    v-hasPermi="['system:priceStrategy:remove']"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPriceStrategy, getPriceStrategy, delPriceStrategy, addPriceStrategy, updatePriceStrategy } from "@/api/chargingStation/chargingPriceStrategy";
import {listStation} from "@/api/chargingStation/station"
import {listPriceParam, addPriceParam, updatePriceParam, updateParamList} from "@/api/chargingStation/chargingPriceParam"
import moment from "moment"
import { log } from 'ezuikit-js';
export default {
  dicts: ['price_strategy_status', 'bill_model', 'price_param_mark'],
  name: "PriceStrategy",
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 价格参数表格加载遮罩层
      paramLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示查询条件
      showSearch: true,
      // 总条数
      total: 0,
      // 充电价格策略信息表格数据
      priceStrategyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        strategyName: undefined,
        stationId: undefined,
        stationName: undefined,
        billModel: undefined,
        description: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "充电价格策略id不能为空", trigger: "change" }
        ],
        strategyName: [
          { required: true, message: "策略名称不能为空", trigger: "change" }
        ],
        stationId: [
          { required: true, message: "充电站id不能为空", trigger: "change" }
        ],
        stationName: [
          { required: true, message: "充电站名称不能为空", trigger: "change" }
        ],
        billModel: [
          { required: true, message: "计费模式不能为空", trigger: "change" }
        ],
        description: [
          { required: true, message: "充电策略说明不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "策略状态不能为空", trigger: "change" }
        ],
      },
      stationList: [],
      paramList: [],
      clickItem: {},
    };
  },
  created() {
    this.getStationList()
    this.getList();
  },
  methods: {
    // 查询价格参数
    getPriceParam(strategyId) {
      this.paramLoading = true
      var param = {strategyId: strategyId}
      listPriceParam(param).then(res => {
        this.paramList = res.rows
        this.paramLoading = false
      })
    },
    // 查询充电站列表
    getStationList() {
      listStation().then(res => {
        this.stationList = res.rows
      })
    },
    /** 查询充电价格策略信息列表 */
    getList() {
      this.loading = true;
      listPriceStrategy(this.queryParams).then(response => {
        this.priceStrategyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        strategyName: undefined,
        stationId: undefined,
        stationName: undefined,
        billModel: undefined,
        description: undefined,
        status: undefined,
        delFlag: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined
      };
      this.resetForm("form");
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加充电价格策略信息";
      // 计费模式默认第一个，状态默认未使用
      this.form.billModel = '0'
      this.form.status = '0'
      // 新增初始化一条参数数据
      this.paramList = [
        {startTime: '00:00', endTime: '24:00', mark: undefined, elecPrice: 0, servicePrice: 0}
      ]
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getPriceStrategy(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改充电价格策略信息";
      });
      this.getPriceParam(id)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 如果价格参数的时段标识未选则提示
          for (var i = 0; i < this.paramList.length; i++) {
            if(!this.paramList[i].mark) {
              console.log(this.paramList[i].mark);
              this.$message({message: '价格参数的时段标识不能为空', type: 'warning'})
              return 
            }
          }

          this.buttonLoading = true;
          if (this.form.id != null) {
            updatePriceStrategy(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.updateParam()
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addPriceStrategy(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.updateParam(response.data)
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除充电价格策略信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delPriceStrategy(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/priceStrategy/export', {
        ...this.queryParams
      }, `priceStrategy_${new Date().getTime()}.xlsx`)
    },
    // 计算总价
    totalPrice(priceParam){
      return (Number(priceParam.elecPrice) + Number(priceParam.servicePrice)).toFixed(4)
    },
    // 价格参数新增按钮
    addParam() {
      let lastParam = this.paramList[this.paramList.length - 1]
      this.paramList[this.paramList.length - 1].endTime = moment('2023-10-12 ' + lastParam.startTime).add(1, 'hours').format('HH:mm')
      var paramItem = {
        startTime: moment('2023-10-12 ' + lastParam.startTime).add(1, 'hours').format('HH:mm'),
        endTime: '24:00',
        mark: undefined,
        elecPrice: 0,
        servicePrice: 0,
        strategyId: this.form.id
      }
      this.paramList.push(paramItem)
    },
    // 价格参数删除按钮
    delectParam(row) {
      let index = this.paramList.findIndex(p => p.endTime == row.endTime)
      if(index < 0) return
      this.paramList.splice(index, 1)
      this.paramList[index].startTime = index == 0 ? '00:00' : this.paramList[index - 1].endTime
    },
    // 更新策略的所有价格参数
    updateParam(strategyId) {
      if(strategyId) {
        this.paramList.forEach(item => {
          item.strategyId = strategyId
        });
      }

      updateParamList(this.paramList).then(res => {})
    },
    // 充电站修改
    stationChange(value) {
      let item = this.stationList.find(s => s.stationId == value)
      if(item) {
        this.form.stationName = item.name
      }
    },
    // 价格参数整行被点击
    rowClick(row) {
      this.clickItem = {...this.paramList.find(p => p.startTime == row.startTime)}
    },
    // 价格参数时间修改
    endTimeChange(value) {
      let index = this.paramList.findIndex(p => p.startTime == this.clickItem.startTime)

      let diff1 =  moment('2020-10-13 ' + value).diff(moment('2020-10-13 ' + this.paramList[index + 1].startTime), 'minutes')
      if(diff1 <= 0) {
        this.$message({ message: '当前时间段的结束时间不能早于开始时间', type: 'warning' })
        this.paramList[index].endTime = this.clickItem.endTime
        return
      }
      
      let diff = moment('2020-10-13 ' + value).diff(moment('2020-10-13 ' + this.paramList[index + 1].endTime), 'minutes')
      if(diff >= 0) {
        this.$message({ message: '当前时间段的结束时间不能晚于下一时间段的结束时间', type: 'warning' })
        this.paramList[index].endTime = this.clickItem.endTime
        return
      }

      this.paramList[index].endTime = value
      this.paramList[index + 1].startTime = value
    }
  }
};
</script>
<style scoped>
.select-time {
  width: 100%;
}
.title-text {
  font-size: 18px;
  margin: 24px 0;
  color: var(--base-color-1);
}
</style>