<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" label-width="80px">
      <el-form-item label="停车场" prop="parkid">
        <el-select v-model="queryParams.parkid" placeholder="--请选择--" clearable style="width: 150px">
          <el-option v-for="item in parkOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="车主姓名" prop="carname">
        <el-input v-model="queryParams.carname" placeholder="请输入车主姓名" style="width: 160px" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="车牌号" prop="CARNUM">
        <el-input v-model="queryParams.CARNUM" placeholder="请输入车牌号" style="width: 160px" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['business:longrentcar:add']">录入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['business:longrentcar:edit']">编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-search" size="mini" :disabled="multiple" @click="getOne" v-hasPermi="['business:longrentcar:query']">查看</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['business:longrentcar:export']">导出Excel</el-button>
      </el-col>
    </el-row>

    <!-- 主表列表（展示车牌号） -->
    <el-table v-loading="loading" :data="longrentcarList" size="mini" @selection-change="handleMainSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="停车场" align="center" prop="parkName" min-width="150" show-overflow-tooltip />
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
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:longrentcar:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:longrentcar:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" :page-sizes="[20, 50, 100]" @pagination="getList" />

    <!-- 新增/修改弹窗（嵌套车牌明细） -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <!-- 基本信息：两列布局 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="停车场" prop="parkid">
              <el-select v-model="form.parkid" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in parkOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="carname">
              <el-input v-model="form.carname" placeholder="请输入车主姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="beginDate">
              <el-date-picker v-model="form.beginDate" type="date" placeholder="选择生效日期" format="yyyy-MM-dd" value-format="yyyyMMdd" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择失效日期" format="yyyy-MM-dd" value-format="yyyyMMdd" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话号码" prop="telnum">
              <el-input v-model="form.telnum" placeholder="请输入电话号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 车牌明细区域 -->
        <el-row style="margin-top: 20px;">
          <el-col :span="24">
            <div style="border-top: 1px solid #e6e6e6; padding-top: 10px; margin-bottom: 10px;">
              <h4 style="margin: 0; display: inline-block;">车牌明细</h4>
              <div style="float: right;">
                <el-button type="primary" size="mini" @click="addGroup">
                  <i class="el-icon-plus"></i> 添加
                </el-button>
                <el-button type="default" size="mini" @click="removeGroup" :disabled="selectedSubRows.length === 0">
                  <i class="el-icon-delete"></i> 删除
                </el-button>
              </div>
            </div>

            <!-- 车牌明细子表 -->
            <el-table :data="form.longcarGroups" size="mini" border style="width: 100%" @selection-change="handleSubSelectionChange">
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="车牌号" prop="CARNUM" min-width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.CARNUM" placeholder="请输入车牌号" size="mini" />
                </template>
              </el-table-column>
              <el-table-column label="状态" min-width="120">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.STATUS" placeholder="请选择" size="mini">
                    <el-option label="有效" value="1" />
                    <el-option label="无效" value="0" />
                  </el-select>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 详情弹窗（与录入弹窗格式一致，只读） -->
    <el-dialog :title="title" :visible.sync="open1" width="800px" append-to-body>
      <el-form ref="detailForm" :model="form1" label-width="120px">
        <!-- 基本信息：两列布局（与录入弹窗完全一致） -->
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

        <!-- 车牌明细区域（与录入弹窗完全一致） -->
        <el-row style="margin-top: 20px;">
          <el-col :span="24">
            <div style="border-top: 1px solid #e6e6e6; padding-top: 10px; margin-bottom: 10px;">
              <h4 style="margin: 0; display: inline-block;">车牌明细</h4>
            </div>

            <!-- 车牌明细子表（只读，无操作按钮） -->
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
import { listLongrentcar, getLongrentcar, delLongrentcar, addLongrentcar, updateLongrentcar, exportLongrentcar } from "@/api/business/longrentcar";
import { listInfo } from "@/api/business/whitelist";
import { listGroup, getGroup, addGroup, updateGroup, delGroup } from "@/api/business/group";

