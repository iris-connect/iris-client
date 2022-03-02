<template>
  <v-form v-bind="$attrs" ref="form" v-model="form.valid" lazy-validation>
    <v-card>
      <v-card-title>Daten importieren</v-card-title>
      <v-card-text>
        <v-stepper vertical v-model="step" flat>
          <validation-input-field
            :value="form.model.target"
            :rules="validationRules.defined"
            :disabled="disabled"
            @input="onTargetChange"
            #default="{ attrs, on }"
          >
            <v-stepper-step
              editable
              :complete="!!attrs.value"
              :rules="attrs.stepperRules"
              step="1"
            >
              Ereignis wählen
            </v-stepper-step>
            <v-stepper-content step="1">
              <select-event v-bind="attrs" v-on="on" />
            </v-stepper-content>
          </validation-input-field>
          <validation-input-field
            v-model="form.model.selection.guests"
            :rules="validationRules.defined"
            :disabled="disabled"
            #default="{ attrs, on }"
          >
            <v-stepper-step
              :editable="!!form.model.target"
              :complete="attrs.value.length > 0"
              :rules="attrs.stepperRules"
              step="2"
            >
              Daten wählen
            </v-stepper-step>
            <v-stepper-content step="2">
              <select-guests-data-table
                :items="selectableGuests"
                :duplicates="duplicateGuests"
                v-bind="attrs"
                v-on="on"
              />
            </v-stepper-content>
          </validation-input-field>
        </v-stepper>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="secondary" text @click="cancel" :disabled="disabled">
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="submit" :disabled="disabled">
          Daten importieren
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-form>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import SelectEvent from "@/modules/event-tracking/modules/message-data/components/select-event.vue";
import SelectGuestsDataTable from "@/modules/event-tracking/modules/message-data/components/select-guests-data-table.vue";
import { parseData } from "@/utils/data";
import rules from "@/common/validation-rules";
import ValidationInputField from "@/components/form/validation-input-field.vue";
import { EventTrackingMessageDataImportSelection } from "@/modules/event-tracking/modules/message-data/services/normalizer";

const EventTrackingMessageDataImportProps = Vue.extend({
  props: {
    data: {
      type: Object as PropType<EventTrackingMessageDataImportSelection | null>,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
});

type EventTrackingMessageDataImportForm = {
  model: {
    target: string;
    selection: {
      guests: string[];
    };
  };
  valid: boolean;
};

@Component({
  components: {
    ValidationInputField,
    SelectGuestsDataTable,
    SelectEvent,
    ConfirmDialog,
    ErrorMessageAlert,
  },
})
export default class EventTrackingMessageDataImport extends EventTrackingMessageDataImportProps {
  @Ref("form") readonly formRef!: HTMLFormElement;
  step = 1;

  form: EventTrackingMessageDataImportForm = {
    valid: false,
    model: {
      target: "",
      selection: {
        guests: [],
      },
    },
  };

  get selectableGuests() {
    return this.data?.selectables?.guests;
  }

  get duplicateGuests() {
    return this.data?.duplicates?.guests;
  }

  onTargetChange(value: string) {
    if (value === this.form.model.target) return;
    this.form.model.target = value;
    if (this.form.model.selection.guests.length > 0) {
      this.form.model.selection.guests = [];
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

  submit() {
    if (this.formRef.validate()) {
      this.$emit("submit", parseData(this.form.model));
    }
  }

  cancel() {
    this.$emit("cancel");
  }
}
</script>
