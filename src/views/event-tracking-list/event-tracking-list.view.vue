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
          <v-btn
            class="float-right"
            color="primary"
            v-bind="attrs"
            :to="{ name: 'event-new' }"
            >Neue Ereignisverfolgung starten
          </v-btn>
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
          :items="tableData.eventList"
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
              :to="{ name: 'event-details', params: { id: item.extID } }"
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
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";

@Component({
  components: {
    EventTrackingFormView: EventTrackingFormView,
  },
})
export default class EventTrackingListView extends Vue {
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

  getStatusColor(status: string): string {
    // TODO use enum / string literals
    if (status == "Angefragt") return "blue";
    else if (status == "UPDATE") return "red";
    else if (status == "Abgeschlossen") return "green";
    else throw Error("TODO this should not happen");
  }
}
</script>