export default {
  name: "Longrentcar",
  data() {
    return {
      loading: true,
      mainIds: [], // 主表选中ID
      single: true, // 主表是否单选（true表示非单选状态，按钮禁用）
      multiple: true, // 主表是否多选（true表示无选中，按钮禁用）
      total: 0,
      longrentcarList: [],
      parkOptions: [],
      title: "",
      open: false, // 新增/修改弹窗开关
      open1: false, // 详情弹窗开关
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        parkid: null,
        carname: null,
        CARNUM: null
      },
      // 主表表单（包含嵌套的车牌明细列表）
      form: {
        id: null,
        parkid: '',
        carname: null,
        beginDate: null,
        endDate: null,
        telnum: null,
        remark: null,
        longcarGroups: [] // 嵌套的车牌明细列表
      },
      form1: {
        id: null,
        parkid: '',
        carname: null,
        beginDate: null,
        endDate: null,
        telnum: null,
        remark: null,
        longcarGroups: [] // 嵌套的车牌明细列表
      }, // 详情表单
      selectedSubRows: [], // 子表选中行
      rules: {
        parkid: [{ required: true, message: "请选择停车场", trigger: "blur" }],
        carname: [{ required: true, message: "请输入车主姓名", trigger: "blur" }],
        beginDate: [{ required: true, message: "请选择生效日期", trigger: "blur" }],
        endDate: [{ required: true, message: "请选择失效日期", trigger: "blur" }]
      }
    };
  },
  created() {
    // 初始化加载停车场列表和主表数据
    this.getParkSelectList().then(() => this.getList());
  },
  methods: {
    // 1. 查询主表列表（包含关联的车牌明细）
    getList() {
      this.loading = true;
      listLongrentcar(this.queryParams).then(response => {
        this.longrentcarList = response.rows.map(item => ({
          ...item,
          parkName: this.parkOptions.find(po => po.value === item.parkid?.toString())?.label || '未知停车场',
          // 显式映射子表字段：将lg_CARNUM转换为CARNUM
          longcarGroups: (item.longcarGroups || []).map(sub => ({
            ID: sub.ID,
            LONGID: sub.LONGID,
            CARNUM: sub.CARNUM, // 关键：将后端的lg_CARNUM映射为前端的CARNUM
            STATUS: sub.STATUS
          }))
        }));
        console.log("第一条数据的子表明细：", this.longrentcarList[0]?.longcarGroups);
        this.total = response.total;
        this.loading = false;
      }).catch(error => {
        console.error("加载主表数据失败：", error);
        this.loading = false;
        this.$modal.msgError("加载数据失败，请重试");
      });
    },

    // 2. 获取停车场下拉列表数据
    getParkSelectList() {
      return listInfo({ parkname: "现代农贸城停车场" }).then(response => {
        this.parkOptions = (response.rows || []).map(item => ({
          label: item.parkname,
          value: item.parkid?.toString()// 确保value为字符串类型，匹配选择器
        }));
      }).catch(() => {
        this.parkOptions = [];
        this.$message.error("加载停车场数据失败");
      });
    },

    // 3. 新增车牌明细行
    addGroup() {
      this.form.longcarGroups.push({
        ID: null, // 子表ID（新增时为null，编辑时回显）
        LONGID: this.form.id, // 关联主表ID
        CARNUM: null, // 车牌号
        STATUS: "1" // 默认有效状态
      });
    },

    // 4. 新增主表数据
    handleAdd() {
      // 重置表单
      this.form = {
        id: null,
        parkid: null,
        carname: null,
        beginDate: null,
        endDate: null,
        telnum: null,
        remark: null,
        longcarGroups: [] // 初始化空的车牌明细
      };
      this.selectedSubRows = []; // 清空子表选中状态
      this.open = true;
      this.title = "录入长租车辆";
    },

    // 5. 编辑主表数据（回显主表+子表数据）
    handleUpdate(row) {
      const id = row ? row.id : this.mainIds[0];
      if (!id) {
        this.$modal.msgWarning("请选择需要编辑的数据");
        return;
      }
      getLongrentcar(id).then(response => {
        // 后端返回的主表数据包含嵌套的子表数组
        this.form = {
          ...response.data,
          longcarGroups: response.data.longcarGroups || [] // 确保子表数据存在
        };
        this.selectedSubRows = []; // 清空子表选中状态
        this.open = true;
        this.title = "修改长租车辆";
      }).catch(error => {
        console.error("获取编辑数据失败：", error);
        this.$modal.msgError("加载编辑数据失败，请重试");
      });
    },

    // 6. 提交表单（保存主表+子表数据）
    submitForm() {
      // 子表和主表的验证逻辑（保持不变）
      if (this.form.longcarGroups.length === 0) {
        this.$modal.msgWarning("请至少添加一条车牌明细");
        return;
      }
      const hasEmptyCarNum = this.form.longcarGroups.some(item => !item.CARNUM);
      if (hasEmptyCarNum) {
        this.$modal.msgWarning("请完善所有车牌明细的车牌号");
        return;
      }

      this.$refs["form"].validate(valid => {
        if (valid) {
          // 区分新增（id为null）和编辑（id有值）
          const saveApi = this.form.id ? updateLongrentcar : addLongrentcar;
          saveApi(this.form).then(() => {
            this.$modal.msgSuccess(this.form.id ? "修改成功" : "录入成功");
            this.open = false;
            this.getList(); // 提交成功后强制刷新列表
          }).catch(error => {
            console.error("主表/子表保存失败：", error);
            this.$modal.msgError("保存失败，请重试");
          });
        }
      });
    },


    // 7. 处理子表数据（删除旧数据+新增新数据）
    handleSubTableData(mainId) {
      return new Promise((resolve, reject) => {
        if (!mainId) {
          reject(new Error("主表ID为空，无法处理子表"));
          return;
        }
        // 步骤1：删除旧子表（编辑场景）
        const deleteOld = () => {
          if (!this.form.id) return Promise.resolve();
          const oldIds = this.form.longcarGroups.filter(item => item.ID).map(item => item.ID);
          if (oldIds.length === 0) return Promise.resolve();
          return delGroup(oldIds).catch(error => {
            console.error("删除旧子表失败：", error);
            return Promise.resolve();
          });
        };

        // 步骤2：构造新子表数据（明确包含CARNUM）
        deleteOld().then(() => {
          const newGroups = this.form.longcarGroups.map(item => ({
            ID: null,
            LONGID: mainId,
            CARNUM: item.CARNUM, // 明确传递车牌号
            STATUS: item.STATUS
          }));
          console.log("准备提交的子表数据：", newGroups); // 新增日志，确认数据

          if (newGroups.length === 0) {
            resolve();
            return;
          }
          // 调用子表新增接口
          addGroup(newGroups).then(() => {
            resolve();
          }).catch(error => {
            console.error("新增子表失败：", error);
            reject(error);
          });
        });
      });
    },

    // 8. 删除选中的车牌明细行
    removeGroup() {
      this.$modal.confirm("确定删除选中的车牌明细吗？").then(() => {
        // 从后往前删除，避免索引错乱
        const indexes = this.selectedSubRows
          .map(item => this.form.longcarGroups.indexOf(item))
          .sort((a, b) => b - a); // 降序排序

        indexes.forEach(index => {
          this.form.longcarGroups.splice(index, 1);
        });
        this.selectedSubRows = []; // 清空选中状态
      });
    },

    // 9. 查看详情
    getOne(row) {
      const id = row ? row.id : this.mainIds[0];
      if (!id) {
        this.$modal.msgWarning("请选择需要查看的数据");
        return;
      }

      getLongrentcar(id).then(response => {
        const data = response.data; // ← 必须是 .data

        if (!data) {
          this.$modal.msgError("接口返回数据为空，无法加载详情");
          return;
        }

        console.log("详情数据：", data);

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

    // 10. 删除主表数据（级联删除子表）
    handleDelete(row) {
      const ids = row ? [row.id] : this.mainIds;
      if (ids.length === 0) {
        this.$modal.msgWarning("请选择需要删除的数据");
        return;
      }
      this.$modal.confirm(`确定删除选中的${ids.length}条数据吗？`).then(() => {
        delLongrentcar(ids).then(() => {
          this.$modal.msgSuccess("删除成功");
          this.getList(); // 刷新列表
          this.mainIds = []; // 清空选中
        }).catch(error => {
          console.error("删除失败：", error);
          this.$modal.msgError("删除失败，请重试");
        });
      });
    },

    // 11. 导出Excel
    handleExport() {
      this.download('business/longrentcar/export', { ...this.queryParams }, `长租车辆_${new Date().getTime()}.xlsx`);
    },

    // 12. 主表选择变化
    handleMainSelectionChange(selection) {
      this.mainIds = selection.map(item => item.id);
      this.single = selection.length !== 1; // 单选按钮状态（只有1条选中才可用）
      this.multiple = !selection.length; // 多选按钮状态（有选中才可用）
    },

    // 13. 子表选择变化
    handleSubSelectionChange(selection) {
      this.selectedSubRows = selection;
    },

    // 14. 搜索
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    // 15. 重置搜索
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },

    // 16. 取消弹窗
    cancel() {
      this.open = false;
      this.open1 = false;
      this.$refs.form?.resetFields();
      this.getList(); // 关闭弹窗后刷新列表
    }
  }
};
</script>

<style scoped>
.detail-form .el-form-item { margin-bottom: 10px; }
</style>
