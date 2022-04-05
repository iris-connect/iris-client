<template>
  <div>
    <select-guests-data-table
      v-bind="{ ...$attrs, ...tableData }"
      v-on="$listeners"
      :loading="eventApi.fetchEventDetails.state.loading"
    />
    <error-message-alert :errors="[eventApi.fetchEventDetails.state.error]" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { PropType } from "vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { bundleEventTrackingApi } from "@/modules/event-tracking/services/api";
import { Guest } from "@/api";
import SelectGuestsDataTable from "@/modules/event-tracking/modules/message-data/components/select-guests-data-table.vue";
const SelectGuestsProps = Vue.extend({
  inheritAttrs: false,
  props: {
    eventId: {
      type: String,
      default: "",
    },
    guests: {
      type: Array as PropType<Guest[] | null>,
      default: null,
    },
    eventStart: {
      type: String,
      default: null,
    },
    eventEnd: {
      type: String,
      default: null,
    },
  },
});
@Component({
  components: {
    SelectGuestsDataTable,
    ErrorMessageAlert,
  },
})
export default class SelectGuests extends SelectGuestsProps {
  eventApi = bundleEventTrackingApi(["fetchEventDetails"]);

  @Watch("eventId", { immediate: true })
  onEventIdChange(eventId: string) {
    if (eventId) {
      this.eventApi.fetchEventDetails.execute(eventId);
    } else {
      this.eventApi.fetchEventDetails.reset();
    }
  }

  get tableData(): {
    items: Guest[];
    eventStart?: string;
    eventEnd?: string;
  } {
    const eventDetails = this.eventApi.fetchEventDetails.state.result;
    return {
      items: eventDetails?.submissionData?.guests || [],
      eventStart: eventDetails?.start,
      eventEnd: eventDetails?.end,
    };
  }
}
</script>
