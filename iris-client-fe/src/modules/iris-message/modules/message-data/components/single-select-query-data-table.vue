<template>
  <data-query-handler
    @update:query="$emit('update:query', $event)"
    :route-control="false"
    #default="{ query }"
  >
    <search-field v-model="query.search" />
    <sortable-data-table
      class="mt-5"
      v-bind="$attrs"
      v-on="listeners"
      v-model="model"
      :items="items"
      :item-key="itemKey"
      :item-class="getItemClass"
      :sort.sync="query.sort"
      :loading="loading"
      show-select
      single-select
      :page.sync="query.page"
      :items-per-page.sync="query.size"
    >
      <template v-for="(_, slot) of $scopedSlots" v-slot:[slot]="scope">
        <slot :name="slot" v-bind="scope" />
      </template>
    </sortable-data-table>
    <error-message-alert :errors="errors" />
  </data-query-handler>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import SearchField from "@/components/pageable/search-field.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import _omit from "lodash/omit";
import _get from "lodash/get";

const SingleSelectQueryDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    errors: {
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    itemKey: {
      type: String,
      default: "id",
    },
    items: {
      type: Array,
      default: () => [],
    },
    value: {
      type: String,
      default: "",
    },
  },
});
@Component({
  components: {
    ErrorMessageAlert,
    SortableDataTable,
    SearchField,
    DataQueryHandler,
  },
})
export default class SingleSelectQueryDataTable extends SingleSelectQueryDataTableProps {
  get listeners(): Record<string, unknown> {
    return _omit(this.$listeners, ["input", "update:query"]);
  }
  getItemClass(item: { isSelectable?: boolean }): string {
    return item.isSelectable === false ? "is-disabled" : "is-selectable";
  }
  get model(): unknown[] {
    if (!this.value) return [];
    return this.items.filter((row) => {
      return _get(row, this.itemKey) === this.value;
    });
  }
  set model(rows: unknown[]) {
    const sel = rows.find((row) => _get(row, this.itemKey));
    this.$emit("input", _get(sel, this.itemKey) || "");
  }
}
</script>
