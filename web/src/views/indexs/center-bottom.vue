<template>
  <div class="center_bottom">
    <Echart
      :options="options"
      id="bottomLeftChart"
      class="echarts_bottom"
    ></Echart>
  </div>
</template>

<script>
export default {
  data() {
    return {
      options: {},
    };
  },
  props: {},
  mounted() {
    this.initTopology();
  },
  methods: {
    initTopology() {
      // 拓扑示例数据：后续接真实接口时，把 nodes/links 替换即可
      const nodes = [
        { id: "site", name: "站点A", category: 0, symbolSize: 46 },
        { id: "switch", name: "交换机", category: 1, symbolSize: 34 },
        { id: "clock_main", name: "氢原子钟(主)", category: 2, symbolSize: 32 },
        { id: "clock_backup", name: "氢原子钟(备)", category: 2, symbolSize: 32 },
        { id: "phase", name: "相位微调器", category: 3, symbolSize: 30 },
        { id: "server", name: "授时服务器", category: 4, symbolSize: 30 },
        { id: "gnss", name: "GNSS天线", category: 5, symbolSize: 28 },
      ];

      const links = [
        { source: "site", target: "switch", name: "网络" },
        { source: "switch", target: "server", name: "以太网" },
        { source: "switch", target: "phase", name: "控制" },
        { source: "phase", target: "clock_main", name: "参考" },
        { source: "phase", target: "clock_backup", name: "参考" },
        { source: "gnss", target: "server", name: "授时" },
      ];

      this.options = {
        tooltip: {
          trigger: "item",
          backgroundColor: "rgba(0,0,0,.6)",
          borderColor: "rgba(147, 235, 248, .8)",
          textStyle: { color: "#FFF" },
          formatter: (p) => {
            if (p.dataType === "edge") {
              const label = p.data && p.data.name ? `（${p.data.name}）` : "";
              return `${p.data.source} → ${p.data.target}${label}`;
            }
            return p.name || "";
          },
        },
        legend: {
          top: 0,
          textStyle: { color: "#B4B4B4" },
          data: ["站点", "网络", "时钟", "调节", "服务器", "GNSS"],
        },
        series: [
          {
            type: "graph",
            layout: "force",
            roam: true,
            draggable: true,
            data: nodes,
            links,
            categories: [
              { name: "站点" },
              { name: "网络" },
              { name: "时钟" },
              { name: "调节" },
              { name: "服务器" },
              { name: "GNSS" },
            ],
            force: {
              repulsion: 260,
              edgeLength: [80, 160],
              gravity: 0.15,
            },
            label: {
              show: true,
              color: "#EAEAEA",
              fontSize: 12,
            },
            lineStyle: {
              color: "rgba(0, 253, 250, .55)",
              width: 2,
              curveness: 0.15,
            },
            edgeLabel: {
              show: true,
              formatter: "{c}",
              color: "rgba(255,255,255,.75)",
              fontSize: 10,
            },
            emphasis: {
              focus: "adjacency",
              lineStyle: { width: 3, color: "rgba(0, 253, 250, .9)" },
            },
          },
        ],
      };
    },
  },
};
</script>
<style lang="scss" scoped>
.center_bottom {
  width: 100%;
  height: 100%;

  .echarts_bottom {
    width: 100%;
    height: 100%;
  }
}
</style>
