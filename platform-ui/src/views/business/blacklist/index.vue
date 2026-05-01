<template>
  <div class="app-container">
    <!-- 表单组件 -->
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="150px">
      <!-- 车牌号 -->
      <el-form-item label="车牌号" prop="carnum" label-width="80px">
        <el-input
          v-model="queryParams.carnum"
          placeholder=""
          style="width: 180px"
          clearable
          maxlength="20"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- 车主姓名 -->
      <el-form-item label="车主姓名" prop="carname" label-width="80px">
        <el-input
          v-model="queryParams.carname"
          placeholder=""
          style="width: 180px"
          clearable
          maxlength="30"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- 联系电话 -->
      <el-form-item label="联系电话" prop="linkphone" label-width="80px">
        <el-input
          v-model="queryParams.linkphone"
          placeholder=""
          style="width: 180px"
          clearable
          maxlength="15"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type" label-width="80px">
        <el-select v-model="queryParams.type" placeholder="--请选择类型--" clearable>
          <el-option
            v-for="dict in dict.type.blacklist_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status" label-width="80px">
        <el-select v-model="queryParams.status" placeholder="--请选择状态--" clearable>
          <el-option
            v-for="dict in dict.type.blacklist_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <!-- 操作按钮 -->
      <el-form-item label-width="80px">
        <el-row>
          <el-col :span="24" style="text-align: center;">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>

    <!-- 功能按钮区 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:blacklist:add']"
        >录入</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:blacklist:edit']"-->
<!--        >编辑</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="mini"
          icon="el-icon-search"
          @click="handleViewDetail"
          v-hasPermi="['system:blacklist:query']"
        >详情</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-dialog title="选择导出列(如有无需导出的列，点击进行勾选)" :visible.sync="exportColumnDialogVisible">
          <el-checkbox-group v-model="exportColumns">
            <el-checkbox v-for="column in tableColumns" :key="column.prop" :label="column.prop" style="width: 120px;">
              {{ column.label }}
            </el-checkbox>
          </el-checkbox-group>
          <div slot="footer" class="dialog-footer">
            <el-button @click="exportColumnDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmExportColumns">确定</el-button>
          </div>
        </el-dialog>
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="openExportColumnDialog"
          v-hasPermi="['system:blacklist:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns.sync="columns"></right-toolbar>
    </el-row>

    <!-- 表格区域 - ✅ 关键修改：添加 width="100%" 和 style="width: 100%" -->
    <el-table
      v-loading="loading"
      :data="blacklistList"
      @selection-change="handleSelectionChange"
      ref="blacklistTable"
      :key="tableKey"
      size="small"
      :row-style="{ height: '40px' }"
      :cell-style="{ padding: '6px 0' }"
      style="width: 100%"
      :fit="true"
    >
    <el-table-column type="selection" width="55" align="center" />

    <el-table-column label="车牌号" align="center" prop="carnum"  min-width="120"  show-overflow-tooltip v-if="columns[0].visible" />
    <el-table-column label="车主姓名" align="center" prop="carname" min-width="120" show-overflow-tooltip v-if="columns[1].visible" />
    <el-table-column label="联系电话" align="center" prop="linkphone" min-width="120" show-overflow-tooltip v-if="columns[2].visible" />

    <el-table-column label="类型" align="center" prop="type" min-width="120" show-overflow-tooltip v-if="columns[3].visible">
      <template slot-scope="scope">
        <dict-tag :options="dict.type.blacklist_type" :value="scope.row.type" />
      </template>
    </el-table-column>

    <el-table-column label="状态" align="center" prop="status"  min-width="100"  show-overflow-tooltip v-if="columns[5].visible">
      <template slot-scope="scope">
        <dict-tag :options="dict.type.blacklist_status" :value="scope.row.status" />
      </template>
    </el-table-column>

    <el-table-column label="备注" align="center" prop="remarks"  min-width="180"  show-overflow-tooltip v-if="columns[4].visible">
      <template slot-scope="scope">
        <el-tooltip effect="dark" :content="scope.row.remarks || ''" placement="top">
          <span>{{ scope.row.remarks ? scope.row.remarks.substring(0, 10) + '...' : '' }}</span>
        </el-tooltip>
      </template>
    </el-table-column>

    <el-table-column label="操作" align="center" class-name="small-padding fixed-width"  min-width="120" >


      <template slot-scope="scope">
        <el-button
          size="mini"
          type="text"
          icon="el-icon-search"
          @click="getOneNew(scope.row)"
          v-hasPermi="['system:blacklist:query']"
        >详情</el-button>
        <el-button
          size="mini"
          type="text"
          icon="el-icon-edit"
          @click="handleUpdate(scope.row)"
          v-hasPermi="['system:blacklist:edit']"
        >修改</el-button>
        <el-button
          size="mini"
          type="text"
          icon="el-icon-delete"
          @click="handleDelete(scope.row)"
          v-hasPermi="['system:blacklist:remove']"
        >删除</el-button>
      </template>
    </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      :page-sizes="[20, 50, 100, 150]"
      @pagination="getList"
    />

    <!-- 添加或修改Blacklist对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body >
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="车牌号" prop="carnum">
              <el-input v-model="form.carnum" placeholder="请输入车牌号" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="carname">
              <el-input v-model="form.carname" placeholder="请输入车主姓名" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="linkphone">
              <el-input v-model="form.linkphone" placeholder="请输入联系电话" maxlength="15" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择类型" clearable style="width: 100%;">
                <el-option v-for="dict in dict.type.blacklist_type||[]" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 100%;">
                <el-option v-for="dict in dict.type.blacklist_status||[]" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
