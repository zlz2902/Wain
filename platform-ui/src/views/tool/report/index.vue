<template>
  <div>
      <iframe id="dome" :src=src ></iframe>
  </div>
</template>

<script>
  import {getToken} from "@/utils/auth";
  export default {
      data(){
        return{
          src:configSrc.reportSrc+"/jmreport/list?token="+getToken()
        }
      }
  }
</script>

<style scoped>
#dome{
  width: 100%;
  height: calc(100vh - 90px);
}
</style>
