<template>
  <div class="home">
    <v-row>
      <v-col>
        <counter-widget
          subtitle="Ereignisse/Woche"
          :count="statistics.eventsCount"
          actionlabel="Zur Ereignisübersicht"
          image="sketch_file_analysis.svg"
          actionlink="events/list"
        ></counter-widget>
      </v-col>
      <v-col>
        <counter-widget
          subtitle="Indexfälle/Woche"
          :count="statistics.indexCasesCount"
          actionlabel="Zur Indexübersicht"
          image="sketch_medicine.svg"
          actionlink="cases/list"
          :linkDisabled="true"
        ></counter-widget>
      </v-col>
      <v-col>
        <counter-widget
          subtitle="Statusänderungen"
          :count="statistics.sumStatus"
          actionlabel="Anzeigen"
          image="sketch_reviewed_docs.svg"
          actionlink="events/list"
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
                <v-btn
                  color="primary"
                  :to="{ name: 'index-new' }"
                  class="mb-5"
                  :disabled="true"
                  >Indexfall-Daten anfordern
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </v-col>
      <!--      <v-col>-->
      <!--        <v-card class="pb-8 pl-2">-->
      <!--          <v-container>-->
      <!--            <h2 class="light-font mb-6">Ticket Tracker</h2>-->
      <!--            <cases-pie-chart></cases-pie-chart>-->
      <!--          </v-container>-->
      <!--        </v-card>-->
      <!--      </v-col>-->
    </v-row>
    <v-row>
      <v-col>
        <v-card>
          <event-list :tableRowData="openEventListData"></event-list>
        </v-card>
      </v-col>
    </v-row>
    <v-row v-if="eventTrackingListError">
      <v-col>
        <v-alert text type="error">{{ eventTrackingListError }}</v-alert>
      </v-col>
    </v-row>
    <!--    <v-row>-->
    <!--      <v-col>-->
    <!--        <v-card class="pb-3 pl-3 pt-3 pt-3">-->
    <!--          <cases-bar-chart></cases-bar-chart>-->
    <!--        </v-card>-->
    <!--      </v-col>-->
    <!--    </v-row>-->
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import CounterWidget from "@/components/dashboard/counter-widget.vue";
// import CasesBarChart from "@/components/dashboard/cases-bar-chart.vue";
// import CasesPieChart from "@/components/dashboard/cases-pie-chart.vue";
import EventList from "@/components/event-list.vue";
import store from "@/store";
import { ExistingDataRequestClientWithLocation, Statistics } from "@/api";
import { TableRow } from "@/components/event-list.vue";
import { ErrorMessage } from "@/utils/axios";
import StatusColors from "@/constants/StatusColors";
import StatusMessages from "@/constants/StatusMessages";

const tableRowMapper = (
  dataRequest: ExistingDataRequestClientWithLocation
): TableRow => {
  return {
    address: getFormattedAddress(dataRequest),
    endTime: getFormattedDate(dataRequest.end),
    startTime: getFormattedDate(dataRequest.start),
    generatedTime: getFormattedDate(dataRequest.requestedAt),
    lastChange: getFormattedDate(dataRequest.lastUpdatedAt),
    extID: dataRequest.externalRequestId || "-",
    code: dataRequest.code || "-",
    name: dataRequest.name || "-",
    status: dataRequest.status?.toString() || "-",
    statusColor: StatusColors.getColor(dataRequest.status),
    statusName: StatusMessages.getMessage(dataRequest.status),
  };
};

function getFormattedAddress(
  data?: ExistingDataRequestClientWithLocation
): string {
  if (data) {
    const contact = data.locationInformation?.contact;
    if (contact) {
      return `${data.name}, ${contact.address.street}, ${contact.address.zip} ${contact.address.city}`;
    }
    return data.name || "-"; // TODO repeating - improve
  }
  return "-";
}

function getFormattedDate(date?: string): string {
  return date
    ? `${new Date(date).toDateString()}, ${new Date(date).toLocaleTimeString()}`
    : "-";
}

@Component({
  components: {
    EventList,
    // CasesPieChart,
    // CasesBarChart,
    CounterWidget,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch("home/fetchEventTrackingList");
    await store.dispatch("home/fetchStatistics");
  },
  beforeRouteLeave(to, from, next) {
    store.commit("home/reset");
    next();
  },
})
export default class Home extends Vue {
  get eventTrackingListError(): ErrorMessage {
    return store.state.home.eventTrackingListError;
  }

  get openEventListData(): TableRow[] {
    const dataRequests = store.state.home.eventTrackingList || [];
    return dataRequests.map(tableRowMapper);
  }

  get statistics(): Statistics {
    return store.state.home.statistics;
  }
}
</script>

<style lang="scss" scoped>
.home {
  > * {
    margin-top: 1em;

    &:last-child {
      margin-bottom: 1em;
    }
  }
}
</style>
