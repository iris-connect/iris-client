<template>
  <div>
    <search-field :debounce="0" v-model="search" />
    <sortable-data-table
      v-bind="$attrs"
      v-model="selection"
      :headers="tableHeaders"
      :items="tableRows"
      :search="search"
      show-select
      show-select-all
      class="mt-5"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import SearchField from "@/components/pageable/search-field.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import {
  getGuestListTableRows,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import { PropType } from "vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { Guest } from "@/api";
const SelectGuestsDataTableProps = Vue.extend({
  inheritAttrs: false,
  props: {
    items: {
      type: Array as PropType<Guest[] | null>,
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
  tableHeaders = [
    { text: "", value: "data-table-select" },
    {
      text: "Nachname",
      value: "lastName",
      align: "start",
    },
    {
      text: "Vorname",
      value: "firstName",
    },
    {
      text: "Check-In",
      value: "checkInTime",
    },
    {
      text: "Check-Out",
      value: "checkOutTime",
    },
    {
      text: "max. Kontaktdauer",
      value: "maxDuration",
    },
    {
      text: "Kommentar",
      value: "comment",
    },
    { text: "", value: "data-table-expand" },
  ];

  get tableRows(): GuestListTableRow[] {
    return getGuestListTableRows(this.items, this.eventStart, this.eventEnd);
  }
}
</script>
