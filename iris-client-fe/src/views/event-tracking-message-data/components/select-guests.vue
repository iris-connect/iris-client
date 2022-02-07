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
      :loading="fetchEventDetails.state.loading"
    />
    <error-message-alert :errors="[fetchEventDetails.state.error]" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import SearchField from "@/components/pageable/search-field.vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import {
  getGuestListTableRows,
  GuestListTableRow,
} from "@/views/event-tracking-details/utils/mappedData";
import { PropType } from "vue";
import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { fetchEventDetailsAction } from "@/modules/event-tracking/api";
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
    ErrorMessageAlert,
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

  fetchEventDetails = fetchEventDetailsAction();

  @Watch("eventId", { immediate: true })
  async onEventIdChange(eventId: string) {
    if (eventId) {
      this.fetchEventDetails.execute(eventId);
    } else {
      this.fetchEventDetails.reset();
    }
  }

  get tableRows(): GuestListTableRow[] {
    const eventDetails = this.fetchEventDetails.state.result;
    return getGuestListTableRows(
      eventDetails?.submissionData?.guests,
      eventDetails?.start,
      eventDetails?.end
    );
  }

  get fetchEventDetailsAction() {
    const action = async (eventId: string) => {
      return normalizeDataRequestDetails(
        (await authClient.getLocationDetails(eventId)).data,
        true
      );
    };
    return asyncAction(action);
  }
}
</script>
