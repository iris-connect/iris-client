<template>
  <v-dialog v-model="confirmDialog" max-width="500">
    <template v-slot:activator="scope">
      <slot name="activator" v-bind="scope" />
    </template>
    <v-card data-test="confirm-dialog">
      <v-card-title>
        <slot name="title" />
      </v-card-title>
      <v-card-text>
        <slot name="text" />
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn
          color="secondary"
          text
          @click="confirmDialog = false"
          data-test="cancel"
        >
          Zurück
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn :color="color" text @click="onConfirm" data-test="confirm">
          Bestätigen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const ConfirmDialogProps = Vue.extend({
  props: {
    color: {
      type: String,
      default: "error",
    },
  },
});

@Component
export default class ConfirmDialog extends ConfirmDialogProps {
  confirmDialog = false;
  onConfirm(): void {
    this.confirmDialog = false;
    this.$emit("confirm");
  }
}
</script>
