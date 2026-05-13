<!--
 * @Author: daidai
 * @Date: 2022-03-04 09:23:59
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-05-07 11:05:02
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\index.vue
-->
<template>
  <div class="contents">
    <div class="contetn_left">
      <div class="pagetab"></div>
      <ItemWrap :disabled="!canShow('dashboard:chart:user-overview')" class="contetn_left-top contetn_lr-item" title="设备信息总览">
        <LeftTop/>
      </ItemWrap>
      <ItemWrap :disabled="!canShow('dashboard:chart:device-overview')" class="contetn_left-center contetn_lr-item" title="设备状态总览">
        <LeftCenter />
      </ItemWrap>
      <ItemWrap
        :disabled="!canShow('dashboard:chart:device-tips')"
        class="contetn_left-bottom contetn_lr-item"
        title="设备提醒"
        style="padding: 0 10px 16px 10px"
      >
        <LeftBottom />
      </ItemWrap>
    </div>
    <div class="contetn_center">
      <CenterMap :disabled="!canShow('dashboard:chart:map')" class="contetn_center_top" />
      <ItemWrap :disabled="!canShow('dashboard:chart:topology')" class="contetn_center-bottom" title="安装计划">
        <CenterBottom />
      </ItemWrap>
    </div>
    <div class="contetn_right">
      <ItemWrap
        :disabled="!canShow('dashboard:chart:alarm-count')"
        class="contetn_left-bottom contetn_lr-item"
        title="报警次数"
      >
        <RightTop />
      </ItemWrap>
      <ItemWrap
        :disabled="!canShow('dashboard:chart:alarm-ranking')"
        class="contetn_left-bottom contetn_lr-item"
        title="报警排名(TOP8)"
        style="padding: 0 10px 16px 10px"
      >
        <RightCenter />
      </ItemWrap>
      <ItemWrap
        :disabled="!canShow('dashboard:chart:realtime-warning')"
        class="contetn_left-bottom contetn_lr-item"
        title="数据统计图 "
      >
        <RightBottom />
      </ItemWrap>
    </div>
  </div>
</template>

<script>
import LeftTop from './left-top.vue'
import LeftCenter from "./left-center.vue";
import LeftBottom from "./left-bottom.vue";
import CenterMap from "./center-map.vue";
import CenterBottom from "./center-bottom.vue";
import RightTop from "./right-top.vue";
import RightCenter from "./right-center.vue";
import RightBottom from "./right-bottom.vue";
import { getChartPerms } from '@/api/chartPerms'

export default {
  components: {
    LeftTop,
    LeftCenter,
    LeftBottom,
    CenterMap,
    RightTop,
    RightCenter,
    RightBottom,
    CenterBottom,
  },
  data() {
    return {
      chartPerms: []
    };
  },
  filters: {
    numsFilter(msg) {
      return msg || 0;
    },
  },
  created() {
    this.loadChartPerms();
    this.refreshChartPerms();
    window.addEventListener('storage', this.handleChartPermsChange);
  },

  mounted() {},
  beforeDestroy() {
    window.removeEventListener('storage', this.handleChartPermsChange);
  },
  methods: {
    loadChartPerms() {
      const raw = localStorage.getItem('chartPerms');
      try {
        this.chartPerms = raw ? JSON.parse(raw) : [];
      } catch (e) {
        this.chartPerms = [];
      }
    },
    refreshChartPerms() {
      getChartPerms().then(res => {
        if (res.success) {
          this.chartPerms = Array.isArray(res.data) ? res.data : [];
          localStorage.setItem('chartPerms', JSON.stringify(this.chartPerms));
        } else {
          this.chartPerms = [];
          localStorage.setItem('chartPerms', JSON.stringify([]));
        }
      }).catch(() => {
        this.chartPerms = [];
        localStorage.setItem('chartPerms', JSON.stringify([]));
      });
    },
    handleChartPermsChange() {
      this.refreshChartPerms();
      this.$forceUpdate();
    },
    canShow(perm) {
      if (!this.chartPerms || this.chartPerms.length === 0) {
        return false;
      }
      return this.chartPerms.includes('dashboard:chart:*') || this.chartPerms.includes(perm);
    }
  },
};
</script>
<style lang="scss" scoped>
// 内容
.contents {
  .contetn_left,
  .contetn_right {
    width: 540px;
    box-sizing: border-box;
    // padding: 16px 0;
  }

  .contetn_center {
    width: 720px;
  }

  //左右两侧 三个块
  .contetn_lr-item {
    height: 310px;
  }

  .contetn_center_top {
    width: 100%;
  }

  // 中间
  .contetn_center {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
  }

  .contetn_center-bottom {
    height: 315px;
  }

  //左边 右边 结构一样
  .contetn_left,
  .contetn_right {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    position: relative;

  
  }
}


@keyframes rotating {
    0% {
        -webkit-transform: rotate(0) scale(1);
        transform: rotate(0) scale(1);
    }
    50% {
        -webkit-transform: rotate(180deg) scale(1.1);
        transform: rotate(180deg) scale(1.1);
    }
    100% {
        -webkit-transform: rotate(360deg) scale(1);
        transform: rotate(360deg) scale(1);
    }
}
</style>
