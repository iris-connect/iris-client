<template>
  <div>
    <h1 class="text-h3 mb-6">Ereignisnachverfolgungen</h1>
    <div class="mb-6">
      <v-dialog
          transition="dialog-bottom-transition"
          max-width="98%"
          width="1600px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="primary"
              v-bind="attrs"
              v-on="on"
            >Neue Ereignisverfolgung starten</v-btn>
          </template>
          <EventTrackingFormView></EventTrackingFormView>
        </v-dialog>
    </div>
    <v-card>
      <p>DISCLAIMER: DEMO TABELLE</p>
      <v-card-title>
        <v-text-field
          v-model="tableData.search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
      </v-card-title>
      <v-data-table
        :headers="tableData.headers"
        :items="tableData.desserts"
        :items-per-page="5"
        class="elevation-1"
        :search="tableData.search"
      ></v-data-table>
    </v-card>
  </div>
</template>

<script lang="ts">
import { ROUTE_NAME_EVENT_TRACKING_FORM } from "@/router";
import { Component, Vue } from "vue-property-decorator";
import EventTrackingFormView from "../event-tracking-form/event-tracking-form.view.vue";
@Component({
    components: {
        "EventTrackingFormView": EventTrackingFormView
    }
})
export default class EventTrackingListView extends Vue {
  routeEventTrackingForm = ROUTE_NAME_EVENT_TRACKING_FORM;
  tableData = {
    search: "",
    headers: [
      {
        text: "Dessert (100g serving)",
        align: "start",
        sortable: false,
        value: "name",
      },
      { text: "Calories", value: "calories" },
      { text: "Fat (g)", value: "fat" },
      { text: "Carbs (g)", value: "carbs" },
      { text: "Protein (g)", value: "protein" },
      { text: "Iron (%)", value: "iron" },
    ],
    desserts: [
      {
        name: "Frozen Yogurt",
        calories: 159,
        fat: 6.0,
        carbs: 24,
        protein: 4.0,
        iron: "1%",
      },
      {
        name: "Ice cream sandwich",
        calories: 237,
        fat: 9.0,
        carbs: 37,
        protein: 4.3,
        iron: "1%",
      },
      {
        name: "Eclair",
        calories: 262,
        fat: 16.0,
        carbs: 23,
        protein: 6.0,
        iron: "7%",
      },
      {
        name: "Cupcake",
        calories: 305,
        fat: 3.7,
        carbs: 67,
        protein: 4.3,
        iron: "8%",
      },
      {
        name: "Gingerbread",
        calories: 356,
        fat: 16.0,
        carbs: 49,
        protein: 3.9,
        iron: "16%",
      },
      {
        name: "Jelly bean",
        calories: 375,
        fat: 0.0,
        carbs: 94,
        protein: 0.0,
        iron: "0%",
      },
      {
        name: "Lollipop",
        calories: 392,
        fat: 0.2,
        carbs: 98,
        protein: 0,
        iron: "2%",
      },
      {
        name: "Honeycomb",
        calories: 408,
        fat: 3.2,
        carbs: 87,
        protein: 6.5,
        iron: "45%",
      },
      {
        name: "Donut",
        calories: 452,
        fat: 25.0,
        carbs: 51,
        protein: 4.9,
        iron: "22%",
      },
      {
        name: "KitKat",
        calories: 518,
        fat: 26.0,
        carbs: 65,
        protein: 7,
        iron: "6%",
      },
    ],
  };
}
</script>
