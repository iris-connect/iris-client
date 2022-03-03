<template>
  <validation-input-field
    v-bind="$attrs"
    v-on="$listeners"
    #default="{ attrs, on }"
  >
    <v-stepper-step
      :editable="editable"
      :complete="isComplete(attrs.value)"
      :rules="attrs.stepperRules"
      :step="step"
    >
      {{ label }}
    </v-stepper-step>
    <v-stepper-content :step="step">
      <slot name="default" v-bind="{ attrs, on }"></slot>
    </v-stepper-content>
  </validation-input-field>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ValidationInputField from "@/components/form/validation-input-field.vue";

const StepperInputFieldProps = Vue.extend({
  props: {
    label: {
      type: String,
      default: "",
    },
    step: {
      type: [String, Number],
      default: 1,
    },
    editable: {
      type: Boolean,
      default: true,
    },
  },
});
@Component({
  components: {
    ValidationInputField,
  },
})
export default class StepperInputField extends StepperInputFieldProps {
  isComplete(value: string | string[]): boolean {
    return !!value && value.length > 0;
  }
}
</script>
