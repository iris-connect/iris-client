<template lang="html">
  <section class="src-components-dashboard-timeline">
    <v-timeline>
      <v-timeline-item>timeline item</v-timeline-item>
      <v-timeline-item class="text-right"> timeline item </v-timeline-item>
      <v-timeline-item>timeline item</v-timeline-item>
    </v-timeline>
  </section>
</template>

<script lang="js">
import { Bar } from 'vue-chartjs'

  export default  {
    extends: Bar,
    name: 'timeline-widget',
    props: [],
    // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
    data: () => ({
      chartdata: {
        labels: ['January', 'February'],
        datasets: [
          {
            label: 'Data One',
            backgroundColor: '#f87979',
            data: [40, 20]
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
