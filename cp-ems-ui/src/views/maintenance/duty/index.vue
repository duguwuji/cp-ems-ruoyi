<template>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="值班日期" prop="dutyDate">
          <el-input
            v-model="queryParams.dutyDate"
            placeholder="请输入值班日期"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="值班人员" prop="onDuty">
          <el-input
            v-model="queryParams.onDuty"
            placeholder="请输入值班人员"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="值班时段" prop="dutyPeriod">
          <el-input
            v-model="queryParams.dutyPeriod"
            placeholder="请输入值班时段"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="替班人员" prop="relief">
          <el-input
            v-model="queryParams.relief"
            placeholder="请输入替班人员"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="值班类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="请选择">
            <el-option v-for="dict in dict.type.duty_type"
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
            v-hasPermi="['system:duty:add']"
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
            v-hasPermi="['system:duty:edit']"
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
            v-hasPermi="['system:duty:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:duty:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table v-loading="loading" :data="dutyList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <!-- <el-table-column label="值班id" align="center" prop="dutyId" v-if="true"/> -->
        <el-table-column label="值班日期" align="center" prop="dutyDate" />
        <el-table-column label="值班人员" align="center" prop="onDuty" />
        <el-table-column label="值班时间段" align="center" prop="dutyPeriod" />
        <el-table-column label="值班类型" align="center" prop="type" >
          <template slot-scope="scope">
            <dict-tag :options="dict.type.duty_type" :value="scope.row.type"/>
          </template>
        </el-table-column>
        <el-table-column label="值班状态" align="center" prop="status" >
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
            @change="handleStatusChange(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="替班人员" align="center" prop="relief" />
        <el-table-column label="值班备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:duty:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:duty:remove']"
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
  
      <!-- 添加或修改值班管理对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="值班日期" prop="dutyDate">
            <el-input v-model="form.dutyDate" placeholder="请输入值班日期" />
          </el-form-item>
          <el-form-item label="值班人员" prop="onDuty">
            <el-input v-model="form.onDuty" placeholder="请输入值班人员" />
          </el-form-item>
          <el-form-item label="值班时间段" prop="dutyPeriod">
            <el-input v-model="form.dutyPeriod" placeholder="请输入值班时间段" />
          </el-form-item>
          <el-form-item label="替班人员" prop="relief">
            <el-input v-model="form.relief" placeholder="请输入替班人员" />
          </el-form-item>
          <el-form-item label="值班状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.value"
              >{{dict.label}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="值班备注" prop="remark">
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
  import { listDuty, getDuty, delDuty, addDuty, updateDuty } from "@/api/system/duty";
  
  export default {
    dicts: ['duty_type', 'sys_normal_disable'],
    name: "Duty",
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
        // 值班管理表格数据
        dutyList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dutyDate: undefined,
          onDuty: undefined,
          dutyPeriod: undefined,
          type: undefined,
          status: undefined,
          relief: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dutyId: [
            { required: true, message: "值班id不能为空", trigger: "blur" }
          ],
          dutyDate: [
            { required: true, message: "值班日期不能为空", trigger: "blur" }
          ],
          onDuty: [
            { required: true, message: "值班人员不能为空", trigger: "blur" }
          ],
          dutyPeriod: [
            { required: true, message: "值班时间段不能为空", trigger: "blur" }
          ],
          type: [
            { required: true, message: "值班类型不能为空", trigger: "change" }
          ],
          status: [
            { required: true, message: "值班状态不能为空", trigger: "change" }
          ],
          relief: [
            { required: true, message: "替班人员不能为空", trigger: "blur" }
          ],
          remark: [
            { required: true, message: "值班备注不能为空", trigger: "blur" }
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询值班管理列表 */
      getList() {
        this.loading = true;
        listDuty(this.queryParams).then(response => {
          this.dutyList = response.rows;
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
          dutyId: undefined,
          dutyDate: undefined,
          onDuty: undefined,
          dutyPeriod: undefined,
          type: undefined,
          status: undefined,
          relief: undefined,
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
        this.ids = selection.map(item => item.dutyId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加值班管理";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.loading = true;
        this.reset();
        const dutyId = row.dutyId || this.ids
        getDuty(dutyId).then(response => {
          this.loading = false;
          this.form = response.data;
          this.open = true;
          this.title = "修改值班管理";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.buttonLoading = true;
            if (this.form.dutyId != null) {
              updateDuty(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }).finally(() => {
                this.buttonLoading = false;
              });
            } else {
              addDuty(this.form).then(response => {
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
        const dutyIds = row.dutyId || this.ids;
        this.$modal.confirm('是否确认删除？').then(() => {
          this.loading = true;
          return delDuty(dutyIds);
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
        this.download('system/duty/export', {
          ...this.queryParams
        }, `duty_${new Date().getTime()}.xlsx`)
      },
      // 修改值班状态
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$modal.confirm('确认要' + text + '该值班吗？').then(function () {
          return updateDuty(row);
        }).then(() => {
          this.$modal.msgSuccess(text + "成功");
        }).catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
    }
  };
  </script>
  