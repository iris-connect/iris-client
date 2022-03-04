<template>
  <div>
    <alert-component v-if="alert">
      <template v-slot:message>
        Die Kontaktdaten zu diesem Indexfall wurden angefragt.
      </template>
    </alert-component>
    <entry-meta-data :entry="indexTrackingDetails" />
    <v-card>
      <v-card-title data-test="case.externalId">
        Details für Indexfall ID: {{ indexData.extID }}
      </v-card-title>
      <v-card-text>
        <v-row class="align-center">
          <v-col cols="12" md="6" data-test="case.name">
            <strong> Index-Bezeichner: </strong>
            {{ indexData.name }}
          </v-col>
          <v-col cols="12" md="6">
            <span class="d-inline-block mr-3">
              <strong> Status: </strong>
            </span>
            <v-chip
              :color="getStatusColor(indexData.status)"
              dark
              data-test="case.status"
            >
              {{ getStatusName(indexData.status) }}
            </v-chip>
          </v-col>
        </v-row>
        <v-row class="align-center mb-3">
          <v-col>
            <div data-test="case.duration">
              <strong> Zeitraum: </strong>
              {{ indexData.startTime }} - {{ indexData.endTime }}
            </div>
            <div data-test="case.comment">
              <strong> Kommentar: </strong>
              {{ indexData.comment }}
            </div>
            <div data-test="case.tan">
              <strong> TAN: </strong>
              {{ indexData.tan }}
            </div>
          </v-col>
        </v-row>
        <v-tabs @change="handleTabsChange">
          <v-tab data-test="case.contacts.tab">
            Kontakte ({{ indexData.contactCount }})
          </v-tab>
          <v-tab data-test="case.events.tab">
            Ereignisse ({{ indexData.eventCount }})
          </v-tab>
          <v-tab-item>
            <v-row class="mt-3">
              <v-col>
                <strong> Zeitraum: </strong>
                {{ contactsDate }}
              </v-col>
            </v-row>
            <v-text-field
              v-model="tableDataContacts.search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
            <iris-data-table
              :loading="listLoading"
              :headers="tableDataContacts.headers"
              :items="contacts"
              :items-per-page="5"
              class="elevation-1 mt-5"
              :search="tableDataContacts.search"
              show-select
              show-select-all
              v-model="tableDataContacts.select"
              show-expand
              single-expand
              :expanded.sync="tableDataContacts.expanded"
              @click:row="(item, slot) => slot.expand(!slot.isExpanded)"
              data-test="case.contacts.data-table"
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
                      <v-col :key="ehIndex" cols="12" sm="6">
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
            </iris-data-table>
          </v-tab-item>
          <v-tab-item>
            <v-row class="mt-3">
              <v-col>
                <strong> Zeitraum: </strong>
                {{ eventsDate }}
              </v-col>
            </v-row>
            <v-text-field
              v-model="tableDataEvents.search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
            <iris-data-table
              :loading="listLoading"
              :headers="tableDataEvents.headers"
              :items="events"
              :items-per-page="5"
              class="elevation-1 mt-5"
              :search="tableDataEvents.search"
              show-select
              show-select-all
              v-model="tableDataEvents.select"
              data-test="case.events.data-table"
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
                      <v-col :key="ehIndex" cols="12" sm="6">
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
            </iris-data-table>
          </v-tab-item>
        </v-tabs>
      </v-card-text>
      <v-card-actions>
        <v-btn color="white" :to="{ name: 'index-list' }" replace>
          Zurück
        </v-btn>
        <v-spacer />
        <v-btn
          v-if="currentTab === 0"
          color="primary"
          :disabled="tableDataContacts.select.length <= 0"
          @click="handleContactsExport"
          data-test="case.contacts.export"
        >
          {{ contactsExportLabel }}
        </v-btn>
        <v-btn
          v-if="currentTab === 1"
          color="primary"
          :disabled="tableDataEvents.select.length <= 0"
          @click="handleEventsExport"
          data-test="case.events.export"
        >
          {{ eventsExportLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>
<style></style>

<script lang="ts">
import {
  Address,
  ContactPersonAllOfWorkPlace,
  DataRequestCaseData,
  DataRequestStatus,
  Sex,
} from "@/api";
import router from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import Genders from "@/constants/Genders";
import StatusMessages from "@/constants/StatusMessages";
import StatusColors from "@/constants/StatusColors";
import dayjs from "@/utils/date";
import ContactCategories from "@/constants/ContactCategories";
import AlertComponent from "@/components/alerts/alert-component.vue";
import IndexTrackingSubmissionUrl from "@/views/index-tracking-details/components/index-tracking-submission-url.vue";
import IrisDataTable from "@/components/iris-data-table.vue";
import exportStandardEvents from "@/views/index-tracking-details/components/data-export/utils/exportStandardEvents";
import exportStandardContacts from "@/views/index-tracking-details/components/data-export/utils/exportStandardContacts";
import { getExportLabel } from "@/utils/data-export/common";
import EntryMetaData from "@/components/entry-meta-data.vue";

type IndexData = {
  extID: string;
  name: string;
  startTime: string;
  endTime: string;
  comment: string;
  eventCount: number;
  contactCount: number;
  tan: string;
  submissionUri: string;
  status: string;
};

type TableRowContact = {
  lastName: string;
  firstName: string;
  dateOfBirth: string;
  firstContactDate: string;
  lastContactDate: string;
  contactCategory: string;
  sex: string;
  email: string;
  phone: string;
  mobilePhone: string;
  workPlace: string;
  basicConditions: string;
};

type TableRowEvent = {
  name: string;
  phone: string;
  address: string;
  additionalInformation: string;
};

function getFormattedDate(date?: string): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

function getFormattedWorkPlace(
  workPlace?: ContactPersonAllOfWorkPlace
): string {
  return [
    [workPlace?.name, workPlace?.phone, workPlace?.pointOfContact]
      .filter((v) => v)
      .join(", "),
    getFormattedAddress(workPlace?.address),
  ]
    .filter((v) => v)
    .join(", ");
}

function getFormattedAddress(address?: Address | null): string {
  if (address) {
    return `${address.street} ${address.houseNumber}, ${address.zipCode} ${address.city}`;
  }
  return "-";
}

@Component({
  components: {
    EntryMetaData,
    IrisDataTable,
    IndexTrackingSubmissionUrl,
    IndexTrackingDetailsView: IndexTrackingDetailsView,
    AlertComponent,
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
  alert = false;

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
      {
        text: "Erster Kontakt am",
        value: "firstContactDate",
      },
      {
        text: "Letzter Kontakt am",
        value: "lastContactDate",
      },
      {
        text: "Kontaktkathegorie",
        value: "contactCategory",
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
      {
        text: "Arbeitsplatz",
        value: "workPlace",
      },
      {
        text: "Kontaktsituation",
        value: "basicConditions",
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

  get indexTrackingDetails(): DataRequestCaseData | null {
    return store.state.indexTrackingDetails.indexTrackingDetails;
  }

  get indexData(): IndexData {
    const dataRequest = this.indexTrackingDetails;
    const contacts =
      dataRequest?.submissionData?.contacts?.contactPersons || [];
    const events = dataRequest?.submissionData?.events?.events || [];

    return {
      extID: dataRequest?.externalCaseId || "-",
      name: dataRequest?.name || "-",
      startTime: getFormattedDate(dataRequest?.start),
      endTime: getFormattedDate(dataRequest?.end),
      contactCount: contacts.length,
      eventCount: events.length,
      comment: dataRequest?.comment || "-",
      tan: dataRequest?.readableToken || "-",
      submissionUri: dataRequest?.submissionUri || "-",
      status: dataRequest?.status || "",
    };
  }

  get listLoading(): boolean {
    return store.state.indexTrackingDetails.indexTrackingDetailsLoading;
  }

  get contactsDate(): string {
    const contacts =
      store.state.indexTrackingDetails.indexTrackingDetails?.submissionData
        ?.contacts;
    return [
      getFormattedDate(contacts?.startDate),
      getFormattedDate(contacts?.endDate),
    ].join(" - ");
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
        firstContactDate: getFormattedDate(
          contact.contactInformation?.firstContactDate
        ),
        lastContactDate: getFormattedDate(
          contact.contactInformation?.lastContactDate
        ),
        contactCategory: ContactCategories.getCategory(
          contact.contactInformation?.contactCategory
        ),
        sex: contact.sex ? this.getSexName(contact.sex) : "-",
        email: contact.email || "-",
        phone: contact.phone || "-",
        mobilePhone: contact.mobilePhone || "-",
        address: getFormattedAddress(contact.address) || "-",
        workPlace: getFormattedWorkPlace(contact.workPlace) || "-",
        basicConditions: contact.contactInformation?.basicConditions || "-",
        raw: contact,
      };
    });
  }

  get eventsDate(): string {
    const events =
      store.state.indexTrackingDetails.indexTrackingDetails?.submissionData
        ?.events;
    return [
      getFormattedDate(events?.startDate),
      getFormattedDate(events?.endDate),
    ].join(" - ");
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
        raw: event,
      };
    });
  }

  created(): void {
    if (this.$route.query.is_created == "true") {
      this.openAlert();
    }

    let query = Object.assign({}, this.$route.query);

    if (query.is_created) {
      delete query.is_created;
      this.$router.replace({ query });
    }
  }

  openAlert(): void {
    this.alert = true;
    setTimeout(() => {
      this.alert = false;
    }, 2000);
  }

  currentTab = 0;
  handleTabsChange(index: number): void {
    this.currentTab = index;
  }

  getStatusName(status: DataRequestStatus): string {
    return StatusMessages.getMessage(status);
  }

  getStatusColor(status: DataRequestStatus): string {
    return StatusColors.getColor(status);
  }

  getSexName(sex: Sex): string {
    return Genders.getName(sex);
  }

  getFileName() {
    return [this.indexData.extID, Date.now()].join("_");
  }

  get contactsExportLabel(): string {
    return getExportLabel(
      this.tableDataContacts.select.length,
      this.contacts.length,
      ["Kontaktdaten"]
    );
  }

  get eventsExportLabel(): string {
    return getExportLabel(
      this.tableDataEvents.select.length,
      this.events.length,
      ["Ereignisdaten"]
    );
  }

  handleContactsExport(): void {
    exportStandardContacts.exportCsv(
      this.tableDataContacts.select,
      this.getFileName()
    );
  }

  handleEventsExport(): void {
    exportStandardEvents.exportCsv(
      this.tableDataEvents.select,
      this.getFileName()
    );
  }
}
</script>
