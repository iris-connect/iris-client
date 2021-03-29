<template>
  <div>
    <v-card>
      <v-card-title>Details für Ereignis ID: {{ eventId }}</v-card-title>
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
          :select.sync="tableData.select"
          show-expand
          single-expand
          :expanded.sync="tableData.expanded"
          @click:row="(item, slot) => slot.expand(!slot.isExpanded)"
        >
          <template v-slot:expanded-item="{ headers, item }">
            <td></td>
            <td :colspan="headers.length - 1">
              <v-row>
                <v-col cols="12" md="2">
                  <v-list-item two-line dense>
                    <v-list-item-content>
                      <v-list-item-title>Geschlecht</v-list-item-title>
                      <v-list-item-subtitle>{{
                        item.sex ? item.sex : "-"
                      }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-col>
                <v-col>
                  <v-list-item two-line dense>
                    <v-list-item-content>
                      <v-list-item-title>E-Mail</v-list-item-title>
                      <v-list-item-subtitle>{{
                        item.email ? item.email : "-"
                      }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-col>
                <v-col>
                  <v-list-item two-line dense>
                    <v-list-item-content>
                      <v-list-item-title>Telefon</v-list-item-title>
                      <v-list-item-subtitle>{{
                        item.phone ? item.phone : "-"
                      }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-col>
                <v-col>
                  <v-list-item two-line dense>
                    <v-list-item-content>
                      <v-list-item-title>Mobil</v-list-item-title>
                      <v-list-item-subtitle>{{
                        item.mobilPhone ? item.mobilPhone : "-"
                      }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-col>
                <v-col>
                  <v-list-item two-line dense>
                    <v-list-item-content>
                      <v-list-item-title>Adresse</v-list-item-title>
                      <v-list-item-subtitle>{{
                        item.address ? item.address : "-"
                      }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-col>
              </v-row>
            </td>
          </template>
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
<style></style>
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
    expanded: [],
    select: [],
    headers: [
      { text: "", value: "data-table-select" },
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
        text: "max. Kontaktdauer",
        value: "maxDuration",
      },
      {
        text: "Kommentar",
        value: "comment",
      },
      { text: "", value: "data-table-expand" },
    ],
    eventParticipants: [
      {
        id: "1",
        lastname: "Auerbach",
        firstname: "Dan",
        checkInTime: "26.03.2021 12:11",
        checkOutTime: "26.03.2021 12:57",
        comment: "An der Bar",
        maxDuration: "1h 15min",
        dateOfBirth: "20.06.1978",
        sex: "",
        email: "dan.auerbach@mail.de",
        phone: "",
        mobilPhone: "",
        address: "Fichtenweg 10, 58022 Meisterstadt",
      },
      {
        id: "2",
        lastname: "Irvine",
        firstname: "Weldon",
        checkInTime: "26.03.2021 12:12",
        checkOutTime: "26.03.2021 13:57",
        comment: "Tisch 7",
        maxDuration: "3min 12s",
        dateOfBirth: "03.11.1999",
        sex: "",
        email: "Irvine.Weldon@mail.de",
        phone: "0121 1234567",
        mobilPhone: "",
        address: "Hausstraße 3a, 58022 Meisterstadt",
      },
    ],
  };

  on(): void {
    console.log("NOT IMPLEMENTED");
  }
}
</script>
