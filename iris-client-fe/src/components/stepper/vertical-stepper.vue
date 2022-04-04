<template>
  <v-stepper
    vertical
    flat
    class="vertical-stepper-container"
    v-on="listeners"
    v-bind="$attrs"
    :value="value"
    @change="$emit('input', $event)"
  >
    <slot name="default" />
  </v-stepper>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import _omit from "lodash/omit";

const VerticalStepperProps = Vue.extend({
  inheritAttrs: false,
  props: {
    value: {
      default: null,
    },
  },
});

@Component
export default class VerticalStepper extends VerticalStepperProps {
  get listeners(): Record<string, unknown> {
    return _omit(this.$listeners, ["input"]);
  }
}
</script>

<style lang="scss" scoped>
.vertical-stepper-container {
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
</style>
