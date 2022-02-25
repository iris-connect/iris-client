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

const SortableDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    sort: {
      type: [Object, String],
      default: undefined,
    },
    querySort: {
      type: Boolean,
      default: true,
    },
  },
});
@Component({
  components: {
    IrisDataTable,
  },
})
export default class SortableDataTable extends SortableDataTableProps {
  get sortBy(): string[] {
    return this.sortModel?.col ? [this.sortModel.col] : [];
  }

  set sortBy(value: string[]) {
    this.sortModel =
      value.length > 0
        ? {
            col: value[0],
            dir: this.sortModel?.dir || TableSortDirection.ASC,
          }
        : undefined;
  }

  get sortDesc(): boolean | undefined {
    return this.sortModel?.dir === TableSortDirection.DESC;
  }

  set sortDesc(value: boolean | undefined) {
    this.sortModel = this.sortModel?.col
      ? {
          col: this.sortModel.col,
          dir: value ? TableSortDirection.DESC : TableSortDirection.ASC,
        }
      : undefined;
  }

  get sortModel(): TableSort | undefined {
    if (this.querySort) {
      const sort = typeof this.sort === "string" ? this.sort : "";
      const sortArgs = sort.split(",");
      const col = sortArgs[0];
      const dir = getSortDir(sortArgs[1]);
      return col && dir ? { col, dir } : undefined;
    }
    return typeof this.sort === "string" ? undefined : this.sort;
  }

  set sortModel(value) {
    const sort = this.querySort
      ? value
        ? [value.col, value.dir].join(",")
        : undefined
      : value;
    this.$emit("update:sort", sort);
  }
}
</script>
