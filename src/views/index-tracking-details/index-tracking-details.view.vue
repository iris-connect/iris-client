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
        <v-tabs>
          <v-tab>Kontakte ({{ indexData.contactCount }})</v-tab>
          <v-tab>Events ({{ indexData.eventCount }})</v-tab>
          <v-tab-item>
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
              <template v-slot:expanded-item="{ headers, item }">
                <td></td>
                <td :colspan="headers.length - 1">
                  <v-row>
                    <template
                      v-for="(
                        expandedHeader, ehIndex
                      ) in tableDataContacts.expandedHeaders"
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
          </v-tab-item>
          <v-tab-item>
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
            >
              <template v-slot:expanded-item="{ headers, item }">
                <td></td>
                <td :colspan="headers.length - 1">
                  <v-row>
                    <template
                      v-for="(
                        expandedHeader, ehIndex
                      ) in tableDataEvents.expandedHeaders"
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
          </v-tab-item>
        </v-tabs>
        <v-row class="mt-2">
          <v-col cols="8">
            <v-btn class="ml-2 mr-2" color="white" @click="$router.back()">
              Zurück
            </v-btn>
          </v-col>
          <v-col cols="2">
            <span style="font-size: 1.25rem"
              >{{ tableDataContacts.select.length }} Kontakte /
              {{ tableDataEvents.select.length }} Events</span
            >
          </v-col>
          <v-col cols="2">
            <v-btn
              class="mr-2 float-right"
              color="primary"
              @click="handleExport"
              :disabled="
                tableDataEvents.select.length +
                  tableDataContacts.select.length <=
                0
              "
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
import { Address, Sex } from "@/api";
import router from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import DataExport from "@/utils/DataExport";
import Genders from "@/constants/Genders";

type IndexData = {
  extID: string;
  name: string;
  startTime: string;
  endTime: string;
  gereratedTime: string;
  lastChange: string;
  comment: string;
  eventCount: number;
  contactCount: number;
  tan: string;
};

type TableRowContact = {
  lastName: string;
  firstName: string;
  dateOfBirth: string;
  sex: string;
  email: string;
  phone: string;
  mobilePhone: string;
};

type TableRowEvent = {
  name: string;
  phone: string;
  address: string;
  additionalInformation: string;
};

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
    await store.dispatch(
      "indexTrackingDetails/fetchIndexTrackingDetails",
      router.currentRoute.params.caseId
    );
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
        text: "Geburtsdatum",
        value: "dateOfBirth",
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
        text: "Telefonnummer",
        value: "phone",
      },
      {
        text: "Adresse",
        value: "address",
      },
      {
        text: "zus. Informationen",
        value: "additionalInformation",
      },
    ],
  };

  get indexData(): IndexData {
    const dataRequest = store.state.indexTrackingDetails.indexTrackingDetails;
    const contacts =
      dataRequest?.submissionData?.contacts?.contactPersons || [];
    const events = dataRequest?.submissionData?.events?.events || [];

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
      lastChange: "-", // TODO: what property to show here?
      contactCount: contacts.length,
      eventCount: events.length,
      comment: dataRequest?.comment || "-",
      tan: "-", // TODO: TAN needed
    };
  }

  get listLoading(): boolean {
    return store.state.indexTrackingDetails.indexTrackingDetailsLoading;
  }

  get contacts(): TableRowContact[] {
    const contacts =
      store.state.indexTrackingDetails.indexTrackingDetails?.submissionData
        ?.contacts?.contactPersons || [];
    return contacts.map((contact, index) => {
      return {
        id: index,
        lastName: contact.lastName || "-",
        firstName: contact.firstName || "-",
        dateOfBirth: contact.dateOfBirth || "-",
        sex: contact.sex ? this.getSexName(contact.sex) : "-",
        email: contact.email || "-",
        phone: contact.phone || "-",
        mobilePhone: contact.mobilePhone || "-",
        address: getFormattedAddress(contact.address) || "-",
      };
    });
  }

  get events(): TableRowEvent[] {
    const events =
      store.state.indexTrackingDetails.indexTrackingDetails?.submissionData
        ?.events?.events || [];
    return events.map((event, index) => {
      return {
        id: index,
        name: event.name || "-",
        phone: event.phone || "-",
        address: getFormattedAddress(event.address) || "-",
        additionalInformation: event.additionalInformation || "-",
      };
    });
  }

  getSexName(sex: Sex): string {
    return Genders.getName(sex);
  }

  handleExport(): void {
    DataExport.exportCsv(
      [
        ...this.tableDataContacts.headers,
        ...this.tableDataContacts.expandedHeaders,
      ],
      this.tableDataContacts.select,
      [this.indexData.extID, Date.now()].join("_")
    );
  }
}
</script>