<!--//这个调整这个一行占了多少个列-->
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" type="textarea" placeholder="请输入备注" maxlength="50" :rows="3"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- Blacklist详情对话框 -->
    <el-dialog :title="title" :visible.sync="open1" width="800px" append-to-body>
      <el-form ref="form1" :model="form1" label-width="150px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="车牌号" prop="carnum">
              <el-input v-model="form1.carnum" placeholder="请输入车牌号" maxlength="20" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="车主姓名" prop="carname">
              <el-input v-model="form1.carname" placeholder="请输入车主姓名" maxlength="30" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系电话" prop="linkphone">
              <el-input v-model="form1.linkphone" placeholder="请输入联系电话" maxlength="15" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类型" prop="type">
              <el-input v-model="form1.typeLabel" placeholder="无" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-input v-model="form1.statusLabel" placeholder="无" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input type = 'textarea' v-model="form1.remarks" placeholder="请输入备注" maxlength="50" disabled />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listBlacklist, getBlacklist, delBlacklist, addBlacklist, updateBlacklist } from "@/api/business/blacklist";

export default {
  name: "Blacklist",
  dicts: ['blacklist_type', 'blacklist_status'],
  data() {
    return {

      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      blacklistList: [],
      tableKey: Date.now(),
      title: "",
      open: false,
      open1: false,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        carnum: null,
        carname: null,
        linkphone: null,
        type: null,
        remarks: null,
        status: null,
        parkid: null,
        startdate: null,
        enddate: null
      },
      form: {
        id: null,
        carnum: null,
        carname: null,
        linkphone: null,
        type: null,
        remarks: null,
        status: null,
        parkid: null,
        startdate: null,
        enddate: null
      },
      form1: {
        id: null,
        carnum: null,
        carname: null,
        linkphone: null,
        type: null,
        typeLabel: null,
        remarks: null,
        status: null,
        statusLabel: null,
        parkid: null,
        startdate: null,
        startdateFormat: null,
        enddate: null,
        enddateFormat: null
      },
      exportColumnDialogVisible: false,
      exportColumns: [],
      tableColumns: [
        { key: 0, label: "车牌号", prop: "carnum", visible: true },
        { key: 1, label: "车主姓名", prop: "carname", visible: true },
        { key: 2, label: "联系电话", prop: "linkphone", visible: true },
        { key: 3, label: "类型", prop: "type", visible: true },
        { key: 4, label: "备注", prop: "remarks", visible: true },
        { key: 5, label: "状态", prop: "status", visible: true },
        // { key: 6, label: "停车场ID", prop: "parkid", visible: true },
        // { key: 7, label: "生效开始日期", prop: "startdate", visible: true },
        // { key: 8, label: "生效结束日期", prop: "enddate", visible: true }
      ],
      columns: [
        { key: 0, label: "车牌号", visible: true },
        { key: 1, label: "车主姓名", visible: true },
        { key: 2, label: "联系电话", visible: true },
        { key: 3, label: "类型", visible: true },
        { key: 4, label: "备注", visible: true },
        { key: 5, label: "状态", visible: true },
        { key: 6, label: "停车场ID", visible: true },
        { key: 7, label: "生效开始日期", visible: true },
        { key: 8, label: "生效结束日期", visible: true }
      ],
      rules: {
        carnum: [{ required: true, message: "车牌号不能为空", trigger: "blur" }],
        carname: [{ required: true, message: "车主姓名不能为空", trigger: "blur" }],
        linkphone: [{ required: true, message: "联系电话不能为空", trigger: "blur" }],
        type: [{ required: true, message: "请选择类型", trigger: "change" }],
        status: [{ required: true, message: "请选择状态", trigger: "change" }],
        // startdate: [{ required: true, message: "请选择生效开始日期", trigger: "change" }],
        // enddate: [{ required: true, message: "请选择生效结束日期", trigger: "change" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listBlacklist(this.queryParams).then(response => {
        this.blacklistList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.tableKey = Date.now();
      }).catch(err => {
        this.loading = false;
        this.$message.error("查询黑名单列表失败：" + err.message);
      });
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    reset() {
      this.form = {
        id: null,
        carnum: null,
        carname: null,
        linkphone: null,
        type: null,
        remarks: null,
        status: null,
        parkid: null,
        startdate: null,
        enddate: null
      };
      this.$refs["form"] && this.$refs["form"].resetFields();
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    resetQuery() {
      this.$refs["queryForm"] && this.$refs["queryForm"].resetFields();
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        carnum: null,
        carname: null,
        linkphone: null,
        type: null,
        remarks: null,
        status: null,
        parkid: null,
        startdate: null,
        enddate: null
      };
      this.handleQuery();
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "录入";
    },
    getOneNew(row) {
      this.reset();
      this.form1 = { ...row };

      const getDictLabel = (dictArray, value) => {
        if (!dictArray || value == null) return "无";
        const target = String(value); // 转字符串
        const item = dictArray.find(d => String(d.value) === target);
        return item ? item.label : "无";
      };

      this.form1.typeLabel = getDictLabel(this.dict.type.blacklist_type, this.form1.type);
      this.form1.statusLabel = getDictLabel(this.dict.type.blacklist_status, this.form1.status);

      this.open1 = true;
      this.title = "查看";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
        this.form = JSON.parse(JSON.stringify(row));
      // 👇 关键：将 status 转为字符串，确保和字典 value 类型一致
      if (this.form.status != null) {
        this.form.status = String(this.form.status);
      }
      if (this.form.type != null) {
        this.form.type = String(this.form.type);
      }
        this.open = true;
        this.title = "修改";
    },
    handleViewDetail() {
      if (this.ids.length !== 1) {
        this.$message.warning("请选择一条数据查看");
        return;
      }
      // 创建一个模拟的 row 对象
      const selectedRow = { id: this.ids[0] };
      this.getOneNew(selectedRow);
    },


    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBlacklist(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(err => {
              this.$message.error("修改失败：" + err.message);
            });
          } else {
            addBlacklist(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).catch(err => {
              this.$message.error("新增失败：" + err.message);
            });
          }
        }
      });
    },

    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除黑名单编号为"' + ids + '"的数据项？').then(() => {
        return delBlacklist(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
        this.ids = [];
        this.single = true;
        this.multiple = true;
      }).catch(() => {});
    },

    openExportColumnDialog() {
      this.exportColumnDialogVisible = true;
      this.exportColumns = this.tableColumns.filter(column => column.visible).map(column => column.prop);
    },

    confirmExportColumns() {
      this.exportColumnDialogVisible = false;
      if (this.exportColumns.length === 0) {
        this.$message.warning("请至少选择一列导出");
        return;
      }
      const params = {
        ...this.queryParams,
        exportColumns: this.exportColumns.join(",")
      };
      this.download('business/blacklist/export', params, `黑名单信息统计表_${new Date().getTime()}.xlsx`);
    },

    parseTime(time, pattern) {
      if (!time) return "";
      const date = new Date();
      if (typeof time === "string" && time.length === 8) {
        const year = time.substring(0, 4);
        const month = time.substring(4, 6) - 1;
        const day = time.substring(6, 8);
        date.setFullYear(year, month, day);
      } else {
        date.setTime(time);
      }
      let format = pattern || "{y}-{m}-{d} {h}:{i}:{s}";
      const o = {
        "y+": date.getFullYear(),
        "m+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "i+": date.getMinutes(),
        "s+": date.getSeconds()
      };
      for (const k in o) {
        const match = new RegExp("(" + k + ")").exec(format);
        if (match) {
          format = format.replace(match[1], match[1].length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
      }
      return format;
    }
  }
};
</script>
