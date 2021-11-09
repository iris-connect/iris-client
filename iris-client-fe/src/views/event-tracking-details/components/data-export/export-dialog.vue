<template>
  <v-dialog v-model="dialog" max-width="500">
    <template v-slot:activator="scope">
      <slot name="activator" v-bind="scope" />
    </template>
    <v-card data-test="export-dialog">
      <v-card-title> Daten exportieren </v-card-title>
      <v-list
        class="px-2"
        v-for="(list, listIndex) in exportLists"
        :key="listIndex"
      >
        <v-subheader>{{ list.title }}</v-subheader>
        <v-list-item
          v-for="(item, index) in list.items"
          :key="index"
          @click="$emit(item.action)"
          class="mx-n2 px-6"
          :data-test="item.test"
        >
          <v-list-item-content>
            <v-list-item-title v-text="item.title">
              {{ item.title }}
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-icon>
            <v-icon> mdi-download </v-icon>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="secondary" text @click="dialog = false" data-test="close">
          Schließen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

export interface ExportList {
  title: string;
  items: {
    title: string;
    action: string;
  };
}

const exportListCSV = [
  {
    title: "Standard",
    action: "export-csv-standard",
    test: "export.csv.standard",
  },
  {
    title: "Standard (Alternativ)",
    action: "export-csv-alternative-standard",
    test: "export.csv.standard-alternative",
  },
  {
    title: "SORMAS (Ereignisteilnehmer-Format)",
    action: "export-csv-sormas-event",
    test: "export.csv.sormas-event-participants",
  },
  {
    title: "SORMAS (Kontaktpersonen-Format)",
    action: "export-csv-sormas-contact",
    test: "export.csv.sormas-contact-persons",
  },
];

const exportListXLSX = [
  {
    title: "OctoWare®",
    action: "export-xlsx-octoware",
    test: "export.xlsx.octoware",
  },
];

const ExportDialogProps = Vue.extend({
  inheritAttrs: false,
});

@Component
export default class ExportDialog extends ExportDialogProps {
  dialog = false;
  exportLists = [
    {
      title: "CSV",
      items: exportListCSV,
    },
    {
      title: "XLSX",
      items: exportListXLSX,
    },
  ];
}
</script>
