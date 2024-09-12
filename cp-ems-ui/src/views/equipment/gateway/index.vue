<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="网关SN" prop="sn">
        <el-input
          v-model="queryParams.sn"
          placeholder="请输入网关SN"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="网关型号" prop="model">
        <el-input
          v-model="queryParams.model"
          placeholder="请输入网关型号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="工厂" prop="factory">
        <el-input
          v-model="queryParams.factory"
          placeholder="请输入工厂"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="区域" prop="areaId">
        <treeselect style="width: 180px;" v-model="queryParams.areaId" :options="areaOptions" :show-count="false" placeholder="请选择区域"/>
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
          v-hasPermi="['system:gateway:add']"
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
          v-hasPermi="['system:gateway:edit']"
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
          v-hasPermi="['system:gateway:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:gateway:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="gatewayList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="网关SN" align="center" prop="sn" />
      <el-table-column label="网关型号" align="center" prop="model" />
      <el-table-column label="网关描述" align="center" prop="description" />
      <el-table-column label="设备时区" align="center" prop="timeZone" />
      <!-- <el-table-column label="设备二维码" align="center" prop="qrCode" /> -->
      <!-- <el-table-column label="工厂" align="center" prop="factory" /> -->
      <el-table-column label="区域" align="center" prop="area" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:gateway:edit']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:gateway:remove']"
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

    <!-- 添加或修改网关信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="860px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网关SN" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入网关SN" />
        </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="网关型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入网关型号" />
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网关描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入网关描述" />
        </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="网关时区" prop="timeZone">
          <el-input v-model="form.timeZone" placeholder="请输入设备时区" />
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域" prop="areaId">
          <treeselect v-model="form.areaId" :options="areaOptions" :show-count="false" placeholder="请选择区域" @select="areaSelect"/>
        </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="网关图片" prop="img">
          <imageUpload v-model="form.img" :limit="2" :values="form.img"/>
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网关二维码" prop="qrCode">
          <imageUpload v-model="form.qrCode" :limit="1" :values="form.qrCode"/>
        </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-form-item label="工厂" prop="factory">
          <el-input v-model="form.factory" placeholder="请输入工厂" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listGateway, getGateway, delGateway, addGateway, updateGateway } from "@/api/system/gateway";
import { topologyTreeSelect} from "@/api/system/itemTopology";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "Gateway",
  components: { Treeselect },
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
      // 网关信息表格数据
      gatewayList: [],
      // 区域树结构
      areaOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        sn: undefined,
        model: undefined,
        // description: undefined,
        // timeZone: undefined,
        // qrCode: undefined,
        factory: undefined,
        area: undefined,
        areaId: undefined,
      },
      // 查询参数
      queryAreaParams: {
        itemType:'building'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        sn: [
          { required: true, message: "网关SN不能为空", trigger: "blur" }
        ],
        model: [
          { required: true, message: "网关型号不能为空", trigger: "blur" }
        ],
        /* description: [
          { required: true, message: "网关描述不能为空", trigger: "blur" }
        ],
        timeZone: [
          { required: true, message: "设备时区不能为空", trigger: "blur" }
        ], */
        /* qrCode: [
          { required: true, message: "设备二维码不能为空", trigger: "blur" }
        ],
        factory: [
          { required: true, message: "工厂不能为空", trigger: "blur" }
        ],
        area: [
          { required: true, message: "区域不能为空", trigger: "blur" }
        ], */
      }
    };
  },
  created() {
    this.getList();
    this.getAreaTree()
  },
  methods: {
    /** 查询网关信息列表 */
    getList() {
      this.loading = true;
      listGateway(this.queryParams).then(response => {
        this.gatewayList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询区域下拉树结构 */
    getAreaTree() {
      topologyTreeSelect(this.queryAreaParams).then(response => {
        this.areaOptions = response.data;
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
        name: undefined,
        sn: undefined,
        model: undefined,
        description: undefined,
        timeZone: undefined,
        img:undefined,
        qrCode: undefined,
        factory: undefined,
        area: undefined,
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
      this.title = "添加网关信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getGateway(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改网关信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateGateway(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addGateway(this.form).then(response => {
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
        return delGateway(ids);
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
      this.download('system/gateway/export', {
        ...this.queryParams
      }, `gateway_${new Date().getTime()}.xlsx`)
    },
    // 选择区域
    areaSelect(value) {
      this.form.area = value.label
    }
  }
};
</script>
