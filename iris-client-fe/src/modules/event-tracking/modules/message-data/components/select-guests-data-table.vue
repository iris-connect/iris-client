<template>
  <multi-select-data-table
    v-bind="$attrs"
    v-on="$listeners"
    :items="tableRows"
    :headers="tableHeaders"
  />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import {
  getGuestListTableHeaders,
  getGuestListTableRows,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import { PropType } from "vue";
import { Guest } from "@/api";
import MultiSelectDataTable from "@/modules/iris-message/modules/message-data/components/multi-select-data-table.vue";
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
  },
});
@Component({
  components: {
    MultiSelectDataTable,
  },
})
export default class SelectGuestsDataTable extends SelectGuestsDataTableProps {
  get tableHeaders() {
    return getGuestListTableHeaders(true);
  }
  get tableRows(): GuestListTableRow[] {
    return getGuestListTableRows(this.items, this.eventStart, this.eventEnd);
  }
}
</script>
