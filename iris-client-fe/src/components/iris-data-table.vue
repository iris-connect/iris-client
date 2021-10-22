<template>
  <v-data-table
    v-bind="$attrs"
    v-on="$listeners"
    :loading="loading"
    :items="items"
    :class="[
      $attrs.class,
      {
        'is-loading': loading,
        'is-empty': items.length <= 0,
      },
    ]"
  >
    <template v-for="(_, slot) of $scopedSlots" v-slot:[slot]="scope">
      <slot :name="slot" v-bind="scope" />
    </template>
  </v-data-table>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

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
  },
});

@Component
export default class IrisDataTable extends IrisDataTableProps {}
</script>
