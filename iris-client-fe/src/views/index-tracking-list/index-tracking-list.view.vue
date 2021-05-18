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
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Suche (min. 2 Buchstaben)"
          single-line
          hide-details
          @keyup="triggerSearch"
        ></v-text-field>
        <v-data-table
          :loading="indexListLoading"
          :page="tableData.page"
          :pageCount="tableData.numberOfPages"
          :server-items-length="tableData.totalElements"
          :headers="tableData.headers"
          :items="indexList"
          :items-per-page="tableData.itemsPerPage"
          class="elevation-1 mt-5 twolineTable"
          :search="tableData.search"
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
import { DataRequestStatus } from "@/api";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import IndexTrackingFormView from "../index-tracking-form/index-tracking-form.view.vue";
import StatusColors from "@/constants/StatusColors";
import StatusMessages from "@/constants/StatusMessages";
import _omit from "lodash/omit";
import { debounce } from "lodash";

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
    await store.dispatch("indexTrackingList/fetchIndexTrackingList", store.state.indexTrackingList.tableData);
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

  runSearch = debounce(async () => {
    let search = store.state.indexTrackingList.tableData.search;
    if ((search && search.length > 1 && search.trim()) || search === '') {
      // If search is changed, page should be reset
      store.state.indexTrackingList.tableData.page = 1;
      await store.dispatch("indexTrackingList/fetchIndexTrackingList", store.state.indexTrackingList.tableData);
    }
  }, 1000);

  async filterStatus(target: DataRequestStatus | null) {
    this.statusFilter = target;
    store.state.indexTrackingList.tableData.statusFilter = target;
    // If filter is changed, page should be reset
    store.state.indexTrackingList.tableData.page = 1;
    await store.dispatch("indexTrackingList/fetchIndexTrackingList", store.state.indexTrackingList.tableData);
  }

  get tableData() {
    return store.state.indexTrackingList.tableData;
  }

  get indexListLoading(): boolean {
    return store.state.indexTrackingList.indexTrackingListLoading;
  }

  get indexList(): TableRow[] {
    const dataRequests = store.state.indexTrackingList.indexTrackingList || [];
    //console.log(dataRequests);
    return (
      dataRequests
        // TODO this filtering could probably also be done in vuetify data-table
        .filter(
          (dataRequests) =>
            !this.statusFilter || this.statusFilter === dataRequests.status
        )
        .map((dataRequest) => {
          return {
            endTime: getFormattedDate(dataRequest.end),
            startTime: getFormattedDate(dataRequest.start),
            extID: dataRequest.externalCaseId || "-",
            name: dataRequest.name || "-",
            status: dataRequest.status?.toString() || "-",
            caseId: dataRequest.caseId,
          };
        })
    );
  }

  // TODO improve this - we need it to circumvent v-slot eslint errors
  // https://stackoverflow.com/questions/61344980/v-slot-directive-doesnt-support-any-modifier
  get itemStatusSlotName(): string {
    return "item.status";
  }
  get itemActionSlotName(): string {
    return "item.actions";
  }

  async updatePagination(pagination: any) {
    store.state.indexTrackingList.tableData.page = pagination.page;
    store.state.indexTrackingList.tableData.itemsPerPage = pagination.itemsPerPage;
    store.state.indexTrackingList.tableData.sortBy = pagination.sortBy;
    store.state.indexTrackingList.tableData.sortOrder = pagination.sortDesc;
    await store.dispatch("indexTrackingList/fetchIndexTrackingList", store.state.indexTrackingList.tableData);
  }

  async triggerSearch() {
    await this.runSearch();
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
