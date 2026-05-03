<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item :label="T.ruleId" prop="ruleId">
        <el-input v-model="queryParams.ruleId" :placeholder="T.ruleId" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.metricType" prop="resourceType">
        <el-input v-model="queryParams.resourceType" :placeholder="T.metricPh" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ T.search }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ T.reset }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['monitor:threshold:add']">{{ T.add }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['monitor:threshold:edit']">{{ T.edit }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['monitor:threshold:remove']">{{ T.delete }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="T.pk" align="center" prop="thresholdId" width="70" />
      <el-table-column :label="T.ruleId" align="center" prop="ruleId" width="80" />
      <el-table-column :label="T.metricType" align="center" prop="resourceType" min-width="120" show-overflow-tooltip />
      <el-table-column :label="T.min" align="center" prop="minValue" width="100" />
      <el-table-column :label="T.max" align="center" prop="maxValue" width="100" />
      <el-table-column :label="T.actions" align="center" width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['monitor:threshold:edit']">{{ T.edit }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['monitor:threshold:remove']">{{ T.delete }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="480px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="T.ruleId" prop="ruleId"><el-input-number v-model="form.ruleId" :min="1" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.metricType" prop="resourceType">
          <el-input v-model="form.resourceType" placeholder="Temperature / Humidity / Pressure" />
        </el-form-item>
        <el-form-item :label="T.min" prop="minValue"><el-input-number v-model="form.minValue" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.max" prop="maxValue"><el-input-number v-model="form.maxValue" controls-position="right" style="width:100%" /></el-form-item>
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
import { listMonRuleThreshold, getMonRuleThreshold, addMonRuleThreshold, updateMonRuleThreshold, delMonRuleThreshold } from '@/api/bizMonitor/monRuleThreshold'

export default {
  name: 'BizMonRuleThreshold',
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
      queryParams: { pageNum: 1, pageSize: 10, ruleId: undefined, resourceType: undefined },
      form: {},
      rules: {
        ruleId: [{ required: true, message: T.reqRuleId, trigger: 'blur' }],
        resourceType: [{ required: true, message: T.reqMetric, trigger: 'blur' }]
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
      if (q.ruleId === '' || q.ruleId == null) q.ruleId = undefined
      else if (q.ruleId !== '') q.ruleId = Number(q.ruleId)
      listMonRuleThreshold(q).then(res => {
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
      this.form = { thresholdId: undefined, ruleId: undefined, resourceType: undefined, minValue: undefined, maxValue: undefined }
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
      this.title = T.addTh
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(i => i.thresholdId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const id = row ? row.thresholdId : this.ids[0]
      getMonRuleThreshold(id).then(res => {
        this.form = res.data
        this.open = true
        this.title = T.editTh
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const req = this.form.thresholdId != null ? updateMonRuleThreshold(this.form) : addMonRuleThreshold(this.form)
        req.then(() => {
          this.$modal.msgSuccess(this.form.thresholdId != null ? T.msgEditOk : T.msgAddOk)
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const ids = row ? row.thresholdId : this.ids.join(',')
      this.$modal.confirm(T.delConfirm).then(() => delMonRuleThreshold(ids)).then(() => {
        this.getList()
        this.$modal.msgSuccess(T.msgDelOk)
      }).catch(() => {})
    }
  }
}
</script>
