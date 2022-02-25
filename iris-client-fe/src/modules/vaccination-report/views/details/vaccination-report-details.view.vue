<template>
  <v-card class="my-7">
    <v-card-title> Impfpflichtmeldung </v-card-title>
    <v-card-text>
      <vaccination-report-facility-info :facility="facility" />
      <info-grid :content="dateInfo" />
      <div class="my-6">
        <span class="mr-3">
          <strong>Status:</strong>
        </span>
        <btn-toggle-select
          :select-options="statusSelectOptions"
          data-test-key="status"
          v-model="status"
        />
      </div>
      <search-field :debounce="0" v-model="search" />
      <iris-data-table
        v-model="selection"
        class="mt-5"
        :headers="tableHeaders"
        :items="tableRows"
        :search="search"
        :loading="vrApi.state.loading"
        show-select
        show-select-all
        :filter="dataTableFilter"
        data-test="vaccination-report.employee.data-table"
      >
        <template v-slot:item.address="{ item }">
          <span class="text-pre-line"> {{ item.address }} </span>
        </template>
        <template v-slot:item.vaccinationStatus="{ item }">
          <v-chip
            :color="getStatusColor(item.vaccinationStatus)"
            :data-test="`status.${item.vaccinationStatus}`"
            dark
          >
            {{ getStatusName(item.vaccinationStatus) }}
          </v-chip>
        </template>
      </iris-data-table>
      <error-message-alert :errors="[vrApi.state.error]" />
    </v-card-text>
    <v-card-actions>
      <v-btn text @click="goBack"> Zurück </v-btn>
      <v-spacer />
      <vaccination-report-data-export
        :report="vaccinationReport"
        :selection="selection"
        :total="tableRows.length"
      />
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
import { Component, Mixins } from "vue-property-decorator";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import SearchField from "@/components/pageable/search-field.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { vaccinationReportApi } from "@/modules/vaccination-report/services/api";
import { VaccinationStatus, VREmployee } from "@/api";
import { getFormattedAddress } from "@/utils/address";
import IrisDataTable from "@/components/iris-data-table.vue";
import HistoryBack from "@/mixins/HistoryBack";
import vaccinationReportConstants from "@/modules/vaccination-report/services/constants";
import InfoGrid from "@/components/info-grid.vue";
import { getFormattedDate } from "@/utils/date";
import { getEnumKeys } from "@/utils/data";
import BtnToggleSelect from "@/components/btn-toggle-select.vue";
import VaccinationReportFacilityInfo from "@/modules/vaccination-report/views/details/components/vaccination-report-facility-info.vue";
import VaccinationReportDataExport from "@/modules/vaccination-report/views/details/modules/data-export/components/vaccination-report-data-export.vue";

export type VREmployeeTableRow = {
  id: number | string;
  lastName: string;
  firstName: string;
  address: string;
  vaccination: string;
  vaccinationStatus: string;
  raw: VREmployee;
};

@Component({
  components: {
    VaccinationReportDataExport,
    VaccinationReportFacilityInfo,
    BtnToggleSelect,
    InfoGrid,
    IrisDataTable,
    ErrorMessageAlert,
    SearchField,
    DataQueryHandler,
  },
})
export default class VaccinationReportDetailsView extends Mixins(
  HistoryBack("vaccination-report-list")
) {
  search = "";
  status = "";
  selection = [];
  tableHeaders = [
    { text: "Nachname", value: "lastName", sortable: true },
    { text: "Vorname", value: "firstName", sortable: true },
    { text: "Adresse", value: "address", sortable: true },
    { text: "Erreger", value: "vaccination", sortable: true },
    { text: "Impfstatus", value: "vaccinationStatus", sortable: true },
  ];
  vrApi = vaccinationReportApi.fetchVaccinationReportDetails();
  mounted() {
    this.vrApi.execute(this.$route.params.id);
  }
  get filteredTableRows() {
    return this.tableRows;
  }
  get tableRows(): VREmployeeTableRow[] {
    const employees: VREmployee[] = this.vaccinationReport?.employees || [];
    return employees.map((employee, index) => {
      return {
        id: index,
        lastName: employee.lastName || "-",
        firstName: employee.firstName || "-",
        address: getFormattedAddress(employee.address),
        vaccination: employee.vaccination || "-",
        vaccinationStatus: employee.vaccinationStatus || "-",
        raw: employee,
      };
    });
  }
  get vaccinationReport() {
    return this.vrApi.state.result;
  }
  get facility() {
    return this.vaccinationReport?.facility;
  }
  get dateInfo() {
    return [
      [["Meldung vom", getFormattedDate(this.vaccinationReport?.reportedAt)]],
    ];
  }
  get statusSelectOptions() {
    return getEnumKeys(VaccinationStatus).map((key) => {
      return {
        text: vaccinationReportConstants.getStatusName(VaccinationStatus[key]),
        value: VaccinationStatus[key],
      };
    });
  }
  getStatusColor = vaccinationReportConstants.getStatusColor;
  getStatusName = vaccinationReportConstants.getStatusName;
  dataTableFilter(value: VREmployeeTableRow) {
    if (this.status) {
      return value.vaccinationStatus === this.status;
    }
    return true;
  }
}
</script>
