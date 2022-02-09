<template>
  <div>
    <alert-component v-if="alert">
      <template v-slot:message>
        Die Kontaktdaten zu diesem Ereignis wurden angefragt.
      </template>
    </alert-component>
    <event-tracking-details-component
      :table-rows="tableRows"
      :event-data="eventData"
      :form-data="formData"
      :loading="loading"
      :errors="errorMessages"
      @field-edit="handleEditableField"
      @status-update="updateRequestStatus"
    >
      <template #data-export="{ selection }">
        <event-tracking-details-data-export
          :event="eventTrackingDetails"
          :items-length="tableRows.length"
          :selection="selection"
        />
      </template>
      <template #data-message="{ messageData, disabled }">
        <iris-message-data-export-dialog
          :data="messageData"
          :disabled="disabled"
        />
      </template>
    </event-tracking-details-component>
  </div>
</template>
<style></style>
<script lang="ts">
import {
  DataRequestClientUpdate,
  DataRequestDetails,
  DataRequestStatusUpdateByUser,
} from "@/api";
import { Component, Vue } from "vue-property-decorator";
import { ErrorMessage } from "@/utils/axios";
import EventTrackingDetailsComponent from "@/views/event-tracking-details/components/event-tracking-details.component.vue";
import AlertComponent from "@/components/alerts/alert-component.vue";
import EventTrackingDetailsDataExport from "@/views/event-tracking-details/components/data-export/event-tracking-details-data-export.vue";
import {
  EventData,
  FormData,
  getEventData,
  getFormData,
  getGuestListTableRows,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import IrisMessageDataExportDialog from "@/views/iris-message-create/components/iris-message-data-export-dialog.vue";
import { bundleEventTrackingApi } from "@/modules/event-tracking/api";
import { getApiErrorMessages, getApiLoading } from "@/utils/api";

@Component({
  components: {
    IrisMessageDataExportDialog,
    EventTrackingDetailsDataExport,
    EventTrackingDetailsComponent,
    AlertComponent,
  },
})
export default class EventTrackingDetailsView extends Vue {
  alert = false;

  eventApi = bundleEventTrackingApi(["fetchEventDetails", "patchDataRequest"]);

  mounted() {
    this.eventApi.fetchEventDetails.execute(this.$route.params.id);
  }

  get eventTrackingDetails(): DataRequestDetails | null {
    return this.eventApi.fetchEventDetails.state.result;
  }

  // Editable values => formData. Readonly values => eventData.
  get formData(): FormData {
    return getFormData(this.eventTrackingDetails);
  }

  get eventData(): EventData {
    return getEventData(this.eventTrackingDetails);
  }

  get loading(): boolean {
    return getApiLoading(this.eventApi);
  }

  get errorMessages(): ErrorMessage[] {
    return getApiErrorMessages(this.eventApi);
  }

  created(): void {
    if (this.$route.query.is_created == "true") {
      this.openAlert();
    }

    let query = Object.assign({}, this.$route.query);
    if (query.is_created) {
      delete query.is_created;
      this.$router.replace({ query });
    }
  }

  openAlert(): void {
    this.alert = true;
    setTimeout(() => {
      this.alert = false;
    }, 2000);
  }

  async updateMessage(
    data: DataRequestClientUpdate
  ): Promise<DataRequestDetails> {
    await this.eventApi.patchDataRequest.execute({
      id: this.$route.params.id,
      data,
    });
    return this.eventApi.fetchEventDetails.execute(this.$route.params.id);
  }

  updateRequestStatus(status: DataRequestStatusUpdateByUser): void {
    this.updateMessage({ status });
  }

  handleEditableField(
    data: Record<string, unknown>,
    resolve: () => void,
    reject: (error: string | undefined) => void
  ): void {
    this.updateMessage(data)
      .then(resolve)
      .catch((error) => {
        // reset action error as it is handled locally
        this.eventApi.patchDataRequest.reset(["error"]);
        reject(error);
      });
  }

  get tableRows(): GuestListTableRow[] {
    return getGuestListTableRows(
      this.eventTrackingDetails?.submissionData?.guests,
      this.eventTrackingDetails?.start,
      this.eventTrackingDetails?.end
    );
  }
}
</script>
