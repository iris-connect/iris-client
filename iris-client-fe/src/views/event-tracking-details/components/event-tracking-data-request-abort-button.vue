<template>
  <v-dialog v-model="confirmDialog" max-width="500">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        v-bind="{ ...$attrs, ...attrs }"
        v-on="on"
        text
        small
        color="error"
        class="px-2 mx-n2 my-n2"
      >
        <slot />
      </v-btn>
    </template>
    <v-card>
      <v-card-title> Anfrage abbrechen? </v-card-title>
      <v-card-text>
        Sind sie sich sicher, dass sie die Anfrage abbrechen wollen? Dieser
        Schritt kann
        <span class="text-decoration-underline">nicht</span> rückgängig gemacht
        werden.
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="secondary" text @click="confirmDialog = false">
          Zurück
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn color="error" text @click="abortRequest"> Bestätigen </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class EventTrackingDataRequestAbortButton extends Vue {
  confirmDialog = false;
  abortRequest(): void {
    this.confirmDialog = false;
    this.$emit("click");
  }
}
</script>
