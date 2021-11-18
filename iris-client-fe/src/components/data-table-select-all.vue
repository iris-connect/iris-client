<template>
  <v-tooltip
    :attach="tooltipAlign"
    right
    :disabled="!partialSelection"
    :value="partialSelection"
    :content-class="partialSelection ? 'tt-arrow' : 'd-none'"
    nudge-right="6"
  >
    <span data-test="data-table-select.tooltip">
      Hier klicken, um alle Daten auszuwählen
    </span>
    <template v-slot:activator="{ on }">
      <v-simple-checkbox
        v-on="on"
        :ripple="false"
        :indeterminate="partialSelection"
        :value="value.length > 0"
        @input="toggle"
      ></v-simple-checkbox>
    </template>
  </v-tooltip>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const DataTableSelectAllProps = Vue.extend({
  inheritAttrs: false,
  props: {
    items: {
      type: Array,
      default: () => [],
    },
    currentItems: {
      type: Array,
      default: () => [],
    },
    value: {
      type: Array,
      default: () => [],
    },
  },
});
@Component
export default class DataTableSelectAll extends DataTableSelectAllProps {
  get partialSelection(): boolean {
    return this.value.length > 0 && this.value.length < this.items.length;
  }
  tooltipAlign: unknown = "";
  mounted() {
    this.tooltipAlign = this.$el.closest(".v-data-table") || "";
  }
  beforeDestroy() {
    this.tooltipAlign = "";
  }
  toggle() {
    if (this.value.length <= 0) {
      this.$emit("input", this.currentItems);
    } else {
      if (this.value.length < this.items.length) {
        this.$emit("input", this.items);
      } else {
        this.$emit("input", []);
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../node_modules/vuetify/src/components/VTooltip/variables";
.tt-arrow {
  &:before {
    content: "";
    width: 0;
    height: 0;
    border-top: 6px solid transparent;
    border-bottom: 6px solid transparent;
    border-right: 12px solid $tooltip-background-color;
    position: absolute;
    left: -12px;
    top: 0;
    bottom: 0;
    margin: auto;
  }
}
</style>
