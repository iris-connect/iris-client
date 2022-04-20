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
      :loading="vrApi.state.loading"
      show-select
      single-select
      :page.sync="query.page"
      :items-per-page.sync="query.size"
      :server-items-length="totalElements"
    >
      <template v-slot:item.address="{ item }">
        <span class="text-pre-line"> {{ item.address }} </span>
      </template>
    </sortable-data-table>
    <error-message-alert :errors="[vrApi.state.error]" />
  </data-query-handler>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import { Page } from "@/api";
import SearchField from "@/components/pageable/search-field.vue";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import { DataQuery } from "@/api/common";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import DataTableItemStatus from "@/components/data-table-item-status.vue";
import { vaccinationReportApi } from "@/modules/vaccination-report/services/api";
import {
  getVaccinationReportTableHeaders,
  getVaccinationReportTableRows,
  VaccinationReportTableRow,
} from "@/modules/vaccination-report/services/mappedData";

const SelectReportProps = Vue.extend({
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
    selectQuery: {
      type: Object as PropType<Partial<DataQuery> | null>,
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
export default class SelectReport extends SelectReportProps {
  tableHeaders = getVaccinationReportTableHeaders();
  vrApi = vaccinationReportApi.fetchPageVaccinationReport();

  get tableRows() {
    return getVaccinationReportTableRows(this.vrApi.state.result?.content);
  }

  get selection(): VaccinationReportTableRow[] {
    if (this.value.length <= 0) return [];
    return this.tableRows.filter((row) => {
      return row.id === this.value;
    });
  }
  set selection(rows: VaccinationReportTableRow[]) {
    const sel = rows.find((row) => row.id);
    this.$emit("input", sel?.id || "");
  }

  handleQueryUpdate(query: DataQuery) {
    this.vrApi.execute(query);
  }

  get totalElements(): number | undefined {
    return this.vrApi.state.result?.totalElements;
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
