<template>
  <div>
    <v-card>
      <v-card-title
        >Details für Ereignis ID: {{ eventData.extID }}</v-card-title
      >
      <v-card-text>
        <v-row class="align-center">
          <v-col cols="12" md="6">
            <strong> Name: </strong>
            {{ eventData.name }}
          </v-col>
          <v-col cols="12" md="6">
            <span class="d-inline-block mr-3">
              <strong> Status: </strong>
            </span>
            <v-chip :color="getStatusColor(eventData.status)" dark>
              {{ getStatusName(eventData.status) }}
            </v-chip>
          </v-col>
        </v-row>
        <v-row class="align-center">
          <v-col>
            <strong> Zeitraum: </strong>
            {{ eventData.startTime }} - {{ eventData.endTime }}
          </v-col>
        </v-row>
        <event-tracking-details-location-info :location="eventData.location" />
        <v-row>
          <v-col cols="12" md="6">
            <strong> Generiert: </strong>
            {{ eventData.generatedTime }}
          </v-col>
          <v-col cols="12" md="6">
            <strong> Letzte Änderung: </strong>
            {{ eventData.lastChange }}
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <strong> Anfragedetails: </strong>
            {{ eventData.additionalInformation }}
          </v-col>
        </v-row>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-data-table
          :loading="listLoading"
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
          <template v-if="statusDataRequested" #no-data>
            <span class="black--text">
              Die Kontaktdaten zu diesem Ereignis werden derzeit angefragt. Zum
              jetzigen Zeitpunkt liegen noch keine Daten vor.
            </span>
          </template>
          <template v-slot:expanded-item="{ headers, item }">
            <td></td>
            <td :colspan="headers.length - 1">
              <v-row>
                <template
                  v-for="(expandedHeader, ehIndex) in tableData.expandedHeaders"
                >
                  <v-col :key="ehIndex" cols="12" sm="4" md="2">
                    <v-list-item two-line dense>
                      <v-list-item-content>
                        <v-list-item-title>
                          {{ expandedHeader.text }}
                        </v-list-item-title>
                        <v-list-item-subtitle>
                          {{
                            item[expandedHeader.value]
                              ? item[expandedHeader.value]
                              : "-"
                          }}
                        </v-list-item-subtitle>
                      </v-list-item-content>
                    </v-list-item>
                  </v-col>
                </template>
              </v-row>
            </td>
          </template>
        </v-data-table>
        <v-row class="mt-2">
          <v-col cols="12">
            <v-btn class="ml-2 mr-2" color="white" @click="$router.back()">
              Zurück
            </v-btn>
            <v-btn
              class="mr-2 float-right"
              color="primary"
              @click="handleExport"
              :disabled="tableData.select.length <= 0"
            >
              Auswahl exportieren
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>
<style></style>
<script lang="ts">
import {
  Address,
  LocationInformation,
  DataRequestDetailsStatusEnum,
  Sex,
} from "@/api";
import router from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import DataExport from "@/utils/DataExport";
import EventTrackingDetailsLocationInfo from "@/views/event-tracking-details/components/event-tracking-details-location-info.vue";
import dayjs from "@/utils/date";

type EventData = {
  extID: string;
  name: string;
  startTime: string;
  endTime: string;
  generatedTime: string;
  status?: DataRequestDetailsStatusEnum;
  lastChange: string;
  location?: LocationInformation;
  additionalInformation: string;
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
  mobilePhone: string;
  address: string;
};

function getFormattedDate(date?: string): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

function getFormattedAddress(address?: Address | null): string {
  if (address) {
    return `${address.street} ${address.houseNumber}, ${address.zipCode} ${address.city}`;
  }
  return "-";
}

