<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="制度编码" prop="regulationCode">
        <el-input
          v-model="queryParams.regulationCode"
          placeholder="请输入制度编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="制度名称" prop="regulationName">
        <el-input
          v-model="queryParams.regulationName"
          placeholder="请输入制度名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="制度描述" prop="regulationDescribe">
        <el-input
          v-model="queryParams.regulationDescribe"
          placeholder="请输入制度描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="制度文件" prop="docOssId">-->
<!--        <el-input-->
<!--          v-model="queryParams.docOssId"-->
<!--          placeholder="请输入制度文件"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="上传时间" prop="uploadTime">
        <el-date-picker clearable
                        v-model="queryParams.uploadTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择上传时间">
        </el-date-picker>
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
          v-hasPermi="['system:regulation:add']"
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
          v-hasPermi="['system:regulation:edit']"
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
          v-hasPermi="['system:regulation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:regulation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="regulationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center" />
<!--      <el-table-column label="主键" align="center" prop="regulationId" v-if="true"/>-->
      <el-table-column label="制度编码" align="center" prop="regulationCode" />
      <el-table-column label="制度名称" align="center" prop="regulationName" />
      <el-table-column label="制度类型" align="center" prop="regulationType" />
      <el-table-column label="制度描述" align="center" prop="regulationDescribe" />
<!--      <el-table-column label="制度文件" align="center" prop="docOssId" />-->
      <el-table-column label="上传时间" align="center" prop="uploadTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:regulation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:regulation:remove']"
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

    <!-- 添加或修改制度管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="制度编码" prop="regulationCode">
          <el-input v-model="form.regulationCode" placeholder="请输入制度编码" />
        </el-form-item>
        <el-form-item label="制度名称" prop="regulationName">
          <el-input v-model="form.regulationName" placeholder="请输入制度名称" />
        </el-form-item>
        <el-form-item label="制度类型" prop="regulationType">
          <el-input v-model="form.regulationType" placeholder="请输入制度类型" />
        </el-form-item>
        <el-form-item label="制度描述" prop="regulationDescribe">
          <el-input v-model="form.regulationDescribe" placeholder="请输入制度描述" />
        </el-form-item>
<!--        <el-form-item label="制度文件" prop="docOssId">-->
<!--          <el-input v-model="form.docOssId" placeholder="请输入制度文件" />-->
<!--        </el-form-item>-->
        <el-form-item label="制度文件" prop="docOssId">
          <fileUpload v-model="form.docOssId"/>
        </el-form-item>
        <el-form-item label="上传时间" prop="uploadTime">
          <el-date-picker clearable
                          v-model="form.uploadTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择上传时间">
          </el-date-picker>
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
import { listRegulation, getRegulation, delRegulation, addRegulation, updateRegulation } from "@/api/system/regulation";

export default {
  name: "Regulation",
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
      // 制度管理表格数据
      regulationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        regulationCode: undefined,
        regulationName: undefined,
        regulationDescribe: undefined,
        regulationType: undefined,
        docOssId: undefined,
        uploadTime: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        regulationId: [
          { required: true, message: "主键不能为空", trigger: "blur" }
        ],
        regulationCode: [
          { required: true, message: "制度编码不能为空", trigger: "blur" }
        ],
        regulationName: [
          { required: true, message: "制度名称不能为空", trigger: "blur" }
        ],
        /* regulationDescribe: [
          { required: true, message: "制度描述不能为空", trigger: "blur" }
        ], */
        regulationType: [
          { required: true, message: "制度类型不能为空", trigger: "change" }
        ],
        /* docOssId: [
          { required: true, message: "制度文件不能为空", trigger: "blur" }
        ], */
        /* uploadTime: [
          { required: true, message: "上传时间不能为空", trigger: "blur" }
        ], */
        // remark: [
        //   { required: true, message: "备注不能为空", trigger: "blur" }
        // ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询制度管理列表 */
    getList() {
      this.loading = true;
      listRegulation(this.queryParams).then(response => {
        this.regulationList = response.rows;
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
        regulationId: undefined,
        regulationCode: undefined,
        regulationName: undefined,
        regulationDescribe: undefined,
        regulationType: undefined,
        docOssId: undefined,
        uploadTime: undefined,
        remark: undefined,
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
      this.ids = selection.map(item => item.regulationId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加制度管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const regulationId = row.regulationId || this.ids
      getRegulation(regulationId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改制度管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.regulationId != null) {
            updateRegulation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRegulation(this.form).then(response => {
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
      const regulationIds = row.regulationId || this.ids;
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true;
        return delRegulation(regulationIds);
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
      this.download('system/regulation/export', {
        ...this.queryParams
      }, `regulation_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
