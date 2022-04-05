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
        :disabled="isPreview"
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
            :disabled="isPreview"
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
            :disabled="isPreview"
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
                v-if="!isPreview"
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
        :headers="tableHeaders.headers"
        :items="tableRows"
        :items-per-page="5"
        class="elevation-1 mt-5"
        :search="tableData.search"
        :show-select="selectEnabled"
        :show-select-all="selectEnabled"
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
          <td v-if="selectEnabled"></td>
          <td :colspan="selectEnabled ? headers.length - 1 : headers.length">
            <expanded-data-table-item
              :item="item"
              :expanded-headers="tableHeaders.expandedHeaders"
            />
          </td>
        </template>
      </iris-data-table>
      <error-message-alert :errors="errors" />
    </v-card-text>
    <v-card-actions v-if="!isPreview">
      <v-btn color="white" :to="{ name: 'event-list' }" replace> Zurück </v-btn>
      <v-spacer />
      <slot
        name="data-actions"
        v-bind="{
          selection: tableData.select,
          messageData: messageData,
          disabled: tableData.select.length <= 0,
        }"
      />
    </v-card-actions>
  </v-card>
</template>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import EditableField from "@/components/form/editable-field.vue";
import EventTrackingStatusChange from "@/views/event-tracking-details/components/event-tracking-status-change.vue";
import EventTrackingDetailsLocationInfo from "@/views/event-tracking-details/components/event-tracking-details-location-info.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import rules from "@/common/validation-rules";
import {
  DataRequestStatus,
  DataRequestStatusUpdateByUser,
  IrisMessageDataDiscriminator,
  IrisMessageDataInsert,
} from "@/api";
import StatusMessages from "@/constants/StatusMessages";
import StatusColors from "@/constants/StatusColors";
import { ErrorMessage } from "@/utils/axios";
import IrisDataTable from "@/components/iris-data-table.vue";
import _map from "lodash/map";
import {
  EventData,
  FormData,
  getGuestListTableHeaders,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import { PropType } from "vue";
import ExpandedDataTableItem from "@/components/expanded-data-table-item.vue";

const EventTrackingDetailsComponentProps = Vue.extend({
  props: {
    tableRows: {
      type: Array as PropType<GuestListTableRow[]>,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    errors: {
      type: Array as PropType<ErrorMessage[]>,
      default: () => [],
    },
    eventData: {
      type: Object as PropType<EventData | null>,
      default: null,
    },
    formData: {
      type: Object as PropType<FormData | null>,
      default: null,
    },
    isPreview: {
      type: Boolean,
      default: false,
    },
    selectEnabled: {
      type: Boolean,
      default: true,
    },
  },
});

@Component({
  components: {
    ExpandedDataTableItem,
    IrisDataTable,
    ErrorMessageAlert,
    EventTrackingDetailsLocationInfo,
    EventTrackingStatusChange,
    EditableField,
  },
})
export default class EventTrackingDetailsComponent extends EventTrackingDetailsComponentProps {
  get tableHeaders() {
    return getGuestListTableHeaders(this.selectEnabled);
  }
  tableData = {
    search: "",
    expanded: [],
    select: [],
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

  get messageData(): IrisMessageDataInsert {
    const guests: string[] = _map(
      this.tableData.select,
      "raw.messageDataSelectId"
    );
    return {
      discriminator: IrisMessageDataDiscriminator.EventTracking,
      description: this.formData?.name || "",
      payload: {
        event: this.eventData?.code || "",
        guests,
      },
    };
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
