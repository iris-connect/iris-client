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

    <v-btn :to="{ name: 'event-new' }"> NEUE NACHVERFOLGUNG </v-btn>
    <v-btn :to="{ name: 'event-list' }"> NACHVERFOLGUNGEN ANSEHEN </v-btn>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class Home extends Vue {}
</script>

<style lang="scss" scoped>
.home {
  > * {
    margin: 1em;
  }
}
</style>
