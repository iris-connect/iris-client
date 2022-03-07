<template>
  <v-stepper vertical v-model="step" flat>
    <stepper-input-field
      :value="model.event"
      @input="onEventChange"
      :rules="validationRules.defined"
      :disabled="disabled"
      label="Ereignis wählen"
      step="1"
      #default="{ attrs, on }"
      data-test="payload.event"
    >
      <select-event
        v-bind="attrs"
        v-on="on"
        :select-query="selectEventQuery"
        :description="description"
        @update:description="$emit('update:description', $event)"
      />
    </stepper-input-field>
    <stepper-input-field
      v-model="model.guests"
      :rules="validationRules.defined"
      :disabled="disabled"
      :editable="!!model.event"
      label="Gäste wählen"
      step="2"
      #default="{ attrs, on }"
      data-test="payload.guests"
    >
      <select-guests :event-id="model.event" v-bind="attrs" v-on="on" />
    </stepper-input-field>
  </v-stepper>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import SelectEvent from "@/modules/event-tracking/modules/message-data/components/select-event.vue";
import { DataRequestStatus, IrisMessageDataSelectionPayload } from "@/api";
import SelectGuests from "@/modules/event-tracking/modules/message-data/components/select-guests.vue";
import { PropType } from "vue";
import rules from "@/common/validation-rules";
import _values from "lodash/values";
import _every from "lodash/every";
import StepperInputField from "@/components/form/stepper-input-field.vue";

const EventTrackingMessageDataExportProps = Vue.extend({
  inheritAttrs: false,
  props: {
    description: {
      type: String,
      default: "",
    },
    value: {
      type: Object as PropType<IrisMessageDataSelectionPayload | null>,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
});

@Component({
  components: {
    StepperInputField,
    SelectGuests,
    SelectEvent,
  },
})
export default class EventTrackingMessageDataExport extends EventTrackingMessageDataExportProps {
  step = 1;

  selectEventQuery = { status: DataRequestStatus.DataReceived };

  model: IrisMessageDataSelectionPayload = this.value || {
    event: "",
    guests: [],
  };

  @Watch("model", { immediate: true, deep: true })
  onModelChange(newValue: IrisMessageDataSelectionPayload) {
    if (_every(_values(newValue), (v) => rules.defined(v) === true)) {
      this.$emit("input", newValue);
    } else {
      this.$emit("input", null);
    }
  }

  onEventChange(value: string) {
    if (value === this.model.event) return;
    this.model.event = value;
    if (this.model.guests.length > 0) {
      this.model.guests = [];
    }
    if (value) {
      this.step = 2;
    }
  }

  get validationRules(): Record<string, Array<unknown>> {
    return {
      defined: [rules.defined],
    };
  }
}
</script>
