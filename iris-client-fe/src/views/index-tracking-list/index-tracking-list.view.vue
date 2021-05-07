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
            @click="filterStatus(statusEnum.DataRequested)"
            style="opacity: 100%; background-color: white"
          >
            {{ getStatusName(statusEnum.DataRequested) }}
          </v-btn>
          <v-btn
            @click="filterStatus(statusEnum.DataReceived)"
            style="opacity: 100%; background-color: white"
          >
            {{ getStatusName(statusEnum.DataReceived) }}
          </v-btn>
          <v-btn
            @click="filterStatus(statusEnum.Closed)"
            style="opacity: 100%; background-color: white"
          >
            {{ getStatusName(statusEnum.Closed) }}
          </v-btn>
          <v-btn
            @click="filterStatus(null)"
            style="opacity: 100%; background-color: white"
          >
            Alle
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
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-data-table
          :loading="indexListLoading"
          :headers="tableData.headers"
          :items="indexList"
          :items-per-page="5"
          class="elevation-1 mt-5 twolineTable"
          :search="tableData.search"
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
import { DataRequestCaseDetailsStatusEnum } from "@/api";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import IndexTrackingFormView from "../index-tracking-form/index-tracking-form.view.vue";
import StatusColors from "@/constants/StatusColors";
import StatusMessages from "@/constants/StatusMessages";

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
    await store.dispatch("indexTrackingList/fetchIndexTrackingList");
  },
  beforeRouteLeave(to, from, next) {
    store.commit("indexTrackingList/reset");
    next();
  },
})
export default class IndexTrackingListView extends Vue {
  statusFilter: DataRequestCaseDetailsStatusEnum | null = null;
  statusEnum = DataRequestCaseDetailsStatusEnum;
  statusButtonSelected = 3;
  filterStatus(target: DataRequestCaseDetailsStatusEnum | null): void {
    this.statusFilter = target;
  }

  tableData = {
    search: "",
    headers: [
      {
        text: "Ext.ID",
        align: "start",
        sortable: true,
        value: "extID",
      },
      { text: "Index-Bezeichner", value: "name" },
      { text: "Zeit (Start)", value: "startTime" },
      { text: "Zeit (Ende)", value: "endTime" },
      { text: "Status", value: "status" },
      { text: "", value: "actions" },
    ],
  };

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

  getStatusColor(status: DataRequestCaseDetailsStatusEnum): string {
    return StatusColors.getColor(status);
  }

  getStatusName(status: DataRequestCaseDetailsStatusEnum): string {
    return StatusMessages.getMessage(status);
  }
}
</script>
