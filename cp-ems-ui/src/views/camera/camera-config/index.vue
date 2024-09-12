<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="摄像头名称" prop="cameraName">
        <el-input
          v-model="queryParams.cameraName"
          placeholder="请输入摄像头名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="品牌" prop="cameraBrand">
        <el-input
          v-model="queryParams.cameraBrand"
          placeholder="请输入品牌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="序列号" prop="cameraSn">
        <el-input
          v-model="queryParams.cameraSn"
          placeholder="请输入序列号"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:cameraConfig:add']"
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
          v-hasPermi="['system:cameraConfig:edit']"
        >编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:cameraConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:cameraConfig:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cameraConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center"/>
      <el-table-column label="摄像头名称" align="center" prop="cameraName" />
      <el-table-column label="品牌" align="center" prop="cameraBrand" />
      <el-table-column label="序列号" align="center" prop="cameraSn" />
      <el-table-column label="视频token" align="center" prop="cameraToken" width="180" show-overflow-tooltip/>
      <el-table-column label="视频key" align="center" prop="cameraKey" width="180" show-overflow-tooltip/>
      <el-table-column label="视频secret" align="center" prop="cameraSecret" width="180" show-overflow-tooltip/>
      <el-table-column label="所在区域" align="center" >
        <template slot-scope="scope">{{scope.row.area.itemName}}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:cameraConfig:edit']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:cameraConfig:remove']"
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

    <!-- 添加或修改视频配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="摄像头名称" prop="cameraName">
          <el-input v-model="form.cameraName" placeholder="请输入摄像头名称" />
        </el-form-item>
        <el-form-item label="品牌" prop="cameraBrand">
          <el-input v-model="form.cameraBrand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="序列号" prop="cameraSn">
          <el-input v-model="form.cameraSn" placeholder="请输入序列号" />
        </el-form-item>
        <el-form-item label="视频token" prop="cameraToken">
          <el-input v-model="form.cameraToken" placeholder="请输入视频token" />
        </el-form-item>
        <el-form-item label="视频key" prop="cameraKey">
          <el-input v-model="form.cameraKey" placeholder="请输入视频key" />
        </el-form-item>
        <el-form-item label="视频secret" prop="cameraSecret">
          <el-input v-model="form.cameraSecret" placeholder="请输入视频secret" />
        </el-form-item>
        <el-form-item label="区域" prop="areaId">
          <treeselect v-model="form.areaId" :options="areaList" :show-count="true" placeholder="请选择区域"/>
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
import { listCameraConfig, getCameraConfig, delCameraConfig, addCameraConfig, updateCameraConfig } from "@/api/system/cameraConfig";
import {
  topologyTreeSelect,
} from "@/api/system/itemTopology";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "CameraConfig",
  components: {
    Treeselect
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
      // 视频配置表格数据
      cameraConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cameraName: undefined,
        cameraBrand: undefined,
        cameraSn: undefined,
        cameraToken: undefined,
        cameraKey: undefined,
        cameraSecret: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        cameraName: [
          { required: true, message: "摄像头名称不能为空", trigger: "blur" }
        ],
        cameraBrand: [
          { required: true, message: "品牌不能为空", trigger: "blur" }
        ],
        cameraSn: [
          { required: true, message: "序列号不能为空", trigger: "blur" }
        ],
        cameraToken: [
          { required: true, message: "视频token不能为空", trigger: "blur" }
        ],
        cameraKey: [
          { required: true, message: "视频key不能为空", trigger: "blur" }
        ],
        cameraSecret: [
          { required: true, message: "视频secret不能为空", trigger: "blur" }
        ],
        areaId: [
          { required: true, message: "地点不能为空", trigger: "blur" }
        ],
      },
      areaList: [],
    };
  },
  created() {
    this.getList();
    this.getAreaList()
  },
  methods: {
    /** 获取区域树 */
    getAreaList() {
      topologyTreeSelect({itemType:'building'}).then(res => {
        this.areaList = res.data
      })
    },
    /** 查询视频配置列表 */
    getList() {
      this.loading = true;
      listCameraConfig(this.queryParams).then(response => {
        this.cameraConfigList = response.rows;
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
        cameraName: undefined,
        cameraBrand: undefined,
        cameraSn: undefined,
        cameraToken: undefined,
        cameraKey: undefined,
        cameraSecret: undefined,
        areaId: undefined,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加视频配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getCameraConfig(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改视频配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateCameraConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addCameraConfig(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true;
        return delCameraConfig(ids);
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
      this.download('system/cameraConfig/export', {
        ...this.queryParams
      }, `cameraConfig_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
<style scoped>
.app-container {
  min-height: calc(100vh - 84px);
}
</style>
