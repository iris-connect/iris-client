<template>
  <div>
    <select-employees-data-table
      v-bind="{ ...$attrs, ...tableData }"
      v-on="$listeners"
      :loading="vrApi.state.loading"
    />
    <error-message-alert :errors="[vrApi.state.error]" />
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
  vrApi = vaccinationReportApi.fetchVaccinationReportDetails();

  @Watch("reportId", { immediate: true })
  onEventIdChange(reportId: string) {
    if (reportId) {
      this.vrApi.execute(reportId);
    } else {
      this.vrApi.reset();
    }
  }

  get tableData(): {
    items: VREmployee[];
  } {
    const details = this.vrApi.state.result;
    return {
      items: details?.employees || [],
    };
  }
}
</script>
