<template>
  <div>
    <data-query-handler @query:update="handleQueryUpdate" #default="{ query }">
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
          <status-select v-model="query.status" />
        </v-col>
      </v-row>
      <v-card>
        <v-card-title>Ereignisnachverfolgungen</v-card-title>
        <v-card-text>
          <search-field v-model="query.search" />
          <sortable-data-table
            :loading="dataTableModel.loading"
            :page.sync="query.page"
            :server-items-length="dataTableModel.itemsLength"
            :headers="dataTableModel.headers"
            :items="dataTableModel.data"
            :sort.sync="query.sort"
            :items-per-page.sync="query.size"
            class="elevation-1 mt-5 twolineTable"
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
          </sortable-data-table>
        </v-card-text>
      </v-card>
    </data-query-handler>
  </div>
</template>

<script lang="ts">
import { DataRequestStatus } from "@/api";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";
import StatusColors from "@/constants/StatusColors";
import StatusMessages from "@/constants/StatusMessages";
import { DataQuery } from "@/api/common";
import StatusTestLabel from "@/constants/StatusTestLabel";
import IrisDataTable from "@/components/iris-data-table.vue";
import { getEventTrackingListTableRows } from "@/views/event-tracking-list/utils/mappeData";
import { fetchPageEventAction } from "@/modules/event-tracking/api";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import SearchField from "@/components/pageable/search-field.vue";
import StatusSelect from "@/components/pageable/status-select.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";

@Component({
  components: {
    SortableDataTable,
    StatusSelect,
    SearchField,
    DataQueryHandler,
    IrisDataTable,
    EventTrackingFormView: EventTrackingFormView,
  },
})
export default class EventTrackingListView extends Vue {
  fetchPageEvent = fetchPageEventAction();

  handleQueryUpdate(query: DataQuery) {
    this.fetchPageEvent.execute(query);
  }

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  get dataTableModel() {
    const eventTrackingList = this.fetchPageEvent.state.result;
    return {
      loading: this.fetchPageEvent.state.loading,
      itemsLength: eventTrackingList?.totalElements || 0,
      data: getEventTrackingListTableRows(eventTrackingList?.content),
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

  getStatusColor(status: DataRequestStatus): string {
    return StatusColors.getColor(status);
  }

  getStatusName(status: DataRequestStatus): string {
    return StatusMessages.getMessage(status);
  }

  getStatusTestLabel(status: DataRequestStatus): string {
    return StatusTestLabel.getStatusTestLabel(status);
  }
}
</script>
