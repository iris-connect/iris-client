<template>
  <vertical-stepper v-model="step">
    <vertical-stepper-step :active-step="step" step="1">
      <stepper-input-field
        :value="model.report"
        @input="onReportChange"
        :rules="validationRules.defined"
        :disabled="disabled"
        label="Impfpflichtmeldung wählen"
        step="1"
        :selected-step="step"
        #default="{ attrs, on }"
        data-test="payload.vaccination-report"
      >
        <select-report v-bind="attrs" v-on="on" />
      </stepper-input-field>
    </vertical-stepper-step>
    <vertical-stepper-step :active-step="step" step="2">
      <stepper-input-field
        v-model="model.employees"
        :rules="validationRules.defined"
        :disabled="disabled"
        :editable="!!model.report"
        label="Mitarbeiter wählen"
        step="2"
        :selected-step="step"
        #default="{ attrs, on }"
        data-test="payload.employees"
      >
        <select-employees :report-id="model.report" v-bind="attrs" v-on="on" />
      </stepper-input-field>
    </vertical-stepper-step>
  </vertical-stepper>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import SelectEvent from "@/modules/event-tracking/modules/message-data/components/select-event.vue";
import { IrisMessageDataSelectionPayload } from "@/api";
import { PropType } from "vue";
import rules from "@/common/validation-rules";
import _values from "lodash/values";
import _every from "lodash/every";
import StepperInputField from "@/components/form/stepper-input-field.vue";
import { exportableStatus } from "@/modules/event-tracking/modules/message-data/services/config";
import VerticalStepperStep from "@/components/stepper/vertical-stepper-step.vue";
import VerticalStepper from "@/components/stepper/vertical-stepper.vue";
import SelectReport from "@/modules/vaccination-report/modules/message-data/components/select-report.vue";
import SelectEmployees from "@/modules/vaccination-report/modules/message-data/components/select-employees.vue";

const VaccinationReportMessageDataExportProps = Vue.extend({
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
    SelectEmployees,
    SelectReport,
    VerticalStepper,
    VerticalStepperStep,
    StepperInputField,
    SelectEvent,
  },
})
export default class VaccinationReportMessageDataExport extends VaccinationReportMessageDataExportProps {
  step = 1;

  exportableStatus = exportableStatus;

  model: IrisMessageDataSelectionPayload = this.value || {
    report: "",
    employees: [],
  };

  @Watch("model", { immediate: true, deep: true })
  onModelChange(newValue: IrisMessageDataSelectionPayload) {
    if (_every(_values(newValue), (v) => rules.defined(v) === true)) {
      this.$emit("input", newValue);
    } else {
      this.$emit("input", null);
    }
  }

  onReportChange(value: string) {
    if (value === this.model.report) return;
    this.model.report = value;
    if (this.model.employees.length > 0) {
      this.model.employees = [];
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
