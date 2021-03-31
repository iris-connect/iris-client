<template>
  <div class="home">
    <v-row>
      <v-col>
        <counter-widget
          subtitle="Ereignisse/Woche"
          count="233"
          actionlabel="Zur Fallübersicht"
          image="sketch_file_analysis.svg"
          actionlink="ereignisse/liste"
        ></counter-widget>
      </v-col>
      <v-col>
        <counter-widget
          subtitle="Indexfälle/Woche"
          count="23"
          actionlabel="Zur Indexübersicht"
          image="sketch_medicine.svg"
          actionlink="ereignisse/liste"
        ></counter-widget>
      </v-col>
      <v-col>
        <counter-widget
          subtitle="Statusänderungen"
          count="12"
          actionlabel="Anzeigen"
          image="sketch_reviewed_docs.svg"
          actionlink="ereignisse/liste"
        ></counter-widget>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <v-card color="primary" class="pb-8 pt-2 pl-2">
          <v-container>
            <h2 class="light-font mb-6">
              Herzlich wilkommen bei IRIS - dem offenen Portal des
              Gesundheitsamtes
            </h2>
            <v-row>
              <v-col>
                <v-btn
                  color="primary"
                  :to="{ name: 'event-new' }"
                  class="mt-5 mb-3"
                  >Neue Ereignisverfolgung starten
                </v-btn>
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-btn color="primary" to="routeEventTrackingForm" class="mb-5"
                  >Neu Fall indexieren
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </v-col>
      <v-col>
        <v-card class="pb-8 pl-2">
          <v-container>
            <h2 class="light-font mb-6">Ticket Tracker</h2>
            <cases-pie-chart></cases-pie-chart>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card>
          <event-list></event-list>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card class="pb-3 pl-3 pt-3 pt-3">
          <cases-bar-chart></cases-bar-chart>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import CounterWidget from "@/components/dashboard/counter-widget.vue";
import CasesBarChart from "@/components/dashboard/cases-bar-chart.vue";
import CasesPieChart from "@/components/dashboard/cases-pie-chart.vue";
import EventList from "@/components/event-list.vue";

@Component({
  components: {
    EventList,
    CasesPieChart,
    CasesBarChart,
    CounterWidget,
  },
})
export default class Home extends Vue {}
</script>

<style lang="scss" scoped>
.home {
  > * {
    margin: 1em;
  }
}
</style>
