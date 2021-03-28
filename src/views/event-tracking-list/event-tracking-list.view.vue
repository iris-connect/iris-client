<template>
  <div>
    <h1 class="text-h3 mb-6">Ereignisnachverfolgungen</h1>
    <div class="mb-6">
      <v-dialog
          transition="dialog-bottom-transition"
          max-width="98%"
          width="1600px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="primary"
              v-bind="attrs"
              v-on="on"
            >Neue Ereignisverfolgung starten</v-btn>
          </template>
          <EventTrackingFormView></EventTrackingFormView>
        </v-dialog>
    </div>
    <v-card>
      <p>DISCLAIMER: DEMO TABELLE</p>
      <v-card-title>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
      </v-card-title>
      <v-data-table
        :headers="tableData.headers"
        :items="tableData.eventList"
        :items-per-page="5"
        class="elevation-1"
        :search="tableData.search"
      >
        <template v-slot:item.status="{ item }">
          <v-chip 
            :color="getStatusColor(item.status)"
            dark
          >
            {{ item.status }}
          </v-chip>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-btn
          color="primary"
          :to="'details/'+item.extID"
          @click="selectItem(item)">
            Details
          </v-btn>
        </template>
      </v-data-table>
    </v-card>
  </div>
</template>

<script lang="ts">
import { ROUTE_NAME_EVENT_TRACKING_FORM } from "@/router";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";
@Component({
    components: {
        "EventTrackingFormView": EventTrackingFormView
    }
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
      { text: "Generiert", value: "generatedTime" },
      { text: "Status", value: "status" },
      { text: "Letzte Ändrung", value: "lastChange" },
      { text: "", value: "actions" },
    ],
    eventList: [
      {
        extID: "GTOAZEIC",
        name: "Toms Bierbrunnen",
        address: "Nobistor 14, 22767 Hamburg",
        startTime: "30.03.2021 17:00",
        endTime: "30.03.2021 22:00",
        generatedTime: "31.03.2021 09:44",
        status: "Angefragt",
        lastChange: "31.03.2021 09:44",
      },
      {
        extID: "IEZDTEDA",
        name: "S&S Konzert 27.03",
        address: "Schick & Schön. Kaiserstraße 15, 5516 Mainz",
        startTime: "27.03.2021 19:00",
        endTime: "28.03.2021 05:00",
        generatedTime: "29.03.2021 10:21",
        status: "UPDATE",
        lastChange: "29.03.2021 14:15",
      },
    ],
  };
  getStatusColor(status: string) {
    if (status == "Angefragt") return "blue";
    else if (status == "UPDATE") return "red";
    else if (status == "Abgeschlossen") return "green";
  };
}
</script>
