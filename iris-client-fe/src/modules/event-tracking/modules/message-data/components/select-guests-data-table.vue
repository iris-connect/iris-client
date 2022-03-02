<template>
  <div>
    <search-field :debounce="0" v-model="search" />
    <sortable-data-table
      v-bind="$attrs"
      v-model="selection"
      :headers="tableHeaders.headers"
      :items="tableRows"
      :search="search"
      show-select
      show-select-all
      show-expand
      single-expand
      :expanded.sync="expanded"
      :item-class="itemClass"
      class="mt-5"
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
      <template v-slot:expanded-item="{ headers, item }">
        <td></td>
        <td :colspan="headers.length - 1">
          <expanded-data-table-item
            :item="item"
            :expanded-headers="tableHeaders.expandedHeaders"
          />
        </td>
      </template>
    </sortable-data-table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import SearchField from "@/components/pageable/search-field.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import {
  getGuestListTableHeaders,
  getGuestListTableRows,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import { PropType } from "vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { Guest } from "@/api";
import ExpandedDataTableItem from "@/components/expanded-data-table-item.vue";
const SelectGuestsDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    items: {
      type: Array as PropType<Guest[] | null>,
      default: null,
    },
    duplicates: {
      type: Array as PropType<string[] | null>,
      default: null,
    },
    eventStart: {
      type: String,
      default: null,
    },
    eventEnd: {
      type: String,
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
    ExpandedDataTableItem,
    ErrorMessageAlert,
    SortableDataTable,
    SearchField,
  },
})
export default class SelectGuestsDataTable extends SelectGuestsDataTableProps {
  get selection(): GuestListTableRow[] {
    if (this.value.length <= 0) return [];
    return this.tableRows.filter((row) => {
      return this.value.indexOf(row.raw.messageDataSelectId) !== -1;
    });
  }
  set selection(rows: GuestListTableRow[]) {
    const sel = rows.map((row) => row.raw.messageDataSelectId);
    this.$emit("input", sel);
  }
  search = "";
  expanded = [];
  get tableHeaders() {
    const headers = getGuestListTableHeaders(true);
    if (this.duplicates) {
      headers.headers.splice(1, 0, {
        text: "",
        value: "note",
        sortable: false,
      });
    }
    return headers;
  }

  get tableRows(): GuestListTableRow[] {
    return getGuestListTableRows(this.items, this.eventStart, this.eventEnd);
  }

  isDuplicate(item: GuestListTableRow): boolean {
    return !!this.duplicates?.find((id) => item.raw.messageDataSelectId === id);
  }
  itemClass(item: GuestListTableRow): string {
    return this.isDuplicate(item) ? "error-lighten-3" : "";
  }
}
</script>
