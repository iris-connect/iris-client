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
import { DataRequestDetails, DataRequestStatusUpdateByUser } from "@/api";
import router from "@/router";
import store from "@/store";
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
  getTableRows,
  TableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import IrisMessageDataExportDialog from "@/views/iris-message-create/components/iris-message-data-export-dialog.vue";

@Component({
  components: {
    IrisMessageDataExportDialog,
    EventTrackingDetailsDataExport,
    EventTrackingDetailsComponent,
    AlertComponent,
  },
  async beforeRouteEnter(from, to, next) {
    next();
    await store.dispatch(
      "eventTrackingDetails/fetchEventTrackingDetails",
      router.currentRoute.params.id
    );
  },
  beforeRouteLeave(to, from, next) {
    store.commit("eventTrackingDetails/reset");
    next();
  },
})
export default class EventTrackingDetailsView extends Vue {
  alert = false;

  get eventTrackingDetails(): DataRequestDetails | null {
    return store.state.eventTrackingDetails.eventTrackingDetails;
  }

  // Editable values => formData. Readonly values => eventData.
  get formData(): FormData {
    return getFormData(this.eventTrackingDetails);
  }

  get eventData(): EventData {
    return getEventData(this.eventTrackingDetails);
  }

  get loading(): boolean {
    return store.state.eventTrackingDetails.eventTrackingDetailsLoading;
  }

  get errorMessages(): ErrorMessage[] {
    return [
      store.state.eventTrackingDetails.eventTrackingDetailsLoadingError,
      store.state.eventTrackingDetails.dataRequestPatchError,
    ];
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

  updateRequestStatus(status: DataRequestStatusUpdateByUser): void {
    store.dispatch("eventTrackingDetails/patchDataRequest", {
      id: router.currentRoute.params.id,
      data: {
        status,
      },
    });
  }

  handleEditableField(
    data: Record<string, unknown>,
    resolve: () => void,
    reject: (error: string | undefined) => void
  ): void {
    store
      .dispatch("eventTrackingDetails/patchDataRequest", {
        id: router.currentRoute.params.id,
        data,
      })
      .then(resolve)
      .catch((error) => {
        // reset vuex error as it is handled locally
        store.commit("eventTrackingDetails/setDataRequestPatchError", null);
        reject(error);
      });
  }

  get tableRows(): TableRow[] {
    return getTableRows(this.eventTrackingDetails);
  }
}
</script>
