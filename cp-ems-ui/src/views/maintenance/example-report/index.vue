<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="例报类型" prop="type">
        <!-- <el-input
            v-model="queryParams.cycle"
            placeholder="请输入例报周期"
            clearable
            @keyup.enter.native="handleQuery"
          /> -->
        <el-select v-model="queryParams.type" placeholder="请选择">
          <el-option v-for="dict in dict.type.example_report_type" :key="dict.value" :label="dict.label"
            :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="例报周期" prop="cycle">
        <!-- <el-input
            v-model="queryParams.cycle"
            placeholder="请输入例报周期"
            clearable
            @keyup.enter.native="handleQuery"
          /> -->
        <el-select v-model="queryParams.cycle" clearable placeholder="请选择">
          <el-option v-for="item in cycleList" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="推送方式" prop="pushMethod">
        <!-- <el-input v-model="queryParams.pushMethod" placeholder="请输入推送方式" clearable @keyup.enter.native="handleQuery" /> -->
        <el-select v-model="queryParams.pushMethod" placeholder="请选择">
          <el-option v-for="dict in dict.type.report_push_type" :key="dict.value" :label="dict.label"
            :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="接收人" prop="userId">
        <el-select v-model="queryParams.userId" clearable placeholder="请选择">
          <el-option v-for="item in userList" :key="item.userId" :label="item.nickName" :value="item.userId"></el-option>
        </el-select>
        <!-- <el-input v-model="queryParams.receiver" placeholder="请输入接收人" clearable @keyup.enter.native="handleQuery" /> -->
      </el-form-item>
      <!-- <el-form-item label="发送时间" prop="sendingTime">
        <el-input v-model="queryParams.sendingTime" placeholder="请输入发送时间" clearable @keyup.enter.native="handleQuery" />
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:exampleReport:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:exampleReport:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:exampleReport:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:exampleReport:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="exampleReportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="例报id" align="center" prop="exampleReportId" v-if="true"/> -->
      <el-table-column label="例报类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.example_report_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="例报周期" align="center" prop="cycle" />
      <el-table-column label="开始日期" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="例报内容" align="center" prop="content" /> -->
      <el-table-column label="推送方式" align="center" prop="pushMethod">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.report_push_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="接收人" align="center" prop="receiver" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:exampleReport:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:exampleReport:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改例报管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="例报类型" prop="type">
          <!-- <el-input v-model="form.cycle" placeholder="请输入例报周期" /> -->
          <el-select v-model="form.type" placeholder="请选择">
            <el-option v-for="dict in dict.type.example_report_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="例报周期" prop="cycle">
          <!-- <el-input v-model="form.cycle" placeholder="请输入例报周期" /> -->
          <el-select v-model="form.cycle" clearable placeholder="请选择">
            <el-option v-for="item in cycleList" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker clearable v-model="form.startDate" type="date" value-format="yyyy-MM-dd 00:00:00"
            @change="changeStart" placeholder="请选择开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker clearable v-model="form.endDate" type="date" value-format="yyyy-MM-dd 23:59:59"
            @change="changeEnd" placeholder="请选择结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="例报内容">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-form-item label="推送方式" prop="pushMethod">
          <!-- <el-input v-model="form.pushMethod" placeholder="请输入推送方式" /> -->
          <el-select v-model="form.pushMethod" placeholder="请选择">
            <el-option v-for="dict in dict.type.report_push_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接收人" prop="userId">
          <!-- <el-input v-model="form.receiver" placeholder="请输入接收人" /> -->
          <el-select v-model="form.userId" clearable placeholder="请选择" @change="personChange">
            <el-option v-for="item in userList" :key="item.userId" :label="item.nickName"
              :value="item.userId"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="发送时间" prop="sendingTime">
          <el-input v-model="form.sendingTime" placeholder="请输入发送时间" />
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
import { listExampleReport, getExampleReport, delExampleReport, addExampleReport, updateExampleReport } from "@/api/system/exampleReport";
import { listInspector } from "@/api/system/user"
export default {
  dicts: ['example_report_type', 'report_push_type'],
  name: "ExampleReport",
  data() {
    return {
      userList: [],
      //周期类型
      cycleList: [
        { label: '月', value: '月' },
        { label: '周', value: '周' },
        { label: '日', value: '日' },
      ],
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
      // 例报管理表格数据
      exampleReportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: undefined,
        cycle: undefined,
        content: undefined,
        pushMethod: undefined,
        receiver: undefined,
        userId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        exampleReportId: [
          { required: true, message: "例报id不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "例报类型不能为空", trigger: "change" }
        ],
        cycle: [
          { required: true, message: "例报周期不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "例报内容不能为空", trigger: "blur" }
        ],
        pushMethod: [
          { required: true, message: "推送方式不能为空", trigger: "blur" }
        ],
        receiver: [
          { required: true, message: "接收人不能为空", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "开始日期不能为空", trigger: "blur" }
        ],
        endDate: [
          { required: true, message: "结束日期不能为空", trigger: "blur" }
        ]
        /* sendingTime: [
          { required: true, message: "发送时间不能为空", trigger: "blur" }
        ], */
      }
    };
  },
  created() {
    this.getList();
    this.getUserList();
  },
  methods: {
    // 选择接收人
    personChange(data) {
      var user = this.userList.find(u => u.userId == data)
      if (user) {
        this.form.receiver = user.nickName
      }
    },
    /** 查询例报管理列表 */
    getList() {
      this.loading = true;
      listExampleReport(this.queryParams).then(response => {
        this.exampleReportList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 查询用户列表
    getUserList() {
      listInspector({ status: '0' }).then(res => {
        this.userList = res.rows
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        exampleReportId: undefined,
        type: undefined,
        cycle: undefined,
        content: undefined,
        pushMethod: undefined,
        receiver: undefined,
        startDate: undefined,
        endDate: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        userId: undefined
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
      this.ids = selection.map(item => item.exampleReportId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加例报管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const exampleReportId = row.exampleReportId || this.ids
      getExampleReport(exampleReportId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改例报管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.exampleReportId != null) {
            updateExampleReport(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addExampleReport(this.form).then(response => {
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
      const exampleReportIds = row.exampleReportId || this.ids;
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true;
        return delExampleReport(exampleReportIds);
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
      this.download('system/exampleReport/export', {
        ...this.queryParams
      }, `exampleReport_${new Date().getTime()}.xlsx`)
    },
    changeStart(value) {
      if (value > this.form.endDate) {
        this.$message({ message: '开始时间不能晚于结束时间', type: 'warning' })
        this.form.startDate = undefined;
      }
    },
    changeEnd(value) {
      if (value < this.form.startDate) {
        this.$message({ message: '结束时间不能早于结束时间', type: 'warning' })
        this.form.endDate = undefined;
      }
    }
  }
};
</script>
