<template>
  <event-tracking-details-component
    :table-rows="tableRows"
    :event-data="eventData"
    :form-data="formData"
    :is-preview="true"
    :select-enabled="false"
  />
</template>
<script lang="ts">
import { DataRequestDetails } from "@/api";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingDetailsComponent from "@/views/event-tracking-details/components/event-tracking-details.component.vue";
import {
  EventData,
  FormData,
  getEventData,
  getFormData,
  getGuestListTableRows,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import { PropType } from "vue";

const EventTrackingMessageDataPreviewProps = Vue.extend({
  props: {
    payload: {
      type: Object as PropType<DataRequestDetails | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    EventTrackingDetailsComponent,
  },
})
export default class EventTrackingMessageDataPreview extends EventTrackingMessageDataPreviewProps {
  get formData(): FormData {
    return getFormData(this.payload);
  }
  get eventData(): EventData {
    return getEventData(this.payload);
  }
  get tableRows(): GuestListTableRow[] {
    return getGuestListTableRows(
      this.payload?.submissionData?.guests,
      this.payload?.start,
      this.payload?.end
    );
  }
}
</script>
