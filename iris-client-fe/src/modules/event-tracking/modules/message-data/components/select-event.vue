<template>
  <single-select-query-data-table
    @update:query="handleQueryUpdate"
    v-bind="$attrs"
    v-on="$listeners"
    @input="handleInput"
    :headers="tableHeaders"
    :items="tableRows"
    :server-items-length="totalElements"
    :loading="fetchPageEvent.state.loading"
    :errors="[fetchPageEvent.state.error]"
  >
    <template v-slot:item.address="{ item }">
      <span class="text-pre-wrap"> {{ item.address }} </span>
    </template>
    <template v-slot:item.status="{ item }">
      <data-table-item-status :status="item.status" />
    </template>
  </single-select-query-data-table>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import { DataRequestStatus } from "@/api";
import SearchField from "@/components/pageable/search-field.vue";
import { getEventTrackingListTableRows } from "@/views/event-tracking-list/utils/mappeData";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import { DataQuery } from "@/api/common";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { eventTrackingApi } from "@/modules/event-tracking/services/api";
import DataTableItemStatus from "@/components/data-table-item-status.vue";
import SingleSelectQueryDataTable from "@/modules/iris-message/modules/message-data/components/single-select-query-data-table.vue";

const SelectEventProps = Vue.extend({
  inheritAttrs: false,
  props: {
    description: {
      type: String,
      default: "",
    },
    selectableStatus: {
      type: Array as PropType<DataRequestStatus[] | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    SingleSelectQueryDataTable,
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

  fetchPageEvent = eventTrackingApi.fetchPageEvent();

  get tableRows() {
    return getEventTrackingListTableRows(
      this.fetchPageEvent.state.result?.content || [],
      this.selectableStatus
    );
  }

  handleInput(value: string) {
    if (!this.description) {
      const match = this.tableRows.find((row) => row.id === value);
      this.$emit("update:description", match?.name || "");
    }
  }

  handleQueryUpdate(query: DataQuery) {
    this.fetchPageEvent.execute(query);
  }

  get totalElements(): number | undefined {
    return this.fetchPageEvent.state.result?.totalElements;
  }
}
</script>
