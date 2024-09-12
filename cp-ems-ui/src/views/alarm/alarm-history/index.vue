<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="参数名称" prop="paramName">
                <el-select v-model="queryParams.paramName" placeholder="请选择参数名称" clearable
                    @keyup.enter.native="handleQuery">
                    <el-option v-for="dict in dict.type.electric_type" :key="dict.value" :label="dict.label"
                        :value="dict.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <!-- <el-form-item label="报警时间" prop="alarmTime">
                <el-date-picker clearable v-model="queryParams.alarmTime" type="date" value-format="yyyy-MM-dd"
                    placeholder="请选择报警时间">
                </el-date-picker>
            </el-form-item> -->
            <!-- <el-form-item label="报警信息" prop="alarmInfo">
                <el-input v-model="queryParams.alarmInfo" placeholder="请输入报警信息" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item> -->
            <el-form-item label="报警等级" prop="alarmLevel">
                    <el-select v-model="queryParams.alarmLevel" placeholder="请选择报警等级">
                        <el-option
                        v-for="dict in dict.type.alarm_level"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value">
                        </el-option>
                    </el-select>
            </el-form-item>
            <el-form-item label="报警时间">
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
            <!-- <el-form-item label="报警区域" prop="area">
                <el-input v-model="queryParams.area" placeholder="请输入报警区域" clearable @keyup.enter.native="handleQuery" />
            </el-form-item> -->
            <!-- <el-form-item label="报警设备" prop="equipment">
                <el-input v-model="queryParams.equipment" placeholder="请输入报警设备" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item> -->
            <!-- <el-form-item label="报警值" prop="alarmVal">
                <el-input v-model="queryParams.alarmVal" placeholder="请输入报警值" clearable @keyup.enter.native="handleQuery" />
            </el-form-item> -->
            <!-- <el-form-item label="结束时间" prop="endTime">
                <el-date-picker clearable v-model="queryParams.endTime" type="date" value-format="yyyy-MM-dd"
                    placeholder="请选择结束时间">
                </el-date-picker>
            </el-form-item> -->
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['system:history:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['system:history:edit']">编辑</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['system:history:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['system:history:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="historyList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column type="index" width="55" align="center" />
            <el-table-column label="参数名称" align="center" prop="paramName" width="100">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.electric_type" :value="scope.row.paramName" />
                </template>
            </el-table-column>
            <el-table-column label="报警时间" align="center" prop="alarmTime" width="180">
                <!-- <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.alarmTime, '{y}-{m}-{d}') }}</span>
                </template> -->
            </el-table-column>
            <el-table-column label="报警信息" align="center" prop="alarmInfo" />
            <el-table-column label="报警等级" align="center" prop="alarmLevel" >
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.alarm_level" :value="scope.row.alarmLevel"/>
                </template>
            </el-table-column>
            <el-table-column label="报警区域" align="center" prop="area" />
            <el-table-column label="报警设备" align="center" prop="equipment" />
            <el-table-column label="报警值" align="center" prop="alarmVal" />
            <el-table-column label="结束时间" align="center" prop="endTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['system:history:edit']">编辑</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['system:history:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改实时报警对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="参数名称" prop="paramName">
                    <!-- <el-input v-model="form.paramName" placeholder="请输入参数名称" /> -->
                    <el-select v-model="form.paramName" placeholder="请选择参数名称" clearable @keyup.enter.native="handleQuery">
                        <el-option v-for="dict in dict.type.electric_type" :key="dict.value" :label="dict.label"
                            :value="dict.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="报警时间" prop="alarmTime">
                    <el-date-picker clearable v-model="form.alarmTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="请选择报警时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="报警信息" prop="alarmInfo">
                    <el-input v-model="form.alarmInfo" placeholder="请输入报警信息" />
                </el-form-item>
                <el-form-item label="报警等级" prop="alarmLevel">
                    <el-select v-model="form.alarmLevel" placeholder="请选择报警等级">
                        <el-option
                        v-for="dict in dict.type.alarm_level"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="报警区域" prop="area">
                    <el-input v-model="form.area" placeholder="请输入报警区域" />
                </el-form-item>
                <el-form-item label="报警设备" prop="equipment">
                    <el-input v-model="form.equipment" placeholder="请输入报警设备" />
                </el-form-item>
                <el-form-item label="报警值" prop="alarmVal">
                    <el-input-number v-model="form.alarmVal" controls-position="right" :min="0" placeholder="请输入报警值" />
                </el-form-item>
                <el-form-item label="结束时间" prop="endTime">
                    <el-date-picker clearable v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="请选择结束时间">
                    </el-date-picker>
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
import { listHistory, getHistory, delHistory, addHistory, updateHistory } from "@/api/system/alarmHistory";

export default {
    dicts: ['alarm_level','electric_type'],
    name: "History",
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
            // 实时报警表格数据
            historyList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                paramName: undefined,
                alarmTime: undefined,
                // alarmInfo: undefined,
                alarmLevel: undefined,
                // area: undefined,
                // equipment: undefined,
                // alarmVal: undefined,
                endTime: undefined,
            },
            // 日期范围
            dateRange: [],
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                id: [
                    { required: true, message: "不能为空", trigger: "blur" }
                ],
                paramName: [
                    { required: true, message: "参数名称不能为空", trigger: "blur" }
                ],
                alarmTime: [
                    { required: true, message: "报警时间不能为空", trigger: "blur" }
                ],
                alarmInfo: [
                    { required: true, message: "报警信息不能为空", trigger: "blur" }
                ],
                alarmLevel: [
                    { required: true, message: "报警等级不能为空", trigger: "blur" }
                ],
                area: [
                    { required: true, message: "报警区域不能为空", trigger: "blur" }
                ],
                equipment: [
                    { required: true, message: "报警设备不能为空", trigger: "blur" }
                ],
                alarmVal: [
                    { required: true, message: "报警值不能为空", trigger: "blur" }
                ],
                endTime: [
                    { required: true, message: "结束时间不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询实时报警列表 */
        getList() {
            this.loading = true;
            listHistory(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                this.historyList = response.rows;
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
                paramName: undefined,
                alarmTime: undefined,
                alarmInfo: undefined,
                alarmLevel: undefined,
                area: undefined,
                equipment: undefined,
                alarmVal: undefined,
                endTime: undefined,
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
            this.ids = selection.map(item => item.id)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加实时报警";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.loading = true;
            this.reset();
            const id = row.id || this.ids
            getHistory(id).then(response => {
                this.loading = false;
                this.form = response.data;
                this.open = true;
                this.title = "修改实时报警";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    this.buttonLoading = true;
                    if (this.form.id != null) {
                        updateHistory(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        }).finally(() => {
                            this.buttonLoading = false;
                        });
                    } else {
                        addHistory(this.form).then(response => {
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
                return delHistory(ids);
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
            this.download('system/history/export', {
                ...this.queryParams
            }, `history_${new Date().getTime()}.xlsx`)
        },
    }
};
</script>
