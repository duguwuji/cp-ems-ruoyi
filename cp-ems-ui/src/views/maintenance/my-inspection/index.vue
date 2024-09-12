<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="计划名称" prop="planName">
        <el-input v-model="queryParams.planName" placeholder="请输入计划名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="巡检人" prop="userId">
        <el-select v-model="queryParams.userId" placeholder="请选择巡检人" clearable @keyup.enter.native="handleQuery">
          <el-option v-for="item in userList" :key="item.userId" :label="item.nickName" :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="巡检状态" prop="inspectionStatus">
        <el-select v-model="queryParams.inspectionStatus" placeholder="请选择状态" clearable @keyup.enter.native="handleQuery">
          <el-option v-for="item in dict.type.inspection_record_status" :key="item.value" :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleOrder"-->
      <!--          v-hasPermi="['system:order:add']"-->
      <!--        >新建工单</el-button-->
      <!--        >-->
      <!--      </el-col>-->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:record:edit']"
          >处理</el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:record:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:record:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center" />
      <el-table-column label="计划名称" align="center" prop="planName" width="200" show-overflow-tooltip />
      <el-table-column label="计划内容" align="center" prop="planContent" width="200" show-overflow-tooltip />
      <el-table-column label="项目名称" align="center" prop="projectName" width="150" show-overflow-tooltip />
      <el-table-column label="计划巡检时间" align="center" prop="planTime" width="180">
      </el-table-column>
      <el-table-column label="实际巡检时间" align="center" prop="inspectionTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.inspectionTime ? scope.row.inspectionTime : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="巡检周期" align="center" prop="inspectionCycle" />
      <el-table-column label="巡检人" align="center" prop="inspectionPerson" />
      <el-table-column label="巡检状态" align="center" prop="inspectionStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inspection_record_status" :value="scope.row.inspectionStatus" />
        </template>
      </el-table-column>
      <el-table-column label="巡检记录" align="center" prop="inspectionRemark" width="200" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.inspectionRemark ? scope.row.inspectionRemark : '--' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <!-- <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template> -->
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <!-- <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template> -->
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" class-name="small-padding" width="240">
        <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)"
            v-hasPermi="['system:record:query']">详情</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:record:edit']" v-if="scope.row.inspectionStatus == 0">处理</el-button>
          <el-button size="mini" type="text" icon="el-icon-tickets" @click="handleOrder(scope.row)"
            v-hasPermi="['system:record:edit']">新建工单</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:record:remove']"
            >删除</el-button
          > -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 巡检记录详情对话框 -->
    <el-drawer
      :title="title"
      :visible.sync="openDetail"
      size="700px"
      :show-close="false"
      class="repair-drawer">
      <div style="height:calc(100% - 72px);padding: 0px 20px;overflow:auto;">
        <div>
          <div class="overview-title">
            <div class="overview-icon"></div><span>巡检状态</span>
          </div>
          <el-steps :active="active" style="padding: 20px 0 40px;">
            <el-step v-for="(step, index) in stepList" :key="index" :title="step.title" :icon="step.icon" :description="step.description"></el-step>
          </el-steps>
        </div>
        <el-form ref="form" :model="form" :rules="rules" label-width="110px">
          <div class="overview-title">
            <div class="overview-icon"></div><span>巡检内容</span>
          </div>
          <div style="font-size:14px;">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="暂无内容" disabled></el-input>
            </el-form-item>
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" placeholder="暂无内容" disabled></el-input>
            </el-form-item>
            <el-form-item label="巡检内容" prop="planContent">
              <el-input type="textarea" rows="3" v-model="form.planContent" placeholder="暂无内容" disabled></el-input>
            </el-form-item>
            <el-form-item label="计划巡检时间" prop="planTime">
              <el-date-picker disabled clearable v-model="form.planTime" type="datetime" format="yyyy-MM-dd HH:mm"
                value-format="yyyy-MM-dd HH:mm" placeholder="暂无内容">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="巡检周期" prop="inspectionCycle">
              <el-select v-model="form.inspectionCycle" placeholder="暂无内容" disabled style="width:100%;">
                <el-option v-for="item in inspectionCycleList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="overview-title">
            <div class="overview-icon"></div><span>巡检结果</span>
          </div>
          <div style="font-size:14px;">
            <el-form-item label="巡检人">
              <el-select v-model="form.userIds" multiple placeholder="暂无内容" @change="personChange" disabled style="width:100%;">
                <el-option v-for="item in userList" :key="item.userId" :label="item.nickName"
                  :value="item.userId"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="实际巡检时间" prop="inspectionTime">
              <el-date-picker :disabled="disabled" clearable v-model="form.inspectionTime" type="datetime" format="yyyy-MM-dd HH:mm"
                value-format="yyyy-MM-dd HH:mm" placeholder="暂无内容">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="巡检记录">
              <el-input type="textarea" rows="3" v-model="form.inspectionRemark" placeholder="暂无内容" :disabled="disabled"></el-input>
            </el-form-item>
          </div>
        </el-form>
      </div>
      <div class="drawer-footer" style="padding: 20px;">
        <el-button type="default" size="small" @click="cancel" icon="el-icon-close">关闭</el-button>
        <el-button v-if="!disabled" type="primary" size="small" @click="submitForm" icon="el-icon-edit-outline" :loading="buttonLoading">完成</el-button>
      </div>
    </el-drawer>

    <!-- 添加工单对话框 -->
    <el-dialog :title="orderTitle" :visible.sync="orderOpen" width="500px" append-to-body>
      <el-form ref="orderForm" :model="orderForm" :rules="orderRules" label-width="80px">
        <el-form-item label="工单编号" prop="orderNo">
          <el-input v-model="orderForm.orderNo" placeholder="请输入工单编号" :disabled="true" />
        </el-form-item>
        <el-form-item label="工单内容" prop="orderContent">
          <!-- <editor v-model="form.orderContent" :min-height="192"/> -->
          <el-input type="textarea" v-model="orderForm.orderContent"></el-input>
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="orderForm.projectName" placeholder="请输入项目名称" :disabled="true" />
        </el-form-item>
        <el-form-item label="负责人" prop="userId">
          <el-select v-model="orderForm.userId" placeholder="请选择" @change="userChange">
            <el-option v-for="item in userList" :key="item.userId" :label="item.nickName"
              :value="item.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="附件" prop="annex">
          <fileUpload v-model="orderForm.annex"/>
        </el-form-item>
        <el-form-item label="工单备注" prop="orderRemark">
          <el-input v-model="orderForm.orderRemark" placeholder="请输入工单备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitOrderForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRecord,
  getRecord,
  delRecord,
  addRecord,
  updateRecord,
} from "@/api/system/inspectionRecord";
import { listInspector } from "@/api/system/user"
import { addOrder } from '@/api/system/repairOrder'
import moment from 'moment/moment'
import { mapGetters } from 'vuex';
export default {
  dicts: ['inspection_record_status'],
  name: "Record",
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
      // 巡检记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openDetail: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planName: undefined,
        planContent: undefined,
        projectName: undefined,
        planTime: undefined,
        inspectionTime: undefined,
        inspectionPerson: undefined,
        inspectionStatus: undefined,
        inspectionRemark: undefined,
        userId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        planName: [
          { required: true, message: "计划名称不能为空", trigger: "blur" },
        ],
        planContent: [
          { required: true, message: "计划内容不能为空", trigger: "blur" },
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" },
        ],
        planTime: [
          { required: true, message: "计划巡检时间不能为空", trigger: "blur" },
        ],
        inspectionTime: [
          { required: true, message: "实际巡检时间不能为空", trigger: "blur" },
        ],
        userIds: [
          { required: true, message: "巡检人不能为空", trigger: "blur" },
        ],
        inspectionStatus: [
          { required: true, message: "巡检状态不能为空", trigger: "blur" },
        ],
        inspectionCycle: [
          { required: true, message: "巡检周期不能为空", trigger: "blur" },
        ],
      },
      orderOpen: false,
      orderTitle: "",
      // 表单参数
      orderForm: {},
      // 表单校验
      orderRules: {
        id: [{ required: true, message: "不能为空", trigger: "blur" }],
        orderNo: [
          { required: true, message: "工单编号不能为空", trigger: "blur" },
        ],
        orderContent: [
          { required: true, message: "工单内容不能为空", trigger: "blur" },
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" },
        ],
        /* assignTime: [
          { required: true, message: "派单时间不能为空", trigger: "blur" },
        ], */
        // finishTime: [
        //   { required: true, message: "完成时间不能为空", trigger: "blur" },
        // ],
        // finishBy: [
        //   { required: true, message: "负责人不能为空", trigger: "blur" },
        // ],
        orderStatus: [
          { required: true, message: "工单状态不能为空", trigger: "change" },
        ],
        // userId: [
        //   { required: true, message: "负责人不能为空", trigger: "blur" },
        // ],
      },
      userList: [],
      inspectionCycleList: [
        { label: '月', value: '月' },
        { label: '周', value: '周' },
        { label: '日', value: '日' },
      ],
      active: 0,
      stepList: [
        {title: '创建', icon: 'el-icon-s-order', description: ''},
        {title: '进行中', icon: 'el-icon-s-management', description: ''},
        {title: '完成', icon: 'el-icon-s-claim', description: ''},
      ],
      disabled: false,
    };
  },
  computed: {
    ...mapGetters({
      userId: 'userId'
    })
  },
  created() {
    this.getUserList()
    this.getList();
  },
  methods: {
    /** 查询巡检记录列表 */
    getList() {
      this.queryParams.userId = this.userId
      this.loading = true;
      listRecord(this.queryParams).then((response) => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 获取用户列表
    getUserList() {
      listInspector({ status: '0' }).then(res => {
        this.userList = res.rows
      })
    },
    // 取消按钮
    cancel() {
      this.orderOpen = false;
      this.openDetail = false;
      this.reset();
      this.disabled = false;
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        planName: undefined,
        planContent: undefined,
        projectName: undefined,
        planTime: undefined,
        inspectionTime: undefined,
        inspectionPerson: undefined,
        inspectionStatus: undefined,
        inspectionRemark: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        inspectionCycle: undefined,
        userIds:[]
      };
      this.resetForm("form");
    },
    // 表单重置
    resetOrder() {
      this.orderForm = {
      };
      this.resetForm("orderForm");
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
      this.title = "添加巡检记录";
    },
    /** 新增工单按钮操作 */
    handleOrder(row) {
      this.resetOrder();
      this.orderForm.projectName = row.projectName;
      this.orderOpen = true;
      this.orderForm.orderNo = 'WX' + moment().format("YYYYMMDDHHmmssSSS");
      this.orderTitle = "添加维修工单";
    },
    /** 处理按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.disabled = false
      this.reset();
      const id = row.id || this.ids;
      getRecord(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.$set(this.form, "userIds", response.data.userIds);
        this.openDetail = true;
        this.title = "处理巡检";
        this.handleActive()
      });
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.loading = true;
      this.disabled = true
      this.reset();
      const id = row.id || this.ids;
      getRecord(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.$set(this.form, "userIds", response.data.userIds);
        this.openDetail = true;
        this.title = "巡检记录详情";
        this.handleActive()
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            this.form.inspectionStatus = '1';
            updateRecord(this.form)
              .then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.openDetail = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          } else {
            this.form.inspectionStatus = '0'
            addRecord(this.form)
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
    /** 提交按钮 */
    submitOrderForm() {
      this.$refs["orderForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          if(this.orderForm.userId){
            this.orderForm.orderStatus = '1'
            this.orderForm.assignTime = moment().format("yyyy-MM-DD HH:mm:ss");
          }
          addOrder(this.orderForm)
            .then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.orderOpen = false;
              this.getList();
              this.$router.push('/maintenance/repair-order')
            })
            .finally(() => {
              this.buttonLoading = false;
            });
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
          return delRecord(ids);
        })
        .then(() => {
          this.loading = false;
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => { })
        .finally(() => {
          this.loading = false;
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/record/export",
        {
          ...this.queryParams,
        },
        `record_${new Date().getTime()}.xlsx`
      );
    },
    // 工单选择负责人
    userChange(data) {
      var user = this.userList.find(u => u.userId == data)
      if (user) {
        this.orderForm.finishBy = user.nickName
      }
    },
    // 工单选择负责人
    personChange(data) {
      var user = this.userList.find(u => u.userId == data)
      if (user) {
        this.form.inspectionPerson = user.nickName
      }
    },
    // 状态转为步骤
    handleActive() {
      switch (this.form.inspectionStatus) {
        case '0':
          this.stepList = [
            {title: '创建', icon: 'el-icon-s-order', description: this.form.createTime},
            {title: '进行中', icon: 'el-icon-s-management', description: ''},
            {title: '完成', icon: 'el-icon-s-claim', description: ''},
          ]
          this.active = 1
          break;
        case '1':
          this.stepList = [
            {title: '创建', icon: 'el-icon-s-order', description: this.form.createTime},
            {title: '巡检', icon: 'el-icon-s-management', description: this.form.inspectionTime},
            {title: '已完成', icon: 'el-icon-s-claim', description: this.form.inspectionTime},
          ]
          this.active = 3
          break;
        default:
          break;
      }
    },
    closeDrawer() {
      this.cancel()
    }
  },
};
</script>
<style scoped>
.overview-title {
  color:var(--base-color-1);
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-weight: bold;
}
.overview-icon {
  width: 5px;
  height: 20px;
  margin-right: 10px;
  background: var(--current-color);
}
.repair-drawer>>>.el-drawer__header {
  font-size: 20px;
}
</style>
