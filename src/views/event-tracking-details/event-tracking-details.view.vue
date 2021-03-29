<template>
  <div>
    <v-card>
      <v-card-title>Details für Ereignis {{ eventId }}</v-card-title>
      <v-card-text>
        <v-col cols="8">
          <v-row>
            Name:
            {{ eventData.name }}
          </v-row>
          <v-row>
            Ort:
            {{ eventData.address }}
          </v-row>
          <v-row>
            Zeitraum:
            {{ eventData.startTime }} - {{ eventData.endTime }}
          </v-row>
        </v-col>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-data-table
          :headers="tableData.headers"
          :items="tableData.eventParticipants"
          :items-per-page="5"
          class="elevation-1 mt-5"
          :search="tableData.search"
          show-select
        >
        </v-data-table>
        <v-row class="mt-2">
          <v-col cols="12">
            <v-btn class="ml-2 mr-2" color="white" @click="on">Zurück </v-btn>
            <v-btn class="mr-2 float-right" color="primary" @click="on"
              >Auswahl exportieren
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import router from "@/router";
import { Component, Vue } from "vue-property-decorator";

@Component
export default class EventTrackingDetailsView extends Vue {
  eventId = router.currentRoute.params.id;
  eventData = {
    extID: "GTOAZEIC",
    name: "Toms Bierbrunnen",
    address: "Nobistor 14, 22767 Hamburg",
    startTime: "30.03.2021 17:00",
    endTime: "30.03.2021 22:00",
    generatedTime: "31.03.2021 09:44",
    status: "Angefragt",
    lastChange: "31.03.2021 09:44",
  };
  tableData = {
    search: "",
    headers: [
      {
        text: "Nachname",
        value: "lastname",
        align: "start",
      },
      {
        text: "Vorname",
        value: "firstname",
      },
      {
        text: "Check-In",
        value: "checkInTime",
      },
      {
        text: "Check-Out",
        value: "checkOutTime",
      },
      {
        text: "Kommentar",
        value: "comment",
      },
    ],
    eventParticipants: [
      {
        id: "1",
        lastname: "Auerbach",
        firstname: "Dan",
        checkInTime: "26.03.2021 12:11",
        checkOutTime: "26.03.2021 12:57",
        comment: "An der Bar mit 2 weiteren Personen",
      },
      {
        id: "2",
        lastname: "Irvine",
        firstname: "Weldon",
        checkInTime: "26.03.2021 12:12",
        checkOutTime: "26.03.2021 13:57",
        comment: "Tisch 7",
      },
    ],
  };

  on(): void {
    console.log("NOT IMPLEMENTED");
  }
}
</script>
