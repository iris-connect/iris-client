<template>
  <iris-data-table
    v-bind="$attrs"
    v-on="$listeners"
    :sort-by.sync="sortBy"
    :sort-desc.sync="sortDesc"
  >
    <template v-for="(_, slot) of $scopedSlots" v-slot:[slot]="scope">
      <slot :name="slot" v-bind="scope" />
    </template>
  </iris-data-table>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import IrisDataTable from "@/components/iris-data-table.vue";
import { TableSort, TableSortDirection } from "@/server/utils/pagination";

export const getSortDir = (dir: unknown): TableSortDirection | undefined => {
  switch (dir) {
    case "asc":
      return TableSortDirection.ASC;
    case "desc":
      return TableSortDirection.DESC;
    default:
      return undefined;
  }
};

const IrisMessageDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    sort: {
      type: Object as PropType<TableSort | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    IrisDataTable,
  },
})
export default class IrisMessageDataTable extends IrisMessageDataTableProps {
  get sortBy(): string[] {
    return this.sort?.col ? [this.sort.col] : [];
  }

  set sortBy(value: string[]) {
    const sort: TableSort | null =
      value.length > 0
        ? {
            col: value[0],
            dir: this.sort?.dir || TableSortDirection.ASC,
          }
        : null;
    this.$emit("update:sort", sort);
  }

  get sortDesc(): boolean | undefined {
    return this.sort?.dir === TableSortDirection.DESC;
  }

  set sortDesc(value: boolean | undefined) {
    const sort: TableSort | null = this.sort?.col
      ? {
          col: this.sort.col,
          dir: value ? TableSortDirection.DESC : TableSortDirection.ASC,
        }
      : null;
    this.$emit("update:sort", sort);
  }
}
</script>
