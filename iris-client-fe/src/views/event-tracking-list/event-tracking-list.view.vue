<template>
  <div>
    <v-row>
      <v-col cols="12">
        <div class="mb-6">
          <v-btn class="float-right" color="primary" :to="{ name: 'event-new' }"
            >Neue Ereignisverfolgung starten
          </v-btn>
        </div>
      </v-col>
    </v-row>
    <v-row class="mb-6">
      <v-col cols="8">
        Status:
        <v-btn-toggle dense mandatory v-model="statusButtonSelected">
          <v-btn
            text
            @click="filterStatus(selectableStatus[status])"
            v-for="status in Object.keys(selectableStatus)"
            :key="status"
          >
            {{ getStatusSelectLabel(selectableStatus[status]) }}
          </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>

    <v-card>
      <v-card-title>Ereignisnachverfolgungen</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="Suche (min. 2 Buchstaben)"
          single-line
          hide-details
          @keyup="triggerSearch(search)"
        ></v-text-field>
        <v-data-table
          :loading="eventListLoading"
          :page="eventList.page"
          :pageCount="eventList.numberOfPages"
          :server-items-length="eventList.totalElements"
          :headers="headers"
          :items="eventList.content"
          :items-per-page="eventList.itemsPerPage"
          class="elevation-1 mt-5 twolineTable"
          :search="search"
          :footer-props="{'items-per-page-options': [5, 10, 15]}"
          @update:options="updatePagination"
        >
          <template v-slot:[itemAddressSlotName]="{ item }">
            <span class="text-pre-wrap"> {{ item.address }} </span>
          </template>
          <template v-slot:[itemStatusSlotName]="{ item }">
            <v-chip :color="getStatusColor(item.status)" dark>
              {{ getStatusName(item.status) }}
            </v-chip>
          </template>
          <template v-slot:[itemActionSlotName]="{ item }">
            <!-- TODO use imported route name -->
            <v-btn
              color="primary"
              :to="{ name: 'event-details', params: { id: item.code } }"
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
import {
  DataRequestStatus,
  ExistingDataRequestClientWithLocation,
} from "@/api";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";
import StatusColors from "@/constants/StatusColors";
import StatusMessages from "@/constants/StatusMessages";
import { debounce } from "lodash";
import dayjs from "@/utils/date";
import {DataPage, DataQuery} from "@/api/common";
import {DataOptions} from "vuetify";

function getFormattedAddress(
  data?: ExistingDataRequestClientWithLocation
): string {
  if (data) {
    if (data.locationInformation) {
      const contact = data.locationInformation.contact;
      if (contact) {
        let name = `${data.locationInformation.name}`;
        if (contact.officialName) {
          name = name + `\n(${contact.officialName})`;
        }
        return (
          name +
          `\n${contact.address.street} \n${contact.address.zip} ${contact.address.city}`
        );
      }

      return data.locationInformation.name;
    }
    return "-";
  }
  return "-";
}

function getFormattedDate(date?: string): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

type TableRow = {
  address: string;
  endTime: string;
  extID: string;
  generatedTime: string;
  lastChange: string;
  name: string;
  startTime: string;
  status: string;
};

@Component({
  components: {
    EventTrackingFormView: EventTrackingFormView,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch(
      "eventTrackingList/fetchEventTrackingList", {page: 1, itemsPerPage: 5}
    );
  },
  beforeRouteLeave(to, from, next) {
    store.commit("eventTrackingList/reset");
    next();
  },
})
export default class EventTrackingListView extends Vue {
  statusFilter: DataRequestStatus | null = null;
  selectableStatus = {
    ...DataRequestStatus,
    All: null,
  };
  statusButtonSelected = Object.keys(this.selectableStatus).length - 1;

  headers = [
    {
      text: "Ext.ID",
      align: "start",
      sortable: true,
      value: "extID",
    },
    { text: "Event", value: "name" },
    { text: "Ort", value: "address", sortable: false },
    { text: "Zeit (Start)", value: "startTime" },
    { text: "Zeit (Ende)", value: "endTime" },
    { text: "Generiert", value: "generatedTime" },
    { text: "Status", value: "status" },
    { text: "Letzte Änderung", value: "lastChange" },
    { text: "", value: "actions", sortable: false },
  ];

  search = "";

  runSearch = debounce(async (input: string) => {
    let search = input?.trim();
    if (!search || search.length > 1) {
      const query: DataQuery = {
        page: 1,
        search: search
      }
      await store.dispatch("eventTrackingList/fetchEventTrackingList", query);
    }
  }, 1000);

  async filterStatus(target: DataRequestStatus | null) {
    this.statusFilter = target;
    const query: DataQuery = {
      // If filter is changed, page should be reset
      page: 1,
      status: target
    };
    await store.dispatch(
      "eventTrackingList/fetchEventTrackingList",
      query
    );
  }

  get eventListLoading(): boolean {
    return store.state.eventTrackingList.eventTrackingListLoading;
  }

  get eventList(): DataPage<TableRow> {
    const dataRequests: DataPage<ExistingDataRequestClientWithLocation> = store.state.eventTrackingList.eventTrackingList;
    return {
      page: dataRequests.page,
      itemsPerPage: dataRequests.itemsPerPage,
      numberOfPages: dataRequests.numberOfPages,
      totalElements: dataRequests.totalElements,
      content: dataRequests.content.map((dataRequest) => {
        return {
          address: getFormattedAddress(dataRequest),
          endTime: getFormattedDate(dataRequest.end),
          startTime: getFormattedDate(dataRequest.start),
          generatedTime: getFormattedDate(dataRequest.requestedAt),
          lastChange: getFormattedDate(dataRequest.lastUpdatedAt),
          extID: dataRequest.externalRequestId || "-",
          code: dataRequest.code,
          name: dataRequest.name || "-",
          status: dataRequest.status?.toString() || "-",
        };
      }),
    };
  }

  // TODO improve this - we need it to circumvent v-slot eslint errors
  // https://stackoverflow.com/questions/61344980/v-slot-directive-doesnt-support-any-modifier
  get itemStatusSlotName(): string {
    return "item.status";
  }

  get itemActionSlotName(): string {
    return "item.actions";
  }

  get itemAddressSlotName(): string {
    return "item.address";
  }

  async updatePagination(pagination: DataOptions) {
    console.log(pagination.sortBy)
    const query: DataQuery = {
      page: pagination.page,
      size: pagination.itemsPerPage,
      sort: pagination.sortBy[0] ?? null,
      sortOrderDesc: pagination.sortDesc[0]
    }
    await store.dispatch(
      "eventTrackingList/fetchEventTrackingList",
      query
    );
  }

  async triggerSearch(input: string) {
    await this.runSearch(input);
  }

  getStatusColor(status: DataRequestStatus): string {
    return StatusColors.getColor(status);
  }

  getStatusName(status: DataRequestStatus): string {
    return StatusMessages.getMessage(status);
  }

  getStatusSelectLabel(status: DataRequestStatus | null): string {
    if (!status) return "Alle";
    return StatusMessages.getMessage(status);
  }
}
</script>
