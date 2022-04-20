<template>
  <vaccination-report-details-component
    class="my-7"
    :loading="vrApi.state.loading"
    :errors="[vrApi.state.error]"
    :vaccination-report="vaccinationReport"
  >
    <template #actions="{ selection, total }">
      <v-card-actions>
        <v-btn text @click="goBack"> Zurück </v-btn>
        <v-spacer />
        <data-export-label
          :selected="selection.length"
          :total="total"
          :label="['Mitarbeiterdaten']"
          :action-label="false"
          #default="{ exportLabel }"
        >
          <span class="mr-3">
            {{ exportLabel }}
          </span>
        </data-export-label>
        <vaccination-report-data-export
          :report="vaccinationReport"
          :selection="selection"
          :total="total"
        />
        <iris-message-data-export-dialog
          :data="messageData(selection)"
          label="senden"
          :disabled="selection.length <= 0"
        />
      </v-card-actions>
    </template>
  </vaccination-report-details-component>
</template>

<script lang="ts">
import { Component, Mixins } from "vue-property-decorator";
import { vaccinationReportApi } from "@/modules/vaccination-report/services/api";
import { IrisMessageDataDiscriminator, IrisMessageDataInsert } from "@/api";
import HistoryBack from "@/mixins/HistoryBack";
import VaccinationReportDataExport from "@/modules/vaccination-report/views/details/modules/data-export/components/vaccination-report-data-export.vue";
import DataExportLabel from "@/components/data-export/data-export-label.vue";
import IrisMessageDataExportDialog from "@/views/iris-message-create/components/iris-message-data-export-dialog.vue";
import _map from "lodash/map";
import VaccinationReportDetailsComponent from "@/modules/vaccination-report/views/details/components/vaccination-report-details.component.vue";
import { VREmployeeTableRow } from "@/modules/vaccination-report/services/mappedData";

@Component({
  components: {
    VaccinationReportDetailsComponent,
    IrisMessageDataExportDialog,
    DataExportLabel,
    VaccinationReportDataExport,
  },
})
export default class VaccinationReportDetailsView extends Mixins(
  HistoryBack("vaccination-report-list")
) {
  vrApi = vaccinationReportApi.fetchVaccinationReportDetails();
  mounted() {
    this.vrApi.execute(this.$route.params.id);
  }

  messageData(selection: VREmployeeTableRow[]): IrisMessageDataInsert {
    const employees: string[] = _map(selection, "raw.messageDataSelectId");
    return {
      discriminator: IrisMessageDataDiscriminator.VaccinationReport,
      description: "",
      payload: {
        report: this.vaccinationReport?.id || "",
        employees,
      },
    };
  }

  get vaccinationReport() {
    return this.vrApi.state.result;
  }
}
</script>
