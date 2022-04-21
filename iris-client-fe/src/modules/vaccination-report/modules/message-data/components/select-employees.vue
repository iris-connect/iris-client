<template>
  <div>
    <select-employees-data-table
      v-bind="{ ...$attrs, ...tableData }"
      v-on="$listeners"
      :loading="fetchVaccinationReportDetails.state.loading"
    />
    <error-message-alert :errors="fetchVaccinationReportDetails.state.error" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { VREmployee } from "@/api";
import { vaccinationReportApi } from "@/modules/vaccination-report/services/api";
import SelectEmployeesDataTable from "@/modules/vaccination-report/modules/message-data/components/select-employees-data-table.vue";
const SelectEmployeesProps = Vue.extend({
  inheritAttrs: false,
  props: {
    reportId: {
      type: String,
      default: "",
    },
  },
});
@Component({
  components: {
    SelectEmployeesDataTable,
    ErrorMessageAlert,
  },
})
export default class SelectEmployees extends SelectEmployeesProps {
  fetchVaccinationReportDetails =
    vaccinationReportApi.fetchVaccinationReportDetails();

  @Watch("reportId", { immediate: true })
  onReportIdChange(reportId: string) {
    if (reportId) {
      this.fetchVaccinationReportDetails.execute(reportId);
    } else {
      this.fetchVaccinationReportDetails.reset();
    }
  }

  get tableData(): {
    items: VREmployee[];
  } {
    const details = this.fetchVaccinationReportDetails.state.result;
    return {
      items: details?.employees || [],
    };
  }
}
</script>
