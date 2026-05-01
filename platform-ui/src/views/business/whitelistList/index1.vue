<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="150px">
      <el-form-item label="停车场" prop="parkid" label-width="80px">
        <el-select v-model="queryParams.parkid" placeholder="--请选择--" clearable style="width: 150px">
          <el-option
            v-for="item in parkOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车牌号" prop="carnum" label-width="80px">
        <el-input
          v-model="queryParams.carnum"
          placeholder="请输入车牌号"
          style="width: 150px"
          clearable
          maxlength="20"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车主姓名" prop="carname" label-width="80px">
        <el-input
          v-model="queryParams.carname"
          placeholder="请输入车主姓名"
          style="width: 150px"
          clearable
          maxlength="30"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="linkphone" label-width="80px">
        <el-input
          v-model="queryParams.linkphone"
          placeholder="请输入联系电话"
          style="width: 160px"
          clearable
          maxlength="15"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type" label-width="80px">
        <el-select v-model="queryParams.type" placeholder="--请选择类型--" clearable style="width: 130px">
          <el-option
            v-for="dict in dict.type.whitelist_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status" label-width="80px">
        <el-select v-model="queryParams.status" placeholder="--请选择状态--" clearable style="width: 130px">
          <el-option
            v-for="dict in dict.type.blacklist_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-row>
          <el-col :span="24" style="text-align: center;">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table
      v-loading="loading"
      :data="whitelistList"
      @selection-change="handleSelectionChange"
      ref="whitelistTable"
      :key="tableKey"
      size="small"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="停车场" align="center" prop="parkName" min-width="150" show-overflow-tooltip v-if="columns[6].visible" />
      <el-table-column label="车牌号" align="center" prop="carnum" min-width="120" show-overflow-tooltip v-if="columns[0].visible" />
      <el-table-column label="车主姓名" align="center" prop="carname" min-width="120" show-overflow-tooltip v-if="columns[1].visible" />
      <el-table-column label="联系电话" align="center" prop="linkphone" min-width="120" show-overflow-tooltip v-if="columns[2].visible" />
      <el-table-column label="类型" align="center" prop="type" min-width="120" show-overflow-tooltip v-if="columns[3].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.whitelist_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" min-width="120" show-overflow-tooltip v-if="columns[4].visible" />
      <el-table-column label="状态" align="center" prop="status" min-width="120" show-overflow-tooltip v-if="columns[5].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.blacklist_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="起始日期" align="center" prop="startdate" min-width="120" show-overflow-tooltip v-if="columns[7].visible" />
      <el-table-column label="结束日期" align="center" prop="enddate" min-width="120" show-overflow-tooltip v-if="columns[8].visible" />
      <el-table-column label="操作" align="center" fixed="right" min-width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="getOne(scope.row)"
            v-hasPermi="['business:whitelist:query']"
          >详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 详情弹窗 -->
    <el-dialog :title="title" :visible.sync="open1" width="800px" append-to-body>
      <el-form ref="form1" :model="form1" label-width="100px" label-position="right">
        <el-row :gutter="20">
          <!-- 第一行：停车场 & 车牌号 -->
          <el-col :span="12">
            <el-form-item label="停车场" prop="parkid">
              <el-input v-model="form1.parkName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车牌号" prop="carnum">
              <el-input v-model="form1.carnum" disabled />
            </el-form-item>
          </el-col>

          <!-- 第二行：车主姓名 & 联系电话 -->
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="carname">
              <el-input v-model="form1.carname" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="linkphone">
              <el-input v-model="form1.linkphone" disabled />
            </el-form-item>
          </el-col>

          <!-- 第三行：类型 & 状态 -->
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-input v-model="form1.type" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-input v-model="form1.status" disabled />
            </el-form-item>
          </el-col>

          <!-- 第四行：起始日期 & 结束日期 -->
          <el-col :span="12">
            <el-form-item label="起始日期" prop="startdate">
              <el-input v-model="form1.startdate" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="enddate">
              <el-input v-model="form1.enddate" disabled />
            </el-form-item>
          </el-col>

          <!-- 第五行：备注 -->
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input
                v-model="form1.remarks"
                type="textarea"
                :rows="3"
                disabled
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listWhitelist, getWhitelist, listInfo } from "@/api/business/whitelistList";

