<template>
  <v-input
    hide-details="auto"
    class="stepper-input-field"
    :value="value"
    :rules="rules"
  >
    <div class="stepper-input" :data-test="dataTest">
      <v-stepper-step
        :editable="editable"
        :complete="complete"
        :rules="stepperRules"
        :step="step"
      >
        {{ label }}
      </v-stepper-step>
      <v-stepper-content :step="step">
        <slot
          name="default"
          v-bind="{
            attrs: { ...$attrs, value },
            on: { ...listeners, input: handleInput },
          }"
        />
      </v-stepper-content>
    </div>
  </v-input>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import _map from "lodash/map";
import _omit from "lodash/omit";

const StepperInputFieldProps = Vue.extend({
  inheritAttrs: false,
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
    value: {
      type: [Array, String],
      default: "",
    },
    rules: {
      type: Array,
      default: () => [],
    },
    dataTest: {
      type: String,
      default: null,
    },
  },
});
@Component
export default class StepperInputField extends StepperInputFieldProps {
  get listeners(): Record<string, unknown> {
    return _omit(this.$listeners, ["input"]);
  }
  get complete(): boolean {
    return !!this.value && this.value.length > 0;
  }
  touched = false;
  handleInput(value: unknown) {
    this.touched = true;
    this.$emit("input", value);
  }
  get stepperRules(): unknown[] {
    return _map(
      this.rules,
      (rule: (value: unknown) => string | boolean) => () =>
        this.touched ? rule(this.value) : true
    );
  }
}
</script>

<style lang="scss" scoped>
.stepper-input-field .stepper-input {
  width: 100%;
}
</style>
