<template>
  <v-stepper vertical v-model="step" flat>
    <stepper-input-field
      :value="model.target"
      @input="onTargetChange"
      :rules="validationRules.defined"
      :disabled="disabled"
      label="Ereignis wählen"
      step="1"
      #default="{ attrs, on }"
      data-test="payload.event"
    >
      <select-event v-bind="attrs" v-on="on" />
    </stepper-input-field>
    <stepper-input-field
      v-model="model.selection.guests"
      :rules="validationRules.defined"
      :disabled="disabled"
      :editable="!!model.target"
      label="Gäste wählen"
      step="2"
      #default="{ attrs, on }"
      data-test="payload.guests"
    >
      <select-guests-data-table
        :items="selectableGuests"
        :duplicates="duplicateGuests"
        v-bind="attrs"
        v-on="on"
      />
    </stepper-input-field>
  </v-stepper>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { PropType } from "vue";
import SelectEvent from "@/modules/event-tracking/modules/message-data/components/select-event.vue";
import SelectGuestsDataTable from "@/modules/event-tracking/modules/message-data/components/select-guests-data-table.vue";
import rules from "@/common/validation-rules";
import { EventTrackingMessageDataImportSelection } from "@/modules/event-tracking/modules/message-data/services/normalizer";
import { IrisMessageDataSelectionPayload } from "@/api";
import _every from "lodash/every";
import StepperInputField from "@/components/form/stepper-input-field.vue";

type ImportValue = {
  target?: string;
  selection: {
    guests: string[];
  };
};

const EventTrackingMessageDataImportProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<Partial<ImportValue> | null>,
      default: null,
    },
    payload: {
      type: Object as PropType<EventTrackingMessageDataImportSelection | null>,
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
    SelectGuestsDataTable,
    SelectEvent,
  },
})
export default class EventTrackingMessageDataImport extends EventTrackingMessageDataImportProps {
  step = 1;

  model: ImportValue = {
    target: this.value?.target,
    selection: {
      guests: this.value?.selection?.guests || [],
    },
  };

  @Watch("model", { immediate: true, deep: true })
  onModelChange(newValue: IrisMessageDataSelectionPayload) {
    if (
      _every(
        [this.model.target, this.model.selection.guests],
        (v) => rules.defined(v) === true
      )
    ) {
      this.$emit("input", newValue);
    } else {
      this.$emit("input", null);
    }
  }

  get selectableGuests() {
    return this.payload?.selectables?.guests;
  }

  get duplicateGuests() {
    return this.payload?.duplicates?.guests;
  }

  onTargetChange(value: string) {
    if (value === this.model.target) return;
    this.model.target = value;
    if (this.model.selection.guests.length > 0) {
      this.model.selection.guests = [];
    }
    this.$emit("update:target", value);
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
