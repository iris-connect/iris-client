<template>
  <v-card class="my-7">
    <v-card-title> Impfstatus Nachweise </v-card-title>
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
          :loading="recordsApi.state.loading"
          :page.sync="query.page"
          :items-per-page.sync="query.size"
          :server-items-length="totalElements"
          @click:row="handleRowClick"
        >
          <template v-slot:item.address="{ item }">
            <span class="text-pre-line"> {{ item.address }} </span>
          </template>
        </sortable-data-table>
        <error-message-alert :errors="[recordsApi.state.error]" />
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
import { vaccinationRecordApi } from "@/modules/vaccination-record/api";
import { DataQuery } from "@/api/common";
import { VaccinationRecord, VaccinationStatus } from "@/api";
import { getFormattedDate } from "@/utils/date";
import { getFormattedAddress } from "@/utils/address";
import vaccinationConstants from "@/modules/vaccination-record/constants";
import _values from "lodash/values";
import _sum from "lodash/sum";
import { getEnumKeys } from "@/utils/data";

const getStatusTableHeader = (status: VaccinationStatus) => {
  return {
    text: `#\xa0${vaccinationConstants.getStatusName(status)}`,
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
export default class VaccinationRecordListView extends Vue {
  tableHeaders = [
    { text: "Einrichtung", value: "name", sortable: true },
    { text: "Adresse", value: "address", sortable: false },
    { text: "#\xa0Angestellte", value: "employeeCount", sortable: false },
    ...getEnumKeys(VaccinationStatus).map((s) =>
      getStatusTableHeader(VaccinationStatus[s])
    ),
    { text: "Meldung vom", value: "reportedAt", sortable: true },
  ];
  recordsApi = vaccinationRecordApi.fetchPageVaccinationRecord();
  handleQueryUpdate(newValue: DataQuery) {
    if (newValue) {
      this.recordsApi.execute(newValue);
    } else {
      this.recordsApi.reset(["result"]);
    }
  }
  get tableRows() {
    const vaccinationRecords: VaccinationRecord[] =
      this.recordsApi.state.result?.content || [];
    return vaccinationRecords.map((record) => {
      const { facility } = record;
      return {
        id: record.id,
        name: facility?.name || "-",
        address: getFormattedAddress(facility?.address),
        employeeCount: _sum(_values(record.vaccinationStatusCount)),
        vaccinationStatusCount: record.vaccinationStatusCount,
        reportedAt: getFormattedDate(record.reportedAt),
      };
    });
  }
  get totalElements(): number | undefined {
    return this.recordsApi.state.result?.totalElements;
  }
  handleRowClick(row: { id?: string }) {
    if (row.id) {
      this.$router.push({
        name: "vaccination-record-details",
        params: { id: row.id },
      });
    }
  }
}
</script>
