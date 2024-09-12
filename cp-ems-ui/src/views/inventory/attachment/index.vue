<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="备件编号" prop="attachmentCode">
        <el-input
          v-model="queryParams.attachmentCode"
          placeholder="请输入备件编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="备件名称" prop="attachmentName">
        <el-input
          v-model="queryParams.attachmentName"
          placeholder="请输入备件名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商" prop="purveyorId">
        <el-select v-model="queryParams.purveyorId" placeholder="请选择供应商">
          <el-option v-for="purveyor in purveyorList"
            :key="purveyor.purveyorId"
            :label="purveyor.purveyorName"
            :value="purveyor.purveyorId"></el-option>
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
          v-hasPermi="['system:attachment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:attachment:edit']"
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
          v-hasPermi="['system:attachment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:attachment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="attachmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="attachmentId" v-if="true"/> -->
      <el-table-column label="备件编号" align="center" prop="attachmentCode" width="200"/>
      <el-table-column label="备件名称" align="center" prop="attachmentName" />
      <el-table-column label="备件型号" align="center" prop="model" />
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="单位" align="center" prop="unit" />
      <el-table-column label="供应商名称" align="center" prop="purveyorName" />
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:attachment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:attachment:remove']"
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

    <!-- 添加或修改备件库管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="备件编号" prop="attachmentCode">
          <el-input v-model="form.attachmentCode" placeholder="请输入备件编码" />
        </el-form-item>
        <el-form-item label="备件名称" prop="attachmentName">
          <el-input v-model="form.attachmentName" placeholder="请输入备件名称" />
        </el-form-item>
        <el-form-item label="备件型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入备件型号" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="供应商" prop="purveyorId">
          <el-select v-model="form.purveyorId" placeholder="请选择供应商" @change="purveyorChange">
            <el-option v-for="purveyor in purveyorList"
              :key="purveyor.purveyorId"
              :label="purveyor.purveyorName"
              :value="purveyor.purveyorId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
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
import { listAttachment, getAttachment, delAttachment, addAttachment, updateAttachment } from "@/api/system/attachment";
import { listPurveyor } from "@/api/system/purveyor";
export default {
  name: "Attachment",
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
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 备件库管理表格数据
      attachmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        attachmentCode: undefined,
        attachmentName: undefined,
        model: undefined,
        unit: undefined,
        purveyorId: undefined,
        purveyorName: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        attachmentCode: [
          { required: true, message: "备件编号不能为空", trigger: "blur" }
        ],
        attachmentName: [
          { required: true, message: "备件名称不能为空", trigger: "blur" }
        ],
        unit: [
          { required: true, message: "单位不能为空", trigger: "blur" }
        ],
        purveyorId: [
          { required: true, message: "供应商id不能为空", trigger: "blur" }
        ],
      },
      purveyorList: [], // 供应商列表
    };
  },
  created() {
    this.getAllPurveyor()
    this.getList();
  },
  methods: {
    /** 查询备件库管理列表 */
    getList() {
      this.loading = true;
      listAttachment(this.queryParams).then(response => {
        this.attachmentList = response.rows;
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
        attachmentId: undefined,
        attachmentCode: undefined,
        attachmentName: undefined,
        model: undefined,
        quantity: 0,
        unit: undefined,
        purveyorId: undefined,
        purveyorName: undefined,
        remark: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined
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
      this.ids = selection.map(item => item.attachmentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加备件库管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const attachmentId = row.attachmentId || this.ids
      getAttachment(attachmentId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改备件库管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.attachmentId != null) {
            updateAttachment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addAttachment(this.form).then(response => {
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
      const attachmentIds = row.attachmentId || this.ids;
      this.$modal.confirm('是否确认删除备件信息？').then(() => {
        this.loading = true;
        return delAttachment(attachmentIds);
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
      this.download('system/attachment/export', {
        ...this.queryParams
      }, `attachment_${new Date().getTime()}.xlsx`)
    },
    // 查询所有的供应商
    getAllPurveyor() {
      listPurveyor().then(res => {
        this.purveyorList = res.rows
      })
    },
    purveyorChange(value) {
      let item = this.purveyorList.find(p => p.purveyorId == value)
      if(!item) return
      this.form.purveyorName = item.purveyorName
    }
  }
};
</script>
