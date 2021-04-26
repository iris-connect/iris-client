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
        <br />
        <tabs>
          <tab title="Kontakte" :counter="indexData.contactCount">
            <v-text-field
              v-model="tableDataContacts.search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
            <v-data-table
              :loading="listLoading"
              :headers="tableDataContacts.headers"
              :items="contacts"
              :items-per-page="5"
              class="elevation-1 mt-5"
              :search="tableDataContacts.search"
              show-select
              v-model="tableDataContacts.select"
              show-expand
              single-expand
              :expanded.sync="tableDataContacts.expanded"
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
                      v-for="(expandedHeader, ehIndex) in tableDataContacts.expandedHeaders"
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
          </tab>
          <tab title="Events" :counter="indexData.eventsCount">
            <v-text-field
              v-model="tableDataEvents.search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
            <v-data-table
              :loading="listLoading"
              :headers="tableDataEvents.headers"
              :items="events"
              :items-per-page="5"
              class="elevation-1 mt-5"
              :search="tableDataEvents.search"
              show-select
              v-model="tableDataEvents.select"
              show-expand
              single-expand
              :expanded.sync="tableDataEvents.expanded"
              @click:row="(item, slot) => slot.expand(!slot.isExpanded)"
            >
              <template v-if="statusDataRequested" #no-data>
                <span class="black--text">
                  Die Daten zu diesem Ereignis werden derzeit angefragt. Zum
                  jetzigen Zeitpunkt liegen noch keine Daten vor.
                </span>
              </template>
              <template v-slot:expanded-item="{ headers, item }">
                <td></td>
                <td :colspan="headers.length - 1">
                  <v-row>
                    <template
                      v-for="(expandedHeader, ehIndex) in tableDataEvents.expandedHeaders"
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
          </tab>
        </tabs>
        <v-row class="mt-2">
          <v-col cols="8">
            <v-btn class="ml-2 mr-2" color="white" @click="$router.back()">
              Zurück
            </v-btn>
          </v-col>
          <v-col cols="2">
            <span style="font-size: 1.25rem;">{{indexData.contactCount}} Kontakte / {{indexData.eventCount}} Events</span>
          </v-col>
          <v-col cols="2">
            <v-btn
              class="mr-2 float-right"
              color="primary"
              @click="handleExport"
              :disabled="tableDataEvents.select.length <= 0"
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
import Tab from "@/views/index-tracking-details/components/tab.vue";
import Tabs from "@/views/index-tracking-details/components/tabs.vue";
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
    Tab,
    Tabs
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
  tableDataContacts = {
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

  tableDataEvents = {
    search: "",
    expanded: [],
    select: [],
    headers: [
      { text: "", value: "data-table-select" },
      {
        text: "Event",
        value: "name",
        align: "start",
      },
      {
        text: "Ort",
        value: "address",
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
      {
        text: "",
        value: "data-table-expand"
      },
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
      [...this.tableDataContacts.headers, ...this.tableDataContacts.expandedHeaders],
      this.tableDataContacts.select,
      [this.indexData.extID, Date.now()].join("_")
    );
  }
}
</script>
