<template>
  <div class="alertOverlay">
    <v-alert
      color="blue"
      prominent
      dismissible
      elevation="4"
      type="success"
      transition="scale-transition"
    >
      <slot name="message"></slot>
    </v-alert>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class AlertComponent extends Vue {
}
</script>
