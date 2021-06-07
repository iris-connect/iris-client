<template>
  <div>
    <event-tracking-details-export
      v-if="showExportModal"
      @close-export-modal="closeExportModal"
      @handle-standard-csv-export="handleStandardCsvExport"
      @handle-alternative-standard-csv-export="
        handleAlternativeStandardCsvExport
      "
      @handle-sormas-csv-event-participants-export="
        handleSormasCsvEventParticipantsExport
      "
      @handle-sormas-csv-contact-person-export="
        handleSormasCsvContactPersonExport
      "
    />
    <v-card>
      <v-card-title>
        <editable-field
          :value="formData.externalRequestId"
          name="externalRequestId"
          :rules="validationRules.defined"
          label="Details für Ereignis ID"
          v-slot="{ entry }"
          @submit="handleEditableField"
          required
        >
          Details für Ereignis ID:
          {{ entry }}
        </editable-field>
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <editable-field
              :value="formData.name"
              name="name"
              :rules="validationRules.defined"
              label="Name"
              v-slot="{ entry }"
              @submit="handleEditableField"
              required
            >
              <strong> Name: </strong><br />
              {{ entry }}
            </editable-field>
          </v-col>
          <v-col cols="12" md="6">
            <editable-field
              :value="formData.comment"
              name="comment"
              label="Kommentar"
              v-slot="{ entry }"
              @submit="handleEditableField"
              component="v-textarea"
              default-value="-"
            >
              <strong> Kommentar: </strong><br />
              <div class="white-space-pre-line">
                {{ entry }}
              </div>
            </editable-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-divider />
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" md="6">
            <v-row>
              <v-col cols="12">
                <strong> Zeitraum: </strong>
                {{ eventData.startTime }} - {{ eventData.endTime }}
              </v-col>
            </v-row>
          </v-col>
          <v-col cols="12" md="6">
            <v-row>
              <v-col cols="12">
                <span class="d-inline-block mr-3">
                  <strong> Status: </strong>
                </span>
                <v-chip :color="statusColor" dark class="my-n2">
                  {{ statusName }}
                </v-chip>
                <event-tracking-status-change
                  :status="eventData.status"
                  @update="handleStatusUpdate"
                />
              </v-col>
            </v-row>
          </v-col>
        </v-row>
        <event-tracking-details-location-info :location="eventData.location" />
        <v-row>
          <v-col cols="12" md="6">
            <strong> Generiert: </strong>
            {{ eventData.generatedTime }}
          </v-col>
          <v-col cols="12" md="6">
            <strong> Letzte Änderung: </strong>
            {{ eventData.lastChange }}
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <strong> Anfragedetails: </strong>
            {{ eventData.additionalInformation }}
          </v-col>
        </v-row>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <v-data-table
          :loading="loading"
          :headers="tableData.headers"
          :items="tableRows"
          :items-per-page="5"
          class="elevation-1 mt-5"
          :search="tableData.search"
          show-select
          v-model="tableData.select"
          show-expand
          single-expand
          :expanded.sync="tableData.expanded"
          @click:row="(item, slot) => slot.expand(!slot.isExpanded)"
        >
          <template v-if="isStatusRequested" #no-data>
            <span class="black--text">
              Die Kontaktdaten zu diesem Ereignis werden derzeit angefragt. Zum
              jetzigen Zeitpunkt liegen noch keine Daten vor.
            </span>
          </template>
          <template v-slot:expanded-item="{ headers, item }">
            <td></td>
            <td :colspan="headers.length - 1">
              <v-row>
                <template
                  v-for="(expandedHeader, ehIndex) in tableData.expandedHeaders"
                >
                  <v-col :key="ehIndex" cols="12" sm="4" md="2">
                    <v-list-item two-line dense>
                      <v-list-item-content>
                        <v-list-item-title>
                          {{ expandedHeader.text }}
                        </v-list-item-title>
                        <v-list-item-subtitle class="text-pre-line">
                          {{
                            item[expandedHeader.value]
                              ? item[expandedHeader.value]
                              : "-"
                          }}
                        </v-list-item-subtitle>
                      </v-list-item-content>
                    </v-list-item>
                  </v-col>
                </template>
              </v-row>
            </td>
          </template>
        </v-data-table>
        <error-message-alert :errors="errors" />
      </v-card-text>
      <v-card-actions>
        <v-btn color="white" @click="$router.back()"> Zurück</v-btn>
        <v-spacer />
        <v-btn
          color="primary"
          @click="handleStandardCsvExport"
          :disabled="tableData.select.length <= 0"
        >
          Auswahl exportieren
        </v-btn>
        <v-btn
          color="white"
          @click="openExportModal"
          :disabled="tableData.select.length <= 0"
        >
          Exportformat wählen
        </v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import EditableField from "@/components/form/editable-field.vue";
