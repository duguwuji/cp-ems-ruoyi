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
      <el-form-item label="计划名称" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入计划名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="巡检人" prop="userId">
        <el-select v-model="queryParams.userId" clearable placeholder="请选择">
          <el-option
          v-for="item in userList"
          :key="item.userId"
          :label="item.nickName"
          :value="item.userId"></el-option>
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
          v-hasPermi="['system:plan:add']"
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
          v-hasPermi="['system:plan:edit']"
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
          v-hasPermi="['system:plan:remove']"
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
          v-hasPermi="['system:plan:export']"
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
      :data="planList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center" />
      <el-table-column label="计划名称" align="center" prop="planName" width="200" show-overflow-tooltip/>
      <el-table-column label="计划内容" align="center" prop="planContent" width="200" show-overflow-tooltip />
      <el-table-column label="项目名称" align="center" prop="projectName" width="150" show-overflow-tooltip />
      <el-table-column
        label="开始日期"
        align="center"
        prop="startDate"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="结束日期"
        align="center"
        prop="endDate"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="巡检周期" align="center" prop="inspectionCycle" />
      <el-table-column label="具体时间" align="center" prop="setTime" />
      <el-table-column label="巡检人" align="center" prop="inspectionPerson" />
      <el-table-column label="巡检计划开关" align="center" prop="status" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status"
          active-value="0"
          inactive-value="1"
          @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding"
        width="180"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:plan:edit']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:plan:remove']"
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

    <!-- 添加或修改巡检计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="计划内容">
          <!-- <editor v-model="form.planContent" :min-height="192" /> -->
          <el-input type="textarea" v-model="form.planContent" placeholder="请输入巡检内容"></el-input>
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            clearable
            v-model="form.startDate"
            type="date"
            value-format="yyyy-MM-dd 00:00:00"
            @change="changeStart"
            placeholder="请选择开始日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            clearable
            v-model="form.endDate"
            type="date"
            value-format="yyyy-MM-dd 23:59:59"
            @change="changeEnd"
            placeholder="请选择结束日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="巡检周期" prop="inspectionCycle">
          <!-- <el-input
            v-model="form.inspectionCycle"
            placeholder="请输入巡检周期"
          /> -->
          <el-select v-model="form.inspectionCycle" placeholder="请选择周期" @change="clearSetDates">
            <el-option
              v-for="item in inspectionCycleList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="巡检日期" prop="setDates" v-if="form.inspectionCycle==='周'">
          <el-select v-model="form.setDates" multiple placeholder="请选择巡检日期" @change="selectSetDate">
            <el-option
              v-for="item in weekList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <!-- <el-input v-model="form.setDate" placeholder="请输入巡检日期" /> -->
        </el-form-item>
        <el-form-item label="巡检日期" prop="setDates" v-if="form.inspectionCycle==='月'">
          <el-select v-model="form.setDates" multiple placeholder="请选择巡检日期">
            <!-- <el-option
              v-for="item in dayList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option> -->
             <template #empty>
                  <selectDate :selectIds="form.setDates" @selectChange="selectChange" />
            </template>
          </el-select>
          <!-- <el-input v-model="form.setDate" placeholder="请输入巡检日期" /> -->
        </el-form-item>
        <el-form-item label="具体时间" prop="setTime" :key="form.setTime">
          <el-time-select
            v-model="form.setTime"
            :picker-options="{
              start: '08:00',
              step: '00:15',
              end: '20:00'
            }"
            placeholder="选择具体时间">
          </el-time-select>
        </el-form-item>
        <el-form-item label="巡检人" prop="userIds">
          <el-select v-model="form.userIds" multiple placeholder="请选择巡检人"  @change="selectUser">
            <el-option
              v-for="item in userList"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId">
            </el-option>
          </el-select>
        </el-form-item>
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
  listPlan,
  getPlan,
  delPlan,
  addPlan,
  updatePlan,
} from "@/api/system/inspectionPlan";
import {listInspector} from "@/api/system/user"
import selectDate from "@/views/maintenance/inspection-plan/selectDate"
export default {
  dicts: ["sys_normal_disable"],
  name: "Plan",
  components: { selectDate },
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
      // 巡检计划表格数据
      planList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planName: undefined,
        planContent: undefined,
        projectName: undefined,
        startDate: undefined,
        endDate: undefined,
        inspectionCycle: undefined,
        inspectionPerson: undefined,
        createTime: undefined,
        userId: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [{ required: true, message: "不能为空", trigger: "blur" }],
        planName: [
          { required: true, message: "计划名称不能为空", trigger: "blur" },
        ],
        planContent: [
          { required: true, message: "计划内容不能为空", trigger: "blur" },
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" },
        ],
        startDate: [
          { required: true, message: "开始日期不能为空", trigger: "blur" },
        ],
        endDate: [
          { required: true, message: "结束日期不能为空", trigger: "blur" },
        ],
        inspectionCycle: [
          { required: true, message: "巡检周期不能为空", trigger: "blur" },
        ],
        // inspectionPerson: [
        //   { required: true, message: "巡检人不能为空", trigger: "blur" },
        // ],
        userIds: [
          { required: true, message: "巡检人不能为空", trigger: "blur" },
        ],
        setDates: [
          {required: true, message: "巡检设定日期不能为空", trigger: "blur"}
        ],
        setTime: [
          {required: true, message: "具体时间不能为空", trigger: "blur"}
        ]
      },
      inspectionCycleList: [
        {label: '月', value: '月'},
        {label: '周', value: '周'},
        {label: '日', value: '日'},
      ],
      weekList: [
        {label: '周日', value: '1'},
        {label: '周一', value: '2'},
        {label: '周二', value: '3'},
        {label: '周三', value: '4'},
        {label: '周四', value: '5'},
        {label: '周五', value: '6'},
        {label: '周六', value: '7'},
      ],
      dayList: [
        {label: '1号', value: '01'},
        {label: '2号', value: '02'},
        {label: '3号', value: '03'},
        {label: '4号', value: '04'},
        {label: '5号', value: '05'},
        {label: '6号', value: '06'},
        {label: '7号', value: '07'},
        {label: '8号', value: '08'},
        {label: '9号', value: '09'},
        {label: '10号', value: '10'},
        {label: '11号', value: '11'},
        {label: '12号', value: '12'},
        {label: '13号', value: '13'},
        {label: '14号', value: '14'},
        {label: '15号', value: '15'},
        {label: '16号', value: '16'},
        {label: '17号', value: '17'},
        {label: '18号', value: '18'},
        {label: '19号', value: '19'},
        {label: '20号', value: '20'},
        {label: '21号', value: '21'},
        {label: '22号', value: '22'},
        {label: '23号', value: '23'},
        {label: '24号', value: '24'},
        {label: '25号', value: '25'},
        {label: '26号', value: '26'},
        {label: '27号', value: '27'},
        {label: '28号', value: '28'},
        {label: '29号', value: '29'},
        {label: '30号', value: '30'},
        {label: '31号', value: '31'},
      ],
      userList: []
    };
  },
  created() {
    this.getList();
    this.getUserList()
  },
  methods: {
    // 巡检计划开关开关状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要' + text + '该巡检计划吗？').then(function() {
        return updatePlan(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    /** 查询巡检计划列表 */
    getList() {
      this.loading = true;
      listPlan(this.queryParams).then((response) => {
        this.planList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 查询用户列表
    getUserList() {
      listInspector({status: '0'}).then(res => {
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
        id: undefined,
        planName: undefined,
        planContent: undefined,
        projectName: undefined,
        startDate: undefined,
        endDate: undefined,
        inspectionCycle: undefined,
        inspectionPerson: undefined,
        userId: undefined,
        setTime: undefined,
        createBy: undefined,
        createTime: undefined,
        updateTime: undefined,
        updateBy: undefined,
        userIds: [],
        setDate: undefined,
        setDates:[],
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
      this.title = "添加巡检计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getPlan(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.$set(this.form, "userIds", response.data.userIds);
        this.$set(this.form, "setDates", response.data.setDates);
        this.open = true;
        this.title = "修改巡检计划";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updatePlan(this.form)
              .then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          } else {
            addPlan(this.form)
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
          return delPlan(ids);
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
        "system/plan/export",
        {
          ...this.queryParams,
        },
        `plan_${new Date().getTime()}.xlsx`
      );
    },
    // 选择巡检人
    selectUser(data) {
      let userNames='';
      data.forEach(element => {
        var inspectionUser = this.userList.find(u => u.userId == element)
      if(inspectionUser) {
        /* this.form.inspectionPerson = inspectionUser.nickName */
        userNames +=inspectionUser.nickName +',';
      }
      });
      this.form.inspectionPerson = userNames.substring(0,userNames.length-1)
    },
    // 选择设定日期
    selectSetDate(data) {
      let setDates='';
      data.forEach(element => {
        var setDate = this.weekList.find(u => u.value == element)
      if(setDate) {
        /* this.form.inspectionPerson = inspectionUser.nickName */
        setDates +=setDate.value +',';
      }
      });
      this.form.setDate = setDates.substring(0,setDates.length-1)
    },
    //清空设定日期
    clearSetDates(){
      this.form.setDates = []
    },
    // 设备选择
    selectChange(data) {
      this.form.setDates = data
      this.form.selectDate = data.join(',')
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
  },
};
</script>
