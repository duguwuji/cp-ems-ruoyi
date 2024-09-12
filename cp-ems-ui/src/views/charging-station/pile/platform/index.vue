<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="终端编码" prop="encoding">
        <el-input
          v-model="queryParams.encoding"
          placeholder="请输入终端编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="终端名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入终端名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="归属商户" prop="merchantId">
        <el-input
          v-model="queryParams.merchantId"
          placeholder="请输入归属商户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="商户名" prop="merchantName" >
        <el-input
          v-model="queryParams.merchantName"
          placeholder="请输入归属商户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="归属电站" prop="stationId">
        <el-input
          v-model="queryParams.stationId"
          placeholder="请输入归属电站"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="电站名" prop="stationName">
        <el-input
          v-model="queryParams.stationName"
          placeholder="请输入归属电站名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="品牌" prop="brand">
        <!-- <el-input
          v-model="queryParams.brand"
          placeholder="请输入品牌"
          clearable
          @keyup.enter.native="handleQuery"
        /> -->
        <el-select v-model="queryParams.brand" placeholder="请选择品牌" clearable>
          <el-option v-for="brand in brandList"
            :key="brand.id"
            :label="brand.brandName"
            :value="brand.brandName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="型号" prop="model">
        <el-select v-model="queryParams.model" placeholder="请选择型号">
            <el-option
              v-for="model in modelList"
              :key="model.id"
              :label="model.modelName"
              :value="model.modelName"></el-option>
          </el-select>
      </el-form-item>
      <el-form-item label="归属商户" prop="merchantId">
        <el-select
          v-model="queryParams.merchantId"
          placeholder="请选择归属商户"
          clearable
          style="width: 215px"
          :disabled="merchantId"
        >
          <el-option
            v-for="merchant in merchantList"
            :key="merchant.merchantId"
            :label="merchant.name"
            :value="merchant.merchantId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="归属电站" prop="stationId">
        <el-select
          v-model="queryParams.stationId"
          placeholder="请选择归属电站"
          clearable
          style="width: 215px"
        >
          <el-option
            v-for="station in stationList"
            :key="station.stationId"
            :label="station.name"
            :value="station.stationId"
          />
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
          v-hasPermi="['system:pile:add']"
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
          v-hasPermi="['system:pile:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-open"
          size="mini"
          :disabled="multiple"
          @click="handleOpen"
          v-hasPermi="['system:pile:edit']"
        >批量启用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-turn-off"
          size="mini"
          :disabled="multiple"
          @click="handleClose"
          v-hasPermi="['system:pile:edit']"
        >批量停用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:pile:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:pile:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pileList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="充电桩ID" align="center" prop="pileId" v-if="true"/> -->
      <el-table-column label="终端编码" align="center" prop="encoding" />
      <!-- <el-table-column label="终端类型" align="center" prop="type" /> -->
      <el-table-column label="终端名称" align="center" prop="name" />
      <!-- <el-table-column label="归属商户" align="center" prop="merchantId" /> -->
      <el-table-column label="归属商户名" align="center" prop="merchantName" />
      <!-- <el-table-column label="归属电站" align="center" prop="stationId" /> -->
      <el-table-column label="归属电站名称" align="center" prop="stationName" />
      <el-table-column label="品牌" align="center" prop="brand" />
      <el-table-column label="型号" align="center" prop="model" />
      <el-table-column label="电桩状态开关" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
            @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="工作状态" align="center" prop="workStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pile_status" :value="scope.row.workStatus" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:pile:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:pile:remove']"
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

    <!-- 添加或修改充电桩信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="终端编码" prop="encoding">
          <el-input v-model="form.encoding" placeholder="请输入终端编码" />
        </el-form-item>
        <el-form-item label="终端名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入终端名称" />
        </el-form-item>
        <el-form-item label="归属商户" prop="merchantId">
          <!-- <el-input v-model="form.merchantId" placeholder="请输入归属商户" /> -->
          <el-select v-model="form.merchantId" placeholder="请输入归属商户" @change="merchantChange">
            <el-option v-for="item in merchantList" :key="item.merchantId" :label="item.name"
              :value="item.merchantId"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="归属商户名" prop="merchantName">
          <el-input v-model="form.merchantName" placeholder="请输入归属商户名" />
        </el-form-item> -->
        <el-form-item label="归属电站" prop="stationId">
          <el-select v-model="form.stationId" placeholder="请输入归属电站" @change="stationChange">
            <el-option v-for="item in stationList" :key="item.stationId" :label="item.name"
              :value="item.stationId"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="归属电站名称" prop="stationName">
          <el-input v-model="form.stationName" placeholder="请输入归属电站名称" />
        </el-form-item> -->
        <el-form-item label="品牌" prop="brand">
          <!-- <el-input v-model="form.brand" placeholder="请输入品牌" /> -->
          <el-select v-model="form.brand" placeholder="请选择品牌" @change="changeBrand">
            <el-option
              v-for="brand in brandList"
              :key="brand.id"
              :label="brand.brandName"
              :value="brand.brandName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-select v-model="form.model" :placeholder="form.brand ? '请选择型号' : '请先选择品牌'" :disabled="form.brand ? false : true">
            <el-option
              v-for="model in selectModel"
              :key="model.id"
              :label="model.modelName"
              :value="model.modelName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
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
import { listPile, getPile, delPile, addPile, updatePile, openOrClosePile } from "@/api/chargingStation/pile";
import { listStation } from "@/api/chargingStation/station";
import { listMerchant } from "@/api/chargingStation/merchant";
import {listBrand} from "@/api/chargingStation/brand"
import {listModel} from "@/api/chargingStation/model"
export default {
  name: "Pile",
  dicts: ['pile_status'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中电桩
      piles: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示查询条件
      showSearch: true,
      // 总条数
      total: 0,
      // 充电桩信息表格数据
      pileList: [],
      // 充电站信息表格数据
      stationList: [],
      // 商户信息表格数据
      merchantList: [],
      //页面跳转商户id
      merchantId: undefined,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        encoding: undefined,
        type: undefined,
        name: undefined,
        merchantId: undefined,
        merchantName: undefined,
        stationId: undefined,
        stationName: undefined,
        brand: undefined,
        model: undefined,
        status: undefined,
        workStatus: undefined,
      },
      queryStationParams: {
        type:'0',
      },
      queryMerchantParams: {
        type:'0',
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        pileId: [
          { required: true, message: "充电桩ID不能为空", trigger: "blur" }
        ],
        encoding: [
          { required: true, message: "终端编码不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "终端类型不能为空", trigger: "change" }
        ],
        name: [
          { required: true, message: "终端名称不能为空", trigger: "blur" }
        ],
        merchantId: [
          { required: true, message: "归属商户不能为空", trigger: "blur" }
        ],
        merchantName: [
          { required: true, message: "归属商户名不能为空", trigger: "blur" }
        ],
        stationId: [
          { required: true, message: "归属电站不能为空", trigger: "blur" }
        ],
        stationName: [
          { required: true, message: "归属电站名称不能为空", trigger: "blur" }
        ],
        brand: [
          { required: true, message: "品牌不能为空", trigger: "blur" }
        ],
        model: [
          { required: true, message: "型号不能为空", trigger: "blur" }
        ],
        /* status: [
          { required: true, message: "电桩状态不能为空", trigger: "change" }
        ],
        workStatus: [
          { required: true, message: "工作状态不能为空", trigger: "change" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ] */
      },
      brandList: [],
      modelList: [], // 所有型号列表
      selectModel: [], // 选中品牌的型号列表
    };
  },
  created() {
    this.merchantId = this.$route.params && this.$route.params.merchantId;
    if(this.merchantId){
      this.queryParams.merchantId = this.merchantId;
    }
    this.getBrandList()
    this.getModelList()
    this.getList();
    this.getStationList();
    this.getMerchantList();
  },
  methods: {
    // 获取型号列表
    getModelList() {
      listModel({status: '0'}).then(res => {
        this.modelList = res.rows
      })
    },
    // 获取品牌列表
    getBrandList() {
      listBrand({status: '0'}).then(res => {
        this.brandList = res.rows
      })
    },
    // 电站选择
    stationChange(data) {
      var station = this.stationList.find(u => u.stationId == data)
      if (station) {
        this.form.stationName = station.name
      }
    },
    // 商户选择
    merchantChange(data) {
      var merchant = this.merchantList.find(u => u.merchantId == data)
      if (merchant) {
        this.form.merchantName = merchant.name
      }
    },
    
    /** 查询充电桩信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.type = '0';
      listPile(this.queryParams).then(response => {
        this.pileList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询充电站信息列表 */
    getStationList() {
      this.loading = true;
      listStation(this.queryStationParams).then(response => {
        this.stationList = response.rows;
        this.loading = false;
      });
    },
    /** 查询商户信息列表 */
    getMerchantList() {
      this.loading = true;
      listMerchant(this.queryMerchantParams).then(response => {
        this.merchantList = response.rows;
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
        pileId: undefined,
        encoding: undefined,
        type: undefined,
        name: undefined,
        merchantId: undefined,
        merchantName: undefined,
        stationId: undefined,
        stationName: undefined,
        brand: undefined,
        model: undefined,
        status: undefined,
        workStatus: undefined,
        delFlag: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
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
      this.ids = selection.map(item => item.pileId)
      this.piles = selection
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加充电桩信息";
      this.selectModel = []
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const pileId = row.pileId || this.ids
      getPile(pileId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改充电桩信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.pileId != null) {
            updatePile(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            this.form.type = '0';
            addPile(this.form).then(response => {
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
      const pileIds = row.pileId || this.ids;
      this.$modal.confirm('是否确认删除充电桩信息编号为"' + pileIds + '"的数据项？').then(() => {
        this.loading = true;
        return delPile(pileIds);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 批量启用按钮操作 */
    handleOpen() {
      const piles = this.piles
      this.$modal.confirm('是否确认批量启用所选终端').then(() => {
        this.loading = true;
        piles.forEach(element => {
        element.status = '0'
      });
        return openOrClosePile(piles);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("批量启用成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 批量停用按钮操作 */
    handleClose() {
      const piles = this.piles
      this.$modal.confirm('是否确认批量停用所选终端').then(() => {
        this.loading = true;
        piles.forEach(element => {
        element.status = '1'
      });
        return openOrClosePile(piles);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("批量停用成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/pile/export', {
        ...this.queryParams
      }, `pile_${new Date().getTime()}.xlsx`)
    },
    // 电站状态开关状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要' + text + '该充电桩吗？').then(function () {
        return updatePile(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    // 修改品牌
    changeBrand(value) {
      this.form.model = undefined
      let brand = this.brandList.find(b => b.brandName == value)
      this.selectModel = this.modelList.filter(m => m.brandId == brand.id)
    }
  }
};
</script>
