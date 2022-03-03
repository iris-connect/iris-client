<template>
  <div>
    <v-row>
      <v-col cols="12">
        <div class="mb-6">
          <v-btn
            class="float-right"
            color="primary"
            :to="{ name: 'event-new' }"
            data-test="view.link.create"
          >
            Neue Ereignisverfolgung starten
          </v-btn>
        </div>
      </v-col>
    </v-row>
    <v-row class="mb-6">
      <v-col cols="12">
        Status:
        <v-btn-toggle
          dense
          mandatory
          :value="statusButtonSelected"
          class="flex-wrap"
        >
          <v-btn
            text
            @click="filterStatus(selectableStatus[status])"
            v-for="status in Object.keys(selectableStatus)"
            :key="status"
            :data-test="`status.select.${getStatusTestLabel(
              selectableStatus[status]
            )}`"
          >
            {{ getStatusSelectLabel(selectableStatus[status]) }}
          </v-btn>
          <!-- this needs to come last, see statusButtonSelected() -->
          <v-btn
            text
            @click="filterStatus(undefined)"
            data-test="status.select.all"
          >
            Alle
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
          label="Suchbegriff (min. 2 Buchstaben)"
          single-line
          hide-details
          @keyup="triggerSearch(search)"
        ></v-text-field>
        <iris-data-table
          :loading="dataTableModel.loading"
          :page="dataTableModel.page"
          :server-items-length="dataTableModel.itemsLength"
          :headers="dataTableModel.headers"
          :items="dataTableModel.data"
          :items-per-page="dataTableModel.itemsPerPage"
          class="elevation-1 mt-5 twolineTable"
          :search="search"
          :footer-props="{ 'items-per-page-options': [10, 20, 30, 50] }"
          @update:options="updatePagination"
          data-test="view.data-table"
        >
          <template v-slot:[itemAddressSlotName]="{ item }">
            <span class="text-pre-wrap"> {{ item.address }} </span>
          </template>
          <template v-slot:[itemStatusSlotName]="{ item }">
            <v-chip
              :color="getStatusColor(item.status)"
              dark
              :data-test="`status.${getStatusTestLabel(item.status)}`"
            >
              {{ getStatusName(item.status) }}
            </v-chip>
          </template>
          <template v-slot:[itemActionSlotName]="{ item }">
            <!-- TODO use imported route name -->
            <v-btn
              color="primary"
              :to="{
                name: 'event-details',
                params: { id: item.code },
              }"
              data-test="select"
            >
              Details
            </v-btn>
          </template>
        </iris-data-table>
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
import { DataQuery, getSortAttribute } from "@/api/common";
import { DataOptions } from "vuetify";
import {
  getPageFromRouteWithDefault,
  getPageSizeFromRouteWithDefault,
  getParamFromRoute,
  getStatusFilterFromRoute,
  getStringParamFromRouteWithOptionalFallback,
} from "@/utils/pagination";
import { Dictionary } from "vue-router/types/router";
import { join } from "@/utils/misc";
import StatusTestLabel from "@/constants/StatusTestLabel";
import IrisDataTable from "@/components/iris-data-table.vue";

function getFormattedAddress(
  data?: ExistingDataRequestClientWithLocation
): string {
  const contact = data?.locationInformation?.contact;
  const officialName = contact?.officialName;
  return join(
    [
      data?.locationInformation?.name ?? "-",
      officialName ? `(${officialName})` : "",
      contact?.address?.street,
      join([contact?.address?.zip, contact?.address?.city], " "),
    ],
    "\n"
  );
}

function getFormattedDate(date?: string): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

@Component({
  components: {
    IrisDataTable,
    EventTrackingFormView: EventTrackingFormView,
  },
  beforeRouteLeave(to, from, next) {
    store.commit("eventTrackingList/reset");
    next();
  },
})
export default class EventTrackingListView extends Vue {
  statusFilter: DataRequestStatus | undefined = getStatusFilterFromRoute(
    this.$route
  );

  selectableStatus: Dictionary<DataRequestStatus> = DataRequestStatus;

  search = getStringParamFromRouteWithOptionalFallback(
    "search",
    this.$route,
    ""
  );

  runSearch = debounce(async (input: string | undefined) => {
    let search = input?.trim();
    if (!search || search.length > 1) {
      this.$router.replace({
        name: this.$route.name as string | undefined,
        query: {
          ...this.$route.query,
          page: `1`,
          search: search || undefined,
        },
      });

      const query: DataQuery = {
        size: getPageSizeFromRouteWithDefault(this.$route),
        page: 0,
        sort: getParamFromRoute("sort", this.$route),
        status: getStatusFilterFromRoute(this.$route),
        search: search,
      };
      await store.dispatch("eventTrackingList/fetchEventTrackingList", query);
    }
  }, 1000);

  async filterStatus(target: DataRequestStatus | undefined): Promise<void> {
    this.statusFilter = target;

    this.$router.replace({
      name: this.$route.name as string | undefined,
      query: {
        ...this.$route.query,
        page: `1`,
        status: target,
      },
    });

    const query: DataQuery = {
      size: getPageSizeFromRouteWithDefault(this.$route),
      page: 0,
      sort: getParamFromRoute("sort", this.$route),
      status: target,
      search: getStringParamFromRouteWithOptionalFallback(
        "search",
        this.$route
      ),
    };
    await store.dispatch("eventTrackingList/fetchEventTrackingList", query);
  }

  get statusButtonSelected(): number {
    if (!this.statusFilter) {
      // return length + 1, assuming "Alle" button is displayed last
      return Object.keys(this.selectableStatus).length;
    }
    return Object.values(this.selectableStatus).findIndex((v) => {
      return v === this.statusFilter;
    });
  }

  get eventListLoading(): boolean {
    return store.state.eventTrackingList.eventTrackingListLoading;
  }

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  get dataTableModel() {
    const { eventTrackingList, eventTrackingListLoading } =
      store.state.eventTrackingList;
    return {
      page: getPageFromRouteWithDefault(this.$route),
      itemsPerPage: getPageSizeFromRouteWithDefault(this.$route),
      loading: eventTrackingListLoading,
      itemsLength: eventTrackingList?.totalElements || 0,
      data: (eventTrackingList?.content || []).map(
        (
          dataRequest:
            | Partial<ExistingDataRequestClientWithLocation>
            | undefined
        ) => {
          return {
            address: getFormattedAddress(dataRequest),
            endTime: getFormattedDate(dataRequest?.end),
            startTime: getFormattedDate(dataRequest?.start),
            generatedTime: getFormattedDate(dataRequest?.requestedAt),
            lastChange: getFormattedDate(dataRequest?.lastUpdatedAt),
            extID: dataRequest?.externalRequestId || "-",
            code: dataRequest?.code,
            name: dataRequest?.name || "-",
            status: dataRequest?.status?.toString() || "-",
          };
        }
      ),
      headers: [
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
      ],
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

  async updatePagination(pagination: DataOptions): Promise<void> {
    let sort = getSortAttribute(pagination.sortBy[0]);
    if (sort) {
      pagination.sortDesc[0] ? (sort = sort + ",desc") : (sort = sort + ",asc");
    }

    this.$router.replace({
      name: this.$route.name as string | undefined,
      query: {
        ...this.$route.query,
        page: `${pagination.page}`,
        size: `${pagination.itemsPerPage}`,
        sort: sort,
      },
    });

    const query: DataQuery = {
      size: pagination.itemsPerPage,
      page: pagination.page - 1,
      sort: sort,
      status: getStatusFilterFromRoute(this.$route),
      search: getStringParamFromRouteWithOptionalFallback(
        "search",
        this.$route
      ),
    };
    await store.dispatch("eventTrackingList/fetchEventTrackingList", query);
  }

  async triggerSearch(input: string | undefined): Promise<void> {
    await this.runSearch(input);
  }

  getStatusColor(status: DataRequestStatus): string {
    return StatusColors.getColor(status);
  }

  getStatusName(status: DataRequestStatus): string {
    return StatusMessages.getMessage(status);
  }

  getStatusTestLabel(status: DataRequestStatus): string {
    return StatusTestLabel.getStatusTestLabel(status);
  }

  getStatusSelectLabel(status: DataRequestStatus | null): string {
    if (!status) return "Alle";
    return StatusMessages.getMessage(status);
  }
}
</script>
