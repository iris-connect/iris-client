<template>
  <div :class="isAbortable || isCloseable || isResumable ? 'mt-5' : ''">
    <status-change-confirm-dialog
      v-if="isAbortable"
      @confirm="abortEventTracking"
      color="error"
    >
      <template #activator="{ on, attrs }">
        <v-btn
          v-on="on"
          v-bind="attrs"
          text
          small
          color="error"
          class="px-2 mx-n2"
          data-test="event.cancel"
        >
          Anfrage abbrechen
        </v-btn>
      </template>
      <template #title> Anfrage abbrechen? </template>
      <template #text>
        Sind sie sich sicher, dass sie die Anfrage abbrechen wollen? Dieser
        Schritt kann
        <span class="text-decoration-underline">nicht</span> rückgängig gemacht
        werden.
      </template>
    </status-change-confirm-dialog>
    <v-btn
      v-if="isCloseable"
      @click="closeEventTracking"
      color="success"
      small
      outlined
      data-test="event.close"
    >
      Als bearbeitet markieren
    </v-btn>
    <v-btn
      v-if="isResumable"
      @click="resumeEventTracking"
      text
      small
      color="secondary"
      class="px-2 mx-n2"
      data-test="event.resume"
    >
      Als unbearbeitet markieren
    </v-btn>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { DataRequestStatus, DataRequestStatusUpdateByUser } from "@/api";
import StatusChangeConfirmDialog from "@/views/event-tracking-details/components/confirm-dialog.vue";

const EventTrackingStatusChangeProps = Vue.extend({
  props: {
    status: {
      type: String as () => DataRequestStatus,
      default: "",
    },
  },
});
@Component({
  components: { StatusChangeConfirmDialog },
})
export default class EventTrackingStatusChange extends EventTrackingStatusChangeProps {
  get isAbortable(): boolean {
    return this.status === DataRequestStatus.DataRequested;
  }
  abortEventTracking(): void {
    this.$emit("update", DataRequestStatusUpdateByUser.Aborted);
  }

  get isCloseable(): boolean {
    return this.status === DataRequestStatus.DataReceived;
  }
  closeEventTracking(): void {
    this.$emit("update", DataRequestStatusUpdateByUser.Closed);
  }

  get isResumable(): boolean {
    return this.status === DataRequestStatus.Closed;
  }
  resumeEventTracking(): void {
    this.$emit("update", DataRequestStatusUpdateByUser.DataReceived);
  }
}
</script>

<style scoped></style>
