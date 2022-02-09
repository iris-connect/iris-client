<template>
  <div>
    <v-stepper vertical v-model="step" width="100%" flat>
      <v-stepper-step
        editable
        :complete="!!selection.event"
        step="1"
        class="ml-0"
      >
        Ereignis wählen
      </v-stepper-step>
      <v-stepper-content step="1">
        <div>
          <select-event
            v-if="step >= 1"
            :value="selection.event"
            @input="onEventChange"
          />
        </div>
      </v-stepper-content>
      <v-stepper-step
        :editable="!!selection.event"
        :complete="step > 2"
        step="2"
      >
        Daten wählen
      </v-stepper-step>
      <v-stepper-content step="2">
        <div>
          <!--          <select-guests :event-id="selection.event" />-->
          <!--          <event-tracking-details-preview-->
          <!--            v-if="step >= 2"-->
          <!--            :data="eventApi.fetchEventDetails.state.result"-->
          <!--          />-->
        </div>
      </v-stepper-content>
    </v-stepper>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import SelectEvent from "@/views/event-tracking-message-data/components/select-event.vue";
import { IrisMessageImportData } from "@/views/iris-message-details/components/iris-message-data-import-dialog.vue";
import EventTrackingDetailsPreview from "@/views/event-tracking-message-data/event-tracking-details.preview.vue";
import { bundleEventTrackingApi } from "@/modules/event-tracking/api";
import SelectGuests from "@/views/event-tracking-message-data/components/select-guests.vue";

const EventTrackingMessageDataImportProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<IrisMessageImportData | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    SelectGuests,
    EventTrackingDetailsPreview,
    SelectEvent,
    ConfirmDialog,
    ErrorMessageAlert,
  },
})
export default class EventTrackingMessageDataImport extends EventTrackingMessageDataImportProps {
  step = 1;
  selection: { event: string; guests: string[] } = {
    event: "",
    guests: [],
  };
  eventApi = bundleEventTrackingApi(["fetchEventDetails"]);
  onEventChange(value: string) {
    if (value === this.selection.event) return;
    this.selection.event = value;
    // this.touched.event = true;
    this.selection.guests = [];
    if (value) {
      this.step = 2;
      this.eventApi.fetchEventDetails.execute(value);
    }
  }
}
</script>
