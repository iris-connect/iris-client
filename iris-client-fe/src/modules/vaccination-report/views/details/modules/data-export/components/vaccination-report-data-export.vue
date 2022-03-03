<template>
  <data-export-label
    :selected="selection.length"
    :total="total"
    :label="['Mitarbeiterdaten']"
    #default="{ exportLabel }"
  >
    <data-export-dialog
      :title="exportLabel"
      :disabled="selection.length <= 0"
      :items="exportFormatList"
      @export-csv-standard="exportStandard('csv')"
      @export-xlsx-standard="exportStandard('xlsx')"
    >
      <template #activator.prepend>
        <v-btn
          color="primary"
          :disabled="selection.length <= 0"
          data-test="export.default"
          @click="exportStandard('csv')"
        >
          {{ exportLabel }}
        </v-btn>
      </template>
    </data-export-dialog>
  </data-export-label>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { VaccinationReportDetails } from "@/api";
import InfoGrid from "@/components/info-grid.vue";
import DataExportLabel from "@/components/data-export/data-export-label.vue";
import DataExportDialog, {
  DataExportFormat,
} from "@/components/data-export/data-export-dialog.vue";
import { ExportFileType } from "@/utils/data-export/data-export";
import { getFormattedDate } from "@/utils/date";
import { VREmployeeTableRow } from "@/modules/vaccination-report/views/details/vaccination-report-details.view.vue";
import exportStandard from "@/modules/vaccination-report/views/details/modules/data-export/services/exportStandard";

const VaccinationReportDataExportProps = Vue.extend({
  inheritAttrs: false,
  props: {
    report: {
      type: Object as PropType<VaccinationReportDetails | null>,
      default: null,
    },
    selection: {
      type: Array as PropType<VREmployeeTableRow[]>,
      default: () => [],
    },
    total: {
      type: Number,
      default: 0,
    },
  },
});

@Component({
  components: {
    DataExportDialog,
    DataExportLabel,
    InfoGrid,
  },
})
export default class VaccinationReportDataExport extends VaccinationReportDataExportProps {
  exportFormatList: DataExportFormat[] = [
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
  ];
  exportStandard(type: ExportFileType): void {
    const fileName = [
      this.report?.facility?.name || "Export",
      getFormattedDate(new Date(), "YYYY-MM-DD_HHmm"),
    ].join("_");
    const exporter =
      type === "xlsx" ? exportStandard.exportXlsx : exportStandard.exportCsv;
    exporter(this.selection, fileName);
  }
}
</script>
