<template lang="html">
  <v-card class="v-card-table">
    <v-card-title
      ><h2 class="light-font mb-6">Offene Ereignisse</h2></v-card-title
    >
    <v-card-text>
      <v-data-table
        :headers="tableData.headers"
        :items="tableRowData"
        :items-per-page="10"
        :hide-default-footer="true"
        :disable-sort="true"
        class="elevation-1 mt-5"
        data-test="open-events.data-table"
      >
        <template v-slot:[itemActionSlotName]="{ item }">
          <v-btn
            color="primary"
            :to="{ name: 'event-details', params: { id: item.code } }"
          >
            Details
          </v-btn>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

export type TableRow = {
  address: string;
  endTime: string;
  extID: string;
  code: string;
  generatedTime: string;
  lastChange: string;
  name: string;
  startTime: string;
  status: string;
  statusColor: string;
  statusName: string;
};

// Define the props by using Vue's canonical way.
const EventTrackingListProps = Vue.extend({
  props: {
    tableRowData: Array,
  },
});

@Component
export default class EventTrackingList extends EventTrackingListProps {
  tableData = {
    search: "",
    headers: [
      {
        text: "Ext.ID",
        align: "start",
        value: "extID",
      },
      { text: "Event", value: "name" },
      { text: "Ort", value: "address" },
      { text: "Zeit (Start)", value: "startTime" },
      { text: "Zeit (Ende)", value: "endTime" },
      { text: "Generiert", value: "generatedTime" },
      // { text: "Status", value: "status" },
      // { text: "Letzte Ändrung", value: "lastChange" },
      { text: "", value: "actions" },
    ],
  };

  // TODO improve this - we need it to circumvent v-slot eslint errors
  // https://stackoverflow.com/questions/61344980/v-slot-directive-doesnt-support-any-modifier
  get itemStatusSlotName(): string {
    return "item.status";
  }

  get itemActionSlotName(): string {
    return "item.actions";
  }
}
</script>

<style scoped lang="scss">
.src-components-event-list {
}
</style>
