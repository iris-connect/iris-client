<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <div class="d-flex align-center">
        <router-link to="/">
          <!-- TODO replace with actual logo -->
          <h1 style="color: white; text-decoration: none !important">IRIS</h1>
        </router-link>
      </div>

      <v-spacer></v-spacer>
    </v-app-bar>

    <v-main class="main">
      <!-- TODO: Consider moving container wrapping to individual view -->
      <v-container>
        <v-row>
          <v-col>
            <router-view />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  name: "App",
});
</script>
