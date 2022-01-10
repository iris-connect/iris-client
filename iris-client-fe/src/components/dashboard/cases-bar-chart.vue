<template>
  <vue-chart :type="type" :data="data" :height="200" />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { ChartData } from "chart.js";
import VueChart from "@/components/vue-chart.vue";
@Component({
  components: {
    VueChart,
  },
})
export default class CasesBarChart extends Vue {
  type = "line";
  data: ChartData = {
    labels: ["KW33", "KW34", "KW35", "KW36", "KW37"],
    datasets: [
      {
        label: "Indexfälle",
        borderColor: "#46ff9f",
        data: [12, 22, 9, 32, 11],
        cubicInterpolationMode: "monotone",
      },
    ],
  };
}
</script>
