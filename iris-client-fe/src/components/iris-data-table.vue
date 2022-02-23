<template>
  <v-data-table
    v-bind="$attrs"
    v-on="$listeners"
    :loading="loading"
    :items="filteredItems"
    v-model="model"
    :class="[
      $attrs.class,
      {
        'is-loading': loading,
        'is-empty': filteredItems.length <= 0,
      },
    ]"
    @current-items="(cItems) => (currentItems = cItems)"
  >
    <template v-if="showSelectAll" #header.data-table-select>
      <data-table-select-all
        :items="items"
        :current-items="currentItems"
        v-model="model"
      ></data-table-select-all>
    </template>
    <template v-for="(_, slot) of $scopedSlots" v-slot:[slot]="scope">
      <slot :name="slot" v-bind="scope" />
    </template>
  </v-data-table>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import DataTableSelectAll from "@/components/data-table-select-all.vue";
import { PropType } from "vue";

type FilterFunction = <T>(value: T, index: number, array: T[]) => boolean;

const IrisDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    loading: {
      type: [Boolean, String],
      default: false,
    },
    items: {
      type: Array,
      default: () => [],
    },
    showSelectAll: {
      type: Boolean,
      default: false,
    },
    value: {
      type: Array,
      default: () => [],
    },
    filter: {
      type: Function as PropType<FilterFunction | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    DataTableSelectAll,
  },
})
export default class IrisDataTable extends IrisDataTableProps {
  currentItems = [];
  get filteredItems() {
    if (this.filter) {
      return this.items.filter(this.filter);
    }
    return this.items;
  }
  get model() {
    return this.value;
  }
  set model(value: unknown) {
    this.$emit("input", value);
  }
}
</script>
