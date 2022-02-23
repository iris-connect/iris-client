<template>
  <v-btn-toggle
    v-bind="$attrs"
    dense
    mandatory
    class="flex-wrap"
    v-model="model"
  >
    <v-btn
      text
      v-for="selectOption in selectOptions"
      :key="selectOption.value"
      :data-test="`btn.select.${selectOption.value}`"
    >
      {{ selectOption.text }}
    </v-btn>
    <v-btn text data-test="btn.select.all"> Alle </v-btn>
  </v-btn-toggle>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import _findIndex from "lodash/findIndex";

type SelectOption = { text: string; value: string };

const BtnToggleSelectProps = Vue.extend({
  inheritAttrs: false,
  props: {
    value: {
      type: String,
      default: null,
    },
    selectOptions: {
      type: Array as PropType<SelectOption[]>,
      default: () => [],
    },
  },
});

@Component
export default class BtnToggleSelect extends BtnToggleSelectProps {
  get model(): number {
    if (!this.value) {
      // return length + 1, assuming "Alle" button is displayed last
      return this.selectOptions.length;
    }
    const index = _findIndex(this.selectOptions, ["value", this.value]);
    return index >= 0 ? index : this.selectOptions.length;
  }
  set model(value: number) {
    this.$emit("input", this.selectOptions[value]?.value || "");
  }
}
</script>
