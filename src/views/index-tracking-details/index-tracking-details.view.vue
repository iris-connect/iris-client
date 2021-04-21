<template>
  <div>
    <v-card>
      <v-card-title
        >Details für Ereignis ID: {{ indexData.extID }}</v-card-title
      >
      <v-card-text>
        <v-col cols="8">
          <v-row>
            Index-Bezeichner:
            {{ indexData.name }}
          </v-row>
          <v-row>
            Kommentar:
            {{ indexData.comment }}
          </v-row>
          <v-row>
            TAN:
            {{ indexData.tan }}
          </v-row>
          <v-row>
            Zeitraum:
            {{ indexData.startTime }} - {{ indexData.endTime }}
          </v-row>
        </v-col>
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
  DataRequestCaseDetails,
  DataRequestCaseDetailsStatusEnum,
  Sex,
} from "@/api";
import router from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import DataExport from "@/utils/DataExport";

type IndexData = {
  extID: string;
  name: string;
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
  mobilePhone: string;
  tan: string;
};

function getFormattedAddressWithContact(
  data: DataRequestCaseDetails | null
): string {
  if (data) {
    return "-";
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
    IndexTrackingDetailsView: IndexTrackingDetailsView,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch("indexTrackingDetails/fetchIndexTrackingDetails", [
      router.currentRoute.params.id,
    ]);
  },
  beforeRouteLeave(to, from, next) {
    store.commit("indexTrackingDetails/reset");
    next();
  },
})
export default class IndexTrackingDetailsView extends Vue {
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

  get indexData(): IndexData {
    const dataRequest = store.state.indexTrackingDetails.indexTrackingDetails;
    return {
      extID: dataRequest?.externalCaseId || "-",
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
      gereratedTime: "-", // TODO: what property to show here?
      status: dataRequest?.status?.toString() || "-",
      lastChange: "-", // TODO: what property to show here?
    };
  }

  get listLoading(): boolean {
    return store.state.indexTrackingDetails.indexTrackingDetailsLoading;
  }

  get statusDataRequested(): boolean {
    if (!store.state.indexTrackingDetails.indexTrackingDetails) return false;
    return (
      store.state.indexTrackingDetails.indexTrackingDetails.status ===
      DataRequestCaseDetailsStatusEnum.DataRequested
    );
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
      [this.indexData.extID, Date.now()].join("_")
    );
  }
}
</script>
