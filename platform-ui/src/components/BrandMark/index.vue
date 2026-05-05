<template>
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 64 64"
    class="brand-mark"
    aria-hidden="true"
    focusable="false"
  >
    <defs>
      <linearGradient :id="gradId" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" stop-color="#409EFF" />
        <stop offset="100%" stop-color="#1e6fd9" />
      </linearGradient>
    </defs>
    <rect width="64" height="64" rx="14" :fill="gradFill" />
    <rect x="13" y="16" width="38" height="26" rx="3" fill="none" stroke="#fff" stroke-width="2.5" />
    <path
      d="M20 28h10M20 33h18M20 38h14"
      stroke="#fff"
      stroke-width="2"
      stroke-linecap="round"
      opacity="0.9"
    />
    <path d="M32 44v4" stroke="#fff" stroke-width="2.5" stroke-linecap="round" />
    <path d="M24 48h16" stroke="#fff" stroke-width="2.5" stroke-linecap="round" />
    <circle cx="46" cy="20" r="9" fill="#ff4d4f" />
    <path d="M46 16.5v4.2M46 23.2h.01" stroke="#fff" stroke-width="1.8" stroke-linecap="round" />
  </svg>
</template>

<script>
let _seq = 0
export default {
  name: 'BrandMark',
  data() {
    return {
      gradId: `brand-mark-grad-${++_seq}`
    }
  },
  computed: {
    gradFill() {
      return `url(#${this.gradId})`
    }
  }
}
</script>

<style scoped>
.brand-mark {
  display: block;
  width: 100%;
  height: 100%;
  flex-shrink: 0;
}
</style>
