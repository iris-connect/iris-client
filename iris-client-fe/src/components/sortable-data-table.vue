<template>
  <iris-data-table
    v-bind="$attrs"
    v-on="listeners"
    :sort-by.sync="sortBy"
    :sort-desc.sync="sortDesc"
    :page.sync="tablePage"
    :footer-props="withDefaultFooterProps"
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
import _omit from "lodash/omit";
import { PropType } from "vue";
import { DEFAULT_ITEMS_PER_PAGE_OPTIONS } from "@/utils/pagination";
import _map from "lodash/map";
import _castArray from "lodash/castArray";
import _isEqual from "lodash/isEqual";

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
      type: [Array, String] as PropType<
        string | TableSort | (string | TableSort)[] | undefined
      >,
      default: undefined,
    },
    querySort: {
      type: Boolean,
      default: true,
    },
    page: {
      type: Number,
      default: 0,
    },
    footerProps: {
      type: Object as PropType<Record<string, unknown> | null>,
      default: null,
    },
  },
});
@Component({
  components: {
    IrisDataTable,
  },
})
export default class SortableDataTable extends SortableDataTableProps {
  get listeners(): Record<string, unknown> {
    return _omit(this.$listeners, ["update:page", "update:sort"]);
  }
  get tablePage(): number {
    return this.page + 1;
  }
  set tablePage(value: number) {
    this.$emit("update:page", Math.max(0, value - 1));
  }

  get withDefaultFooterProps() {
    return {
      "items-per-page-options": DEFAULT_ITEMS_PER_PAGE_OPTIONS,
      ...this.footerProps,
    };
  }

  get sortBy(): string | string[] | undefined {
    return _map(this.sortModel, "col");
  }

  set sortBy(value: string | string[] | undefined) {
    if (!value || value.length <= 0) {
      this.sortModel = [];
    } else {
      this.sortModel = _castArray(value).map((col, index) => {
        return {
          col: col,
          dir: this.sortModel[index]?.dir || TableSortDirection.ASC,
        };
      });
    }
  }

  get sortDesc(): boolean | boolean[] | undefined {
    return this.sortModel.map((s) => s.dir === TableSortDirection.DESC);
  }

  set sortDesc(value: boolean | boolean[] | undefined) {
    this.sortModel = this.sortModel.map((s, index) => {
      const sortDesc = Array.isArray(value) ? value[index] : value;
      return {
        col: s.col,
        dir: sortDesc ? TableSortDirection.DESC : TableSortDirection.ASC,
      };
    });
  }

  get sortModel(): TableSort[] {
    if (this.sort) {
      return _castArray(this.sort).map((s) => {
        if (typeof s !== "string") return s;
        const sortArgs = s.split(",");
        return {
          col: sortArgs[0],
          dir: sortArgs[1] as TableSortDirection,
        };
      });
    }
    return [];
  }

  set sortModel(value: TableSort[]) {
    if (_isEqual(value, this.sortModel)) return;
    const sort = this.querySort
      ? value.map((s) => [s.col, s.dir].join(","))
      : value;
    this.$emit("update:sort", sort);
  }
}
</script>
