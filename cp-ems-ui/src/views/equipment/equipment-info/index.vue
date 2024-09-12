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
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备SN" prop="sn">
        <el-input
          v-model="queryParams.sn"
          placeholder="请输入设备SN"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备型号" prop="model">
        <el-input
          v-model="queryParams.model"
          placeholder="请输入设备型号"
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
      <el-form-item label="设备类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择">
          <el-option
            v-for="item in dict.type.energy_type"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
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
          v-hasPermi="['system:equipmentInfo:add']"
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
          v-hasPermi="['system:equipmentInfo:edit']"
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
          v-hasPermi="['system:equipmentInfo:remove']"
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
          v-hasPermi="['system:equipmentInfo:export']"
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
      :data="equipmentInfoList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="55" align="center" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="设备SN" align="center" prop="sn" />
      <el-table-column label="设备型号" align="center" prop="model" />
      <el-table-column label="设备类型" align="center" prop="type" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.energy_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="设备描述" align="center" prop="description" />
      <!-- <el-table-column label="设备图片" align="center" prop="img" width="240px">
        <template slot-scope="scope">
          <div style="display: flex;justify-content:space-evenly;">
            <ImagePreview
            v-for="(item, index) in scope.row.imgOss"
            :key="index"
            :width=100 :height=100
            :src="item.url"
            :preview-src-list="[item.url]"/>
          </div>
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="设备二维码" align="center" prop="qrCode" /> -->
      <!-- <el-table-column label="工厂" align="center" prop="factory" /> -->
      <!-- <el-table-column label="区域" align="center" prop="areaName" /> -->
      <!-- <el-table-column label="状态" align="center" prop="status" /> -->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="openDrawer(scope.row)"
            v-hasPermi="['system:equipmentInfo:query']"
            >详情</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:equipmentInfo:edit']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:equipmentInfo:remove']"
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

    <!-- 添加或修改设备信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="设备SN" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入设备SN" />
        </el-form-item>
        <el-form-item label="设备型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入设备型号" />
        </el-form-item>
        <el-form-item label="设备类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option
              v-for="item in dict.type.energy_type"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入设备描述" />
        </el-form-item>
        <el-form-item label="设备图片" prop="img">
          <!-- <el-input v-model="form.img" placeholder="请输入设备图片" /> -->
          <imageUpload v-model="form.img" :limit="2" :values="form.img"/>
        </el-form-item>
        <el-form-item label="设备二维码" prop="qrCode">
          <!-- <el-input v-model="form.qrCode" placeholder="请输入设备二维码" /> -->
          <imageUpload v-model="form.qrCode" :limit="2" :values="form.qrCode"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 设备信息抽屉 -->
    <el-drawer :visible.sync="drawer" :with-header="false" destroy-on-close size="700px">
      <div style="width:100%;height:100%;padding:16px;">
        <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="设备信息" name="first">
          <EquipmentInfoVue :equipmentInfo="form"/>
        </el-tab-pane>
        <el-tab-pane label="设备报警" name="second">
          <EquipmentAlarmVue :equipmentInfo="form"/>
        </el-tab-pane>
        <!-- <el-tab-pane label="设备数据" name="third">设备数据</el-tab-pane>
        <el-tab-pane label="设备资料" name="fourth">设备资料</el-tab-pane> -->
      </el-tabs>
      </div>
    </el-drawer>
  </div>
</template>

  <script>
import {
  listEquipmentInfo,
  getEquipmentInfo,
  delEquipmentInfo,
  addEquipmentInfo,
  updateEquipmentInfo,
} from "@/api/system/equipmentInfo";
import {
  topologyTreeSelect,
} from "@/api/system/itemTopology";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import EquipmentInfoVue from "@/views/equipment/equipment-info/Info.vue"
import EquipmentAlarmVue from "@/views/equipment/equipment-info/Alarm.vue"
export default {
  name: "EquipmentInfo",
  dicts: ['energy_type'],
  components: {
    Treeselect,
    EquipmentInfoVue,
    EquipmentAlarmVue
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
      // 设备信息表格数据
      equipmentInfoList: [],
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
        description: undefined,
        img: undefined,
        qrCode: undefined,
        factory: undefined,
        type: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [{ required: true, message: "不能为空", trigger: "blur" }],
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        sn: [{ required: true, message: "设备SN不能为空", trigger: "blur" }],
        model: [
          { required: true, message: "设备型号不能为空", trigger: "blur" },
        ],
        /* description: [
          { required: true, message: "设备描述不能为空", trigger: "blur" },
        ], */
        /* img: [{ required: true, message: "设备图片不能为空", trigger: "blur" }],
        qrCode: [
          { required: true, message: "设备二维码不能为空", trigger: "blur" },
        ], */
        // factory: [{ required: true, message: "工厂不能为空", trigger: "blur" }],
        type: [{ required: true, message: "设备类型不能为空", trigger: "blur" }],
      },
      areaList: [],
      drawer: false,
      activeName: 'first',
    };
  },
  created() {
    this.getList();
    this.getAreaList()
  },
  methods: {
    // 获取区域拓扑
    getAreaList() {
      var that = this
      topologyTreeSelect().then(res => {
        that.areaList = res.data
      })
    },
    // 区域选择
    areaChange(value) {
      console.log(value);
    },
    /** 查询设备信息列表 */
    getList() {
      this.loading = true;
      listEquipmentInfo(this.queryParams).then((response) => {
        this.equipmentInfoList = response.rows;
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
        name: undefined,
        sn: undefined,
        model: undefined,
        description: undefined,
        img: undefined,
        qrCode: undefined,
        factory: undefined,
        type: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
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
      this.title = "添加设备信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getEquipmentInfo(id).then((response) => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改设备信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateEquipmentInfo(this.form)
              .then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          } else {
            addEquipmentInfo(this.form)
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
        .confirm('是否确认删除设备信息编号为"' + ids + '"的数据项？')
        .then(() => {
          this.loading = true;
          return delEquipmentInfo(ids);
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
        "system/equipmentInfo/export",
        {
          ...this.queryParams,
        },
        `equipmentInfo_${new Date().getTime()}.xlsx`
      );
    },
    // 详情按钮操作
    openDrawer(row) {
      this.activeName = 'first'
      this.drawer = true
      this.form = row
    },
    handleClick(tab, event) {
      // console.log(tab, event);
    }
  },
};
</script>
