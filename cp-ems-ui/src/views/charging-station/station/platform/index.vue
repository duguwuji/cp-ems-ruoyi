<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="归属商户" prop="merchantId">
          <el-input
            v-model="queryParams.merchantId"
            placeholder="请输入归属商户"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item> -->
      <el-form-item label="归属商户" prop="merchantName">
        <el-input v-model="queryParams.merchantName" placeholder="请输入归属商户名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="充电站名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入充电站名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="电站电价" prop="price">
        <el-input v-model="queryParams.price" placeholder="请输入电站电价" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="电站地址" prop="address">
          <el-input
            v-model="queryParams.address"
            placeholder="请输入电站地址"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item> -->
      <!-- <el-form-item label="电站活动" prop="activity">
          <el-input
            v-model="queryParams.activity"
            placeholder="请输入电站活动"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:station:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:station:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:station:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:station:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="充电站ID" align="center" prop="stationId" v-if="true"/>
        <el-table-column label="归属商户" align="center" prop="merchantId" /> -->
      
      <el-table-column label="充电站名称" align="center" prop="name" />
      <el-table-column label="电站电价" align="center" prop="price" />
      <el-table-column label="归属商户名" align="center" prop="merchantName" />
      <el-table-column label="电站地址" align="center" prop="address" />
      <!-- <el-table-column label="电站活动" align="center" prop="activity" /> -->
      <el-table-column label="电站状态开关" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
            @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:station:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:station:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改充电站信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="充电站名" prop="name">
          <el-input v-model="form.name" placeholder="请输入充电站名称" />
        </el-form-item>
        <el-form-item label="电站电价" prop="price">
          <el-input-number v-model="form.price" style="width: 40%;" :controls="false" placeholder="请输入电站电价" />
        </el-form-item>
        <el-form-item label="归属商户" prop="merchantId">
          <el-select v-model="form.merchantId" placeholder="请输入归属商户" @change="merchantChange">
            <el-option v-for="item in merchantList" :key="item.merchantId" :label="item.name"
              :value="item.merchantId"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="归属商户名" prop="merchantName">
            <el-input v-model="form.merchantName" placeholder="请输入归属商户名" />
          </el-form-item> -->
        
        <el-form-item label="电站地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入电站地址" />
        </el-form-item>
        <el-form-item label="电站活动" prop="activity">
          <el-input v-model="form.activity" placeholder="请输入电站活动" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listStation, getStation, delStation, addStation, updateStation } from "@/api/chargingStation/station";
import { listMerchant } from "@/api/chargingStation/merchant";
export default {
  name: "Station",
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
      // 充电站信息表格数据
      stationList: [],
      // 商户信息表格数据
      merchantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        merchantId: undefined,
        merchantName: undefined,
        name: undefined,
        price: undefined,
        address: undefined,
        activity: undefined,
        status: undefined,
      },
      queryMerchantParams: {
        name: undefined,
        type: '0',
        contact: undefined,
        avatar: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        stationId: [
          { required: true, message: "充电站ID不能为空", trigger: "blur" }
        ],
        merchantId: [
          { required: true, message: "归属商户不能为空", trigger: "blur" }
        ],
        merchantName: [
          { required: true, message: "归属商户名不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "充电站名称不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "电站电价不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "电站地址不能为空", trigger: "blur" }
        ],
        /* activity: [
          { required: true, message: "电站活动不能为空", trigger: "blur" }
        ], */
        /* status: [
          { required: true, message: "电站状态不能为空", trigger: "change" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ] */
      }
    };
  },
  created() {
    this.getList();
    this.getMerchantList();
  },
  methods: {
    // 商户选择
    merchantChange(data) {
      var merchant = this.merchantList.find(u => u.merchantId == data)
      if (merchant) {
        this.form.merchantName = merchant.name
      }
    },
    /** 查询充电站信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.type = '0';
      listStation(this.queryParams).then(response => {
        this.stationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询商户信息列表 */
    getMerchantList() {
      this.loading = true;
      listMerchant(this.queryMerchantParams).then(response => {
        this.merchantList = response.rows;
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
        stationId: undefined,
        merchantId: undefined,
        merchantName: undefined,
        name: undefined,
        price: undefined,
        address: undefined,
        activity: undefined,
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
      this.ids = selection.map(item => item.stationId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加充电站信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const stationId = row.stationId || this.ids
      getStation(stationId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改充电站信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.stationId != null) {
            updateStation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            this.form.type = '0';
            addStation(this.form).then(response => {
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
      const stationIds = row.stationId || this.ids;
      this.$modal.confirm('是否确认删除充电站信息编号为"' + stationIds + '"的数据项？').then(() => {
        this.loading = true;
        return delStation(stationIds);
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
      this.download('system/station/export', {
        ...this.queryParams
      }, `station_${new Date().getTime()}.xlsx`)
    },
    // 电站状态开关状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要' + text + '该充电站吗？').then(function () {
        return updateStation(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
  }
};
</script>
  