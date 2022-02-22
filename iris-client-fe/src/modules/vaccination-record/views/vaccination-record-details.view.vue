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
        :loading="recordApi.state.loading"
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
import { vaccinationRecordApi } from "@/modules/vaccination-record/api";
import { VREmployee } from "@/api";
import { getFormattedAddress } from "@/utils/address";
import IrisDataTable from "@/components/iris-data-table.vue";
import HistoryBack from "@/mixins/HistoryBack";
import vaccinationConstants from "@/modules/vaccination-record/constants";

@Component({
  components: {
    IrisDataTable,
    ErrorMessageAlert,
    SortableDataTable,
    SearchField,
    DataQueryHandler,
  },
})
export default class VaccinationRecordDetailsView extends Mixins(
  HistoryBack("vaccination-record-list")
) {
  search = "";
  selection = [];
  tableHeaders = [
    { text: "Nachname", value: "lastName", sortable: true },
    { text: "Vorname", value: "firstName", sortable: true },
    { text: "Adresse", value: "address", sortable: true },
    { text: "Erreger", value: "vaccination", sortable: true },
    { text: "Impfstatus", value: "vaccinationStatus", sortable: true },
  ];
  recordApi = vaccinationRecordApi.fetchVaccinationRecordDetails();
  mounted() {
    this.recordApi.execute(this.$route.params.id);
  }
  get tableRows() {
    const employees: VREmployee[] =
      this.recordApi.state.result?.employees || [];
    return employees.map((employee, index) => {
      return {
        id: index,
        lastName: employee.lastName || "-",
        firstName: employee.firstName || "-",
        address: getFormattedAddress(employee.address),
        vaccination: employee.vaccination || "-",
        vaccinationStatus: vaccinationConstants.getStatusName(
          employee.vaccinationStatus
        ),
      };
    });
  }
  getStatusColor = vaccinationConstants.getStatusColor;
}
</script>
