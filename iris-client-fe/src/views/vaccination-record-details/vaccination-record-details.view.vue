<template>
  <v-card class="my-7">
    <v-card-title> Details für Impfstatus Nachweis </v-card-title>
    <v-card-text>
      <search-field :debounce="0" v-model="search" />
      <sortable-data-table
        v-model="selection"
        class="mt-5"
        :headers="tableHeaders"
        :items="tableRows"
        :search="search"
        show-select
        show-select-all
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
      <error-message-alert :errors="[recordApi.state.error]" />
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
import { VaccinationStatus, VrCompanyEmployee } from "@/api";
import { getFormattedAddress } from "@/utils/address";
import IrisDataTable from "@/components/iris-data-table.vue";

@Component({
  components: {
    IrisDataTable,
    ErrorMessageAlert,
    SortableDataTable,
    SearchField,
    DataQueryHandler,
  },
})
export default class VaccinationRecordDetailsView extends Vue {
  search = "";
  selection = [];
  tableHeaders = [
    { text: "Nachname", value: "lastName", sortable: true },
    { text: "Vorname", value: "firstName", sortable: true },
    { text: "Adresse", value: "address", sortable: true },
    { text: "Erreger", value: "vaccination", sortable: true },
    { text: "Impfstatus", value: "vaccinationStatus", sortable: true },
  ];
  recordApi = vaccinationRecordApi.fetchVaccinationRecord();
  mounted() {
    this.recordApi.execute(this.$route.params.id);
  }
  get tableRows() {
    const employees: VrCompanyEmployee[] =
      this.recordApi.state.result?.employees || [];
    return employees.map((employee, index) => {
      return {
        id: index,
        lastName: employee.lastName || "-",
        firstName: employee.firstName || "-",
        address: getFormattedAddress(employee),
        vaccination: employee.vaccination || "-",
        vaccinationStatus: this.getStatusName(employee.vaccinationStatus),
      };
    });
  }
  // If we pass the statusName to the table, the user is able to sort / search for it
  // caveat: We have to use the statusName for retrieving the color
  getStatusColor(status: string): string {
    switch (status) {
      case this.getStatusName(VaccinationStatus.VACCINATED):
        return "green";
      case this.getStatusName(VaccinationStatus.NOT_VACCINATED):
        return "red";
      case this.getStatusName(VaccinationStatus.SUSPICIOUS_PROOF):
        return "warning";
      default:
        return "gray";
    }
  }
  getStatusName(status?: VaccinationStatus): string {
    switch (status) {
      case VaccinationStatus.VACCINATED:
        return "Geimpft";
      case VaccinationStatus.NOT_VACCINATED:
        return "Ungeimpft";
      case VaccinationStatus.SUSPICIOUS_PROOF:
        return "Verdächtiger Nachweis";
      default:
        return "unbekannt";
    }
  }
}
</script>
