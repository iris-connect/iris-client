<template>
  <div>
    <v-row>
      <v-col cols="12">
        <div class="mb-6">
          <v-btn
              class="float-right"
              color="primary"
              :to="{ name: 'index-new' }"
          >
            Neuen Indexfall erstellen
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
      <v-card-title>Indexfallverfolgung</v-card-title>
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
            :loading="indexListLoading"
            :page="indexList.page"
            :pageCount="indexList.numberOfPages"
            :server-items-length="indexList.totalElements"
            :headers="headers"
            :items="indexList.content"
            :items-per-page="indexList.itemsPerPage"
            class="elevation-1 mt-5 twolineTable"
            :search="search"
            :footer-props="{'items-per-page-options': [5, 10, 15]}"
            @update:options="updatePagination"
        >
          <template v-slot:[itemStatusSlotName]="{ item }">
            <v-chip :color="getStatusColor(item.status)" dark>
              {{ getStatusName(item.status) }}
            </v-chip>
          </template>
          <template v-slot:[itemActionSlotName]="{ item }">
            <!-- TODO use imported route name -->
            <v-btn
                color="primary"
                :to="{ name: 'index-details', params: { caseId: item.caseId } }"
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
import {DataRequestCaseDetails, DataRequestStatus} from "@/api";
import store from "@/store";
import {Component, Vue} from "vue-property-decorator";
import IndexTrackingFormView from "../index-tracking-form/index-tracking-form.view.vue";
import StatusColors from "@/constants/StatusColors";
import StatusMessages from "@/constants/StatusMessages";
import _omit from "lodash/omit";
import {debounce} from "lodash";
import {DataPage, DataQuery} from "@/api/common";
import {DataOptions, DataTableItemProps} from "vuetify";

function getFormattedDate(date?: string): string {
  return date
      ? `${new Date(date).toDateString()}, ${new Date(date).toLocaleTimeString()}`
      : "-";
}

type TableRow = {
  endTime: string;
  extID: string;
  name: string;
  startTime: string;
  status: string;
};

@Component({
  components: {
    IndexTrackingFormView: IndexTrackingFormView,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch("indexTrackingList/fetchIndexTrackingList", {page: 1, itemsPerPage: 5});
  },
  beforeRouteLeave(to, from, next) {
    store.commit("indexTrackingList/reset");
    next();
  },
})
export default class IndexTrackingListView extends Vue {
  statusFilter: DataRequestStatus | null = null;
  selectableStatus = {
    ..._omit(DataRequestStatus, ["Aborted"]),
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
    {text: "Index-Bezeichner", value: "name"},
    {text: "Zeit (Start)", value: "startTime"},
    {text: "Zeit (Ende)", value: "endTime"},
    {text: "Status", value: "status"},
    {text: "", value: "actions", sortable: false},
  ];

  search = "";

  runSearch = debounce(async (input: string) => {
    let search = input?.trim();
    if (!search || search.length > 1) {
      const query: DataQuery = {
        page: 1,
        search: search
      }
      await store.dispatch("indexTrackingList/fetchIndexTrackingList", query);
    }
  }, 1000);

  async filterStatus(target: DataRequestStatus | null) {
    this.statusFilter = target;
    const query: DataQuery = {
      // If filter is changed, page should be reset
      page: 1,
      status: target
    };
    await store.dispatch("indexTrackingList/fetchIndexTrackingList", query);
  }

  get indexListLoading(): boolean {
    return store.state.indexTrackingList.indexTrackingListLoading;
  }

  get indexList(): DataPage<TableRow> {
    const dataRequests: DataPage<DataRequestCaseDetails> = store.state.indexTrackingList.indexTrackingList;
    return {
      page: dataRequests.page,
      itemsPerPage: dataRequests.itemsPerPage,
      numberOfPages: dataRequests.numberOfPages,
      totalElements: dataRequests.totalElements,
      content: dataRequests.content.map((dataRequest) => {
        return {
          endTime: getFormattedDate(dataRequest.end),
          startTime: getFormattedDate(dataRequest.start),
          extID: dataRequest.externalCaseId || "-",
          name: dataRequest.name || "-",
          status: dataRequest.status?.toString() || "-",
          caseId: dataRequest.caseId,
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

  async updatePagination(pagination: DataOptions) {
    const query: DataQuery = {
      page: pagination.page,
      size: pagination.itemsPerPage,
      sort: pagination.sortBy[0] ?? null,
      sortOrderDesc: pagination.sortDesc[0]
    }
    await store.dispatch("indexTrackingList/fetchIndexTrackingList", query);
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