export default {
  name: "Whitelist",
  dicts: ['whitelist_type', 'blacklist_status'],
  data() {
    return {
      loading: true,
      ids: [],
      showSearch: true,
      total: 0,
      whitelistList: [],
      parkOptions: [],
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
        status: null,
        parkid: null,
      },
      form: {
        id: null,
        parkid: null,
        carnum: null,
        carname: null,
        linkphone: null,
        type: null,
        status: null,
        startdate: null,
        enddate: null,
        remarks: null
      },
      form1: {
        id: null,
        parkid: '',
        carnum: '',
        carname: '',
        linkphone: '',
        type: null,
        status: null,
        startdate: '',
        enddate: '',
        remarks: ''
      },
      columns: [
        { key: 0, label: `车牌号`, visible: true },
        { key: 1, label: `车主姓名`, visible: true },
        { key: 2, label: `联系电话`, visible: true },
        { key: 3, label: `类型`, visible: true },
        { key: 4, label: `备注`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: '停车场', visible: true },
        { key: 7, label: `起始日期`, visible: true },
        { key: 8, label: `结束日期`, visible: true },
      ],
      rules: {
        carnum: [
          { required: true, message: '请输入车牌号', trigger: 'blur' },
          { max: 20, message: '车牌号长度不能超过20个字符', trigger: 'blur' }
        ],
        carname: [
          { required: true, message: '请输入车主姓名', trigger: 'blur' },
          { max: 30, message: '车主姓名长度不能超过30个字符', trigger: 'blur' }
        ],
        linkphone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { max: 15, message: '联系电话长度不能超过15个字符', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ],
        startdate: [
          { required: true, message: '请选择起始日期', trigger: 'change' }
        ],
        enddate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ],
      }
    };
  },
  created() {
    this.getParkSelectList().then(() => {
      this.getList();
    });
  },
  methods: {
    getList() {
      this.loading = true;
      listWhitelist(this.queryParams).then(response => {
        const rows = Array.isArray(response.rows) ? response.rows : [];
        this.whitelistList = rows.map(item => ({
          ...item,
          parkName: this.parkOptions.find(po => po.value === item.parkid?.toString())?.label || '未知停车场'
        }));
        this.total = response.total || rows.length;
        this.loading = false;
      }).catch(() => {
        this.whitelistList = [];
        this.total = 0;
        this.loading = false;
      });
    },
    formatDate(dateStr) {
      if (!dateStr || dateStr.length !== 8) return '';
      const year = dateStr.substr(0, 4);
      const month = dateStr.substr(4, 2);
      const day = dateStr.substr(6, 2);
      return `${year}-${month}-${day}`;
    },
    getParkSelectList() {
      return listInfo({ parkname: '现代农贸城停车场' }).then(response => {
        const parkList = Array.isArray(response.rows) ? response.rows : [];
        this.parkOptions = parkList.map(item => ({
          label: item.parkname,
          value: item.parkid?.toString()
        }));
      }).catch(() => {
        this.parkOptions = [];
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
        status: null,
        startdate: null,
        enddate: null,
        remarks: null,
        parkid: ''
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加白名单";
    },
    getOne(row) {
      console.log('传入的row:', row); // 检查传入的参数
      this.open1=false;
      this.form1 = {
        id: null,
        parkid: '',
        carnum: '',
        carname: '',
        linkphone: '',
        type: null,
        status: null,
        startdate: '',
        enddate: '',
        remarks: '',
        parkName: ''
      };
      getWhitelist(row.id).then(response => {
        const data = response.data;

        if (!data) {
          this.$modal.msgError("未找到该记录");
          return;
        }

        Object.assign(this.form1, data);

        // 处理空值
        Object.keys(this.form1).forEach(key => {
          if (this.form1[key] === null || this.form1[key] === undefined) {
            this.form1[key] = '';
          }
        });

        // 🚀 关键：将 type 和 status 的数字转为中文标签
        const typeLabel = this.dict.type.whitelist_type?.find(item => item.value === this.form1.type)?.label || this.form1.type;
        const statusLabel = this.dict.type.blacklist_status?.find(item => item.value === this.form1.status)?.label || this.form1.status;

        this.form1.type = typeLabel;
        this.form1.status = statusLabel;

        // 🔍 查找停车场名称（通过 parkOptions 映射）
        const parkOption = this.parkOptions.find(po => po.value === this.form1.parkid?.toString());
        this.form1.parkName = parkOption ? parkOption.label : '未知停车场';

        // 格式化日期
        if (this.form1.startdate && typeof this.form1.startdate === 'string') {
          this.form1.startdate = this.formatDate(this.form1.startdate);
        }
        if (this.form1.enddate && typeof this.form1.enddate === 'string') {
          this.form1.enddate = this.formatDate(this.form1.enddate);
        }

        this.$nextTick(() => {
          this.open1 = true;
          this.title = "白名单详情";
        });
      }).catch(err => {
        console.error('获取详情失败：', err);
        this.$modal.msgError('获取详情失败，请重试');
      });
    },
    handleUpdate(row) {
      const id = row.id || this.ids;
      getWhitelist(id).then(response => {
        this.form = response.data;
        this.form.parkid = this.form.parkid?.toString() || '';
        this.open = true;
        this.title = "修改白名单";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWhitelist(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWhitelist(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
  }
};
</script>
