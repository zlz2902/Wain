<template>
  <div class="app-container">
    <!--表单组件-->
    <el-form :model="queryParams" ref="queryForm" size="mini" :inline="true" v-show="showSearch" label-width="150px">
      <!--根据 htmlType 渲染不同的表单项-->
      <!--输入框-->
      <el-form-item label="车牌号" prop="carnum">
        <el-input
          v-model="queryParams.carnum"
          placeholder="请输入车牌号"
          style="width: 240px"
          clearable
          maxlength= "20"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--选择框，带检索功能--> <!--如果是下拉选择框或单选框，并且有字典类型-->
      <!--根据 htmlType 渲染不同的表单项-->
      <!--输入框-->
      <el-form-item label="车主姓名" prop="carname">
        <el-input
          v-model="queryParams.carname"
          placeholder="请输入车主姓名"
          style="width: 240px"
          clearable
          maxlength= "30"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select style="width: 240px" v-model="queryParams.status" placeholder="请选择状态" clearable>
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:car:add']" >录入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:car:remove']" >批量删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"  :columns.sync="columns" ></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="carList" size="mini" height="65vh" @selection-change="handleSelectionChange">
      <el-table-column type="selection" :show-overflow-tooltip="true" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" width="120" v-if="false"/>
      <el-table-column label="车牌号" align="center" prop="carnum" min-width="120" show-overflow-tooltip v-if="columns[0].visible"/>
      <el-table-column label="车主姓名" align="center" prop="carname" min-width="120" show-overflow-tooltip v-if="columns[1].visible"/>
      <el-table-column label="车型" align="center" prop="cartype" min-width="130" v-if="columns[2].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.car_freshwate_fish" :value="scope.row.cartype"/>
        </template>
      </el-table-column>
      <el-table-column label="联系电话" align="center" prop="linkphone" min-width="120" show-overflow-tooltip v-if="columns[3].visible"/>
      <el-table-column label="收费车型" align="center" prop="type" min-width="130" v-if="columns[4].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.charges_freshwate_fish" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" min-width="130" v-if="columns[6].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.blacklist_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" min-width="120" show-overflow-tooltip v-if="columns[5].visible"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" min-width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="getOne(scope.row)"
            v-hasPermi="['business:car:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:car:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:car:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      :page-sizes="[20, 50, 100, 150]"
      @pagination="getList"
    />

    <!-- 添加或修改特殊车辆管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="车牌号" prop="carnum">
              <el-input v-model="form.carnum" placeholder="请输入车牌号" maxlength="20"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="carname">
              <el-input v-model="form.carname" placeholder="请输入车主姓名" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车型" prop="cartype">
              <el-select v-model="form.cartype" placeholder="请选择车型" style="width:100%;">
                <el-option
                  v-for="dict in dict.type.car_freshwate_fish"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="linkphone">
              <el-input v-model="form.linkphone" placeholder="请输入联系电话" maxlength="15"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收费类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择收费类型" style="width:100%;">
                <el-option
                  v-for="dict in dict.type.charges_freshwate_fish"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width:100%;">
                <el-option
                  v-for="dict in dict.type.blacklist_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" maxlength="50"/>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>


    <!-- 特殊车辆管理详情对话框 -->
    <el-dialog :title="title" :visible.sync="open1" width="800px" append-to-body>
      <el-form ref="form" :model="form1" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="车牌号" prop="carnum">
              <el-input v-model="form1.carnum" placeholder="请输入车牌号" maxlength="20" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="carname">
              <el-input v-model="form1.carname" placeholder="请输入车主姓名" maxlength="30" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车型" prop="cartype">
              <el-select v-model="form1.cartype" placeholder="请选择车型" style="width:100%;" disabled>
                <el-option
                  v-for="dict in dict.type.car_freshwate_fish"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="linkphone">
              <el-input v-model="form1.linkphone" placeholder="请输入联系电话" maxlength="15" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收费车型" prop="type">
              <el-select v-model="form1.type" placeholder="请选择收费车型" style="width: 100%;" disabled>
                <el-option
                  v-for="dict in dict.type.charges_freshwate_fish"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form1.status" placeholder="请选择状态" style="width: 100%;" disabled>
                <el-option
                  v-for="dict in dict.type.blacklist_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form1.remarks" type="textarea" placeholder="请输入内容" maxlength="50" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>




  </div>
</template>

<script>
import { listCar, getCar, delCar, addCar, updateCar } from "@/api/business/car";

export default {
  name: "Car",
  dicts: ['charges_freshwate_fish', 'car_freshwate_fish', 'blacklist_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 特殊车辆管理表格数据
      carList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      open1: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        carnum: null,
        carname: null,
        cartype: null,
        linkphone: null,
        type: null,
        remarks: null,
        status: null,
        parkid: null,
        startdate: null,
        enddate: null
      },
      // 表单参数
      form: {},
      form1: {},
      exportColumnDialogVisible: false,
      exportColumns: [],
      tableColumns: [
        { key: 0, label: `车牌号`, prop: 'carnum', visible: false },
        { key: 1, label: `车主姓名`, prop: 'carname', visible: false },
        { key: 2, label: `01-5吨；02-5-35吨；03-35吨以上`, prop: 'cartype', visible: false },
        { key: 3, label: `联系电话`, prop: 'linkphone', visible: false },
        { key: 4, label: `类型 01-采购车；02-运输车`, prop: 'type', visible: false },
        { key: 5, label: `备注`, prop: 'remarks', visible: false },
        { key: 6, label: `状态`, prop: 'status', visible: false },
        { key: 7, label: `停车id`, prop: 'parkid', visible: false },
        { key: 8, label: `开始时间`, prop: 'startdate', visible: false },
        { key: 9, label: `结束时间`, prop: 'enddate', visible: false },
      ],
      columns: [
        { key: 0, label: `车牌号`, visible: true },
        { key: 1, label: `车主姓名`, visible: true },
        { key: 2, label: `01-5吨；02-5-35吨；03-35吨以上`, visible: true },
        { key: 3, label: `联系电话`, visible: true },
        { key: 4, label: `类型 01-采购车；02-运输车`, visible: true },
        { key: 5, label: `备注`, visible: true },
        { key: 6, label: `状态`, visible: true },
        { key: 7, label: `停车id`, visible: true },
        { key: 8, label: `开始时间`, visible: true },
        { key: 9, label: `结束时间`, visible: true },
      ],

      // 表单校验
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
          { required: true, message: '请选择收费类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ],
        cartype: [
          { required: true, message: '请选择车型', trigger: 'change' }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询特殊车辆管理列表 */
    getList() {
      this.loading = true;
      listCar(this.queryParams).then(response => {
        this.carList = response.rows;
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
        id: null,
        carnum: null,
        carname: null,
        cartype: null,
        linkphone: null,
        type: null,
        remarks: null,
        status: null,
        parkid: null,
        startdate: null,
        enddate: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加特殊车辆管理";
    },
    /** 详情按钮操作 */
    getOne(row) {
      this.reset();
      const id = row.id || this.ids
      getCar(id).then(response => {
        this.form1 = response.data;
        this.open1 = true;
        this.title = "特殊车辆管理详情";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCar(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改特殊车辆管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCar(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCar(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除特殊车辆管理编号为"' + ids + '"的数据项？').then(function () {
        return delCar(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  }
}
</script>
