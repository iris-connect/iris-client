<template>
  <v-stepper vertical v-model="step" flat>
    <v-stepper-step
      :rules="validationRules.event"
      editable
      :complete="!!selection.event"
      step="1"
    >
      Ereignis wählen
    </v-stepper-step>
    <v-stepper-content step="1">
      <select-event
        :value="selection.event"
        @input="onEventChange"
        :description="description"
        @update:description="$emit('update:description', $event)"
      />
    </v-stepper-content>
    <v-stepper-step
      :rules="validationRules.guests"
      :editable="!!selection.event"
      :complete="selection.guests.length > 0"
      step="2"
    >
      Gäste wählen
    </v-stepper-step>
    <v-stepper-content step="2">
      <select-guests
        :event-id="selection.event"
        :value="selection.guests"
        @input="onGuestsChange"
      />
    </v-stepper-content>
  </v-stepper>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import IrisDataTable from "@/components/iris-data-table.vue";
import EventTrackingFormView from "@/views/event-tracking-form/event-tracking-form.view.vue";
import SelectEvent from "@/views/event-tracking-message-data/components/select-event.vue";
import { IrisMessageDataSelectionPayload } from "@/api";
import SelectGuests from "@/views/event-tracking-message-data/components/select-guests.vue";
import { PropType } from "vue";
import rules from "@/common/validation-rules";

const EventTrackingMessageDataExportProps = Vue.extend({
  props: {
    description: {
      type: String,
      default: "",
    },
    value: {
      type: Object as PropType<IrisMessageDataSelectionPayload | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    SelectGuests,
    SelectEvent,
    IrisDataTable,
    EventTrackingFormView,
  },
})
export default class EventTrackingMessageDataExport extends EventTrackingMessageDataExportProps {
  step = 1;

  touched: Record<string, boolean | undefined> = {};
  selection: IrisMessageDataSelectionPayload = this.value || {
    event: "",
    guests: [],
  };

  validate(key: string, touched?: boolean) {
    const result = rules.defined(this.selection[key]);
    return (touched ? this.touched[key] : true) ? result : true;
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      event: [() => this.validate("event", true)],
      guests: [() => this.validate("guests", true)],
    };
  }

  @Watch("selection", { immediate: true, deep: true })
  onSelectionChange(newValue: IrisMessageDataSelectionPayload) {
    if (this.validate("event") === true && this.validate("guests") === true) {
      this.$emit("input", newValue);
    } else {
      this.$emit("input", null);
    }
  }

  onEventChange(value: string) {
    if (value === this.selection.event) return;
    this.selection.event = value;
    this.touched.event = true;
    this.selection.guests = [];
    if (value) {
      this.step = 2;
    }
  }

  onGuestsChange(value: string[]) {
    this.selection.guests = value;
    this.touched.guests = true;
  }
}
</script>
