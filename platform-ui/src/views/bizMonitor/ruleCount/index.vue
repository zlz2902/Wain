<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item :label="T.ruleId" prop="ruleId">
        <el-input v-model="queryParams.ruleId" :placeholder="T.ruleId" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.deviceNo" prop="deviceNo">
        <el-input v-model="queryParams.deviceNo" :placeholder="T.deviceNo" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ T.search }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ T.reset }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['monitor:ruleCount:add']">{{ T.add }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['monitor:ruleCount:edit']">{{ T.edit }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['monitor:ruleCount:remove']">{{ T.delete }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="T.pk" align="center" prop="countId" width="70" />
      <el-table-column :label="T.ruleId" align="center" prop="ruleId" width="80" />
      <el-table-column :label="T.deviceNo" align="center" prop="deviceNo" min-width="120" show-overflow-tooltip />
      <el-table-column :label="T.currentCount" align="center" prop="currentCount" width="120" />
      <el-table-column :label="T.lastCheck" align="center" prop="lastCheckTime" width="170">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.lastCheckTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="T.actions" align="center" width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['monitor:ruleCount:edit']">{{ T.edit }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['monitor:ruleCount:remove']">{{ T.delete }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="480px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="T.ruleId" prop="ruleId"><el-input-number v-model="form.ruleId" :min="1" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.deviceNo" prop="deviceNo"><el-input v-model="form.deviceNo" /></el-form-item>
        <el-form-item :label="T.currentCount" prop="currentCount"><el-input-number v-model="form.currentCount" :min="0" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.lastCheck" prop="lastCheckTime">
          <el-date-picker v-model="form.lastCheckTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ T.ok }}</el-button>
        <el-button @click="cancel">{{ T.cancel }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { T } from '../i18n-zh'
import { listMonRuleCount, getMonRuleCount, addMonRuleCount, updateMonRuleCount, delMonRuleCount } from '@/api/bizMonitor/monRuleCount'

export default {
  name: 'BizMonRuleCount',
  data() {
    return {
      T,
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      dataList: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, ruleId: undefined, deviceNo: undefined },
      form: {},
      rules: {
        ruleId: [{ required: true, message: T.reqRuleId, trigger: 'blur' }],
        deviceNo: [{ required: true, message: T.reqDevNo2, trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const q = { ...this.queryParams }
      if (q.ruleId === '') q.ruleId = undefined
      else if (q.ruleId != null && q.ruleId !== '') q.ruleId = Number(q.ruleId)
      listMonRuleCount(q).then(res => {
        this.dataList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { countId: undefined, ruleId: undefined, deviceNo: undefined, currentCount: 0, lastCheckTime: undefined }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = T.addCount
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(i => i.countId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const id = row ? row.countId : this.ids[0]
      getMonRuleCount(id).then(res => {
        this.form = res.data
        this.open = true
        this.title = T.editCount
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const req = this.form.countId != null ? updateMonRuleCount(this.form) : addMonRuleCount(this.form)
        req.then(() => {
          this.$modal.msgSuccess(this.form.countId != null ? T.msgEditOk : T.msgAddOk)
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const ids = row ? row.countId : this.ids.join(',')
      this.$modal.confirm(T.delConfirm).then(() => delMonRuleCount(ids)).then(() => {
        this.getList()
        this.$modal.msgSuccess(T.msgDelOk)
      }).catch(() => {})
    }
  }
}
</script>
