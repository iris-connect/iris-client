<template>
  <div>
    <v-row class="mt-4">
      <v-col>
        <v-card>
          <v-card-title>Aktueller Status der CheckIn Apps</v-card-title>
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
              :sort-by="['status', 'name']"
              :sort-desc="[true, false]"
              multi-sort
              item-key="name"
              :items="appList"
              :items-per-page="10"
              :expanded.sync="tableData.expanded"
              show-expand
              class="elevation-1 mt-5"
              :search="tableData.search"
              data-test="view.data-table"
            >
              <template #item.status="{ item }">
                <checkin-app-status-indicator
                  :loading="appWithStatus(item.name).loading"
                  :status="appWithStatus(item.name).status"
                />
              </template>
              <template v-slot:expanded-item="{ headers, item }">
                <td :colspan="headers.length">
                  {{
                    appWithStatus(item.name).loading
                      ? "Bitte warten..."
                      : appWithStatus(item.name).message
                  }}
                </td>
              </template>
            </iris-data-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import { CheckinApp, CheckinAppList, CheckinAppStatus } from "@/api";
import IrisDataTable from "@/components/iris-data-table.vue";
import CheckinAppStatusIndicator from "@/views/checkin-app-status-list/components/checkin-app-status-indicator.vue";
import { AppWithStatusList } from "@/views/checkin-app-status-list/checkin-app-status-list.store";
import _orderBy from "lodash/orderBy";

interface AppWithStatus {
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

  sort = (
    items: CheckinApp[],
    sortBy: string[],
    sortDesc: boolean[]
  ): CheckinApp[] => {
    const sortItems = sortBy.map((s: string) => {
      if (s === "status") {
        return (item: CheckinApp) => {
          const status =
            this.appWithStatus(item.name).status || CheckinAppStatus.UNKNOWN;
          switch (status) {
            case CheckinAppStatus.OK:
              return 1;
            case CheckinAppStatus.UNKNOWN:
              return 2;
            case CheckinAppStatus.WARNING:
              return 3;
            case CheckinAppStatus.ERROR:
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

  get appList(): CheckinAppList {
    return this.$store.state.checkinAppStatusList.list || [];
  }

  @Watch("appList")
  onAppListChange(list: CheckinAppList): void {
    this.$store.commit("checkinAppStatusList/setAppWithStatusList", {});
    list.forEach((item) => {
      this.$store.dispatch("checkinAppStatusList/fetchStatus", item.name);
    });
  }

  get appWithStatusList(): AppWithStatusList {
    return this.$store.state.checkinAppStatusList.appWithStatusList || {};
  }

  appWithStatus(name: string): AppWithStatus {
    return this.appWithStatusList[name] || {};
  }
}
</script>
