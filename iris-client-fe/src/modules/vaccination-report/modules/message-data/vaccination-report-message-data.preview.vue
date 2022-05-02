<template>
  <vaccination-report-details-component
    :vaccination-report="payload"
    :select-enabled="false"
  />
</template>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import VaccinationReportDetailsComponent from "@/modules/vaccination-report/views/details/components/vaccination-report-details.component.vue";
import { VaccinationReportDetails } from "@/api";

const VaccinationReportMessageDataPreviewProps = Vue.extend({
  props: {
    payload: {
      type: Object as PropType<VaccinationReportDetails | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    VaccinationReportDetailsComponent,
  },
})
export default class VaccinationReportMessageDataPreview extends VaccinationReportMessageDataPreviewProps {}
</script>
