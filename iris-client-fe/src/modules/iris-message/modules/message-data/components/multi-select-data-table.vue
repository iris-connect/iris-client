<template>
  <expandable-data-table
    :headers="tableHeaders.headers"
    :expanded-headers="tableHeaders.expandedHeaders"
    v-model="model"
    :items="items"
    :item-class="itemClass"
  >
    <template v-slot:item.note="{ item }">
      <v-tooltip right v-if="isDuplicate(item)">
        <template v-slot:activator="{ on, attrs }">
          <v-icon v-on="on" v-bind="attrs" color="error">
            mdi-alert-octagon
          </v-icon>
        </template>
        Dieser Datensatz existiert bereits im Zielobjekt
      </v-tooltip>
    </template>
    <template v-for="(_, slot) of $scopedSlots" v-slot:[slot]="scope">
      <slot :name="slot" v-bind="scope" />
    </template>
  </expandable-data-table>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { DataTableHeaders } from "@/components/iris-data-table.vue";
import _cloneDeep from "lodash/cloneDeep";
import ExpandableDataTable from "@/components/expandable-data-table.vue";

type SelectableTableRow = {
  raw?: {
    messageDataSelectId?: string;
  };
};

const MultiSelectDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    items: {
      type: Array as PropType<SelectableTableRow[] | null>,
      default: null,
    },
    duplicates: {
      type: Array as PropType<string[] | null>,
      default: null,
    },
    headers: {
      type: Object as PropType<DataTableHeaders>,
      default: null,
    },
    value: {
      type: Array as PropType<(string | undefined)[]>,
      default: () => [],
    },
  },
});
@Component({
  components: {
    ExpandableDataTable,
  },
})
export default class MultiSelectDataTable extends MultiSelectDataTableProps {
  get model(): SelectableTableRow[] {
    if (this.value.length <= 0) return [];
    return (this.items || []).filter((row) => {
      return (
        row.raw?.messageDataSelectId &&
        this.value.indexOf(row.raw?.messageDataSelectId) !== -1
      );
    });
  }
  set model(rows: SelectableTableRow[]) {
    const sel = rows
      .map((row) => row.raw?.messageDataSelectId)
      .filter((v) => v);
    this.$emit("input", sel);
  }

  get tableHeaders() {
    const headers = _cloneDeep(this.headers);
    if (this.duplicates) {
      headers.headers.splice(1, 0, {
        text: "",
        value: "note",
        sortable: false,
      });
    }
    return headers;
  }

  isDuplicate(item: SelectableTableRow): boolean {
    return !!this.duplicates?.find(
      (id) => item.raw?.messageDataSelectId === id
    );
  }

  itemClass(item: SelectableTableRow): string {
    return this.isDuplicate(item) ? "error-lighten-3" : "";
  }
}
</script>
