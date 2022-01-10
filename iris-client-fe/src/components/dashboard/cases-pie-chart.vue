<template>
  <vue-chart :type="type" :data="data" />
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
  type = "doughnut";
  data: ChartData = {
    labels: ["Angefragt", "Update", "Geschlossen "],
    datasets: [
      {
        label: "Indexfälle",
        backgroundColor: ["#0E6A81", "#00B6B0", "#46ff9f"],
        data: [12, 8, 39],
      },
    ],
  };
}
</script>
