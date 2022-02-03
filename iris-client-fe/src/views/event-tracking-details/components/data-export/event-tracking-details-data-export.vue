<template>
  <export-dialog
    :title="exportLabel"
    :items="exportList"
    @export-csv-standard="exportStandard('csv')"
    @export-csv-standard-alternative="exportCsvStandardAlternative"
    @export-csv-sormas-event="exportSormasEventParticipants"
    @export-csv-sormas-contact="exportSormasContactPersons"
    @export-xlsx-standard="exportStandard('xlsx')"
    @export-xlsx-octoware="handleExportXlsxOctoware"
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
        @click="exportStandard('csv')"
        :disabled="selection.length <= 0"
        data-test="export.csv.standard"
      >
        {{ exportLabel }}
      </v-btn>
    </template>
  </export-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { GuestListTableRow } from "@/views/event-tracking-details/utils/mappedData";
import { PropType } from "vue";
import { DataRequestDetails } from "@/api";
import ExportDialog, {
  DataExportItem,
} from "@/views/event-tracking-details/components/data-export/export-dialog.vue";
import exportStandard from "@/views/event-tracking-details/components/data-export/utils/exportStandard";
import exportOctoware from "@/views/event-tracking-details/components/data-export/utils/exportOctoware";
import exportSormasEventParticipants from "@/views/event-tracking-details/components/data-export/utils/exportSormasEventParticipants";
import exportSormasContactPersons from "@/views/event-tracking-details/components/data-export/utils/exportSormasContactPersons";
import { getExportLabel } from "@/utils/data-export/common";

type FileType = "csv" | "xlsx";

const exportList: DataExportItem[] = [
  {
    label: "Standard",
    csv: {
      action: "export-csv-standard",
      test: "export.csv.standard",
    },
    xlsx: {
      action: "export-xlsx-standard",
      test: "export.xlsx.standard",
    },
  },
  {
    label: "Standard (Alternativ)",
    csv: {
      action: "export-csv-standard-alternative",
      test: "export.csv.standard-alternative",
    },
  },
  {
    label: "SORMAS (Ereignisteilnehmer-Format)",
    csv: {
      action: "export-csv-sormas-event",
      test: "export.csv.sormas-event-participants",
    },
  },
  {
    label: "SORMAS (Kontaktpersonen-Format)",
    csv: {
      action: "export-csv-sormas-contact",
      test: "export.csv.sormas-contact-persons",
    },
  },
  {
    label: "OctoWare®",
    xlsx: {
      action: "export-xlsx-octoware",
      test: "export.xlsx.octoware",
    },
  },
];

const EventTrackingDetailsDataExportProps = Vue.extend({
  inheritAttrs: false,
  props: {
    event: {
      type: Object as PropType<DataRequestDetails | null>,
      default: null,
    },
    selection: {
      type: Array as PropType<GuestListTableRow[]>,
      default: () => [],
    },
    itemsLength: {
      type: Number,
      default: 0,
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

  exportList = exportList;

  get exportLabel(): string {
    return getExportLabel(this.selection.length, this.itemsLength);
  }

  exportStandard(type: FileType): void {
    const exporter =
      type === "xlsx" ? exportStandard.exportXlsx : exportStandard.exportCsv;
    exporter(this.selection, this.getFileName());
  }

  exportCsvStandardAlternative(): void {
    exportStandard.exportCsv(this.selection, this.getFileName(), false);
  }

  exportSormasEventParticipants(): void {
    const data = exportSormasEventParticipants.mapData(this.selection);
    exportSormasEventParticipants.exportCsv(data, this.getFileName());
  }

  exportSormasContactPersons(): void {
    const data = exportSormasContactPersons.mapData(this.event, this.selection);
    exportSormasContactPersons.exportCsv(data, this.getFileName());
  }

  handleExportXlsxOctoware(): void {
    const data = exportOctoware.mapData(this.event, this.selection);
    exportOctoware.exportXlsx(data, this.getFileName());
  }
}
</script>
