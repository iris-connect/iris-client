<template>
  <v-card>
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
      <expandable-data-table
        v-model="table.selection"
        :headers="table.headers"
        :expanded-headers="table.expandedHeaders"
        :items="tableRows"
        :loading="loading"
        :select-enabled="selectEnabled"
        :filter="dataTableFilter"
        data-test="vaccination-report.employee.data-table"
      >
        <template v-slot:item.vaccinationStatus="{ item }">
          <vaccination-status-chip :status="item.vaccinationStatus" />
        </template>
      </expandable-data-table>
      <error-message-alert :errors="errors" />
    </v-card-text>
    <slot
      name="actions"
      v-bind="{ selection: table.selection, total: tableRows.length }"
    />
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { VaccinationReportDetails, VaccinationStatus } from "@/api";
import vaccinationReportConstants from "@/modules/vaccination-report/services/constants";
import InfoGrid from "@/components/info-grid.vue";
import { getFormattedDate } from "@/utils/date";
import { getEnumKeys } from "@/utils/data";
import BtnToggleSelect from "@/components/btn-toggle-select.vue";
import VaccinationReportFacilityInfo from "@/modules/vaccination-report/views/details/components/vaccination-report-facility-info.vue";
import {
  getVREmployeeTableHeaders,
  getVREmployeeTableRows,
  VREmployeeTableRow,
} from "@/modules/vaccination-report/services/mappedData";
import { PropType } from "vue";
import VaccinationStatusChip from "@/modules/vaccination-report/components/vaccination-status-chip.vue";
import ExpandableDataTable from "@/components/expandable-data-table.vue";

const VaccinationReportDetailsComponentProps = Vue.extend({
  inheritAttrs: false,
  props: {
    vaccinationReport: {
      type: Object as PropType<VaccinationReportDetails | null>,
      default: null,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    errors: {
      type: [Array, String],
      default: () => [],
    },
    selectEnabled: {
      type: Boolean,
      default: true,
    },
  },
});

@Component({
  components: {
    ExpandableDataTable,
    VaccinationStatusChip,
    VaccinationReportFacilityInfo,
    BtnToggleSelect,
    InfoGrid,
    ErrorMessageAlert,
  },
})
export default class VaccinationReportDetailsComponent extends VaccinationReportDetailsComponentProps {
  status = "";
  table = {
    selection: [],
    ...getVREmployeeTableHeaders(this.selectEnabled),
  };
  get tableRows(): VREmployeeTableRow[] {
    return getVREmployeeTableRows(this.vaccinationReport?.employees);
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
  dataTableFilter(value: VREmployeeTableRow) {
    if (this.status) {
      return value.vaccinationStatus === this.status;
    }
    return true;
  }
}
</script>
