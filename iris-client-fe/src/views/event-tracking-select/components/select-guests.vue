<template>
  <div>
    <search-field :debounce="0" v-model="search" />
    <sortable-data-table
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
import { Component, Vue, Watch } from "vue-property-decorator";
import SearchField from "@/components/pageable/search-field.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import { DataRequestDetails } from "@/api";
import {
  getGuestListTableRows,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import store from "@/store";
import { PropType } from "vue";
const SelectGuestsProps = Vue.extend({
  inheritAttrs: false,
  props: {
    eventId: {
      type: String,
      default: "",
    },
    value: {
      type: Array as PropType<(string | undefined)[]>,
      default: () => [],
    },
  },
});
@Component({
  components: {
    SortableDataTable,
    SearchField,
  },
})
export default class SelectGuests extends SelectGuestsProps {
  get selection(): GuestListTableRow[] {
    if (this.value.length <= 0) return [];
    return this.tableRows.filter((row) => {
      return this.value.indexOf(row.raw.guestId) !== -1;
    });
  }
  set selection(rows: GuestListTableRow[]) {
    const sel = rows.map((row) => row.raw.guestId);
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
  @Watch("eventId", { immediate: true })
  onEventIdChange(eventId: string) {
    if (eventId) {
      store.dispatch("eventTrackingDetails/fetchEventTrackingDetails", eventId);
    } else {
      store.commit("eventTrackingDetails/reset");
    }
  }
  get eventTrackingDetails(): DataRequestDetails | null {
    return store.state.eventTrackingDetails.eventTrackingDetails;
  }
  get tableRows(): GuestListTableRow[] {
    return getGuestListTableRows(
      this.eventTrackingDetails?.submissionData?.guests,
      this.eventTrackingDetails?.start,
      this.eventTrackingDetails?.end
    );
  }
}
</script>
