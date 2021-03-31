<script lang="js">

import {Line} from "vue-chartjs";

export default {
  extends: Line,
  name: 'cases-bar-chart',
  props: [],
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  data: () => ({
    chartdata: {
      labels: ['KW33', 'KW34', 'KW35', 'KW36', 'KW37'],
      datasets: [
        {
          label: 'Indexfälle',
          backgroundColor: '#46ff9f',
          data: [12, 22, 9, 32, 11]
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false
    }
  }),

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  mounted() {
    this.renderChart(this.chartdata, this.options)
  },
  methods: {},
  computed: {}
}
</script>
