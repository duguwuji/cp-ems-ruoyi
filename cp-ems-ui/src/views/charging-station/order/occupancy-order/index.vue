<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="占位订单编号" prop="occupancyNo">
        <el-input
          v-model="queryParams.occupancyNo"
          placeholder="请输入占位订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="充电订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入充电订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select
          v-model="queryParams.orderStatus"
          placeholder="请选择"
          clearable
          style="width: 215px"
        >
          <el-option
            v-for="dict in dict.type.occupancy_order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="结算状态" prop="settleStatus">
        <el-select
          v-model="queryParams.settleStatus"
          placeholder="请选择"
          clearable
          style="width: 215px"
        >
          <el-option
            v-for="dict in dict.type.occupancy_order_settle"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:occupancyOrder:add']"
        >新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:occupancyOrder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:occupancyOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:occupancyOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="occupancyOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="占位订单id" align="center" prop="id" v-if="true"/> -->
      <el-table-column label="占位订单编号" align="center" prop="occupancyNo" width="180"/>
      <el-table-column label="充电订单号" align="center" prop="orderNo" width="180" />
      <el-table-column label="结算状态" align="center" prop="settleStatus" width="100" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.occupancy_order_settle" :value="scope.row.settleStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" width="100" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.occupancy_order_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="用户名称" align="center" prop="orderInfo.userName" width="120" show-overflow-tooltip/>
      <el-table-column label="电话" align="center" prop="orderInfo.phone" width="120" />
      <el-table-column label="商户名称" align="center" prop="orderInfo.merchantName" width="120" show-overflow-tooltip/>
      <el-table-column label="充电站名称" align="center" prop="orderInfo.stationName" width="200" show-overflow-tooltip/>
      <el-table-column label="充电桩名称" align="center" prop="orderInfo.pileName" width="120" show-overflow-tooltip/>
      <el-table-column label="占位时长" align="center" prop="duration" width="80" />
      <el-table-column label="占位费用" align="center" prop="fee" width="80" />
      <el-table-column label="是否产生占位费" align="center" prop="isFee" width="120" >
        <template slot-scope="scope">
          {{scope.row.isFee == '0' ? '是' : '否'}}
        </template>
      </el-table-column>
      <el-table-column label="支付时间" align="center" prop="payTime" width="180">
      </el-table-column>
      <!-- <el-table-column label="订单备注" align="center" prop="remark" /> -->
      <el-table-column label="结束原因" align="center" prop="endReason" width="180" show-overflow-tooltip>
        <template slot-scope="scope">
          {{scope.row.endReason ? scope.row.endReason : '--'}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:occupancyOrder:edit']"
          >详情</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:occupancyOrder:remove']"
          >删除</el-button> -->
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

    <!-- 添加或修改占位订单信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="占位订单编号" prop="occupancyNo">
          <el-input v-model="form.occupancyNo" placeholder="请输入占位订单编号" />
        </el-form-item>
        <el-form-item label="充电订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入充电订单号" />
        </el-form-item>
        <el-form-item label="占位时长" prop="duration">
          <el-input v-model="form.duration" placeholder="请输入占位时长" />
        </el-form-item>
        <el-form-item label="占位费用" prop="fee">
          <el-input v-model="form.fee" placeholder="请输入占位费用" />
        </el-form-item>
        <!-- <el-form-item label="是否产生占位费" prop="isFee">
          <el-input v-model="form.isFee" placeholder="请输入是否产生占位费" />
        </el-form-item> -->
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择支付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="订单备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入订单备注" />
        </el-form-item>
        <el-form-item label="结束原因" prop="endReason">
          <el-input v-model="form.endReason" placeholder="请输入结束原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOccupancyOrder, getOccupancyOrder, delOccupancyOrder, addOccupancyOrder, updateOccupancyOrder } from "@/api/chargingStation/occupancyOrder";

export default {
  name: "OccupancyOrder",
  dicts: ['occupancy_order_status', 'occupancy_order_settle'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
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
      // 占位订单信息表格数据
      occupancyOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        occupancyNo: undefined,
        orderNo: undefined,
        duration: undefined,
        fee: undefined,
        isFee: undefined,
        payTime: undefined,
        settleStatus: undefined,
        orderStatus: undefined,
        endReason: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "占位订单id不能为空", trigger: "blur" }
        ],
        occupancyNo: [
          { required: true, message: "占位订单编号不能为空", trigger: "blur" }
        ],
        orderNo: [
          { required: true, message: "充电订单号不能为空", trigger: "blur" }
        ],
        duration: [
          { required: true, message: "占位时长不能为空", trigger: "blur" }
        ],
        fee: [
          { required: true, message: "占位费用不能为空", trigger: "blur" }
        ],
        isFee: [
          { required: true, message: "是否产生占位费不能为空", trigger: "blur" }
        ],
        payTime: [
          { required: true, message: "支付时间不能为空", trigger: "blur" }
        ],
        settleStatus: [
          { required: true, message: "结算状态不能为空", trigger: "change" }
        ],
        orderStatus: [
          { required: true, message: "占位订单状态不能为空", trigger: "change" }
        ],
        remark: [
          { required: true, message: "订单备注不能为空", trigger: "blur" }
        ],
        endReason: [
          { required: true, message: "结束原因不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询占位订单信息列表 */
    getList() {
      this.loading = true;
      listOccupancyOrder(this.queryParams).then(response => {
        this.occupancyOrderList = response.rows;
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
        occupancyNo: undefined,
        orderNo: undefined,
        duration: undefined,
        fee: undefined,
        isFee: undefined,
        payTime: undefined,
        settleStatus: undefined,
        orderStatus: undefined,
        remark: undefined,
        endReason: undefined,
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
      this.title = "添加占位订单信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getOccupancyOrder(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改占位订单信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateOccupancyOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOccupancyOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
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
      this.$modal.confirm('是否确认删除占位订单信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delOccupancyOrder(ids);
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
      this.download('system/occupancyOrder/export', {
        ...this.queryParams
      }, `occupancyOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>