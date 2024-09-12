<template>
  <div class="app-container flex-between bg-container">
    <div class="content-tree">
      <div>
        <el-tabs v-model="item" type="card">
          <el-tab-pane name="first">
            <span slot="label"><i class="el-icon-help"></i> 能源区域</span>
            <topologicaTree v-if="item=='first'" item-type="building" :showCheckbox="false" @selectItem="treeItem($event)"/>
          </el-tab-pane>
          <el-tab-pane name="second">
            <span slot="label"><i class="el-icon-set-up"></i> 能源分项</span>
            <topologicaTree v-if="item=='second'" item-type="energySubsections" :showCheckbox="false" @selectItem="treeItem($event)"/>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <div class="content-data">
      <div class="overview-title">
        <div class="overview-icon"></div>
        <span>定额配置</span>
      </div>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="定额时间">
          <el-date-picker
            v-model="dateRange"
            style="width: 240px"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="定额类型" prop="quotaType">
          <el-select v-model="queryParams.quotaType" placeholder="请选择定额类型">
            <el-option v-for="item in dict.type.quota_type" :key="item.value" :label="item.label" :value="item.value">
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
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:quota:add']"
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
            v-hasPermi="['system:quota:edit']"
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
            v-hasPermi="['system:quota:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:quota:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="quotaList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="55" align="center" />
        <el-table-column label="定额类型" align="center" prop="quotaType" >
          <template slot-scope="scope">
            <dict-tag :options="dict.type.quota_type" :value="scope.row.quotaType"/>
          </template>
        </el-table-column>
        <el-table-column label="定额对象" align="center" prop="itemName" />
        <el-table-column label="定额时间" align="center" prop="quotaTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.quotaTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="定额数值" align="center" prop="quotaValue" />
        <el-table-column label="实际累计能耗" align="center" prop="realEnergy" />
        <el-table-column label="临界范围" align="center" prop="criticalVo" />
        <el-table-column label="越限范围" align="center" prop="overMedianVo" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:quota:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:quota:remove']"
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
    </div>

    <!-- 添加或修改定额配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="定额类型" prop="quotaType">
          <el-select v-model="form.quotaType" placeholder="请选择定额类型" @change="changeType" :disabled="edit">
            <el-option v-for="item in dict.type.quota_type" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="定额时间" prop="quotaTime">
          <el-date-picker clearable :disabled="edit"
                          v-model="form.quotaTime"
                          :type="form.dateType"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择定额时间"
                          :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="定额数值" prop="quotaValue">
          <el-input v-model="form.quotaValue" placeholder="填写定额数值,如10000,20000等" />
        </el-form-item>
        <el-form-item label="临界范围" prop="critical">
          <el-input v-model="form.critical" placeholder="填写临界范围比例,如0.8、0.9等" />
        </el-form-item>
        <el-form-item label="越限范围" prop="overMedian">
          <el-input v-model="form.overMedian" placeholder="填写越限范围比例,如1.1、1.2等" />
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
import { listQuota, getQuota, delQuota, addQuota, updateQuota } from "@/api/system/quota";
import topologicaTree from "@/components/TopologicaTree/index";
import moment from 'moment'

export default {
  name: "Quota",
  dicts: ['quota_type'],
  components: {
    topologicaTree,
  },
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
      // 定额配置表格数据
      quotaList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //选项卡
      item:"first",
      //括朴类型
      itemType:"building",
      treeOptions:[],
      itemId:undefined,
      // 日期范围
      dateRange: [],
      edit:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        quotaType: undefined,
        itemId: undefined,
        quotaTime: undefined,
        quotaValue: undefined,
        realEnergy: undefined,
        critical: undefined,
        overMedian: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        quotaId: [
          { required: true, message: "主键不能为空", trigger: "blur" }
        ],
        quotaType: [
          { required: true, message: "定额类型不能为空", trigger: "change" }
        ],
        itemId: [
          { required: true, message: "区域不能为空", trigger: "blur" }
        ],
        quotaTime: [
          { required: true, message: "定额时间不能为空", trigger: "blur" }
        ],
        quotaValue: [
          { required: true, message: "定额数值不能为空", trigger: "blur" }
        ],
        realEnergy: [
          { required: true, message: "实际累计能耗不能为空", trigger: "blur" }
        ],
        critical: [
          { required: true, message: "临界范围不能为空", trigger: "blur" }
        ],
        overMedian: [
          { required: true, message: "越限范围不能为空", trigger: "blur" }
        ],
      },
      pickerOptions: {
        disabledDate(date) {
          return date.getTime() > Date.now(); // 禁用大于今天的日期
        },
      },
    };
  },
  created() {
    // this.getList();
  },
  methods: {
    treeItem(value) {
      this.itemId = value.id;
      this.queryParams.itemId = value.id;
      this.getList();
    },
    getTree(value){
      this.treeOptions = value;
    },
    changeType(){
      if(this.form.quotaType == "0"){
        this.form.dateType = "month"
      }if(this.form.quotaType == "1"){
        this.form.dateType = "year"
      }
    },
    /** 查询定额配置列表 */
    getList() {
      this.loading = true;
      listQuota(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.quotaList = response.rows;
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
        quotaId: undefined,
        quotaType: undefined,
        itemId: undefined,
        quotaTime: undefined,
        quotaValue: undefined,
        realEnergy: undefined,
        critical: undefined,
        overMedian: undefined,
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.quotaId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.quotaTime = moment().format("yyyy-MM-01 00:00:00");
      this.form.quotaType = "0"
      this.form.dateType = "month"
      this.form.itemId = this.itemId;
      this.edit = false;
      this.open = true;
      this.title = "添加定额配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      this.edit = true;
      const quotaId = row.quotaId || this.ids
      getQuota(quotaId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改定额配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.form.quotaType == "0"){
        this.form.quotaTime = moment(this.form.quotaTime).format("yyyy-MM-01 00:00:00")
      }if(this.form.quotaType == "1"){
        this.form.quotaTime = moment(this.form.quotaTime).format("yyyy-01-01 00:00:00")
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.quotaId != null) {
            updateQuota(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addQuota(this.form).then(response => {
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
      const quotaIds = row.quotaId || this.ids;
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true;
        return delQuota(quotaIds);
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
      this.download('system/quota/export', {
        ...this.queryParams
      }, `quota_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 700px;
  height: calc(100vh - 84px);

  .overview-title {
    height: 30px;
    line-height: 30px;
    display: flex;
    margin-bottom: 20px;
    color: var(--base-color-1);

    .overview-icon {
      width: 5px;
      height: 20px;
      margin: 5px 10px 0 0;
      background: var(--current-color);
    }
  }

  .content-tree {
    width: 25%;
    height: 100%;
    background: var(--base-item-bg);
    margin-right: 6px;
    padding: 20px;
  }
  .content-data {
    width: calc(100% - 25% - 10px);
    height: 100%;
    background: var(--base-item-bg);
    padding: 12px 20px;

    .data-chart {
      height: 40%;
      // border: 1px solid;
    }

    .data-table {
      height: calc(100% - 51px);
      // border: 1px solid;
    }
    @media screen and (max-width: 1262px) {
      .data-table {
        height: calc(100% - 101px);
      }
    }
    @media screen and (max-width: 645px) {
      .data-table {
        height: calc(100% - 151px);
      }
    }

  }
}
</style>
