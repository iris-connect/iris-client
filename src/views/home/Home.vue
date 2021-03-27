<template>
  <div class="home">
    <v-img
      alt="IRIS Logo"
      class="shrink mr-2"
      contain
      src="@/assets/logo.png"
      transition="scale-transition"
      width="240"
    />

    <v-btn :to="routeEventTrackingForm"> NEUE NACHVERFOLGUNG </v-btn>
    <v-btn :to="routeEventTrackingList"> NACHVERFOLGUNGEN ANSEHEN </v-btn>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import {
  ROUTE_NAME_EVENT_TRACKING_FORM,
  ROUTE_NAME_EVENT_TRACKING_LIST,
} from "@/router";

@Component
export default class Home extends Vue {
  routeEventTrackingForm = ROUTE_NAME_EVENT_TRACKING_FORM;
  routeEventTrackingList = ROUTE_NAME_EVENT_TRACKING_LIST;
}
</script>

<style lang="scss" scoped>
.home {
  > * {
    margin: 1em;
  }
}
</style>
