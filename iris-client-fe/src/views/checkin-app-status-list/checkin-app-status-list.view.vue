<template>
  <div>
    <v-row class="my-6">
      <v-col cols="12">
        Status:
        <v-btn-toggle dense mandatory class="flex-wrap" v-model="statusFilter">
          <v-btn
            text
            v-for="status in Object.keys(statusFilterSelect)"
            :key="status"
            :value="status"
            :data-test="`status.select.${getStatusTestLabel(
              statusFilterSelect[status]
            )}`"
          >
            {{ getStatusFilterLabel(statusFilterSelect[status]) }}
          </v-btn>
          <v-btn text value="ALL" data-test="status.select.all"> Alle </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>
    <v-card>
      <v-card-title>CheckIn Apps: Statusübersicht</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
          data-test="search"
        ></v-text-field>
        <iris-data-table
          :loading="listLoading"
          :headers="tableData.headers"
          :custom-sort="sort"
          :sort-by="['status']"
          :sort-desc="[true]"
          multi-sort
          item-key="name"
          :items="filteredRows"
          :items-per-page="10"
          :expanded.sync="tableData.expanded"
          show-expand
          class="elevation-1 mt-5"
          :search="tableData.search"
          data-test="view.data-table"
        >
          <template #item.status="{ item }">
            <checkin-app-status-indicator
              :loading="item.loading"
              :status="item.status"
              :data-test="`status.${getStatusTestLabel(item.status)}`"
            />
          </template>
          <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="headers.length">
              {{ item.loading ? "Bitte warten..." : item.message }}
            </td>
          </template>
        </iris-data-table>
        <v-alert class="mt-5 mb-0" v-if="listLoadingError" text type="error">
          {{ listLoadingError }}
        </v-alert>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import { CheckinApp, CheckinAppStatus } from "@/api";
import IrisDataTable from "@/components/iris-data-table.vue";
import CheckinAppStatusIndicator from "@/views/checkin-app-status-list/components/checkin-app-status-indicator.vue";
import _orderBy from "lodash/orderBy";
import _filter from "lodash/filter";
import { AppStatusInfo } from "@/views/checkin-app-status-list/checkin-app-status-list.store";

interface TableRow {
  name: string;
  loading?: boolean;
  message?: string | null;
  status?: CheckinAppStatus;
}

@Component({
  components: {
    CheckinAppStatusIndicator,
    IrisDataTable,
  },
  async beforeRouteEnter(to, from, next) {
    next();
    await store.dispatch("checkinAppStatusList/fetchList");
  },
  beforeRouteLeave(to, from, next) {
    store.commit("checkinAppStatusList/reset");
    next();
  },
})
export default class CheckinAppStatusListView extends Vue {
  tableData = {
    search: "",
    expanded: [],
    select: [],
    headers: [
      {
        text: "Provider",
        value: "name",
        align: "start",
      },
      {
        text: "Status",
        value: "status",
        align: "end",
      },
      { text: "", value: "data-table-expand" },
    ],
  };

  statusFilterSelect = CheckinAppStatus;

  statusFilterSelect2 = ["Ok", "Warnung", "Fehler", "Unbekannt", "Alle"];
  statusFilter = "ALL";

  getStatusFilterLabel(status: CheckinAppStatus): string {
    switch (status) {
      case CheckinAppStatus.OK:
        return "Ok";
      case CheckinAppStatus.ERROR:
        return "Fehler";
      case CheckinAppStatus.WARNING:
        return "Warnung";
      case CheckinAppStatus.UNKNOWN:
      default:
        return "Unbekannt";
    }
  }

  getStatusTestLabel(status: CheckinAppStatus & { ALL: "ALL" }): string {
    switch (status) {
      case "ALL":
        return "all";
      case CheckinAppStatus.ERROR:
        return "error";
      case CheckinAppStatus.OK:
        return "ok";
      case CheckinAppStatus.WARNING:
        return "warning";
      case CheckinAppStatus.UNKNOWN:
      default:
        return "unknown";
    }
  }

  get filteredRows(): TableRow[] {
    if (!this.statusFilter || this.statusFilter === "ALL") {
      return this.tableRows;
    }
    return _filter(this.tableRows, ["status", this.statusFilter]);
  }

  sort = (
    items: TableRow[],
    sortBy: string[],
    sortDesc: boolean[]
  ): TableRow[] => {
    const sortItems = sortBy.map((s: string) => {
      if (s === "status") {
        return (item: TableRow) => {
          switch (item.status) {
            case CheckinAppStatus.OK:
              return 1;
            case CheckinAppStatus.WARNING:
              return 2;
            case CheckinAppStatus.ERROR:
              return 3;
            case CheckinAppStatus.UNKNOWN:
              return 4;
            default:
              return 5;
          }
        };
      }
      return s;
    });
    const sortOrder = sortDesc.map((desc: boolean) => (desc ? "desc" : "asc"));
    return _orderBy(items, sortItems, sortOrder);
  };

  get listLoading(): boolean {
    return this.$store.state.checkinAppStatusList.listLoading;
  }

  get listLoadingError(): ErrorMessage {
    return this.$store.state.checkinAppStatusList.listLoadingError;
  }

  get appList(): CheckinApp[] {
    return this.$store.state.checkinAppStatusList.list || [];
  }

  @Watch("appList")
  onAppListChange(list: CheckinApp[]): void {
    this.$store.commit("checkinAppStatusList/setAppStatusInfoList", null);
    list.forEach((item) => {
      this.$store.dispatch("checkinAppStatusList/fetchStatusInfo", item.name);
    });
  }

  get tableRows(): TableRow[] {
    return this.appList.map((item) => {
      const info: AppStatusInfo = this.$store.getters[
        "checkinAppStatusList/appStatusInfo"
      ](item.name);
      return {
        status: info.status || CheckinAppStatus.UNKNOWN,
        ...item,
        ...info,
      };
    });
  }
}
</script>
