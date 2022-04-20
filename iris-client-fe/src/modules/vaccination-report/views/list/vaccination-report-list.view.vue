<template>
  <v-card class="my-7">
    <v-card-title> Impfpflichtmeldungen </v-card-title>
    <v-card-text>
      <data-query-handler
        @query:update="handleQueryUpdate"
        #default="{ query }"
      >
        <search-field v-model="query.search" />
        <sortable-data-table
          class="mt-5"
          :item-class="() => 'cursor-pointer'"
          :headers="tableHeaders"
          :sort.sync="query.sort"
          :items="tableRows"
          :loading="vrApi.state.loading"
          :page.sync="query.page"
          :items-per-page.sync="query.size"
          :server-items-length="totalElements"
          @click:row="handleRowClick"
          data-test="view.data-table"
        >
          <template v-slot:item.address="{ item }">
            <span class="text-pre-line"> {{ item.address }} </span>
          </template>
        </sortable-data-table>
        <error-message-alert :errors="[vrApi.state.error]" />
      </data-query-handler>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import SearchField from "@/components/pageable/search-field.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { vaccinationReportApi } from "@/modules/vaccination-report/services/api";
import { DataQuery } from "@/api/common";
import {
  getVaccinationReportTableHeaders,
  getVaccinationReportTableRows,
} from "@/modules/vaccination-report/services/mappedData";

@Component({
  components: {
    ErrorMessageAlert,
    SortableDataTable,
    SearchField,
    DataQueryHandler,
  },
})
export default class VaccinationReportListView extends Vue {
  tableHeaders = getVaccinationReportTableHeaders();
  vrApi = vaccinationReportApi.fetchPageVaccinationReport();
  handleQueryUpdate(newValue: DataQuery) {
    if (newValue) {
      this.vrApi.execute(newValue);
    } else {
      this.vrApi.reset(["result"]);
    }
  }
  get tableRows() {
    return getVaccinationReportTableRows(this.vrApi.state.result?.content);
  }
  get totalElements(): number | undefined {
    return this.vrApi.state.result?.totalElements;
  }
  handleRowClick(row: { id?: string }) {
    if (row.id) {
      this.$router.push({
        name: "vaccination-report-details",
        params: { id: row.id },
      });
    }
  }
}
</script>
