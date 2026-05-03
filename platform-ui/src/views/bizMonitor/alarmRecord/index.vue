<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item :label="T.deviceNo" prop="deviceNo">
        <el-input v-model="queryParams.deviceNo" :placeholder="T.deviceNo" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.ruleId" prop="ruleId">
        <el-input v-model="queryParams.ruleId" :placeholder="T.ruleId" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.alarmType" prop="alarmType">
        <el-input v-model="queryParams.alarmType" :placeholder="T.alarmTypeFuzzy" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.handled" prop="isHandled">
        <el-select v-model="queryParams.isHandled" :placeholder="T.sceneAll" clearable style="width: 100px">
          <el-option :label="T.unhandled" :value="0" />
          <el-option :label="T.handledY" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item :label="T.alarmTime">
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['monitor:alarm:add']">{{ T.add }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-check" size="mini" :disabled="single" @click="openHandle()" v-hasPermi="['monitor:alarm:edit']">{{ T.handleBtn }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['monitor:alarm:remove']">{{ T.delete }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="T.alarmId" align="center" prop="alarmId" width="80" />
      <el-table-column :label="T.deviceNo" align="center" prop="deviceNo" min-width="110" show-overflow-tooltip />
      <el-table-column :label="T.stationId" align="center" prop="stationId" width="80" />
      <el-table-column :label="T.ruleId" align="center" prop="ruleId" width="70" />
      <el-table-column :label="T.level" align="center" prop="alarmLevel" width="60" />
      <el-table-column :label="T.type" align="center" prop="alarmType" width="100" />
      <el-table-column :label="T.alarmName" align="center" prop="alarmName" min-width="100" show-overflow-tooltip />
      <el-table-column :label="T.alarmValue" align="center" prop="alarmValue" width="80" />
      <el-table-column :label="T.alarmTime" align="center" prop="alarmTime" width="165">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.alarmTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="T.handledCol" align="center" prop="isHandled" width="80">
        <template slot-scope="scope">{{ scope.row.isHandled === 1 ? T.yes : T.no }}</template>
      </el-table-column>
      <el-table-column :label="T.actions" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)" v-hasPermi="['monitor:alarm:query']">{{ T.detailBtn }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleOneHandle(scope.row)" v-if="scope.row.isHandled !== 1" v-hasPermi="['monitor:alarm:edit']">{{ T.handleBtn }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['monitor:alarm:remove']">{{ T.delete }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="T.addAlarm" :visible.sync="openAdd" width="560px" append-to-body>
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
        <el-form-item :label="T.deviceNo" prop="deviceNo"><el-input v-model="addForm.deviceNo" /></el-form-item>
        <el-form-item :label="T.stationId" prop="stationId"><el-input-number v-model="addForm.stationId" :min="0" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.ruleId" prop="ruleId"><el-input-number v-model="addForm.ruleId" :min="1" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.alarmLevel" prop="alarmLevel"><el-input-number v-model="addForm.alarmLevel" :min="1" :max="5" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.alarmType" prop="alarmType"><el-input v-model="addForm.alarmType" /></el-form-item>
        <el-form-item :label="T.alarmName" prop="alarmName"><el-input v-model="addForm.alarmName" /></el-form-item>
        <el-form-item :label="T.alarmValue" prop="alarmValue"><el-input-number v-model="addForm.alarmValue" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.detail" prop="alarmDetail"><el-input v-model="addForm.alarmDetail" type="textarea" /></el-form-item>
        <el-form-item :label="T.phase" prop="phase"><el-input v-model="addForm.phase" /></el-form-item>
        <el-form-item :label="T.sbInfo" prop="sbInfo"><el-input v-model="addForm.sbInfo" type="textarea" /></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAdd">{{ T.ok }}</el-button>
        <el-button @click="openAdd = false">{{ T.cancel }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="T.handleAlarm" :visible.sync="openHandleDlg" width="400px" append-to-body>
      <el-form label-width="100px">
        <el-form-item :label="T.alarmId"><el-input :value="handleRow.alarmId" disabled /></el-form-item>
        <el-form-item :label="T.markHandled">
          <el-switch v-model="handleRow.isHandled" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitHandle">{{ T.ok }}</el-button>
        <el-button @click="openHandleDlg = false">{{ T.cancel }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="T.alarmDetailDlg" :visible.sync="openDetail" width="640px" append-to-body>
      <el-form v-if="detail" label-width="100px" size="small">
        <el-form-item :label="T.alarmId"><span>{{ detail.alarmId }}</span></el-form-item>
        <el-form-item :label="T.deviceNo"><span>{{ detail.deviceNo }}</span></el-form-item>
        <el-form-item :label="T.stationId"><span>{{ detail.stationId }}</span></el-form-item>
        <el-form-item :label="T.ruleId"><span>{{ detail.ruleId }}</span></el-form-item>
        <el-form-item :label="T.levelType"><span>{{ detail.alarmLevel }} / {{ detail.alarmType }}</span></el-form-item>
        <el-form-item :label="T.alarmName"><span>{{ detail.alarmName }}</span></el-form-item>
        <el-form-item :label="T.alarmValue"><span>{{ detail.alarmValue }}</span></el-form-item>
        <el-form-item :label="T.handledCol"><span>{{ detail.isHandled === 1 ? T.yes : T.no }}</span></el-form-item>
        <el-form-item :label="T.alarmTime"><span>{{ parseTime(detail.alarmTime) }}</span></el-form-item>
        <el-form-item :label="T.detail"><span>{{ detail.alarmDetail }}</span></el-form-item>
        <el-form-item :label="T.phase"><span>{{ detail.phase }}</span></el-form-item>
        <el-form-item :label="T.sbInfo"><span>{{ detail.sbInfo }}</span></el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { T } from '../i18n-zh'
import { listMonAlarmRecord, getMonAlarmRecord, addMonAlarmRecord, handleMonAlarmRecord, delMonAlarmRecord } from '@/api/bizMonitor/monAlarmRecord'

export default {
  name: 'BizMonAlarmRecord',
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
      dateRange: [],
      queryParams: { pageNum: 1, pageSize: 10, deviceNo: undefined, ruleId: undefined, alarmType: undefined, isHandled: undefined },
      openAdd: false,
      addForm: {},
      addRules: {
        deviceNo: [{ required: true, message: T.reqDevNo2, trigger: 'blur' }],
        ruleId: [{ required: true, message: T.reqRuleId, trigger: 'blur' }]
      },
      openHandleDlg: false,
      handleRow: { alarmId: undefined, isHandled: 1 },
      openDetail: false,
      detail: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const q = this.addDateRange({ ...this.queryParams }, this.dateRange, 'AlarmTime')
      if (q.ruleId === '') q.ruleId = undefined
      else if (q.ruleId != null && q.ruleId !== '') q.ruleId = Number(q.ruleId)
      listMonAlarmRecord(q).then(res => {
        this.dataList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(i => i.alarmId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.addForm = { deviceNo: undefined, stationId: undefined, ruleId: undefined, alarmLevel: 1, alarmType: 'THRESHOLD', alarmName: undefined, alarmValue: undefined, alarmDetail: undefined, phase: undefined, sbInfo: undefined }
      this.openAdd = true
      this.$nextTick(() => this.$refs.addForm && this.$refs.addForm.clearValidate())
    },
    submitAdd() {
      this.$refs.addForm.validate(valid => {
        if (!valid) return
        addMonAlarmRecord(this.addForm).then(() => {
          this.$modal.msgSuccess(T.msgAddOk)
          this.openAdd = false
          this.getList()
        })
      })
    },
    handleView(row) {
      getMonAlarmRecord(row.alarmId).then(res => {
        this.detail = res.data
        this.openDetail = true
      })
    },
    handleOneHandle(row) {
      this.handleRow = { alarmId: row.alarmId, isHandled: 1 }
      this.openHandleDlg = true
    },
    openHandle() {
      if (this.ids.length !== 1) return
      const row = this.dataList.find(r => r.alarmId === this.ids[0])
      if (row) this.handleOneHandle(row)
    },
    submitHandle() {
      handleMonAlarmRecord({ alarmId: this.handleRow.alarmId, isHandled: this.handleRow.isHandled }).then(() => {
        this.$modal.msgSuccess(T.handleOk)
        this.openHandleDlg = false
        this.getList()
      })
    },
    handleDelete(row) {
      const alarmIds = row ? row.alarmId : this.ids.join(',')
      this.$modal.confirm(T.delAlarmConfirm).then(() => delMonAlarmRecord(alarmIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess(T.msgDelOk)
      }).catch(() => {})
    }
  }
}
</script>
