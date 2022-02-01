<template>
  <div v-bind="$attrs">
    <canvas ref="canvas" :width="width" :height="height"></canvas>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { ChartData, ChartOptions, ChartType } from "chart.js";
import Chart from "chart.js/auto";
import { PropType } from "vue";

const VueChartProps = Vue.extend({
  inheritAttrs: false,
  props: {
    type: {
      type: String as PropType<ChartType>,
      default: null,
    },
    width: {
      type: Number,
      default: 400,
    },
    height: {
      type: Number,
      default: 400,
    },
    data: {
      type: Object as PropType<ChartData>,
      default: null,
    },
    options: {
      type: Object as PropType<ChartOptions>,
      default: () => ({
        responsive: true,
        maintainAspectRatio: false,
      }),
    },
  },
});

@Component
export default class VueChart extends VueChartProps {
  $refs!: {
    canvas: HTMLCanvasElement;
  };
  chart: Chart | null = null;
  mounted() {
    this.chart = new Chart(this.$refs.canvas, {
      type: this.type,
      data: this.data,
      options: this.options,
    });
  }
  beforeDestroy() {
    if (this.chart) {
      this.chart.destroy();
    }
  }
  @Watch("data")
  onDataChange(value: ChartData): void {
    if (this.chart) {
      this.chart.data = value;
      this.chart.update();
    }
  }
}
</script>
