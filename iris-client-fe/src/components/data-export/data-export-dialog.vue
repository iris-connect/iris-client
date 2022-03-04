<template>
  <v-dialog v-model="dialog" max-width="500">
    <template v-slot:activator="{ on, attrs }">
      <slot name="activator" v-bind="{ on, attrs }">
        <v-btn
          v-on="on"
          v-bind="attrs"
          color="white"
          :disabled="disabled"
          data-test="export-dialog.activator"
        >
          Exportformat wählen
        </v-btn>
      </slot>
      <slot name="activator.prepend" />
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
                :disabled="disabled"
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
                :disabled="disabled"
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

interface DataExportAction {
  action: string;
  test: string;
}

export interface DataExportFormat {
  label: string;
  csv?: DataExportAction;
  xlsx?: DataExportAction;
}

const DataExportDialogProps = Vue.extend({
  inheritAttrs: false,
  props: {
    title: {
      type: String,
      default: "Daten exportieren",
    },
    items: {
      type: Array as PropType<DataExportFormat[]>,
      default: () => [],
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
});
@Component
export default class DataExportDialog extends DataExportDialogProps {
  dialog = false;
}
</script>
