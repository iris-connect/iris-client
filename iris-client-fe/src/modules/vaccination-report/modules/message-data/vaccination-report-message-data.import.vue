<template>
  <vertical-stepper v-model="step">
    <vertical-stepper-step :active-step="step" step="1">
      <stepper-input-field
        :value="model.target"
        @input="onTargetChange"
        :rules="validationRules.defined"
        :disabled="disabled"
        label="Impfpflichtmeldung wählen"
        step="1"
        #default="{ attrs, on }"
        data-test="payload.vaccination-report"
      >
        <select-report v-bind="attrs" v-on="on" />
      </stepper-input-field>
    </vertical-stepper-step>
    <vertical-stepper-step :active-step="step" step="2">
      <stepper-input-field
        v-model="model.selection.employees"
        :rules="validationRules.defined"
        :disabled="disabled"
        :editable="!!model.target"
        label="Mitarbeiter wählen"
        step="2"
        #default="{ attrs, on }"
        data-test="payload.employees"
      >
        <select-employees-data-table
          :items="selectableEmployees"
          :duplicates="duplicateEmployees"
          v-bind="attrs"
          v-on="on"
        />
      </stepper-input-field>
    </vertical-stepper-step>
  </vertical-stepper>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { PropType } from "vue";
import rules from "@/common/validation-rules";
import { IrisMessageDataSelectionPayload } from "@/api";
import _every from "lodash/every";
import StepperInputField from "@/components/form/stepper-input-field.vue";
import VerticalStepper from "@/components/stepper/vertical-stepper.vue";
import VerticalStepperStep from "@/components/stepper/vertical-stepper-step.vue";
import SelectReport from "@/modules/vaccination-report/modules/message-data/components/select-report.vue";
import SelectEmployeesDataTable from "@/modules/vaccination-report/modules/message-data/components/select-employees-data-table.vue";
import { VaccinationReportMessageDataImportSelection } from "@/modules/vaccination-report/modules/message-data/services/normalizer";

type ImportValue = {
  target?: string;
  selection: {
    employees: string[];
  };
};

const VaccinationReportMessageDataImportProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<Partial<ImportValue> | null>,
      default: null,
    },
    payload: {
      type: Object as PropType<VaccinationReportMessageDataImportSelection | null>,
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
    SelectEmployeesDataTable,
    SelectReport,
    VerticalStepperStep,
    VerticalStepper,
    StepperInputField,
  },
})
export default class VaccinationReportMessageDataImport extends VaccinationReportMessageDataImportProps {
  step = 1;

  model: ImportValue = {
    target: this.value?.target,
    selection: {
      employees: this.value?.selection?.employees || [],
    },
  };

  @Watch("model", { immediate: true, deep: true })
  onModelChange(newValue: IrisMessageDataSelectionPayload) {
    if (
      _every(
        [this.model.target, this.model.selection.employees],
        (v) => rules.defined(v) === true
      )
    ) {
      this.$emit("input", newValue);
    } else {
      this.$emit("input", null);
    }
  }

  get selectableEmployees() {
    return this.payload?.selectables?.employees;
  }

  get duplicateEmployees() {
    return this.payload?.duplicates?.employees;
  }

  onTargetChange(value: string) {
    if (value === this.model.target) return;
    this.model.target = value;
    if (this.model.selection.employees.length > 0) {
      this.model.selection.employees = [];
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
