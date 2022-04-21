<template>
  <single-select-query-data-table
    @update:query="handleQueryUpdate"
    v-bind="$attrs"
    v-on="$listeners"
    @input="handleInput"
    :headers="tableHeaders"
    :items="tableRows"
    :server-items-length="totalElements"
    :loading="fetchPageVaccinationReport.state.loading"
    :errors="[fetchPageVaccinationReport.state.error]"
  >
    <template v-slot:item.address="{ item }">
      <span class="text-pre-line"> {{ item.address }} </span>
    </template>
  </single-select-query-data-table>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { DataQuery } from "@/api/common";
import { vaccinationReportApi } from "@/modules/vaccination-report/services/api";
import {
  getVaccinationReportTableHeaders,
  getVaccinationReportTableRows,
} from "@/modules/vaccination-report/services/mappedData";
import SingleSelectQueryDataTable from "@/modules/iris-message/modules/message-data/components/single-select-query-data-table.vue";
import { join } from "@/utils/misc";
import { getFormattedDate } from "@/utils/date";

const SelectReportProps = Vue.extend({
  inheritAttrs: false,
  props: {
    description: {
      type: String,
      default: "",
    },
  },
});
@Component({
  components: {
    SingleSelectQueryDataTable,
  },
})
export default class SelectReport extends SelectReportProps {
  tableHeaders = getVaccinationReportTableHeaders();
  fetchPageVaccinationReport =
    vaccinationReportApi.fetchPageVaccinationReport();

  get tableRows() {
    return getVaccinationReportTableRows(
      this.fetchPageVaccinationReport.state.result?.content
    );
  }

  handleInput(value: string) {
    if (!this.description) {
      const match = this.tableRows.find((row) => row.id === value);
      const name = match?.facility?.name;
      const reportedAt = getFormattedDate(match?.reportedAt, "L", "");
      const description = name ? join([name, reportedAt], " ") : "";
      this.$emit("update:description", description);
    }
  }

  handleQueryUpdate(query: DataQuery) {
    this.fetchPageVaccinationReport.execute(query);
  }

  get totalElements(): number | undefined {
    return this.fetchPageVaccinationReport.state.result?.totalElements;
  }
}
</script>
