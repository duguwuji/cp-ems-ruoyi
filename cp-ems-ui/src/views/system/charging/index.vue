<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="开始时间" prop="startDate">
          <el-date-picker clearable
            v-model="queryParams.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endDate">
          <el-date-picker clearable
            v-model="queryParams.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item> -->
      <!-- <el-form-item label="单价" prop="unitPrice">
          <el-input
            v-model="queryParams.unitPrice"
            placeholder="请输入单价"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item> -->
      <el-form-item label="尖峰平谷" prop="periodSwitch">
        <!-- <el-input
            v-model="queryParams.periodSwitch"
            placeholder="请输入尖峰平谷"
            clearable
            @keyup.enter.native="handleQuery"
          /> -->
        <el-select v-model="queryParams.periodSwitch" placeholder="尖峰平谷" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_notice_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="尖时单价" prop="sharpPrice">
          <el-input
            v-model="queryParams.sharpPrice"
            placeholder="请输入尖时单价"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="峰时单价" prop="peekPrice">
          <el-input
            v-model="queryParams.peekPrice"
            placeholder="请输入峰时单价"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="平时单价" prop="ordinaryPrice">
          <el-input
            v-model="queryParams.ordinaryPrice"
            placeholder="请输入平时单价"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="谷时单价" prop="valleyPrice">
          <el-input
            v-model="queryParams.valleyPrice"
            placeholder="请输入谷时单价"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item> -->
      <el-form-item label="阶梯计费" prop="stepSwitch">
        <!-- <el-input
            v-model="queryParams.stepSwitch"
            placeholder="请输入阶梯计费"
            clearable
            @keyup.enter.native="handleQuery"
          /> -->
        <el-select v-model="queryParams.stepSwitch" placeholder="阶梯计费" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_notice_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:charging:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:charging:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:charging:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:charging:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chargingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="计费方案ID" align="center" prop="chargingId" v-if="true"/> -->
      <el-table-column label="类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.energy_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="单价" align="center" prop="unitPrice" /> -->
      <el-table-column label="尖峰平谷" align="center" prop="periodSwitch">
        <template slot-scope="scope">
          {{ scope.row.periodSwitch == 0 ? '是' : '否' }}
        </template>
      </el-table-column>
      <!--         <el-table-column label="尖时单价" align="center" prop="sharpPrice" />
        <el-table-column label="峰时单价" align="center" prop="peekPrice" />
        <el-table-column label="平时单价" align="center" prop="ordinaryPrice" />
        <el-table-column label="谷时单价" align="center" prop="valleyPrice" /> -->
      <el-table-column label="阶梯计费" align="center" prop="stepSwitch">
        <template slot-scope="scope">
          {{ scope.row.stepSwitch == 0 ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
            @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:charging:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:charging:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改计费方案信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option v-for="item in dict.type.energy_type" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startDate">
          <el-date-picker clearable v-model="form.startDate" @change="changeStart" type="date" value-format="yyyy-MM-dd 00:00:00"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endDate">
          <el-date-picker clearable v-model="form.endDate" @change="changeEnd" type="date" value-format="yyyy-MM-dd 23:59:59"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice" v-if="form.periodSwitch == '1' || form.periodSwitch == null">
          <el-input-number v-model="form.unitPrice" :min="0" :precision="2" placeholder="请输入单价"></el-input-number>
        </el-form-item>
        <el-form-item label="尖峰平谷" prop="periodSwitch">
          <el-switch v-model="form.periodSwitch" active-value="0" inactive-value="1"></el-switch>
          <!-- <el-input v-model="form.periodSwitch" placeholder="请输入尖峰平谷开关" /> -->
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="尖时单价" prop="sharpPrice" v-if="form.periodSwitch == '0'">
              <el-input-number v-model="form.sharpPrice" :min="0" :precision="2" placeholder="请输入尖时单价" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="峰时单价" prop="peekPrice" v-if="form.periodSwitch == '0'">
              <el-input-number v-model="form.peekPrice" :min="0" :precision="2" placeholder="请输入峰时单价" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="平时单价" prop="ordinaryPrice" v-if="form.periodSwitch == '0'">
              <el-input-number v-model="form.ordinaryPrice" :min="0" :precision="2" placeholder="请输入平时单价" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="谷时单价" prop="valleyPrice" v-if="form.periodSwitch == '0'">
              <el-input-number v-model="form.valleyPrice" :precision="2" placeholder="请输入谷时单价" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-form-item label="尖时单价" prop="sharpPrice" v-if="form.periodSwitch == '0'">
          <el-input-number v-model="form.sharpPrice" :min="0" :precision="2" placeholder="请输入尖时单价" />
        </el-form-item>
        <el-form-item label="峰时单价" prop="peekPrice" v-if="form.periodSwitch == '0'">
          <el-input-number v-model="form.peekPrice" :min="0" :precision="2" placeholder="请输入峰时单价" />
        </el-form-item>
        <el-form-item label="平时单价" prop="ordinaryPrice" v-if="form.periodSwitch == '0'">
          <el-input-number v-model="form.ordinaryPrice" :min="0" :precision="2" placeholder="请输入平时单价" />
        </el-form-item>
        <el-form-item label="谷时单价" prop="valleyPrice" v-if="form.periodSwitch == '0'">
          <el-input-number v-model="form.valleyPrice" :precision="2" placeholder="请输入谷时单价" :min="0" />
        </el-form-item> -->
        <el-form-item label="阶梯计费" prop="stepSwitch">
          <!-- <el-input v-model="form.stepSwitch" placeholder="请输入阶梯计费开关" /> -->
          <el-switch v-model="form.stepSwitch" active-value="0" inactive-value="1"></el-switch>
        </el-form-item>
        <div v-if="form.stepSwitch == '0'">
          <el-button type="primary" icon="el-icon-plus" size="mini" style="margin-bottom: 8px;" @click="addParam"
            v-hasPermi="['system:chargingStep:add']">新增</el-button>
          <div>
            <el-table v-loading="paramLoading" :data="paramList" @row-click="rowClick">
              <el-table-column label="阶梯" align="center" type="index" width="50"></el-table-column>
              <el-table-column label="开始用量" prop="startStep" align="center">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.startStep" placeholder="请输入" size="mini" :controls="false"
                    :disabled="true" :precision="2" style="width: 100%;"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="结束用量" prop="endStep" align="center">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.endStep" @input="endStepChange" @change="endStepFinalChange"
                    placeholder="请输入" size="mini" :controls="false" :precision="2" style="width: 100%;"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="差价(元)" prop="priceDifference" align="center">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.priceDifference" placeholder="请输入" size="mini" :controls="false"
                    :precision="2" style="width: 100%;"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" v-show="scope.$index + 1 < paramList.length"
                    @click="delectParam(scope.row)" v-hasPermi="['system:chargingStep:remove']">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <!-- <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { listCharging, getCharging, delCharging, addCharging, updateCharging } from "@/api/system/charging";
import { listChargingStep, updateParamList } from "@/api/system/chargingStep";
export default {
  name: "Charging",
  dicts: ['sys_normal_disable', 'energy_type', 'sys_notice_status'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 参数表格加载遮罩层
      paramLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 计费方案信息表格数据
      chargingList: [],
      paramList: [],
      clickItem: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: undefined,
        startDate: undefined,
        endDate: undefined,
        unitPrice: undefined,
        periodSwitch: undefined,
        sharpPrice: undefined,
        peekPrice: undefined,
        ordinaryPrice: undefined,
        valleyPrice: undefined,
        stepSwitch: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        chargingId: [
          { required: true, message: "计费方案ID不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "类型不能为空", trigger: "change" }
        ],
        startDate: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endDate: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        unitPrice: [
          { required: true, message: "单价不能为空", trigger: "blur" }
        ],
        /* periodSwitch: [
          { required: true, message: "尖峰平谷开关不能为空", trigger: "blur" }
        ], */
        /* sharpPrice: [
          { required: true, message: "尖时单价不能为空", trigger: "blur" }
        ],
        peekPrice: [
          { required: true, message: "峰时单价不能为空", trigger: "blur" }
        ],
        ordinaryPrice: [
          { required: true, message: "平时单价不能为空", trigger: "blur" }
        ],
        valleyPrice: [
          { required: true, message: "谷时单价不能为空", trigger: "blur" }
        ], */
        /* stepSwitch: [
          { required: true, message: "阶梯计费开关不能为空", trigger: "blur" }
        ], */
        /* status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ] */
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 查询阶梯计费参数
    getPriceParam(chargingId) {
      this.paramLoading = true
      var param = { chargingId: chargingId }
      listChargingStep(param).then(res => {
        this.paramList = res.rows
        this.paramLoading = false
      })
    },
    // 计费方案开关状态修改
    handleStatusChange(row) {
      if (row.status === "0") {
        let text = row.status === "0" ? "启用" : "停用";
        this.$modal
          .confirm("确认要" + text + "该计费方案吗？")
          .then(function () {
            return updateCharging(row);
          })
          .then(() => {
            this.$modal.msgSuccess(text + "成功");
            this.getList();
          })
          .catch(function () {
            row.status = row.status === "0" ? "1" : "0";
          });
      } else {
        this.$modal
          .confirm("抱歉，无法禁用该计费方案！")
          .then(() => {
            row.status = "0";
          })
          .catch(() => {
            row.status = "0";
          });
      }
    },
    /** 查询计费方案信息列表 */
    getList() {
      this.loading = true;
      listCharging(this.queryParams).then(response => {
        this.chargingList = response.rows;
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
        chargingId: undefined,
        type: undefined,
        startDate: undefined,
        endDate: undefined,
        unitPrice: 0,
        periodSwitch: undefined,
        sharpPrice: 0,
        peekPrice: 0,
        ordinaryPrice: 0,
        valleyPrice: 0,
        stepSwitch: undefined,
        status: undefined,
        delFlag: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
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
      this.ids = selection.map(item => item.chargingId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      // 新增初始化一条参数数据
      this.paramList = [
        { startStep: 0, endStep: 9999, priceDifference: 0 }
      ]
      this.title = "添加计费方案信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const chargingId = row.chargingId || this.ids
      getCharging(chargingId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改计费方案信息";
        this.getPriceParam(chargingId)
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.chargingId != null) {
            updateCharging(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.updateParam()
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addCharging(this.form).then(response => {
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
      const chargingIds = row.chargingId || this.ids;
      this.$modal.confirm('是否确认删除计费方案信息编号为"' + chargingIds + '"的数据项？').then(() => {
        this.loading = true;
        return delCharging(chargingIds);
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
      this.download('system/charging/export', {
        ...this.queryParams
      }, `charging_${new Date().getTime()}.xlsx`)
    },
    // 价格参数新增按钮
    addParam() {
      let lastParam = this.paramList[this.paramList.length - 1]
      var paramItem = {
        startStep: lastParam.endStep,
        endStep: 9999,
        priceDifference: 0,
        chargingId: this.form.chargingId
      }
      this.paramList.push(paramItem)
    },
    // 价格参数删除按钮
    delectParam(row) {
      let index = this.paramList.findIndex(p => p.endStep == row.endStep)
      if (index < 0) return
      this.paramList.splice(index, 1)
      this.paramList[index].startStep = index == 0 ? 0 : this.paramList[index - 1].endStep
    },
    // 更新策略的所有价格参数
    updateParam(chargingId) {
      if (chargingId) {
        this.paramList.forEach(item => {
          item.chargingId = chargingId
        });
      }
      updateParamList(this.paramList).then(res => { })
    },
    // 价格参数整行被点击
    rowClick(row) {
      this.clickItem = { ...this.paramList.find(p => p.startStep == row.startStep) }
    },
    // 价格参数时间修改
    endStepChange(value) {
      let index = this.paramList.findIndex(p => p.startStep == this.clickItem.startStep)

      this.paramList[index].endStep = value
      if (this.paramList[index + 1]) {
        this.paramList[index + 1].startStep = value
      }
    },
    endStepFinalChange(value) {
      let index = this.paramList.findIndex(p => p.startStep == this.clickItem.startStep)
      if (value <= this.paramList[index].startStep) {
        this.$message({ message: '当前阶梯结束用量不能小于开始用量', type: 'warning' })
        this.paramList[index].endStep = this.clickItem.endStep
        return
      }
      if (!(value > 0)) {
        this.$message({ message: '请输入大于0的数字', type: 'warning' })
        this.paramList[index].endStep = this.clickItem.endStep
        return
      }
    },
    changeStart(value){
      if(value>this.form.endDate){
        this.$message({ message: '开始时间不能晚于结束时间', type: 'warning' })
        this.form.startDate = undefined;
      }
    },
    changeEnd(value){
      if(value<this.form.startDate){
        this.$message({ message: '结束时间不能早于结束时间', type: 'warning' })
        this.form.endDate = undefined;
      }
    }
  }
};
</script>
  