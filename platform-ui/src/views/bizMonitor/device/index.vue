<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item :label="T.deviceNo" prop="deviceNo">
        <el-input v-model="queryParams.deviceNo" :placeholder="T.deviceNo" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.deviceName" prop="deviceName">
        <el-input v-model="queryParams.deviceName" :placeholder="T.deviceName" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.gateway" prop="gatewayno">
        <el-input v-model="queryParams.gatewayno" :placeholder="T.gateway" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="T.stationId" prop="stationId">
        <el-input v-model="queryParams.stationId" :placeholder="T.stationId" clearable @keyup.enter.native="handleQuery" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['monitor:device:add']">{{ T.add }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['monitor:device:edit']">{{ T.edit }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['monitor:device:remove']">{{ T.delete }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="T.pk" align="center" prop="deviceId" width="70" />
      <el-table-column :label="T.deviceNo" align="center" prop="deviceNo" min-width="120" show-overflow-tooltip />
      <el-table-column :label="T.gateway" align="center" prop="gatewayno" width="100" />
      <el-table-column :label="T.deviceName" align="center" prop="deviceName" min-width="100" show-overflow-tooltip />
      <el-table-column :label="T.deviceType" align="center" prop="deviceType" width="90" />
      <el-table-column :label="T.terminal" align="center" prop="terminalno" width="90" />
      <el-table-column :label="T.stationId" align="center" prop="stationId" width="80" />
      <el-table-column :label="T.owner" align="center" prop="owner" width="90" />
      <el-table-column :label="T.enabled" align="center" prop="isEnabled" width="70">
        <template slot-scope="scope">{{ scope.row.isEnabled === 1 ? T.yes : T.no }}</template>
      </el-table-column>
      <el-table-column :label="T.created" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="T.actions" align="center" class-name="small-padding fixed-width" width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['monitor:device:edit']">{{ T.edit }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['monitor:device:remove']">{{ T.delete }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="T.deviceNo" prop="deviceNo">
          <el-input v-model="form.deviceNo" :placeholder="T.uniqueNo" :disabled="form.deviceId != null" />
        </el-form-item>
        <el-form-item :label="T.gateway" prop="gatewayno"><el-input v-model="form.gatewayno" /></el-form-item>
        <el-form-item :label="T.deviceName" prop="deviceName"><el-input v-model="form.deviceName" /></el-form-item>
        <el-form-item :label="T.deviceType" prop="deviceType"><el-input v-model="form.deviceType" /></el-form-item>
        <el-form-item :label="T.terminal" prop="terminalno"><el-input v-model="form.terminalno" /></el-form-item>
        <el-form-item :label="T.stationId" prop="stationId"><el-input-number v-model="form.stationId" :min="1" controls-position="right" style="width:100%" /></el-form-item>
        <el-form-item :label="T.owner" prop="owner"><el-input v-model="form.owner" /></el-form-item>
        <el-form-item :label="T.installDate" prop="installDate">
          <el-date-picker v-model="form.installDate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="T.pickDt" style="width:100%" />
        </el-form-item>
        <el-form-item :label="T.isEnabled" prop="isEnabled">
          <el-radio-group v-model="form.isEnabled">
            <el-radio :label="1">{{ T.yes }}</el-radio>
            <el-radio :label="0">{{ T.no }}</el-radio>
          </el-radio-group>
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
import { listMonDevice, getMonDevice, addMonDevice, updateMonDevice, delMonDevice } from '@/api/bizMonitor/monDevice'

export default {
  name: 'BizMonDevice',
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
      queryParams: { pageNum: 1, pageSize: 10, deviceNo: undefined, deviceName: undefined, gatewayno: undefined, stationId: undefined },
      form: {},
      rules: {
        deviceNo: [{ required: true, message: T.reqDeviceNo, trigger: 'blur' }],
        deviceName: [{ required: true, message: T.reqDeviceName, trigger: 'blur' }],
        stationId: [{ required: true, message: T.reqStation, trigger: 'blur' }]
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
      if (q.stationId === '' || q.stationId == null) q.stationId = undefined
      else q.stationId = Number(q.stationId)
      listMonDevice(this.addDateRange(q, this.dateRange)).then(res => {
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
      this.form = { deviceId: undefined, deviceNo: undefined, gatewayno: undefined, deviceName: undefined, deviceType: undefined, terminalno: undefined, stationId: undefined, owner: undefined, installDate: undefined, isEnabled: 1 }
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
      this.title = T.addDevice
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(i => i.deviceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const id = row ? row.deviceId : this.ids[0]
      getMonDevice(id).then(res => {
        this.form = res.data
        this.open = true
        this.title = T.editDevice
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const req = this.form.deviceId != null ? updateMonDevice(this.form) : addMonDevice(this.form)
        req.then(() => {
          this.$modal.msgSuccess(this.form.deviceId != null ? T.msgEditOk : T.msgAddOk)
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const deviceIds = row ? row.deviceId : this.ids.join(',')
      this.$modal.confirm(T.delConfirmDevice).then(() => delMonDevice(deviceIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess(T.msgDelOk)
      }).catch(() => {})
    }
  }
}
</script>
