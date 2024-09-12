<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户名称" prop="merchantName">
        <el-input
          v-model="queryParams.merchantName"
          placeholder="请输入商户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="充电站名称" prop="stationName">
        <el-input
          v-model="queryParams.stationName"
          placeholder="请输入充电站名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号" prop="carNo">
        <el-input
          v-model="queryParams.carNo"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="充电方式" prop="chargeMethod">
        <el-select
          v-model="queryParams.chargeMethod"
          placeholder="请选择"
          clearable
          style="width: 215px"
        >
          <el-option
            v-for="dict in dict.type.charge_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="订单来源" prop="orderSource">
        <el-select
          v-model="queryParams.orderSource"
          placeholder="请选择"
          clearable
          style="width: 215px"
        >
          <el-option
            v-for="dict in dict.type.order_source"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="结算类型" prop="settleType">
        <el-select
          v-model="queryParams.settleType"
          placeholder="请选择"
          clearable
          style="width: 215px"
        >
          <el-option
            v-for="dict in dict.type.settle_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select
          v-model="queryParams.orderStatus"
          placeholder="请选择"
          clearable
          style="width: 215px"
        >
          <el-option
            v-for="dict in dict.type.order_status"
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
          v-hasPermi="['system:orderInfo:add']"
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
          v-hasPermi="['system:orderInfo:edit']"
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
          v-hasPermi="['system:orderInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:orderInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderInfoList" @selection-change="handleSelectionChange" class="order-table">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="center" prop="orderNo" width="180" />
      <el-table-column label="下单时间" align="center" prop="createTime" width="180">
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="结算类型" align="center" prop="settleType" width="120">
        <template slot-scope="scope">
          <dict-tag v-if="scope.row.settleType" :options="dict.type.settle_type" :value="scope.row.settleType"/>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="用户名称" align="center" prop="userName" width="120" show-overflow-tooltip/>
      <el-table-column label="电话" align="center" prop="phone" width="120" />
      <el-table-column label="账户余额" align="center" prop="settleBalance" width="100"/>
      <el-table-column label="商户名称" align="center" prop="merchantName" width="120" show-overflow-tooltip/>
      <el-table-column label="充电站名称" align="center" prop="stationName" width="200" show-overflow-tooltip/>
      <el-table-column label="充电桩名称" align="center" prop="pileName" width="120" show-overflow-tooltip/>
      <el-table-column label="充电开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime ? scope.row.startTime : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="充电结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime ? scope.row.endTime : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="车牌号" align="center" prop="carNo" width="100" >
        <template slot-scope="scope">
          <span>{{ scope.row.carNo ? scope.row.carNo : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="VIN码" align="center" prop="carVin" width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.carVin ? scope.row.carVin : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="充电方式" align="center" prop="chargeMethod" width="100" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.charge_method" :value="scope.row.chargeMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" align="center" prop="payType" width="100">
        <template slot-scope="scope">
          <dict-tag v-if="scope.row.payType" :options="dict.type.pay_type" :value="scope.row.payType"/>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="结算时间" align="center" prop="settleTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.settleTime ? scope.row.settleTime : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结算金额" align="center" prop="settlePrice" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.settlePrice ? scope.row.settlePrice : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际支付金额" align="center" prop="paidPrice" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.paidPrice ? scope.row.paidPrice : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="优惠金额" align="center" prop="discountAmt" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.discountAmt ? scope.row.discountAmt : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电费" align="center" prop="elecAmt" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.elecAmt ? scope.row.elecAmt : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务费" align="center" prop="serveAmt" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.serveAmt ? scope.row.serveAmt : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="充电时长" align="center" prop="chargeDuration" width="100"/>
      <el-table-column label="总充电量" align="center" prop="energy" width="100"/>
      <el-table-column label="订单来源" align="center" prop="orderSource" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_source" :value="scope.row.orderSource"/>
        </template>
      </el-table-column>
      <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="toOrderDetail(scope.row)"
            v-hasPermi="['system:orderInfo:query']"
          >详情</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:orderInfo:remove']"
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

    <!-- 添加或修改订单信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="商户名称" prop="merchantName">
          <el-input v-model="form.merchantName" placeholder="请输入商户名称" />
        </el-form-item>
        <el-form-item label="充电站名称" prop="stationName">
          <el-input v-model="form.stationName" placeholder="请输入充电站名称" />
        </el-form-item>
        <el-form-item label="充电桩名称" prop="pileName">
          <el-input v-model="form.pileName" placeholder="请输入充电桩名称" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="车牌号" prop="carNo">
          <el-input v-model="form.carNo" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="VIN码" prop="carVin">
          <el-input v-model="form.carVin" placeholder="请输入VIN码" />
        </el-form-item>
        <el-form-item label="充电方式" prop="chargeMethod">
          <el-input v-model="form.chargeMethod" placeholder="请输入充电方式" />
        </el-form-item>
        <el-form-item label="结算时间" prop="settleTime">
          <el-date-picker clearable
            v-model="form.settleTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择结算时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结算金额" prop="settlePrice">
          <el-input v-model="form.settlePrice" placeholder="请输入结算金额" />
        </el-form-item>
        <el-form-item label="实际支付金额" prop="paidPrice">
          <el-input v-model="form.paidPrice" placeholder="请输入实际支付金额" />
        </el-form-item>
        <el-form-item label="优惠金额" prop="discountAmt">
          <el-input v-model="form.discountAmt" placeholder="请输入优惠金额" />
        </el-form-item>
        <el-form-item label="电费" prop="elecAmt">
          <el-input v-model="form.elecAmt" placeholder="请输入电费" />
        </el-form-item>
        <el-form-item label="服务费" prop="serveAmt">
          <el-input v-model="form.serveAmt" placeholder="请输入服务费" />
        </el-form-item>
        <el-form-item label="充电时长" prop="chargeDuration">
          <el-input v-model="form.chargeDuration" placeholder="请输入充电时长" />
        </el-form-item>
        <el-form-item label="总充电量" prop="energy">
          <el-input v-model="form.energy" placeholder="请输入总充电量" />
        </el-form-item>
        <el-form-item label="订单来源" prop="orderSource">
          <el-input v-model="form.orderSource" placeholder="请输入订单来源" />
        </el-form-item>
        <el-form-item label="账户余额" prop="settleBalance">
          <el-input v-model="form.settleBalance" placeholder="请输入账户余额" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listOrderInfo, getOrderInfo, delOrderInfo, addOrderInfo, updateOrderInfo } from "@/api/chargingStation/orderInfo";

export default {
  name: "HistoryOrder",
  dicts: ['order_status', 'charge_method', 'order_source', 'settle_type', 'pay_type'],
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
      // 订单信息表格数据
      orderInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        userName: undefined,
        phone: undefined,
        merchantId: undefined,
        merchantName: undefined,
        stationId: undefined,
        stationName: undefined,
        pileId: undefined,
        pileName: undefined,
        startTime: undefined,
        endTime: undefined,
        carNo: undefined,
        carVin: undefined,
        chargeMethod: undefined,
        settleType: undefined,
        payType: undefined,
        settleTime: undefined,
        orderStatus: undefined,
        orderSource: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "实时订单id不能为空", trigger: "blur" }
        ],
        orderNo: [
          { required: true, message: "订单编号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "电话不能为空", trigger: "blur" }
        ],
        merchantId: [
          { required: true, message: "商户id不能为空", trigger: "blur" }
        ],
        merchantName: [
          { required: true, message: "商户名称不能为空", trigger: "blur" }
        ],
        stationId: [
          { required: true, message: "充电站id不能为空", trigger: "blur" }
        ],
        stationName: [
          { required: true, message: "充电站名称不能为空", trigger: "blur" }
        ],
        pileId: [
          { required: true, message: "充电桩id不能为空", trigger: "blur" }
        ],
        pileName: [
          { required: true, message: "充电桩名称不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "充电开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "充电结束时间不能为空", trigger: "blur" }
        ],
        carId: [
          { required: true, message: "车辆id不能为空", trigger: "blur" }
        ],
        carNo: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        carVin: [
          { required: true, message: "VIN码不能为空", trigger: "blur" }
        ],
        chargeMethod: [
          { required: true, message: "充电方式不能为空", trigger: "blur" }
        ],
        settleType: [
          { required: true, message: "结算类型不能为空", trigger: "change" }
        ],
        payType: [
          { required: true, message: "支付方式不能为空", trigger: "change" }
        ],
        settleTime: [
          { required: true, message: "订单结算时间不能为空", trigger: "blur" }
        ],
        settlePrice: [
          { required: true, message: "结算金额不能为空", trigger: "blur" }
        ],
        paidPrice: [
          { required: true, message: "实际支付金额不能为空", trigger: "blur" }
        ],
        discountAmt: [
          { required: true, message: "优惠金额不能为空", trigger: "blur" }
        ],
        elecAmt: [
          { required: true, message: "电费不能为空", trigger: "blur" }
        ],
        serveAmt: [
          { required: true, message: "服务费不能为空", trigger: "blur" }
        ],
        orderStatus: [
          { required: true, message: "订单状态不能为空", trigger: "change" }
        ],
        chargeDuration: [
          { required: true, message: "充电时长不能为空", trigger: "blur" }
        ],
        energy: [
          { required: true, message: "总充电量不能为空", trigger: "blur" }
        ],
        orderSource: [
          { required: true, message: "订单来源不能为空", trigger: "blur" }
        ],
        settleBalance: [
          { required: true, message: "账户余额不能为空", trigger: "blur" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订单信息列表 */
    getList() {
      this.loading = true;
      listOrderInfo(this.queryParams).then(response => {
        this.orderInfoList = response.rows;
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
        orderNo: undefined,
        userId: undefined,
        userName: undefined,
        phone: undefined,
        merchantId: undefined,
        merchantName: undefined,
        stationId: undefined,
        stationName: undefined,
        pileId: undefined,
        pileName: undefined,
        startTime: undefined,
        endTime: undefined,
        carId: undefined,
        carNo: undefined,
        carVin: undefined,
        chargeMethod: undefined,
        settleType: undefined,
        payType: undefined,
        settleTime: undefined,
        settlePrice: undefined,
        paidPrice: undefined,
        discountAmt: undefined,
        elecAmt: undefined,
        serveAmt: undefined,
        orderStatus: undefined,
        chargeDuration: undefined,
        energy: undefined,
        orderSource: undefined,
        settleBalance: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
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
      this.title = "添加订单信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getOrderInfo(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改订单信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateOrderInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOrderInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除订单信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delOrderInfo(ids);
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
      this.download('system/orderInfo/export', {
        ...this.queryParams
      }, `orderInfo_${new Date().getTime()}.xlsx`)
    },
    // 详情按钮
    toOrderDetail(row) {
      this.$router.push({path: '/charging-station/order-info/' + row.id})
    }
  }
};
</script>
<style scoped>
.order-table>>>.el-table__body-wrapper::-webkit-scrollbar {
  height: 16px;
}
.order-table>>>.el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background: #dddee0;
}
.order-table>>>.el-table__body-wrapper::-webkit-scrollbar-thumb:hover {
  background: #c7c9cc;
}
.order-table>>>.el-table__body-wrapper::-webkit-scrollbar-track {
  background: #f1f1f1;
}
</style>
