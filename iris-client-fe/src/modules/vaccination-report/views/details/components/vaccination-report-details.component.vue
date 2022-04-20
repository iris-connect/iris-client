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
      <search-field :debounce="0" v-model="search" />
      <iris-data-table
        v-model="table.selection"
        class="mt-5"
        :headers="table.headers"
        :items="tableRows"
        :search="search"
        :loading="loading"
        :show-select="selectEnabled"
        :show-select-all="selectEnabled"
        show-expand
        :expanded.sync="table.expanded"
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
        <template v-slot:expanded-item="{ headers, item }">
          <td v-if="selectEnabled"></td>
          <td :colspan="selectEnabled ? headers.length - 1 : headers.length">
            <expanded-data-table-item
              :expanded-headers="table.expandedHeaders"
              :item="item"
            />
          </td>
        </template>
      </iris-data-table>
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
import SearchField from "@/components/pageable/search-field.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { VaccinationReportDetails, VaccinationStatus } from "@/api";
import IrisDataTable from "@/components/iris-data-table.vue";
import vaccinationReportConstants from "@/modules/vaccination-report/services/constants";
import InfoGrid from "@/components/info-grid.vue";
import { getFormattedDate } from "@/utils/date";
import { getEnumKeys } from "@/utils/data";
import BtnToggleSelect from "@/components/btn-toggle-select.vue";
import VaccinationReportFacilityInfo from "@/modules/vaccination-report/views/details/components/vaccination-report-facility-info.vue";
import ExpandedDataTableItem from "@/components/expanded-data-table-item.vue";
import {
  getVREmployeeTableHeaders,
  getVREmployeeTableRows,
  VREmployeeTableRow,
} from "@/modules/vaccination-report/services/mappedData";
import { PropType } from "vue";
import { ErrorMessage } from "@/utils/axios";

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
      type: Array as PropType<ErrorMessage[]>,
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
    ExpandedDataTableItem,
    VaccinationReportFacilityInfo,
    BtnToggleSelect,
    InfoGrid,
    IrisDataTable,
    ErrorMessageAlert,
    SearchField,
  },
})
export default class VaccinationReportDetailsComponent extends VaccinationReportDetailsComponentProps {
  search = "";
  status = "";
  table = {
    selection: [],
    expanded: [],
    ...getVREmployeeTableHeaders(this.selectEnabled),
  };
  get filteredTableRows() {
    return this.tableRows;
  }
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
