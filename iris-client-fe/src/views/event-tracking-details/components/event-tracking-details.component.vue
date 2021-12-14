<template>
  <v-card>
    <v-card-title>
      <editable-field
        :value="formData.externalRequestId"
        name="externalRequestId"
        :rules="validationRules.sanitisedAndDefined"
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
            :rules="validationRules.sanitisedAndDefined"
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
            :rules="validationRules.sanitised"
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
            <v-col cols="12" data-test="event.duration">
              <strong> Zeitraum: </strong>
              {{ eventData.startTime }} - {{ eventData.endTime }}
            </v-col>
          </v-row>
        </v-col>
        <v-col cols="12" md="6">
          <v-row>
            <v-col cols="12" data-test="event.status">
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
        <v-col data-test="event.requestDetails">
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
      <iris-data-table
        :loading="loading"
        :headers="tableData.headers"
        :items="tableRows"
        :items-per-page="5"
        class="elevation-1 mt-5"
        :search="tableData.search"
        show-select
        show-select-all
        v-model="tableData.select"
        show-expand
        single-expand
        :expanded.sync="tableData.expanded"
        @click:row="(item, slot) => slot.expand(!slot.isExpanded)"
        data-test="event.contacts.data-table"
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
      </iris-data-table>
      <error-message-alert :errors="errors" />
    </v-card-text>
    <v-card-actions>
      <v-btn color="white" :to="{ name: 'event-list' }" replace> Zurück </v-btn>
      <v-spacer />
      <slot name="data-export" v-bind:selection="tableData.select" />
    </v-card-actions>
  </v-card>
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
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import rules from "@/common/validation-rules";
import { DataRequestStatus, DataRequestStatusUpdateByUser } from "@/api";
import StatusMessages from "@/constants/StatusMessages";
import StatusColors from "@/constants/StatusColors";
import { ErrorMessage } from "@/utils/axios";
import IrisDataTable from "@/components/iris-data-table.vue";

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
    IrisDataTable,
    ErrorMessageAlert,
    EventTrackingDetailsLocationInfo,
    EventTrackingStatusChange,
    EditableField,
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

  validationRules = {
    defined: [rules.defined],
    sanitisedAndDefined: [rules.defined, rules.sanitised],
    sanitised: [rules.sanitised],
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