import EventTrackingStatusChange from "@/views/event-tracking-details/components/event-tracking-status-change.vue";
import EventTrackingDetailsLocationInfo from "@/views/event-tracking-details/components/event-tracking-details-location-info.vue";
import {
  EventData,
  TableRow,
  FormData,
} from "@/views/event-tracking-details/event-tracking-details.view.vue";
import EventTrackingDetailsExport from "@/views/event-tracking-details/components/csv-export-modal.component.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import rules from "@/common/validation-rules";
import { DataRequestStatus, DataRequestStatusUpdateByUser } from "@/api";
import StatusMessages from "@/constants/StatusMessages";
import StatusColors from "@/constants/StatusColors";
import { ErrorMessage } from "@/utils/axios";

const EventTrackingDetailsComponentProps = Vue.extend({
  props: {
    tableRows: {
      type: Array as () => TableRow[],
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    errors: {
      type: Array as () => ErrorMessage[],
      default: () => [],
    },
    eventData: {
      type: Object as () => EventData,
      default: null,
    },
    formData: {
      type: Object as () => FormData,
      default: () => ({}),
    },
  },
});

@Component({
  components: {
    ErrorMessageAlert,
    EventTrackingDetailsLocationInfo,
    EventTrackingStatusChange,
    EditableField,
    EventTrackingDetailsExport,
  },
})
export default class EventTrackingDetailsComponent extends EventTrackingDetailsComponentProps {
  tableData = {
    search: "",
    expanded: [],
    select: [],
    headers: [
      { text: "", value: "data-table-select" },
      {
        text: "Nachname",
        value: "lastName",
        align: "start",
      },
      {
        text: "Vorname",
        value: "firstName",
      },
      {
        text: "Check-In",
        value: "checkInTime",
      },
      {
        text: "Check-Out",
        value: "checkOutTime",
      },
      {
        text: "max. Kontaktdauer",
        value: "maxDuration",
      },
      {
        text: "Kommentar",
        value: "comment",
      },
      { text: "", value: "data-table-expand" },
    ],
    expandedHeaders: [
      {
        text: "Geschlecht",
        value: "sex",
      },
      {
        text: "E-Mail",
        value: "email",
      },
      {
        text: "Telefon",
        value: "phone",
      },
      {
        text: "Mobil",
        value: "mobilePhone",
      },
      {
        text: "Adresse",
        value: "address",
      },
    ],
  };

  showExportModal = false;

  validationRules = {
    defined: [rules.defined],
  };

  get isStatusRequested(): boolean {
    return this.eventData?.status === DataRequestStatus.DataRequested;
  }

  get statusName(): string {
    return StatusMessages.getMessage(this.eventData?.status);
  }

  get statusColor(): string {
    return StatusColors.getColor(this.eventData?.status);
  }

  handleEditableField(
    data: Record<string, unknown>,
    resolve: () => void,
    reject: (error: string | undefined) => void
  ): void {
    this.$emit("field-edit", data, resolve, reject);
  }

  handleStatusUpdate(status: DataRequestStatusUpdateByUser): void {
    this.$emit("status-update", status);
  }

  handleStandardCsvExport(): void {
    const exportData: TableRow[] = this.tableData.select;
    this.$emit("handle-standard-csv-export", exportData);
  }

  handleAlternativeStandardCsvExport(): void {
    const exportData: TableRow[] = this.tableData.select;
    this.$emit("handle-alternative-standard-csv-export", exportData);
  }

  handleSormasCsvEventParticipantsExport(): void {
    const exportData: TableRow[] = this.tableData.select;
    this.$emit("handle-sormas-csv-event-participants-export", exportData);
  }

  handleSormasCsvContactPersonExport(): void {
    const exportData: TableRow[] = this.tableData.select;
    this.$emit("handle-sormas-csv-contact-person-export", exportData);
  }

  openExportModal(): void {
    this.showExportModal = true;
  }

  closeExportModal(): void {
    this.showExportModal = false;
  }
}
</script>
<style scoped lang="scss">
.white-space-pre-line {
  white-space: pre-line;
  max-height: 150px;
  width: 100%;
  overflow-y: auto;
}
</style>
