<template>
  <data-query-handler
    @query:update="handleQueryUpdate"
    :route-control="false"
    :data-query="selectQuery"
    #default="{ query }"
  >
    <search-field v-model="query.search" />
    <sortable-data-table
      class="mt-5"
      v-bind="$attrs"
      v-model="selection"
      :headers="tableHeaders"
      item-key="id"
      :item-class="getItemClass"
      :sort.sync="query.sort"
      :items="tableRows"
      :loading="eventApi.fetchPageEvent.state.loading"
      show-select
      single-select
      :page.sync="query.page"
      :items-per-page.sync="query.size"
      :server-items-length="totalElements"
    >
      <template v-slot:item.address="{ item }">
        <span class="text-pre-wrap"> {{ item.address }} </span>
      </template>
      <template v-slot:item.status="{ item }">
        <data-table-item-status :status="item.status" />
      </template>
    </sortable-data-table>
    <error-message-alert :errors="[eventApi.fetchPageEvent.state.error]" />
  </data-query-handler>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import { DataRequestStatus, Page } from "@/api";
import SearchField from "@/components/pageable/search-field.vue";
import {
  EventTrackingListTableRow,
  getEventTrackingListTableRows,
} from "@/views/event-tracking-list/utils/mappeData";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import { DataQuery } from "@/api/common";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { bundleEventTrackingApi } from "@/modules/event-tracking/services/api";
import DataTableItemStatus from "@/components/data-table-item-status.vue";

const SelectEventProps = Vue.extend({
  inheritAttrs: false,
  props: {
    pagination: {
      type: Object as PropType<Page<unknown> | null>,
      default: null,
    },
    route: {
      type: Boolean,
      default: true,
    },
    value: {
      type: String,
      default: "",
    },
    description: {
      type: String,
      default: "",
    },
    selectQuery: {
      type: Object as PropType<Partial<DataQuery> | null>,
      default: null,
    },
    selectableStatus: {
      type: Array as PropType<DataRequestStatus[] | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    DataTableItemStatus,
    ErrorMessageAlert,
    DataQueryHandler,
    SearchField,
    SortableDataTable,
  },
})
export default class SelectEvent extends SelectEventProps {
  tableHeaders = [
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
  ];

  eventApi = bundleEventTrackingApi(["fetchPageEvent"]);

  get tableRows() {
    return getEventTrackingListTableRows(
      this.eventApi.fetchPageEvent.state.result?.content || [],
      this.selectableStatus
    );
  }

  get selection(): EventTrackingListTableRow[] {
    if (this.value.length <= 0) return [];
    return this.tableRows.filter((row) => {
      return row.id === this.value;
    });
  }
  set selection(rows: EventTrackingListTableRow[]) {
    const sel = rows.find((row) => row.id);
    if (!this.description) {
      this.$emit("update:description", sel?.name || "");
    }
    this.$emit("input", sel?.id || "");
  }

  handleQueryUpdate(query: DataQuery) {
    this.eventApi.fetchPageEvent.execute(query);
  }

  get totalElements(): boolean {
    return this.eventApi.fetchPageEvent.state.result?.totalElements;
  }
  getItemClass(item: { isSelectable?: boolean }): string {
    return item.isSelectable === false ? "is-disabled" : "is-selectable";
  }
}
</script>

<style lang="scss">
.v-data-table {
  tbody tr {
    &.is-disabled {
      opacity: 0.5;
      &:hover {
        background: transparent !important;
      }
    }
  }
}
</style>
