<template>
  <div>
    <v-row>
      <v-col cols="8">
        <div class="mb-6">
          Status:
          <v-btn class="ml-2 mr-2" color="white" @click="on">Angefragt </v-btn>
          <v-btn class="mr-2" color="white" @click="on">Update </v-btn>
          <v-btn class="mr-2" color="white" @click="on">Geschlossen </v-btn>
        </div>
      </v-col>
      <v-col cols="4">
        <div class="mb-6">
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
          :headers="tableData.headers"
          :items="eventList"
          :items-per-page="5"
          class="elevation-1 mt-5"
          :search="tableData.search"
        >
          <template v-slot:[itemStatusSlotName]="{ item }">
            <v-chip :color="getStatusColor(item.status)" dark>
              {{ item.status }}
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
import { ExistingDataRequestClientWithLocationStatusEnum } from "@/api";
import { ROUTE_NAME_EVENT_TRACKING_FORM } from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";

type TableRow = {
  address: string;
  endTime: string;
  extID: string;
  // generatedTime: string;
  // lastChange: string;
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
      // { text: "Generiert", value: "generatedTime" },
      { text: "Status", value: "status" },
      // { text: "Letzte Ändrung", value: "lastChange" },
      { text: "", value: "actions" },
    ],
  };

  get eventList(): TableRow[] {
    const dataRequests =
      store.state.eventTrackingList.eventTrackingList?.dataRequests || [];
    return dataRequests.map((dataRequest) => {
      return {
        // TODO formatted address
        address: dataRequest.locationInformation?.contact.address.street || "-",
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
      };
    });
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
    // TODO use enum / string literals
    if (status == ExistingDataRequestClientWithLocationStatusEnum.DataRequested)
      return "blue";
    else if (
      status == ExistingDataRequestClientWithLocationStatusEnum.DataReceived
    )
      return "red";
    else if (status == ExistingDataRequestClientWithLocationStatusEnum.Closed)
      return "green";
    else throw Error("TODO this should not happen");
  }
}
</script>
