<template>
  <export-dialog
    @export-csv-standard="handleExportCsvStandard"
    @export-csv-alternative-standard="handleExportCsvAlternativeStandard"
    @export-csv-sormas-event="handleExportCsvSormasEventParticipants"
    @export-csv-sormas-contact="handleExportCsvSormasContactPersons"
    @export-xlsx-octoware="handleXlsxExportOctoware"
  >
    <template #activator="{ attrs, on }">
      <v-btn
        v-on="on"
        v-bind="attrs"
        color="white"
        :disabled="selection.length <= 0"
        data-test="export-dialog.activator"
      >
        Exportformat wählen
      </v-btn>
      <v-btn
        color="primary"
        @click="handleExportCsvStandard"
        :disabled="selection.length <= 0"
        data-test="export.csv.standard"
      >
        Auswahl exportieren
      </v-btn>
    </template>
  </export-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { TableRow } from "@/views/event-tracking-details/event-tracking-details.view.vue";
import { PropType } from "vue";
import { DataRequestDetails } from "@/api";
import ExportDialog from "@/views/event-tracking-details/components/data-export/export-dialog.vue";
import exportCsvStandard from "@/views/event-tracking-details/components/data-export/utils/exportCsvStandard";
import exportXlsxOctoware from "@/views/event-tracking-details/components/data-export/utils/exportXlsxOctoware";
import exportCsvSormasEventParticipants from "@/views/event-tracking-details/components/data-export/utils/exportCsvSormasEventParticipants";
import exportCsvSormasContactPersons from "@/views/event-tracking-details/components/data-export/utils/exportCsvSormasContactPersons";

const EventTrackingDetailsDataExportProps = Vue.extend({
  props: {
    selection: {
      type: Array as PropType<TableRow[]>,
      default: () => [],
    },
    event: {
      type: Object as PropType<DataRequestDetails | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    ExportDialog,
  },
})
export default class EventTrackingDetailsDataExport extends EventTrackingDetailsDataExportProps {
  getFileName() {
    return [this.event?.externalRequestId || "Export", Date.now()].join("_");
  }

  handleExportCsvStandard(): void {
    exportCsvStandard.exportData(this.selection, this.getFileName());
  }

  handleExportCsvAlternativeStandard(): void {
    exportCsvStandard.exportData(this.selection, this.getFileName(), false);
  }

  handleExportCsvSormasEventParticipants(): void {
    const data = exportCsvSormasEventParticipants.mapData(this.selection);
    exportCsvSormasEventParticipants.exportData(data, this.getFileName());
  }

  handleExportCsvSormasContactPersons(): void {
    const data = exportCsvSormasContactPersons.mapData(
      this.event,
      this.selection
    );
    exportCsvSormasContactPersons.exportData(data, this.getFileName());
  }

  handleXlsxExportOctoware(): void {
    const data = exportXlsxOctoware.mapData(this.event, this.selection);
    exportXlsxOctoware.exportData(data, this.getFileName());
  }
}
</script>
