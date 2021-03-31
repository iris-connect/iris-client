<script lang="js">

import {Doughnut} from "vue-chartjs";

export default  {
  extends: Doughnut,
  name: 'cases-pie-chart',
  props: [],
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  data: () => ({
    chartdata: {
      labels: ['Angefragt', 'Update', 'Geschlossen '],
      datasets: [
        {
          label: 'Indexfälle',
          backgroundColor: ['#0E6A81', '#00B6B0', '#46ff9f'],
          data: [12, 8,39]
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false
    }
  }),

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  mounted () {
    this.renderChart(this.chartdata, this.options)
  },
  methods: {

  },
  computed: {

  }
}
</script>
