<template>
  <div class="alertOverlay">
    <v-alert
      :value="alert"
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

const AlertComponentProps = Vue.extend({
  props: {
    is_created: {
      type: String,
      default: "false",
    },
  },
});

@Component
export default class AlertComponent extends AlertComponentProps {
  alert = false;

  created(): void {
    if (this.is_created == "true") {
      this.openAlert();
    }
  }

  openAlert(): void {
    this.alert = true;
    setTimeout(() => {
      this.alert = false;
    }, 2000);
  }
}
</script>
