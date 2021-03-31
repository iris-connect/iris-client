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
          :items="guests"
          :items-per-page="5"
          class="elevation-1 mt-5"
          :search="tableData.search"
          show-select
          v-model="tableData.select"
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
            <v-btn
              class="mr-2 float-right"
              color="primary"
              @click="handleExport"
              :disabled="tableData.select.length <= 0"
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
import { Sex } from "@/api";
import router from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import DataExport from "@/utils/DataExport";

type EventData = {
  extID: string;
  name: string;
  address: string;
  startTime: string;
  endTime: string;
  gereratedTime: string;
  status: string;
  lastChange: string;
};

type TableRow = {
  lastName: string;
  firstName: string;
  checkInTime: string;
  checkOutTime: string;
  maxDuration: string;
  comment: string;
  sex: string;
  email: string;
  phone: string;
  mobilPhone: string;
  address: string;
};

@Component({
  components: {
    EventTrackingDetailsView: EventTrackingDetailsView,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch("eventTrackingDetails/fetchEventTrackingDetails", [
      router.currentRoute.params.id,
    ]);
  },
})
export default class EventTrackingDetailsView extends Vue {
  eventId = router.currentRoute.params.id;
  tableData = {
    search: "",
    expanded: [],
    select: [],
    headers: [
      { text: "", value: "data-table-select" },
      {
        text: "Nachname",
        value: "lastName",
        align: "start",
      },
      {
        text: "Vorname",
        value: "firstName",
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
  };

  get eventData(): EventData {
    const dataRequest = store.state.eventTrackingDetails.eventTrackingDetails;
    return {
      extID: dataRequest?.externalRequestId || "-",
      name: dataRequest?.name || "-",
      address: dataRequest?.locationInformation?.contact.address.street || "-",
      startTime: dataRequest?.start
        ? `${new Date(dataRequest.start).toLocaleDateString(
            "de-DE"
          )}, ${new Date(dataRequest.start).toLocaleTimeString("de-DE")}`
        : "-",
      endTime: dataRequest?.end
        ? `${new Date(dataRequest.end).toLocaleDateString("de-DE")}, ${new Date(
            dataRequest.end
          ).toLocaleTimeString("de-DE")}`
        : "-",
      gereratedTime: "-", // TODO: what property to show here?
      status: dataRequest?.status?.toString() || "-",
      lastChange: "-", // TODO: what property to show here?
    };
  }

  get guests(): TableRow[] {
    const dataRequests =
      store.state.eventTrackingDetails.eventTrackingDetails?.guests || [];
    const eventDataRequest =
      store.state.eventTrackingDetails.eventTrackingDetails;
    return dataRequests.map((dataRequest, index) => {
      const checkOut = new Date(dataRequest.attendanceInformation.attendTo);
      const checkIn = new Date(dataRequest.attendanceInformation.attendFrom);
      const startTime = eventDataRequest?.start
        ? new Date(eventDataRequest.start)
        : checkIn;
      const endTime = eventDataRequest?.end
        ? new Date(eventDataRequest.end)
        : checkOut;
      const duration =
        Math.min(checkOut.valueOf(), endTime.valueOf()) -
        Math.max(checkIn.valueOf(), startTime.valueOf());
      return {
        id: index,
        lastName: dataRequest.lastName || "-",
        firstName: dataRequest.firstName || "-",
        checkInTime: `${new Date(
          dataRequest.attendanceInformation.attendFrom
        ).toLocaleDateString("de-DE")}, ${new Date(
          dataRequest.attendanceInformation.attendFrom
        ).toLocaleTimeString("de-DE")}`,
        checkOutTime: `${new Date(
          dataRequest.attendanceInformation.attendTo
        ).toLocaleDateString("de-DE")}, ${new Date(
          dataRequest.attendanceInformation.attendTo
        ).toLocaleTimeString("de-DE")}`,
        maxDuration:
          duration > 0
            ? `${new Date(duration).getHours()}h, ${new Date(
                duration
              ).getMinutes()}min`
            : "keine",
        comment: "-", // TODO: descriptionOfParticipation or additionalInformation?
        sex: dataRequest.sex ? this.getSexName(dataRequest.sex) : "-",
        email: dataRequest.email || "-",
        phone: dataRequest.phone || "-",
        mobilPhone: dataRequest.mobilePhone || "-",
        address: dataRequest.address?.street || "-",
      };
    });
  }

  on(): void {
    console.log("NOT IMPLEMENTED");
  }

  getSexName(sex: Sex): string {
    switch (sex) {
      case Sex.Male:
        return "m";
      case Sex.Female:
        return "w";
      case Sex.Other:
        return "d";
      default:
        return "Unbekannt";
    }
  }

  handleExport(): void {
    DataExport.exportCsv(
      this.tableData.headers,
      this.tableData.select,
      [this.eventId, Date.now()].join("_")
    );
  }
}
</script>
