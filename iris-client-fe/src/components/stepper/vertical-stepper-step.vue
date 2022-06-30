<template>
  <div
    :class="isActive ? 'overflow-auto' : ''"
    v-on="$listeners"
    v-bind="$attrs"
  >
    <slot name="default" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import _parseInt from "lodash/parseInt";

const VerticalStepperStepProps = Vue.extend({
  inheritAttrs: false,
  props: {
    step: {
      type: [String, Number] as PropType<number | string | null>,
      default: 1,
    },
    activeStep: {
      type: [String, Number] as PropType<number | string | null>,
      default: null,
    },
  },
});

@Component
export default class VerticalStepperStep extends VerticalStepperStepProps {
  get isActive(): boolean {
    if (this.step === null || this.activeStep === null) return false;
    const step =
      typeof this.step === "string" ? _parseInt(this.step) : this.step;
    const activeStep =
      typeof this.activeStep === "string"
        ? _parseInt(this.activeStep)
        : this.activeStep;
    return step === activeStep;
  }
}
</script>
