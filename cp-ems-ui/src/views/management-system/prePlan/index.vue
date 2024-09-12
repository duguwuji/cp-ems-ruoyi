<template>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="预案编号" prop="number">
          <el-input
            v-model="queryParams.number"
            placeholder="请输入预案编号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="站名" prop="station">
          <el-input
            v-model="queryParams.station"
            placeholder="请输入站名"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="设备" prop="device">
          <el-input
            v-model="queryParams.device"
            placeholder="请输入设备"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="编制人" prop="preparedBy">
          <el-input
            v-model="queryParams.preparedBy"
            placeholder="请输入编制人"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <!-- <el-form-item label="预案关键词" prop="keywords">
          <el-input
            v-model="queryParams.keywords"
            placeholder="请输入预案关键词"
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
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:prePlan:add']"
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
            v-hasPermi="['system:prePlan:edit']"
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
            v-hasPermi="['system:prePlan:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:prePlan:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table v-loading="loading" :data="prePlanList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <!-- <el-table-column label="预案id" align="center" prop="prePlanId" v-if="true"/> -->
        <el-table-column label="预案编号" align="center" prop="number" />
        <el-table-column label="预案类型" align="center" prop="type" />
        <el-table-column label="站名" align="center" prop="station" />
        <el-table-column label="设备" align="center" prop="device" />
        <el-table-column label="预案内容" align="center" prop="content" />
        <el-table-column label="编制人" align="center" prop="preparedBy" />
        <el-table-column label="预案关键词" align="center" prop="keywords" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:prePlan:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:prePlan:remove']"
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
  
      <!-- 添加或修改预案管理对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="预案编号" prop="number">
            <el-input v-model="form.number" placeholder="请输入预案编号" />
          </el-form-item>
          <el-form-item label="预案类型" prop="type">
            <el-input v-model="form.type" placeholder="请输入类型" />
          </el-form-item>
          <el-form-item label="站名" prop="station">
            <el-input v-model="form.station" placeholder="请输入站名" />
          </el-form-item>
          <el-form-item label="设备" prop="device">
            <el-input v-model="form.device" placeholder="请输入设备" />
          </el-form-item>
          <el-form-item label="预案内容">
            <editor v-model="form.content" :min-height="192"/>
          </el-form-item>
          <el-form-item label="编制人" prop="preparedBy">
            <el-input v-model="form.preparedBy" placeholder="请输入编制人" />
          </el-form-item>
          <el-form-item label="预案关键词" prop="keywords">
            <el-input v-model="form.keywords" placeholder="请输入预案关键词" />
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
  import { listPrePlan, getPrePlan, delPrePlan, addPrePlan, updatePrePlan } from "@/api/system/prePlan";
  
  export default {
    name: "PrePlan",
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
        // 预案管理表格数据
        prePlanList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          number: undefined,
          type: undefined,
          station: undefined,
          device: undefined,
          content: undefined,
          preparedBy: undefined,
          keywords: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          prePlanId: [
            { required: true, message: "预案id不能为空", trigger: "blur" }
          ],
          number: [
            { required: true, message: "预案编号不能为空", trigger: "blur" }
          ],
          type: [
            { required: true, message: "预案类型不能为空", trigger: "change" }
          ],
           station: [
            { required: true, message: "站名不能为空", trigger: "blur" }
          ],
          /*
          device: [
            { required: true, message: "设备不能为空", trigger: "blur" }
          ],
          content: [
            { required: true, message: "预案内容不能为空", trigger: "blur" }
          ],
          preparedBy: [
            { required: true, message: "编制人不能为空", trigger: "blur" }
          ],
          keywords: [
            { required: true, message: "预案关键词不能为空", trigger: "blur" }
          ], */
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询预案管理列表 */
      getList() {
        this.loading = true;
        listPrePlan(this.queryParams).then(response => {
          this.prePlanList = response.rows;
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
          prePlanId: undefined,
          number: undefined,
          type: undefined,
          station: undefined,
          device: undefined,
          content: undefined,
          preparedBy: undefined,
          keywords: undefined,
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
        this.ids = selection.map(item => item.prePlanId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加预案管理";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.loading = true;
        this.reset();
        const prePlanId = row.prePlanId || this.ids
        getPrePlan(prePlanId).then(response => {
          this.loading = false;
          this.form = response.data;
          this.open = true;
          this.title = "修改预案管理";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.buttonLoading = true;
            if (this.form.prePlanId != null) {
              updatePrePlan(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }).finally(() => {
                this.buttonLoading = false;
              });
            } else {
              addPrePlan(this.form).then(response => {
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
        const prePlanIds = row.prePlanId || this.ids;
        this.$modal.confirm('是否确认删除？').then(() => {
          this.loading = true;
          return delPrePlan(prePlanIds);
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
        this.download('system/prePlan/export', {
          ...this.queryParams
        }, `prePlan_${new Date().getTime()}.xlsx`)
      }
    }
  };
  </script>
  