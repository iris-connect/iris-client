<template>
  <v-card class="my-7">
    <v-card-title> Impfpflichtmeldung </v-card-title>
    <v-card-text>
      <vaccination-report-facility-info :facility="facility" />
      <info-grid :content="dateInfo" />
      <btn-toggle-select
        class="my-5"
        :select-options="statusSelectOptions"
        v-model="status"
      />
      <search-field :debounce="0" v-model="search" />
      <sortable-data-table
        v-model="selection"
        class="mt-5"
        :headers="tableHeaders"
        :items="tableRows"
        :search="search"
        :loading="vrApi.state.loading"
        show-select
        show-select-all
        :filter="dataTableFilter"
      >
        <template v-slot:item.address="{ item }">
          <span class="text-pre-line"> {{ item.address }} </span>
        </template>
        <template v-slot:item.vaccinationStatus="{ item }">
          <v-chip :color="getStatusColor(item.vaccinationStatus)" dark>
            {{ item.vaccinationStatus }}
          </v-chip>
        </template>
      </sortable-data-table>
      <error-message-alert :errors="[vrApi.state.error]" />
    </v-card-text>
    <v-card-actions>
      <v-btn text @click="goBack"> Zurück </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
import { Component, Mixins } from "vue-property-decorator";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import SearchField from "@/components/pageable/search-field.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { vaccinationReportApi } from "@/modules/vaccination-report/api";
import { VaccinationStatus, VREmployee } from "@/api";
import { getFormattedAddress } from "@/utils/address";
import IrisDataTable from "@/components/iris-data-table.vue";
import HistoryBack from "@/mixins/HistoryBack";
import vaccinationReportConstants from "@/modules/vaccination-report/constants";
import InfoGrid from "@/components/info-grid.vue";
import { getFormattedDate } from "@/utils/date";
import { getEnumKeys } from "@/utils/data";
import BtnToggleSelect from "@/components/btn-toggle-select.vue";
import VaccinationReportFacilityInfo from "@/modules/vaccination-report/components/vaccination-report-facility-info.vue";

type TableRow = {
  id: number | string;
  lastName: string;
  firstName: string;
  address: string;
  vaccination: string;
  vaccinationStatus: string;
};

@Component({
  components: {
    VaccinationReportFacilityInfo,
    BtnToggleSelect,
    InfoGrid,
    IrisDataTable,
    ErrorMessageAlert,
    SortableDataTable,
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
  get tableRows(): TableRow[] {
    const employees: VREmployee[] = this.vrApi.state.result?.employees || [];
    return employees.map((employee, index) => {
      return {
        id: index,
        lastName: employee.lastName || "-",
        firstName: employee.firstName || "-",
        address: getFormattedAddress(employee.address),
        vaccination: employee.vaccination || "-",
        vaccinationStatus: vaccinationReportConstants.getStatusName(
          employee.vaccinationStatus
        ),
      };
    });
  }
  get facility() {
    return this.vrApi.state.result?.facility;
  }
  get dateInfo() {
    const reportedAt = this.vrApi.state.result?.reportedAt;
    return [[["Meldung vom", getFormattedDate(reportedAt)]]];
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
  dataTableFilter(value: TableRow) {
    if (this.status) {
      const statusName = vaccinationReportConstants.getStatusName(this.status);
      return value.vaccinationStatus === statusName;
    }
    return true;
  }
}
</script>
