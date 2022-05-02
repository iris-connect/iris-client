<template>
  <div>
    <select-guests-data-table
      v-bind="{ ...$attrs, ...tableData }"
      v-on="$listeners"
      :loading="fetchEventDetails.state.loading"
    />
    <error-message-alert :errors="fetchEventDetails.state.error" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { PropType } from "vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { eventTrackingApi } from "@/modules/event-tracking/services/api";
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
  fetchEventDetails = eventTrackingApi.fetchEventDetails();

  @Watch("eventId", { immediate: true })
  onEventIdChange(eventId: string) {
    if (eventId) {
      this.fetchEventDetails.execute(eventId);
    } else {
      this.fetchEventDetails.reset();
    }
  }

  get tableData(): {
    items: Guest[];
    eventStart?: string;
    eventEnd?: string;
  } {
    const eventDetails = this.fetchEventDetails.state.result;
    return {
      items: eventDetails?.submissionData?.guests || [],
      eventStart: eventDetails?.start,
      eventEnd: eventDetails?.end,
    };
  }
}
</script>
