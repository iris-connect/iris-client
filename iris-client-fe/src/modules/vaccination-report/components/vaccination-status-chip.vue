<template>
  <v-chip :color="statusColor" :data-test="`status.${status}`" dark>
    {{ statusName }}
  </v-chip>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import vaccinationReportConstants from "@/modules/vaccination-report/services/constants";
const VaccinationStatusChipProps = Vue.extend({
  inheritAttrs: false,
  props: {
    status: {
      type: String,
      default: "",
    },
  },
});
@Component
export default class VaccinationStatusChip extends VaccinationStatusChipProps {
  get statusColor() {
    return vaccinationReportConstants.getStatusColor(this.status);
  }
  get statusName() {
    return vaccinationReportConstants.getStatusName(this.status);
  }
}
</script>
