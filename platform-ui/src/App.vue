<template>
  <div id="app">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";

export default {
  name: "App",
  components: { ThemePicker },
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
      }
    }
  }
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
/deep/ .el-submenu__title{
    height: 26px;
    line-height: 26px;
    font-size: 12px;
}
/deep/ .el-menu-item{
    height: 26px;
    line-height: 26px;
    font-size: 12px;
}
/deep/ #app .sidebar-container .svg-icon{
    margin-right: 8px;
}
/deep/ .el-form-item--mini.el-form-item {
    margin-bottom: 12px;
}
/deep/ .el-form-item__error {
  color: transparent !important; /* 文字透明 */
  font-size: 0 !important; /* 文字大小为 0 */
  padding: 0 !important; /* 去掉内边距 */
  margin: 0 !important; /* 去掉外边距 */
}
/deep/ .el-input.is-disabled .el-input__inner{
  color: #414141;
}
/deep/ .el-input__inner{
  color: black;
}
/deep/ .el-form-item__label{
  color: #2e2e2e;
}
v-deep.el-descriptions-item__content{
    max-width: 400px;
}
/deep/ .el-input--mini .el-input__inner{
  padding: 0 0 0 25px;
}

</style>
