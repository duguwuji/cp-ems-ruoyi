<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="参数名称" prop="paramName">
        <el-select
          v-model="queryParams.paramName"
          placeholder="请选择参数名称"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="dict in dict.type.electric_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报警等级" prop="alarmLevel">
        <el-select
          v-model="queryParams.alarmLevel"
          placeholder="请选择报警等级"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="dict in dict.type.alarm_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="事件类型" prop="eventType">
        <el-select
          v-model="queryParams.eventType"
          placeholder="请选择事件类型"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="dict in dict.type.alarm_event_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报警开关" prop="alarmSwitch">
        <el-select
          v-model="queryParams.alarmSwitch"
          placeholder="请选择报警状态"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="提醒人" prop="userId">
        <el-select v-model="queryParams.userId" placeholder="请选择报警提醒人" clearable @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in userData"
            :key="item.userId"
            :label="item.nickName"
            :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >查询</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
          v-hasPermi="['system:alarmRule:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:alarmRule:edit']"
          >编辑</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:alarmRule:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:alarmRule:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="alarmRuleList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center" />
      <!-- <el-table-column label="参数id" align="center" prop="paramId" /> -->
      <el-table-column label="参数名称" align="center" prop="paramName" >
        <template slot-scope="scope">
          {{ paramTypeFormat(scope.row.paramName) }}
        </template>
      </el-table-column>
<!--      <el-table-column label="报警类型" align="center" prop="alarmType" />-->
      <el-table-column label="报警等级" align="center" prop="alarmLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.alarm_level" :value="scope.row.alarmLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="事件类型" align="center" prop="eventType" >
        <template slot-scope="scope">
          {{ eventTypeFormat(scope.row.eventType) }}
        </template>
      </el-table-column>
      <el-table-column label="条件1" align="center" prop="condition1" />
      <el-table-column label="阈值1" align="center" prop="thresholdValue1" />
      <el-table-column label="条件2" align="center" prop="condition2" />
      <el-table-column label="阈值2" align="center" prop="thresholdValue2" />
      <el-table-column label="提醒人" align="center" prop="nickName" />
      <el-table-column label="自动创建工单" align="center" prop="alarmSwitch" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.createOrderSwitch"
          active-value="0"
          inactive-value="1"
          @change="handleCreareOrderSwitch(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="报警开关" align="center" prop="alarmSwitch" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.alarmSwitch"
          active-value="0"
          inactive-value="1"
          @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:alarmRule:edit']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:alarmRule:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改报警规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="参数名称" prop="paramName">
              <el-select v-model="form.paramName" placeholder="请选择参数名称">
                <el-option
                  v-for="dict in dict.type.electric_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警等级" prop="alarmLevel">
              <el-select v-model="form.alarmLevel" placeholder="请选择报警等级" style="width:100%">
                <el-option
                  v-for="dict in dict.type.alarm_level"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="条件1" prop="condition1">
              <el-select v-model="form.condition1" placeholder="请选择条件1" style="width:100%">
                <el-option
                  v-for="dict in dict.type.alarm_rule_condition"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阈值1" prop="thresholdValue1">
              <el-input-number v-model="form.thresholdValue1" controls-position="right" :min="0" placeholder="请输入阈值1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="条件2" prop="condition2">
              <el-select v-model="form.condition2" placeholder="请选择条件2" style="width:100%">
                <el-option
                  v-for="dict in dict.type.alarm_rule_condition"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阈值2" prop="thresholdValue2">
              <el-input-number v-model="form.thresholdValue2" controls-position="right" :min="0" placeholder="请输入阈值2"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="事件类型" prop="eventType">
              <el-select v-model="form.eventType" placeholder="请选择事件类型" style="width:100%">
                <el-option
                  v-for="dict in dict.type.alarm_event_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提醒人" prop="userId">
              <el-select v-model="form.userId" placeholder="请选择报警提醒人" style="width:100%">
                <el-option
                  v-for="item in userData"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="报警描述" prop="alarmInfo">
              <el-input v-model="form.alarmInfo" placeholder="请输入报警描述" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="自动创建工单" prop="alarmSwitch">
              <el-radio-group v-model="form.createOrderSwitch">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警开关" prop="alarmSwitch">
              <el-radio-group v-model="form.alarmSwitch">
                <el-radio
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-form-item label="参数id" prop="paramId">
          <el-input v-model="form.paramId" placeholder="请输入参数id" />
        </el-form-item>
        <el-form-item label="参数名称" prop="paramName">
          <el-input v-model="form.paramName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="报警类型" prop="alarmType">
          <el-input v-model="form.alarmType" placeholder="请输入报警类型" />
        </el-form-item>
        <el-form-item label="报警等级" prop="alarmLevel">
          <el-select v-model="form.alarmLevel" placeholder="请选择报警等级">
            <el-option
              v-for="dict in dict.type.alarm_level"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型" prop="eventType">
          <el-input v-model="form.eventType" placeholder="请输入事件类型" />
        </el-form-item>
        <el-form-item label="条件1" prop="condition1">
          <el-input v-model="form.condition1" placeholder="请输入条件1" />
        </el-form-item>
        <el-form-item label="阈值1" prop="thresholdValue1">
          <el-input v-model="form.thresholdValue1" placeholder="请输入阈值1" />
        </el-form-item>
        <el-form-item label="条件2" prop="condition2">
          <el-input v-model="form.condition2" placeholder="请输入条件2" />
        </el-form-item>
        <el-form-item label="阈值2" prop="thresholdValue2">
          <el-input v-model="form.thresholdValue2" placeholder="请输入阈值2" />
        </el-form-item>
        <el-form-item label="报警开关" prop="alarmSwitch">
          <el-radio-group v-model="form.alarmSwitch">
            <el-radio
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

  <script>
