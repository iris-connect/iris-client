<template lang="html">
  <v-card class="v-card-table">
    <v-card-title
      ><h2 class="light-font mb-6">Offene Ereignisse</h2></v-card-title
    >
    <v-card-text>
      <v-data-table
        :headers="tableData.headers"
        :items="tableData.eventList"
        :items-per-page="10"
        class="elevation-1 mt-5"
      >
        <template v-slot:[itemStatusSlotName]="{ item }">
          <v-chip :color="getStatusColor(item.status)" dark>
            {{ item.status }}
          </v-chip>
        </template>
        <template v-slot:[itemActionSlotName]="{ item }">
          <!-- TODO use imported route name -->
          <v-btn
            color="primary"
            :to="'/ereignisse/' + item.extID"
            @click="selectItem(item)"
          >
            Details
          </v-btn>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script lang="js">

export default {
  name: 'event-list',
  props: [],
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  mounted() {
//
  },
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  data: () => ({
    tableData: {
      search: "",
      headers: [
        {
          text: "Ext.ID",
          align: "start",
          sortable: true,
          value: "extID",
        },
        {text: "Event", value: "name"},
        {text: "Ort", value: "address"},
        {text: "Start", value: "startTime"},
        {text: "Ende", value: "endTime"},
        {text: "Generiert", value: "generatedTime"},
        {text: "Status", value: "status"},
        {text: "Update", value: "lastChange"},
        {text: "", value: "actions"},
      ],
      eventList: [
        {
          extID: "GTOAZEIC",
          name: "Toms Bierbrunnen",
          address: "Nobistor 14, 22767 Hamburg",
          startTime: "30.03.2021 17:00",
          endTime: "30.03.2021 22:00",
          generatedTime: "31.03.2021 09:44",
          status: "Angefragt",
          lastChange: "31.03.2021 09:44",
        },
        {
          extID: "IEZDTEDA",
          name: "S&S Konzert 27.03",
          address: "Schick & Schön. Kaiserstraße 15, 5516 Mainz",
          startTime: "27.03.2021 19:00",
          endTime: "28.03.2021 05:00",
          generatedTime: "29.03.2021 10:21",
          status: "UPDATE",
          lastChange: "29.03.2021 14:15",
        },
      ],
    }
  }),
  methods: {
    // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
    getStatusColor(status) {
      // TODO use enum / string literals
      if (status === "Angefragt") return "#00C4FF";
      else if (status === "UPDATE") return "#FF7666";
      else if (status === "Abgeschlossen") return "#46FF9F";
    },
    // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
    get itemStatusSlotName() {
      return "item.status";
    },
    // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
    get itemActionSlotName() {
      return "item.actions";
    }
  },
  computed: {}
}
</script>

<style scoped lang="scss">
.src-components-event-list {
}
</style>
