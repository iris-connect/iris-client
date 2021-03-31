<template>
  <div>
    <v-row>
      <v-col cols="12">
        <div>
          <v-dialog
            transition="dialog-bottom-transition"
            max-width="98%"
            width="1600px"
          >
            <template v-slot:activator="{ attrs }">
              <v-btn
                class="float-right"
                color="primary"
                v-bind="attrs"
                :to="routeEventTrackingForm"
                >Neue Ereignisverfolgung starten
              </v-btn>
            </template>
          </v-dialog>
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
      <v-card-subtitle>DISCLAIMER: DEMO TABELLE</v-card-subtitle>
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
          class="elevation-1 mt-5"
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
              :to="'/ereignisse/' + item.extID"
              @click="selectItem(item)"
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
  LocationContact,
} from "@/api";
import { ROUTE_NAME_EVENT_TRACKING_FORM } from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";

function getFormattedAddress(contact?: LocationContact) {
  if (contact) {
    return `${contact.officialName}, ${contact.address.street}, ${contact.address.zip} ${contact.address.city}`;
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
})
export default class EventTrackingListView extends Vue {
  routeEventTrackingForm = ROUTE_NAME_EVENT_TRACKING_FORM;

  statusFilter: ExistingDataRequestClientWithLocationStatusEnum | null = null;
  statusEnum = ExistingDataRequestClientWithLocationStatusEnum;
  statusButtonSelected = 3;
  filterStatus(
    target: ExistingDataRequestClientWithLocationStatusEnum | null
  ): void {
    this.statusFilter = target;
    switch (target) {
      case ExistingDataRequestClientWithLocationStatusEnum.DataRequested:
        this.statusButtonSelected = 0;
        break;
      case ExistingDataRequestClientWithLocationStatusEnum.DataReceived:
        this.statusButtonSelected = 1;
        break;
      case ExistingDataRequestClientWithLocationStatusEnum.Closed:
        this.statusButtonSelected = 2;
        break;
      default:
        this.statusButtonSelected = 3;
        break;
    }
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
      { text: "Letzte Ändrung", value: "lastChange" },
      { text: "", value: "actions" },
    ],
  };

  get eventListLoading(): boolean {
    return store.state.eventTrackingList.eventTrackingListLoading;
  }

  get eventList(): TableRow[] {
    const dataRequests =
      store.state.eventTrackingList.eventTrackingList?.dataRequests || [];
    return (
      dataRequests
        // TODO this filtering could probably also be done in vuetify data-table
        .filter(
          (dataRequests) =>
            !this.statusFilter || this.statusFilter === dataRequests.status
        )
        .map((dataRequest) => {
          return {
            // TODO formatted address
            address: getFormattedAddress(
              dataRequest.locationInformation?.contact
            ),
            endTime: dataRequest.end
              ? `${new Date(dataRequest.end).toDateString()}, ${new Date(
                  dataRequest.end
                ).toLocaleTimeString()}`
              : "-",
            startTime: dataRequest.start
              ? `${new Date(dataRequest.start).toDateString()}, ${new Date(
                  dataRequest.start
                ).toLocaleTimeString()}`
              : "-",
            extID: dataRequest.externalRequestId || "-",
            // generatedTime: new Date().toString() || "-",
            // lastChange: new Date().toString() || "-",
            name: dataRequest.name || "-",
            status: dataRequest.status?.toString() || "-",
            generatedTime: dataRequest.requestedAt
              ? `${new Date(
                  dataRequest.requestedAt
                ).toDateString()}, ${new Date(
                  dataRequest.requestedAt
                ).toLocaleTimeString()}`
              : "-",
            lastChange: dataRequest.lastUpdatedAt
              ? `${new Date(
                  dataRequest.lastUpdatedAt
                ).toDateString()}, ${new Date(
                  dataRequest.lastUpdatedAt
                ).toLocaleTimeString()}`
              : "-",
          };
        })
    );
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
