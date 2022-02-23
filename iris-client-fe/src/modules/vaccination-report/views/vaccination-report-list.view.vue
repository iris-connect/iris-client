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
import { vaccinationReportApi } from "@/modules/vaccination-report/api";
import { DataQuery } from "@/api/common";
import { VaccinationReport, VaccinationStatus } from "@/api";
import { getFormattedDate } from "@/utils/date";
import { getFormattedAddress } from "@/utils/address";
import vaccinationReportConstants from "@/modules/vaccination-report/constants";
import _values from "lodash/values";
import _sum from "lodash/sum";
import { getEnumKeys } from "@/utils/data";

const getStatusTableHeader = (status: VaccinationStatus) => {
  return {
    text: `#\xa0${vaccinationReportConstants.getStatusName(status)}`,
    value: "vaccinationStatusCount." + status,
    sortable: true,
    width: 0,
  };
};

@Component({
  components: {
    ErrorMessageAlert,
    SortableDataTable,
    SearchField,
    DataQueryHandler,
  },
})
export default class VaccinationReportListView extends Vue {
  tableHeaders = [
    { text: "Einrichtung", value: "facility.name", sortable: true },
    { text: "Adresse", value: "address", sortable: false },
    { text: "#\xa0Angestellte", value: "employeeCount", sortable: false },
    ...getEnumKeys(VaccinationStatus).map((s) =>
      getStatusTableHeader(VaccinationStatus[s])
    ),
    { text: "Meldung vom", value: "reportedAt", sortable: true },
  ];
  vrApi = vaccinationReportApi.fetchPageVaccinationReport();
  handleQueryUpdate(newValue: DataQuery) {
    if (newValue) {
      this.vrApi.execute(newValue);
    } else {
      this.vrApi.reset(["result"]);
    }
  }
  get tableRows() {
    const vaccinationReports: VaccinationReport[] =
      this.vrApi.state.result?.content || [];
    return vaccinationReports.map((report) => {
      const { facility } = report;
      return {
        id: report.id,
        facility: {
          name: facility?.name || "-",
        },
        address: getFormattedAddress(facility?.address),
        employeeCount: _sum(_values(report.vaccinationStatusCount)),
        vaccinationStatusCount: report.vaccinationStatusCount,
        reportedAt: getFormattedDate(report.reportedAt),
      };
    });
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
