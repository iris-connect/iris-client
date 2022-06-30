<template>
  <multi-select-data-table
    v-bind="$attrs"
    v-on="$listeners"
    :items="tableRows"
    :headers="tableHeaders"
  >
    <template v-slot:item.vaccinationStatus="{ item }">
      <vaccination-status-chip :status="item.vaccinationStatus" />
    </template>
  </multi-select-data-table>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { VREmployee } from "@/api";
import {
  getVREmployeeTableHeaders,
  getVREmployeeTableRows,
  VREmployeeTableRow,
} from "@/modules/vaccination-report/services/mappedData";
import MultiSelectDataTable from "@/modules/iris-message/modules/message-data/components/multi-select-data-table.vue";
import VaccinationStatusChip from "@/modules/vaccination-report/components/vaccination-status-chip.vue";
const SelectEmployeesDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    items: {
      type: Array as PropType<VREmployee[] | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    VaccinationStatusChip,
    MultiSelectDataTable,
  },
})
export default class SelectEmployeesDataTable extends SelectEmployeesDataTableProps {
  get tableHeaders() {
    return getVREmployeeTableHeaders(true);
  }
  get tableRows(): VREmployeeTableRow[] {
    return getVREmployeeTableRows(this.items);
  }
}
</script>
