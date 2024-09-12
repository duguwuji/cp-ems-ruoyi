<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单编号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入工单编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="派单时间" prop="assignTime">
        <el-date-picker
          clearable
          v-model="queryParams.assignTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择派单时间"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="完成时间" prop="finishTime">
        <el-date-picker
          clearable
          v-model="queryParams.finishTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择完成时间"
        >
        </el-date-picker>
      </el-form-item> -->
      <el-form-item label="责任人" prop="userId">
        <el-select v-model="queryParams.userId" placeholder="请选择责任人" clearable @keyup.enter.native="handleQuery">
          <el-option v-for="item in userList" :key="item.userId" :label="item.nickName" :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="工单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable @keyup.enter.native="handleQuery">
          <el-option v-for="item in dict.type.repair_status" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:order:add']">新建工单</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleDistribute"
          v-hasPermi="['system:order:edit']"
          >派发</el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:order:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:order:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center" />
      <el-table-column label="工单编号" align="center" prop="orderNo" width="200" />
      <el-table-column label="工单内容" align="center" prop="orderContent" width="200" show-overflow-tooltip/>
      <el-table-column label="项目名称" align="center" prop="projectName" width="180" show-overflow-tooltip/>
      <el-table-column label="派单时间" align="center" prop="assignTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.assignTime ? scope.row.assignTime : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="finishTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.finishTime ? scope.row.finishTime : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="责任人" align="center" prop="finishBy">
        <template slot-scope="scope">
          {{ scope.row.finishBy ? scope.row.finishBy : '--' }}
        </template>
      </el-table-column>
      <el-table-column label="工单状态" align="center" prop="orderStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.repair_status" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180"/>
      <el-table-column label="工单备注" align="center" prop="orderRemark" width="180" show-overflow-tooltip>
        <template slot-scope="scope">{{ scope.row.orderRemark ? scope.row.orderRemark : '--' }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding" fixed="right" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasRole="['admin']">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)"
            v-hasPermi="['system:order:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleDistribute(scope.row)"
            v-hasPermi="['system:order:edit']" v-if="scope.row.orderStatus == '0'">派发</el-button>
          <el-button size="mini" type="text" icon="el-icon-circle-close" @click="handleCancel(scope.row)"
            v-hasPermi="['system:order:edit']" v-if="scope.row.orderStatus != '2' && scope.row.orderStatus != '3'">取消</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:order:remove']">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
      <!-- 新增维修工单对话框 -->
    <el-dialog :title="title" :visible.sync="openAdd" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入工单编号" :disabled="true" />
        </el-form-item>
        <el-form-item label="工单内容" prop="orderContent">
          <!-- <editor v-model="form.orderContent" :min-height="192"/> -->
          <el-input type="textarea" v-model="form.orderContent" placeholder="请输入工单内容"></el-input>
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="责任人">
          <el-select v-model="form.userId" placeholder="请选择责任人" @change="userChange">
            <el-option v-for="item in userList" :key="item.userId" :label="item.nickName"
              :value="item.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工单状态" prop="orderStatus" v-if="form.id != null">
          <el-select v-model="form.orderStatus" placeholder="请选择状态">
            <el-option v-for="item in dict.type.repair_status" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="附件" prop="annex">
          <fileUpload v-model="form.annex"/>
        </el-form-item>
        <el-form-item label="工单备注" prop="orderRemark">
          <el-input v-model="form.orderRemark" placeholder="请输入工单备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 处理维修工单对话框 -->
    <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入工单编号" :disabled="true" />
        </el-form-item>
        <el-form-item label="工单内容" prop="orderContent">
          <el-input type="textarea" v-model="form.orderContent" :disabled="dealWith" placeholder='请输入工单内容'></el-input>
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" :disabled="dealWith" />
        </el-form-item>
        <el-form-item label="责任人" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择责任人" @change="userChange">
            <el-option v-for="item in userList" :key="item.userId" :label="item.nickName"
              :value="item.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工单状态" prop="orderStatus" v-if="form.id != null">
          <el-select v-model="form.orderStatus" placeholder="请选择状态" :disabled="true">
            <el-option v-for="item in dict.type.repair_status" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="附件" prop="annex">
          <fileUpload v-model="form.annex"/>
        </el-form-item>
        <el-form-item label="工单备注" prop="orderRemark">
          <el-input v-model="form.orderRemark" placeholder="请输入工单备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">派 发</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
    <!-- 维修工单详情对话框 -->
    <el-drawer
      :title="title"
      :visible.sync="openDetail"
      size="700px"
      :show-close="false"
      class="repair-drawer">
      <div style="height:calc(100% - 72px);padding: 0px 20px;overflow:auto;">
        <div>
          <div class="overview-title">
            <div class="overview-icon"></div><span>工单状态</span>
          </div>
          <el-steps :active="active" style="padding: 20px 0 40px;">
            <el-step v-for="(step, index) in stepList" :key="index" :title="step.title" :icon="step.icon" :description="step.description"></el-step>
          </el-steps>
        </div>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <div class="overview-title">
            <div class="overview-icon"></div><span>工单内容</span>
          </div>
          <div style="font-size:14px;">
            <el-form-item label="工单编号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="暂无内容" :disabled="disabled"></el-input>
            </el-form-item>
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" placeholder="暂无内容" :disabled="disabled"></el-input>
            </el-form-item>
            <el-form-item label="工单内容" prop="orderContent">
              <el-input type="textarea" rows="3" v-model="form.orderContent" placeholder="暂无内容" :disabled="disabled"></el-input>
            </el-form-item>
            <el-form-item label="附件">
              <span v-if="disabled&&!form.annex" style="color:var(--base-color-3);">暂无内容</span>
              <fileUpload v-else v-model="form.annex" :disable="disabled"/>
            </el-form-item>
          </div>
          <div class="overview-title">
            <div class="overview-icon"></div><span>处理结果</span>
          </div>
          <div style="font-size:14px;">
            <el-form-item label="负责人" :prop="dealWith ? 'userId' : ''">
              <el-select v-model="form.userId" placeholder="暂无内容" :disabled="disabled&&!dealWith" @change="userChange" style="width: 100%;">
                <el-option v-for="item in userList" :key="item.userId" :label="item.nickName"
                  :value="item.userId"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="工单备注">
              <el-input type="textarea" rows="3" v-model="form.orderRemark" placeholder="暂无内容" :disabled="disabled&&!dealWith"></el-input>
            </el-form-item>
          </div>
        </el-form>
      </div>
      <div class="drawer-footer" style="padding: 20px;">
        <el-button type="default" size="small" @click="cancel" icon="el-icon-close">关闭</el-button>
        <el-button v-if="!disabled" type="primary" size="small" @click="saveUpdate" icon="el-icon-edit-outline" :loading="buttonLoading">保存</el-button>
        <el-button v-if="dealWith" type="primary" size="small" @click="submitForm" icon="el-icon-edit-outline" :loading="buttonLoading">派发</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {
  listOrder,
  getOrder,
  delOrder,
  addOrder,
  updateOrder,
} from "@/api/system/repairOrder";
import { listInspector } from "@/api/system/user"
import moment from 'moment/moment'

export default {
  name: "repair-order",
  dicts: ["repair_status"],
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
      // 维修工单表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层
      openDetail: false,
      openAdd: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        orderContent: undefined,
        projectName: undefined,
        assignTime: undefined,
        finishTime: undefined,
        finishBy: undefined,
        orderStatus: undefined,
        orderRemark: undefined,
        userId: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
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
        assignTime: [
          { required: true, message: "派单时间不能为空", trigger: "blur" },
        ],
        // finishTime: [
        //   { required: true, message: "完成时间不能为空", trigger: "blur" },
        // ],
        userId: [
          { required: true, message: "责任人不能为空", trigger: "blur" },
        ],
        orderStatus: [
          { required: true, message: "工单状态不能为空", trigger: "change" },
        ],
        // orderRemark: [
        //   { required: true, message: "工单备注不能为空", trigger: "blur" },
        // ],
      },
      addRules: {
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
        assignTime: [
          { required: true, message: "派单时间不能为空", trigger: "blur" },
        ],
        // finishTime: [
        //   { required: true, message: "完成时间不能为空", trigger: "blur" },
        // ],
        /* userId: [
          { required: true, message: "责任人不能为空", trigger: "blur" },
        ], */
        orderStatus: [
          { required: true, message: "工单状态不能为空", trigger: "change" },
        ],
        // orderRemark: [
        //   { required: true, message: "工单备注不能为空", trigger: "blur" },
        // ],
      },
      userList: [],
      dealWith: false, // 是否是派发
      active: 0,
      stepList: [
        {title: '创建', icon: 'el-icon-s-order', description: ''},
        {title: '待派发', icon: 'el-icon-s-promotion', description: ''},
        {title: '处理', icon: 'el-icon-s-management', description: ''},
        {title: '完成', icon: 'el-icon-s-claim', description: ''},
      ],
      disabled: false, // 是否可输入
    };
  },
  created() {
    this.getList();
    this.getUserList();
  },
  methods: {
    /** 查询维修工单列表 */
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then((response) => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 获取用户列表
    getUserList() {
      listInspector({ status: '0' }).then(res => {
        // this.userList = res.rows
        res.rows.forEach((item, index) => {
          if (item.userId != 1) {
            this.userList.push(item)
          }
        })
      })
    },
    // 取消按钮
    cancel() {
      // this.open = false;
      this.openAdd = false;
      this.openDetail = false;
      this.dealWith = false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        orderNo: undefined,
        orderContent: undefined,
        projectName: undefined,
        assignTime: undefined,
        finishTime: undefined,
        finishBy: undefined,
        orderStatus: undefined,
        orderRemark: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        userId: undefined,
        annex: undefined
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
      this.form.orderNo = 'WX' + moment().format("YYYYMMDDHHmmssSSS");
      this.dealWith = false;
      this.openAdd = true;
      this.title = "添加维修工单";
    },
    /** 派发按钮操作 */
    handleDistribute(row) {
      this.loading = true;
      this.reset();
      this.dealWith = true;
      this.disabled = true
      const id = row.id || this.ids;
      getOrder(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.openDetail = true;
        // this.open = true;
        this.title = "工单派发";
      });
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.loading = true;
      this.reset();
      this.disabled = true;
      this.dealWith = false
      const id = row.id || this.ids;
      getOrder(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.openDetail = true;
        this.title = "工单详情";
        this.handleActive()
      });
    },
    /** 编辑按钮 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      this.disabled = false;
      this.dealWith = false
      const id = row.id || this.ids;
      getOrder(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.openDetail = true;
        this.title = "编辑工单";
        this.handleActive()
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            this.form.assignTime = moment().format("yyyy-MM-DD HH:mm:ss");
            this.form.orderStatus = '1';
            updateOrder(this.form)
              .then((response) => {
                this.$modal.msgSuccess("派发成功");
                // this.open = false;
                this.openDetail = false
                this.dealWith = false
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          } else {
            if(this.form.userId){
              this.form.orderStatus = '1';
              this.form.assignTime = moment().format("yyyy-MM-DD HH:mm:ss");
            }
            addOrder(this.form)
              .then((response) => {
                this.$modal.msgSuccess("新增成功");
                this.openAdd = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          }
        }
      });
    },
    /** 修改保存按钮 */
    saveUpdate() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          updateOrder(this.form)
            .then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.openDetail = false;
              this.getList();
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
          return delOrder(ids);
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
    /** 取消按钮操作 */
    handleCancel(row) {
      this.$modal
        .confirm('是否确认取消该工单？')
        .then(() => {
          this.loading = true;
          row.orderStatus = '3'
          return updateOrder(row);
        })
        .then(() => {
          this.loading = false;
          this.getList();
          this.$modal.msgSuccess("取消工单成功");
        })
        .catch(() => { })
        .finally(() => {
          this.loading = false;
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/order/export",
        {
          ...this.queryParams,
        },
        `order_${new Date().getTime()}.xlsx`
      );
    },
    // 责任人选择
    userChange(data) {
      var user = this.userList.find(u => u.userId == data)
      if (user) {
        this.form.finishBy = user.nickName
      }
    },
    // 状态转为步骤
    handleActive() {
      switch (this.form.orderStatus) {
        case '0':
          this.stepList = [
            {title: '创建', icon: 'el-icon-s-order', description: this.form.createTime},
            {title: '待派发', icon: 'el-icon-s-promotion', description: ''},
            {title: '处理', icon: 'el-icon-s-management', description: ''},
            {title: '完成', icon: 'el-icon-s-claim', description: ''},
          ]
          this.active = 1
          break;
        case '1':
          this.stepList = [
            {title: '创建', icon: 'el-icon-s-order', description: this.form.createTime},
            {title: '已派发', icon: 'el-icon-s-promotion', description: this.form.assignTime},
            {title: '进行中', icon: 'el-icon-s-management', description: ''},
            {title: '完成', icon: 'el-icon-s-claim', description: ''},
          ]
          this.active = 2
          break;
        case '2':
          this.stepList = [
            {title: '创建', icon: 'el-icon-s-order', description: this.form.createTime},
            {title: '已派发', icon: 'el-icon-s-promotion', description: this.form.assignTime},
            {title: '处理', icon: 'el-icon-s-management', description: ''},
            {title: '已完成', icon: 'el-icon-s-claim', description: this.form.finishTime},
          ]
          this.active = 4
          break;
        case '3':
          this.stepList = [
            {title: '创建', icon: 'el-icon-s-order', description: this.form.createTime},
            {title: '已取消', icon: 'el-icon-s-release', description: this.form.updateTime},
          ]
          this.active = 4
          break;
        default:
          break;
      }
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
