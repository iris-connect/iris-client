<template>
  <div>
    <search-field v-if="searchable" :debounce="0" v-model="search" />
    <iris-data-table
      class="mt-5"
      v-bind="$attrs"
      v-on="$listeners"
      :headers="headers"
      :search="search"
      :show-select="selectEnabled"
      :show-select-all="selectEnabled"
      :show-expand="expandable"
      single-expand
      :expanded.sync="expanded"
    >
      <template v-slot:expanded-item="{ headers, item }">
        <td v-if="selectEnabled"></td>
        <td :colspan="selectEnabled ? headers.length - 1 : headers.length">
          <expanded-data-table-item
            :expanded-headers="expandedHeaders"
            :item="item"
          />
        </td>
      </template>
      <template v-for="(_, slot) of $scopedSlots" v-slot:[slot]="scope">
        <slot :name="slot" v-bind="scope" />
      </template>
    </iris-data-table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import SearchField from "@/components/pageable/search-field.vue";
import IrisDataTable from "@/components/iris-data-table.vue";
import ExpandedDataTableItem from "@/components/expanded-data-table-item.vue";
import { PropType } from "vue";
import { DataTableHeader } from "vuetify";

const ExpandableDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    headers: {
      type: Array as PropType<DataTableHeader[]>,
      default: () => [],
    },
    expandedHeaders: {
      type: Array as PropType<DataTableHeader[] | null>,
      default: null,
    },
    selectEnabled: {
      type: Boolean,
      default: true,
    },
    searchable: {
      type: Boolean,
      default: true,
    },
  },
});
@Component({
  components: {
    ExpandedDataTableItem,
    IrisDataTable,
    SearchField,
  },
})
export default class ExpandableDataTable extends ExpandableDataTableProps {
  search = "";
  expanded = [];
  get expandable(): boolean {
    return (
      Array.isArray(this.expandedHeaders) && this.expandedHeaders.length > 0
    );
  }
}
</script>
