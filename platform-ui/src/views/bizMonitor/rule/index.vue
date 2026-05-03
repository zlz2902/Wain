<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item :label="T.ruleName" prop="ruleName">
        <el-input v-model="queryParams.ruleName" :placeholder="T.ruleName" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.sceneType" prop="sceneType">
        <el-select v-model="queryParams.sceneType" :placeholder="T.sceneAll" clearable style="width: 120px">
          <el-option :label="T.sceneThreshold" :value="1" />
          <el-option :label="T.sceneWork" :value="2" />
          <el-option :label="T.sceneTimeout" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item :label="T.enabled" prop="isEnabled">
        <el-select v-model="queryParams.isEnabled" :placeholder="T.sceneAll" clearable style="width: 100px">
          <el-option :label="T.yes" :value="1" />
          <el-option :label="T.no" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item :label="T.createdRange">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
          range-separator="-" :start-placeholder="T.startDate" :end-placeholder="T.endDate" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ T.search }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ T.reset }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['monitor:rule:add']">{{ T.add }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['monitor:rule:edit']">{{ T.edit }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['monitor:rule:remove']">{{ T.delete }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="T.ruleId" align="center" prop="ruleId" width="80" />
      <el-table-column :label="T.ruleName" align="center" prop="ruleName" min-width="120" show-overflow-tooltip />
      <el-table-column :label="T.sceneType" align="center" prop="sceneType" width="90">
        <template slot-scope="scope">{{ sceneLabel(scope.row.sceneType) }}</template>
      </el-table-column>
      <el-table-column :label="T.enabled" align="center" prop="isEnabled" width="70">
        <template slot-scope="scope">{{ scope.row.isEnabled === 1 ? T.yes : T.no }}</template>
      </el-table-column>
      <el-table-column :label="T.alarmLevel" align="center" prop="alarmLevel" width="90" />
      <el-table-column :label="T.accumulateTh" align="center" prop="accumulateCount" width="120" />
      <el-table-column :label="T.created" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="T.actions" align="center" width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['monitor:rule:edit']">{{ T.edit }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['monitor:rule:remove']">{{ T.delete }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="520px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item :label="T.ruleName" prop="ruleName"><el-input v-model="form.ruleName" /></el-form-item>
        <el-form-item :label="T.sceneType" prop="sceneType">
          <el-select v-model="form.sceneType" :placeholder="T.pickScene" style="width:100%">
            <el-option :label="T.sceneThreshold" :value="1" />
            <el-option :label="T.sceneWork" :value="2" />
            <el-option :label="T.sceneTimeout" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item :label="T.isEnabled" prop="isEnabled">
          <el-radio-group v-model="form.isEnabled"><el-radio :label="1">{{ T.yes }}</el-radio><el-radio :label="0">{{ T.no }}</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item :label="T.alarmLevel" prop="alarmLevel"><el-input-number v-model="form.alarmLevel" :min="1" :max="5" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.accumulate" prop="accumulateCount"><el-input-number v-model="form.accumulateCount" :min="1" controls-position="right" style="width:100%" /></el-form-item>
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
import { listMonRule, getMonRule, addMonRule, updateMonRule, delMonRule } from '@/api/bizMonitor/monRule'

export default {
  name: 'BizMonRule',
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
      dateRange: [],
      queryParams: { pageNum: 1, pageSize: 10, ruleName: undefined, sceneType: undefined, isEnabled: undefined },
      form: {},
      rules: {
        ruleName: [{ required: true, message: T.reqRuleName, trigger: 'blur' }],
        sceneType: [{ required: true, message: T.reqScene, trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    sceneLabel(v) {
      const m = { 1: T.sceneThreshold, 2: T.sceneWork, 3: T.sceneTimeout }
      return m[v] != null ? m[v] : v
    },
    getList() {
      this.loading = true
      listMonRule(this.addDateRange(this.queryParams, this.dateRange)).then(res => {
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
      this.form = { ruleId: undefined, ruleName: undefined, sceneType: 1, isEnabled: 1, alarmLevel: 1, accumulateCount: 1 }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = T.addRule
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(i => i.ruleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const id = row ? row.ruleId : this.ids[0]
      getMonRule(id).then(res => {
        this.form = res.data
        this.open = true
        this.title = T.editRule
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const req = this.form.ruleId != null ? updateMonRule(this.form) : addMonRule(this.form)
        req.then(() => {
          this.$modal.msgSuccess(this.form.ruleId != null ? T.msgEditOk : T.msgAddOk)
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const ruleIds = row ? row.ruleId : this.ids.join(',')
      this.$modal.confirm(T.delRuleCascade).then(() => delMonRule(ruleIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess(T.msgDelOk)
      }).catch(() => {})
    }
  }
}
</script>
