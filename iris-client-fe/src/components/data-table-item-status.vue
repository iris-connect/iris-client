<template>
  <v-chip
    :color="getStatusColor(status)"
    dark
    :data-test="`status.${getStatusTestLabel(status)}`"
  >
    {{ getStatusName(status) }}
  </v-chip>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { DataRequestStatus } from "@/api";
import StatusColors from "@/constants/StatusColors";
import StatusMessages from "@/constants/StatusMessages";
import StatusTestLabel from "@/constants/StatusTestLabel";
import { PropType } from "vue";

const DataTableItemStatusProps = Vue.extend({
  props: {
    status: Object as PropType<DataRequestStatus | null>,
  },
});

@Component
export default class DataTableItemStatus extends DataTableItemStatusProps {
  getStatusColor(status: DataRequestStatus): string {
    return StatusColors.getColor(status);
  }
  getStatusName(status: DataRequestStatus): string {
    return StatusMessages.getMessage(status);
  }
  getStatusTestLabel(status: DataRequestStatus): string {
    return StatusTestLabel.getStatusTestLabel(status);
  }
}
</script>