@Component({
  components: {
    EventTrackingDetailsLocationInfo,
    EventTrackingDetailsView: EventTrackingDetailsView,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch("eventTrackingDetails/fetchEventTrackingDetails", [
      router.currentRoute.params.id,
    ]);
  },
  beforeRouteLeave(to, from, next) {
    store.commit("eventTrackingDetails/reset");
    next();
  },
})
export default class EventTrackingDetailsView extends Vue {
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
    expandedHeaders: [
      {
        text: "Geschlecht",
        value: "sex",
      },
      {
        text: "E-Mail",
        value: "email",
      },
      {
        text: "Telefon",
        value: "phone",
      },
      {
        text: "Mobil",
        value: "mobilePhone",
      },
      {
        text: "Adresse",
        value: "address",
      },
    ],
  };

  get eventData(): EventData {
    const dataRequest = store.state.eventTrackingDetails.eventTrackingDetails;
    return {
      extID: dataRequest?.externalRequestId || "-",
      name: dataRequest?.name || "-",
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
      generatedTime: getFormattedDate(dataRequest?.requestedAt),
      status: dataRequest?.status,
      lastChange: getFormattedDate(dataRequest?.lastUpdatedAt),
      location: dataRequest?.locationInformation,
      additionalInformation:
        dataRequest?.submissionData?.additionalInformation || "-",
    };
  }

  get listLoading(): boolean {
    return store.state.eventTrackingDetails.eventTrackingDetailsLoading;
  }

  get statusDataRequested(): boolean {
    if (!store.state.eventTrackingDetails.eventTrackingDetails) return false;
    return (
      store.state.eventTrackingDetails.eventTrackingDetails.status ===
      DataRequestDetailsStatusEnum.DataRequested
    );
  }

  getStatusName(status: DataRequestDetailsStatusEnum): string {
    switch (status) {
      case DataRequestDetailsStatusEnum.DataRequested:
        return "Angefragt";
      case DataRequestDetailsStatusEnum.DataReceived:
        return "Geliefert";
      case DataRequestDetailsStatusEnum.Closed:
        return "Abgeschlossen";
      default:
        return "-";
    }
  }

  getStatusColor(status: DataRequestDetailsStatusEnum): string {
    switch (status) {
      case DataRequestDetailsStatusEnum.DataRequested:
        return "blue";
      case DataRequestDetailsStatusEnum.DataReceived:
        return "red";
      case DataRequestDetailsStatusEnum.Closed:
        return "green";
      default:
        return "gray"; // TODO
    }
  }

  get guests(): TableRow[] {
    // TODO attendanceInformation is optional
    const guests =
      store.state.eventTrackingDetails.eventTrackingDetails?.submissionData
        ?.guests || [];
    const eventDataRequest =
      store.state.eventTrackingDetails.eventTrackingDetails;
    return guests.map((guest, index) => {
      const attendTo = guest.attendanceInformation?.attendTo;
      const checkOut = attendTo ? new Date(attendTo) : null;

      const attendFrom = guest.attendanceInformation?.attendFrom;
      const checkIn = attendFrom ? new Date(attendFrom) : null;

      const startTime = eventDataRequest?.start
        ? new Date(eventDataRequest.start)
        : checkIn;
      const endTime = eventDataRequest?.end
        ? new Date(eventDataRequest.end)
        : checkOut;

      let checkInTime = "-";
      if (checkIn && startTime) {
        checkInTime = `${checkIn.toLocaleDateString(
          "de-DE"
        )}, ${checkIn.toLocaleTimeString("de-DE")}`;
      }

      let checkOutTime = "-";
      if (checkOut) {
        checkOutTime = `${checkOut.toLocaleDateString(
          "de-DE"
        )}, ${checkOut.toLocaleTimeString("de-DE")}`;
      }

      // min(iE, cE)-max(iS, cS)
      // |--------------| TIME INFECTED PERSON _i Started and Ended_ AT LOCATION
      //       |----------------| TIME CONTACT PERSON _c Started and Ended_ AT LOCATION
      //       |--------| RELEVANT

      let maxDuration = "keine";
      if (checkOut && checkIn && startTime && endTime) {
        let durationSeconds =
          (Math.min(checkOut.valueOf(), endTime.valueOf()) -
            Math.max(checkIn.valueOf(), startTime.valueOf())) /
          1000;
        let hours = Math.floor(durationSeconds / 3600);
        let minutes = Math.round((durationSeconds - hours * 3600) / 60);
        if (durationSeconds > 0) {
          if (hours > 0)
            maxDuration = hours.toString() + "h " + minutes.toString() + "min";
          else if (minutes > 0) maxDuration = minutes.toString() + "min";
        }
      }
      return {
        id: index,
        lastName: guest.lastName || "-",
        firstName: guest.firstName || "-",
        checkInTime,
        checkOutTime,
        maxDuration: maxDuration,
        comment: guest.attendanceInformation.additionalInformation || "-", // TODO: Line Breaks
        sex: guest.sex ? this.getSexName(guest.sex) : "-",
        email: guest.email || "-",
        phone: guest.phone || "-",
        mobilePhone: guest.mobilePhone || "-",
        address: getFormattedAddress(guest.address),
      };
    });
  }

  /**
   * @deprecated
   */
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
      [...this.tableData.headers, ...this.tableData.expandedHeaders],
      this.tableData.select,
      [this.eventData.extID, Date.now()].join("_")
    );
  }
}
</script>
