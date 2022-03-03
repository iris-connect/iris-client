<template>
  <span class="d-flex flex-wrap pt-4">
    <span
      class="d-block pr-6 pb-4"
      v-for="(header, index) in expandedHeaders"
      :key="index"
    >
      <span class="d-block caption">
        <strong>{{ header.text }}</strong>
      </span>
      <span class="d-block text-pre-line">
        {{ getItemValue(header.value) }}
      </span>
    </span>
  </span>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { DataTableHeader } from "vuetify";
import _get from "lodash/get";

const ExpandedDataTableItemProps = Vue.extend({
  inheritAttrs: false,
  props: {
    expandedHeaders: {
      type: Array as PropType<DataTableHeader[]>,
      default: () => [],
    },
    item: {
      type: Object,
      default: null,
    },
  },
});

@Component
export default class ExpandedDataTableItem extends ExpandedDataTableItemProps {
  getItemValue(key: string): string {
    return _get(this.item, key, "-");
  }
}
</script>
