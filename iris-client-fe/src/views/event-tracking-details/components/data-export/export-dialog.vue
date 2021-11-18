<template>
  <v-dialog v-model="dialog" max-width="500">
    <template v-slot:activator="scope">
      <slot name="activator" v-bind="scope" />
    </template>
    <v-card data-test="export-dialog">
      <v-card-title> {{ title }} </v-card-title>
      <v-simple-table class="px-2 pb-2">
        <thead>
          <tr>
            <th class="text-left">Daten</th>
            <th class="text-center">CSV</th>
            <th class="text-center">Excel</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in items" :key="index">
            <td>{{ item.label }}</td>
            <td class="text-center">
              <v-btn
                v-if="item.csv"
                icon
                @click="$emit(item.csv.action)"
                :data-test="item.csv.test"
              >
                <v-icon> mdi-download </v-icon>
              </v-btn>
            </td>
            <td class="text-center">
              <v-btn
                v-if="item.xlsx"
                icon
                @click="$emit(item.xlsx.action)"
                :data-test="item.xlsx.test"
              >
                <v-icon> mdi-download </v-icon>
              </v-btn>
            </td>
          </tr>
        </tbody>
      </v-simple-table>
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
import { PropType } from "vue";

export interface DataExportFormat {
  action: string;
  test: string;
}

export interface DataExportItem {
  label: string;
  csv?: DataExportFormat;
  xlsx?: DataExportFormat;
}

const ExportDialogProps = Vue.extend({
  inheritAttrs: false,
  props: {
    title: {
      type: String,
      default: "Daten exportieren",
    },
    items: {
      type: Array as PropType<DataExportItem[]>,
      default: () => [],
    },
  },
});
@Component
export default class ExportDialog extends ExportDialogProps {
  dialog = false;
}
</script>
