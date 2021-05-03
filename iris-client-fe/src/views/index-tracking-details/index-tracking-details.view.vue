<template>
  <div>
    <v-card>
      <v-card-title
        >Details für Indexfall ID: {{ indexData.extID }}</v-card-title
      >
      <v-card-text>
        <v-row class="align-center">
          <v-col cols="12" md="6">
            <strong> Index-Bezeichner: </strong>
            {{ indexData.name }}
          </v-col>
          <v-col cols="12" md="6">
            <span class="d-inline-block mr-3">
              <strong> Status: </strong>
            </span>
            <v-chip :color="getStatusColor(indexData.status)" dark>
              {{ getStatusName(indexData.status) }}
            </v-chip>
          </v-col>
        </v-row>
        <v-row class="align-center mb-3">
          <v-col>
            <div>
              <strong> Zeitraum: </strong>
              {{ indexData.startTime }} - {{ indexData.endTime }}
            </div>
            <div>
              <strong> Kommentar: </strong>
              {{ indexData.comment }}
            </div>
            <div>
              <strong> TAN: </strong>
              {{ indexData.tan }}
            </div>
          </v-col>
        </v-row>
        <v-tabs @change="handleTabsChange">
          <v-tab>Kontakte ({{ indexData.contactCount }})</v-tab>
          <v-tab>Ereignisse ({{ indexData.eventCount }})</v-tab>
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
      </v-card-text>
      <v-card-actions>
        <v-btn color="white" @click="$router.back()"> Zurück </v-btn>
        <v-spacer />
        <v-btn
          v-if="currentTab === 0"
          color="primary"
          :disabled="tableDataContacts.select.length <= 0"
          @click="handleContactsExport"
        >
          Kontaktdaten exportieren
        </v-btn>
        <v-btn
          v-if="currentTab === 1"
          color="primary"
          :disabled="tableDataEvents.select.length <= 0"
          @click="handleEventsExport"
        >
          Ereignisdaten exportieren
        </v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>
<style></style>
<script lang="ts">
import { Address, DataRequestCaseDetailsStatusEnum, Sex } from "@/api";
import router from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import DataExport from "@/utils/DataExport";
import Genders from "@/constants/Genders";
import StatusMessages from "@/constants/StatusMessages";
import StatusColors from "@/constants/StatusColors";

type IndexData = {
  extID: string;
  name: string;
  startTime: string;
  endTime: string;
  comment: string;
  eventCount: number;
  contactCount: number;
  tan: string;
  status: string;
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
      contactCount: contacts.length,
      eventCount: events.length,
      comment: dataRequest?.comment || "-",
      tan: "-", // TODO: TAN needed
      status: dataRequest?.status || "",
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

  currentTab = 0;
  handleTabsChange(index: number): void {
    this.currentTab = index;
  }

  getStatusName(status: DataRequestCaseDetailsStatusEnum): string {
    return StatusMessages.getMessage(status);
  }

  getStatusColor(status: DataRequestCaseDetailsStatusEnum): string {
    return StatusColors.getColor(status);
  }

  getSexName(sex: Sex): string {
    return Genders.getName(sex);
  }

  handleContactsExport(): void {
    DataExport.exportCsv(
      [
        ...this.tableDataContacts.headers,
        ...this.tableDataContacts.expandedHeaders,
      ],
      this.tableDataContacts.select,
      [this.indexData.extID, Date.now()].join("_")
    );
  }

  handleEventsExport(): void {
    DataExport.exportCsv(
      [...this.tableDataEvents.headers],
      this.tableDataEvents.select,
      [this.indexData.extID, Date.now()].join("_")
    );
  }
}
</script>
