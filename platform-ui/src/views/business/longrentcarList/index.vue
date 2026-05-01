<template>
  <div class="app-container">
    <!-- 搜索表单：车牌号 + 独立的生效日期和失效日期选择器 -->
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" label-width="80px">
      <el-form-item label="车牌号" prop="CARNUM">
        <el-input
          v-model="queryParams.CARNUM"
          placeholder="请输入车牌号"
          style="width: 160px"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生效日期" prop="beginDate">
        <el-date-picker
          v-model="queryParams.beginDate"
          type="date"
          placeholder="选择开始日期"
          format="yyyy-MM-dd"
          value-format="yyyyMMdd"
          style="width: 160px"
          clearable
        />
      </el-form-item>
      <el-form-item label="失效日期" prop="endDate">
        <el-date-picker
          v-model="queryParams.endDate"
          type="date"
          placeholder="选择结束日期"
          format="yyyy-MM-dd"
          value-format="yyyyMMdd"
          style="width: 160px"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 主表列表（保持原有展示） -->
    <el-table v-loading="loading" :data="longrentcarList" size="mini" @selection-change="handleMainSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车主姓名" align="center" prop="carname" min-width="120" show-overflow-tooltip />
      <el-table-column label="车牌号" align="center" min-width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.longcarGroups && scope.row.longcarGroups.length">
            {{ scope.row.longcarGroups.map(g => g.CARNUM).join('，') }}
          </span>
          <span v-else style="color: #999;">无</span>
        </template>
      </el-table-column>
      <el-table-column label="电话号码" align="center" prop="telnum" min-width="120" show-overflow-tooltip />
      <el-table-column label="生效日期" align="center" prop="beginDate" min-width="120" />
      <el-table-column label="失效日期" align="center" prop="endDate" min-width="120" />
      <el-table-column label="备注" align="center" prop="remark" min-width="120" show-overflow-tooltip />
      <el-table-column label="操作" align="center" fixed="right" min-width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="getOne(scope.row)"
            v-hasPermi="['business:longrentcar:query']"
          >详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      :page-sizes="[20, 50, 100]"
      @pagination="getList"
    />

    <!-- 详情弹窗（保持原有功能） -->
    <el-dialog :title="title" :visible.sync="open1" width="800px" append-to-body>
      <el-form ref="detailForm" :model="form1" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="停车场" prop="parkName">
              <el-input v-model="form1.parkName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="carname">
              <el-input v-model="form1.carname" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="beginDate">
              <el-input v-model="form1.beginDate" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="endDate">
              <el-input v-model="form1.endDate" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话号码" prop="telnum">
              <el-input v-model="form1.telnum" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form1.remark" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row style="margin-top: 20px;">
          <el-col :span="24">
            <div style="border-top: 1px solid #e6e6e6; padding-top: 10px; margin-bottom: 10px;">
              <h4 style="margin: 0; display: inline-block;">车牌明细</h4>
            </div>
            <el-table :data="form1.longcarGroups || []" size="mini" border style="width: 100%">
              <el-table-column label="车牌号" min-width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.CARNUM" disabled size="mini" />
                </template>
              </el-table-column>
              <el-table-column label="状态" min-width="120">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.STATUS" disabled size="mini">
                    <el-option label="有效" value="1" />
                    <el-option label="无效" value="0" />
                  </el-select>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listLongrentcar, getLongrentcar } from "@/api/business/longrentcar";
import { listInfo } from "@/api/business/whitelist";

export default {
  name: "Longrentcar",
  data() {
    return {
      loading: true,
      mainIds: [], // 主表选中ID
      multiple: true, // 控制详情按钮状态
      total: 0,
      longrentcarList: [],
      parkOptions: [], // 停车场下拉数据
      title: "",
      open1: false, // 详情弹窗开关
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        CARNUM: null, // 车牌号（精确搜索）
        beginDate: null, // 生效日期（范围开始）
        endDate: null    // 失效日期（范围结束）
      },
      // 详情表单（保持原有字段）
      form1: {
        id: null,
        parkName: '',
        carname: null,
        beginDate: null,
        endDate: null,
        telnum: null,
        remark: null,
        longcarGroups: [] // 车牌明细
      }
    };
  },
  created() {
    // 初始化加载停车场和列表数据
    this.getParkSelectList().then(() => this.getList());
  },
  methods: {
    // 1. 查询列表（范围搜索逻辑不变）
    getList() {
      this.loading = true;
      // 直接使用独立的beginDate和endDate参数
      listLongrentcar(this.queryParams).then(response => {
        this.longrentcarList = response.rows.map(item => ({
          ...item,
          // 匹配停车场名称
          parkName: this.parkOptions.find(po => po.value === item.parkid?.toString())?.label || '未知停车场',
          // 处理车牌明细
          longcarGroups: (item.longcarGroups || []).map(sub => ({
            ID: sub.ID,
            LONGID: sub.LONGID,
            CARNUM: sub.CARNUM,
            STATUS: sub.STATUS
          }))
        }));
        this.total = response.total;
        this.loading = false;
      }).catch(error => {
        console.error("加载列表失败：", error);
        this.loading = false;
        this.$modal.msgError("加载数据失败，请重试");
      });
    },

    // 2. 获取停车场列表
    getParkSelectList() {
      return listInfo({ parkname: "现代农贸城停车场" }).then(response => {
        this.parkOptions = (response.rows || []).map(item => ({
          label: item.parkname,
          value: item.parkid?.toString()
        }));
      }).catch(() => {
        this.parkOptions = [];
        this.$message.error("加载停车场数据失败");
      });
    },

    // 3. 查看详情
    getOne(row) {
      const id = row ? row.id : this.mainIds[0];
      if (!id) {
        this.$modal.msgWarning("请选择需要查看的数据");
        return;
      }

      getLongrentcar(id).then(response => {
        const data = response.data;
        if (!data) {
          this.$modal.msgError("接口返回数据为空，无法加载详情");
          return;
        }

        // 匹配停车场名称
        const parkIdStr = data.parkid?.toString();
        const parkName = this.parkOptions.find(po => po.value === parkIdStr)?.label || "未知停车场";

        this.form1 = {
          ...data,
          parkName,
          longcarGroups: data.longcarGroups || []
        };

        this.open1 = true;
        this.title = "长租车辆详情";
      }).catch(error => {
        console.error("获取详情失败：", error);
        this.$modal.msgError("加载详情失败，请重试");
      });
    },

    // 4. 搜索
    handleQuery() {
      this.queryParams.pageNum = 1;
      // 可选：添加日期合法性校验（开始日期不能晚于结束日期）
      if (this.queryParams.beginDate && this.queryParams.endDate && this.queryParams.beginDate > this.queryParams.endDate) {
        this.$modal.msgWarning("生效日期不能晚于失效日期");
        return;
      }
      this.getList();
    },

    // 5. 重置搜索
    resetQuery() {
      this.$refs.queryForm.resetFields();
      // 清空日期参数
      this.queryParams.beginDate = null;
      this.queryParams.endDate = null;
      this.handleQuery();
    },

    // 6. 主表选择变化（控制详情按钮）
    handleMainSelectionChange(selection) {
      this.mainIds = selection.map(item => item.id);
      this.multiple = !selection.length; // 无选中时禁用详情按钮
    }
  }
};
</script>

<style scoped>
.detail-form .el-form-item { margin-bottom: 10px; }
</style>
