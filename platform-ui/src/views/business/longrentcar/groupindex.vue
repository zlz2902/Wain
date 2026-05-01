<template>
  <div class="app-container">
    <!-- 车牌明细录入弹窗（匹配图片的「录入-车牌明细」） -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="车牌号" prop="CARNUM">
              <el-input v-model="form.CARNUM" placeholder="请输入车牌号" maxlength="10" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="有效状态" prop="STATUS">
              <el-select v-model="form.STATUS" placeholder="请选择有效状态" style="width: 100%;">
                <el-option label="有效" value="1" />
                <el-option label="无效" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listGroup, getGroup, delGroup, addGroup, updateGroup } from "@/api/business/group";

export default {
  name: "GroupIndex",
  props: {
    longrentcarId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      open: false,
      title: "添加车牌明细",
      form: {
        LONGID: this.longrentcarId,
        CARNUM: null,
        STATUS: "1" // 默认有效
      },
      rules: {
        CARNUM: [
          { required: true, message: "请输入车牌号", trigger: "blur" }
        ],
        STATUS: [
          { required: true, message: "请选择有效状态", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 取消弹窗
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        ID: null,
        LONGID: this.longrentcarId,
        CARNUM: null,
        STATUS: "1"
      };
      this.resetForm("form");
    },
    // 新增车牌
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加车牌明细";
    },
    // 提交表单
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.ID != null) {
            updateGroup(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$emit("refresh");
            });
          } else {
            addGroup(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.$emit("refresh");
            });
          }
        }
      });
    },
    // 删除车牌
    handleDelete(row) {
      const IDs = row.ID;
      this.$modal.confirm(`是否确认删除该车牌明细？`).then(() => {
        return delGroup(IDs);
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.$emit("refresh");
      }).catch(() => {});
    }
  }
};
</script>
