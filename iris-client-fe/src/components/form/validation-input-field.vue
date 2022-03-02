<template>
  <v-input
    hide-details="auto"
    class="validation-input-field"
    :value="value"
    :rules="rules"
  >
    <div class="w-100">
      <slot
        v-bind="{
          attrs: { ...$attrs, value, rules, stepperRules },
          on: { input: handleInput },
        }"
        v-on="$listeners"
        @input="handleInput"
      />
    </div>
  </v-input>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import _map from "lodash/map";

const ValidationInputFieldProps = Vue.extend({
  inheritAttrs: false,
  props: {
    value: {
      type: [Array, String],
      default: "",
    },
    rules: {
      type: Array,
      default: () => [],
    },
  },
});

@Component
export default class ValidationInputField extends ValidationInputFieldProps {
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
.w-100 {
  width: 100%;
}
</style>