import {
  listAlarmRule,
  getAlarmRule,
  delAlarmRule,
  addAlarmRule,
  updateAlarmRule,
} from "@/api/system/alarmRule";
import { listUser } from '@/api/system/user'

export default {
  dicts: ["alarm_level", "sys_normal_disable", "alarm_rule_condition", "alarm_event_type" ,"electric_type"],
  name: "AlarmRule",
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
      // 报警规则表格数据
      alarmRuleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        paramId: undefined,
        paramName: undefined,
        alarmType: undefined,
        alarmLevel: undefined,
        eventType: undefined,
        condition1: undefined,
        thresholdValue1: undefined,
        condition2: undefined,
        thresholdValue2: undefined,
        alarmSwitch: undefined,
        createOrderSwitch: undefined,
        userId:undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [{ required: true, message: "不能为空", trigger: "blur" }],
        paramId: [
          { required: true, message: "参数id不能为空", trigger: "blur" },
        ],
        paramName: [
          { required: true, message: "参数名称不能为空", trigger: "blur" },
        ],
        alarmType: [
          { required: true, message: "报警类型不能为空", trigger: "change" },
        ],
        alarmLevel: [
          { required: true, message: "报警等级不能为空", trigger: "blur" },
        ],
        eventType: [
          { required: true, message: "事件类型不能为空", trigger: "change" },
        ],
        condition1: [
          { required: true, message: "条件1不能为空", trigger: "blur" },
        ],
        thresholdValue1: [
          { required: true, message: "阈值1不能为空", trigger: "blur" },
        ],
        condition2: [
          { required: true, message: "条件2不能为空", trigger: "blur" },
        ],
        thresholdValue2: [
          { required: true, message: "阈值2不能为空", trigger: "blur" },
        ],
        alarmSwitch: [
          { required: true, message: "报警开关不能为空", trigger: "blur" },
        ],
        createOrderSwitch: [
          { required: true, message: "自动创建工单开关不能为空", trigger: "blur" },
        ],
        userId: [
          { required: true, message: "提醒人不能为空", trigger: "blur" }
        ],
      },
      userData:[],
    };
  },
  created() {
    this.getList();
    this.getUsers();
  },
  methods: {
    /** 查询所有用户 */
    getUsers() {
      listUser().then(res => {
        this.userData = res.rows;
      })
    },
    /** 查询报警规则列表 */
    getList() {
      this.loading = true;
      listAlarmRule(this.queryParams).then((response) => {
        this.alarmRuleList = response.rows;
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
        paramId: undefined,
        paramName: undefined,
        alarmType: undefined,
        alarmLevel: undefined,
        eventType: undefined,
        condition1: undefined,
        thresholdValue1: undefined,
        condition2: undefined,
        thresholdValue2: undefined,
        alarmSwitch: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        userId:undefined,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加报警规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getAlarmRule(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改报警规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateAlarmRule(this.form)
              .then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          } else {
            addAlarmRule(this.form)
              .then((response) => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除？')
        .then(() => {
          this.loading = true;
          return delAlarmRule(ids);
        })
        .then(() => {
          this.loading = false;
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {})
        .finally(() => {
          this.loading = false;
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/alarmRule/export",
        {
          ...this.queryParams,
        },
        `alarmRule_${new Date().getTime()}.xlsx`
      );
    },
    // 事件类型
    eventTypeFormat(eventType) {
      let index = this.dict.type.alarm_event_type.findIndex(
        (item) => item.value == eventType
      );
      return index >= 0 ? this.dict.type.alarm_event_type[index].label : "-";
    },
    //电力参数类型
    paramTypeFormat(paramType){
      let index = this.dict.type.electric_type.findIndex(
        (item) => item.value == paramType
      );
      return index >= 0 ? this.dict.type.electric_type[index].label : "-";
    },
    // 报警开关状态修改
    handleStatusChange(row) {
      let text = row.alarmSwitch === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要' + text + '该报警规则吗？').then(function() {
        return updateAlarmRule(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.alarmSwitch = row.alarmSwitch === "0" ? "1" : "0";
      });
    },
    // 自动创建工单开关状态修改
    handleCreareOrderSwitch(row) {
      let text = row.createOrderSwitch === "0" ? "开启" : "关闭";
      this.$modal.confirm('确认要' + text + '自动创建工单吗？').then(function() {
        return updateAlarmRule(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.createOrderSwitch = row.createOrderSwitch === "0" ? "1" : "0";
      });
    },
  },
};
</script>
