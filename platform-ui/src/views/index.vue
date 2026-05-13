<template>
  <div class="home home-embed" :style="embedWrapStyle">
    <iframe
      class="home-iframe"
      :src="homeEmbedUrlWithChartPerms"
      title="大屏"
      frameborder="0"
      allowfullscreen
    />
  </div>
</template>

<script>

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.8.7",
      /** 必须与 newWork/web 的 devServer 端口一致，不可指向 platform-ui 自己（8080/80）否则会无限嵌套 */
      homeEmbedUrl: process.env.VUE_APP_HOME_EMBED_URL || "http://localhost:9528/"
    };
  },
  computed: {
    homeEmbedUrlWithChartPerms() {
      let perms = [];
      const storePerms = this.$store.getters.permissions || [];
      if (Array.isArray(storePerms) && storePerms.length > 0) {
        perms = storePerms;
      } else {
        try {
          perms = JSON.parse(localStorage.getItem('chartPerms') || '[]');
        } catch (e) {
          perms = [];
        }
      }
      perms = Array.isArray(perms)
        ? perms.filter(item => typeof item === 'string' && (item.indexOf('dashboard:chart:') === 0 || item === '*:*:*'))
        : [];
      // 作者：Smallway，2026-05-13
      // 兼容超级管理员权限，确保其能够查看大屏所有图表
      if (perms.includes('*:*:*')) {
        perms.push('dashboard:chart:*');
      }
      const encodedPerms = encodeURIComponent(JSON.stringify(perms));
      const hashIndex = this.homeEmbedUrl.indexOf('#');
      if (hashIndex !== -1) {
        const base = this.homeEmbedUrl.slice(0, hashIndex);
        const hash = this.homeEmbedUrl.slice(hashIndex);
        const joiner = hash.includes('?') ? '&' : '?';
        return `${base}${joiner}chartPerms=${encodedPerms}${hash}`;
      }
      const joiner = this.homeEmbedUrl.includes('?') ? '&' : '?';
      return `${this.homeEmbedUrl}${joiner}chartPerms=${encodedPerms}`;
    },
    /** 与 layout/components/AppMain.vue 中 min-height 算法一致，占满主内容区可视高度 */
    embedWrapStyle() {
      const top = this.$store.state.settings.tagsView ? 84 : 50;
      const h = `calc(100vh - ${top}px)`;
      return {
        width: "100%",
        height: h,
        minHeight: h,
        boxSizing: "border-box",
        display: "flex",
        flexDirection: "column"
      };
    }
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home-iframe {
  flex: 1;
  width: 100%;
  min-height: 0;
  border: 0;
  display: block;
}

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

