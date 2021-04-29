<template>
  <div>
    <v-row>
      <v-col cols="12">
        <div class="mb-6">
          <v-btn class="float-right" color="primary" :to="{ name: 'event-new' }"
            >Neue Ereignisverfolgung starten
          </v-btn>
        </div>
      </v-col>
    </v-row>
    <v-row class="mb-6">
      <v-col cols="8">
        Status:
        <v-btn-toggle dense mandatory v-model="statusButtonSelected">
          <v-btn
            @click="filterStatus(statusEnum.DataRequested)"
            style="opacity: 100%; background-color: white"
          >
            {{ getStatusName(statusEnum.DataRequested) }}
          </v-btn>
          <v-btn
            @click="filterStatus(statusEnum.DataReceived)"
            style="opacity: 100%; background-color: white"
          >
            {{ getStatusName(statusEnum.DataReceived) }}
          </v-btn>
          <v-btn
            @click="filterStatus(statusEnum.Closed)"
            style="opacity: 100%; background-color: white"
          >
            {{ getStatusName(statusEnum.Closed) }}
          </v-btn>
          <v-btn
            @click="filterStatus(null)"
            style="opacity: 100%; background-color: white"
          >
            Alle
          </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>

    <v-card>
      <v-card-title>Ereignisnachverfolgungen</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-data-table
          :loading="eventListLoading"
          :headers="tableData.headers"
          :items="eventList"
          :items-per-page="5"
          class="elevation-1 mt-5 twolineTable"
          :search="tableData.search"
        >
          <template v-slot:[itemStatusSlotName]="{ item }">
            <v-chip :color="getStatusColor(item.status)" dark>
              {{ getStatusName(item.status) }}
            </v-chip>
          </template>
          <template v-slot:[itemActionSlotName]="{ item }">
            <!-- TODO use imported route name -->
            <v-btn
              color="primary"
              :to="{ name: 'event-details', params: { id: item.code } }"
            >
              Details
            </v-btn>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import {
  ExistingDataRequestClientWithLocationStatusEnum,
  ExistingDataRequestClientWithLocation,
} from "@/api";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";
import { orderBy } from "lodash";
import dayjs from "@/utils/date";

function getFormattedAddress(
  data?: ExistingDataRequestClientWithLocation
): string {
  if (data) {
    if (data.locationInformation) {
      const contact = data.locationInformation.contact;
      if (contact) {
        return `${data.locationInformation.name}, ${contact.address.street}, ${contact.address.zip} ${contact.address.city}`;
      }
      return data.locationInformation.name;
    }
    return "-";
  }
  return "-";
}

function getFormattedDate(date?: string): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

type TableRow = {
  address: string;
  endTime: string;
  extID: string;
  generatedTime: string;
  lastChange: string;
  name: string;
  startTime: string;
  status: string;
};

@Component({
  components: {
    EventTrackingFormView: EventTrackingFormView,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch("eventTrackingList/fetchEventTrackingList");
  },
  beforeRouteLeave(to, from, next) {
    store.commit("eventTrackingList/reset");
    next();
  },
})
export default class EventTrackingListView extends Vue {
  statusFilter: ExistingDataRequestClientWithLocationStatusEnum | null = null;
  statusEnum = ExistingDataRequestClientWithLocationStatusEnum;
  statusButtonSelected = 3;
  filterStatus(
    target: ExistingDataRequestClientWithLocationStatusEnum | null
  ): void {
    this.statusFilter = target;
  }

  tableData = {
    search: "",
    headers: [
      {
        text: "Ext.ID",
        align: "start",
        sortable: true,
        value: "extID",
      },
      { text: "Event", value: "name" },
      { text: "Ort", value: "address" },
      { text: "Zeit (Start)", value: "startTime" },
      { text: "Zeit (Ende)", value: "endTime" },
      { text: "Generiert", value: "generatedTime" },
      { text: "Status", value: "status" },
      { text: "Letzte Änderung", value: "lastChange" },
      { text: "", value: "actions" },
    ],
  };

  get eventListLoading(): boolean {
    return store.state.eventTrackingList.eventTrackingListLoading;
  }

  get eventList(): TableRow[] {
    const dataRequests =
      store.state.eventTrackingList.eventTrackingList?.dataRequests || [];
    const list = dataRequests
      // TODO this filtering could probably also be done in vuetify data-table
      .filter(
        (dataRequests) =>
          !this.statusFilter || this.statusFilter === dataRequests.status
      )
      .map((dataRequest) => {
        return {
          address: getFormattedAddress(dataRequest),
          endTime: getFormattedDate(dataRequest.end),
          startTime: getFormattedDate(dataRequest.start),
          generatedTime: getFormattedDate(dataRequest.requestedAt),
          lastChange: getFormattedDate(dataRequest.lastUpdatedAt),
          extID: dataRequest.externalRequestId || "-",
          code: dataRequest.code,
          name: dataRequest.name || "-",
          status: dataRequest.status?.toString() || "-",
        };
      });
    // default sorting. Could be done in data-table but then there would be a sort icon in the header.
    return orderBy(list, "lastChange", "desc");
  }

  // TODO improve this - we need it to circumvent v-slot eslint errors
  // https://stackoverflow.com/questions/61344980/v-slot-directive-doesnt-support-any-modifier
  get itemStatusSlotName(): string {
    return "item.status";
  }
  get itemActionSlotName(): string {
    return "item.actions";
  }

  on(): void {
    console.log("NOT IMPLEMENTED");
  }

  selectItem(item: unknown): void {
    console.log("NOT IMPLEMENTED", item);
  }

  getStatusColor(
    status: ExistingDataRequestClientWithLocationStatusEnum
  ): string {
    switch (status) {
      case ExistingDataRequestClientWithLocationStatusEnum.DataRequested:
        return "blue";
      case ExistingDataRequestClientWithLocationStatusEnum.DataReceived:
        return "red";
      case ExistingDataRequestClientWithLocationStatusEnum.Closed:
        return "green";
      default:
        return "gray"; // TODO
    }
  }

  getStatusName(
    status: ExistingDataRequestClientWithLocationStatusEnum
  ): string {
    switch (status) {
      case ExistingDataRequestClientWithLocationStatusEnum.DataRequested:
        return "Angefragt";
      case ExistingDataRequestClientWithLocationStatusEnum.DataReceived:
        return "Geliefert";
      case ExistingDataRequestClientWithLocationStatusEnum.Closed:
        return "Abgeschlossen";
      default:
        return "Unbekannt"; // TODO find better name
    }
  }
}
</script>
