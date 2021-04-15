<template>
  <v-app>
    <v-app-bar app color="white" flat>
      <v-container class="py-0 fill-height">
        <v-img
          alt="IRIS Logo"
          class="shrink mr-4"
          contain
          src="@/assets/logo.png"
          transition="scale-transition"
          width="100"
        />

        <template v-if="menuEnabled">
          <template v-for="link in links">
            <v-btn
              v-if="link.meta.menu"
              :key="link.name"
              :to="link.path"
              :exact="link.meta.menuExact"
              text
            >
              {{ link.meta.menuName }}
            </v-btn>
          </template>
          <v-spacer></v-spacer>

          <v-responsive max-width="260">
            <v-text-field
              dense
              flat
              hide-details
              rounded
              solo-inverted
            ></v-text-field>
          </v-responsive>
        </template>
      </v-container>
    </v-app-bar>

    <v-main class="grey lighten-3">
      <v-container>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import { routes } from "@/router";

export default Vue.extend({
  name: "App",
  data: () => ({
    links: routes,
  }),
  computed: {
    menuEnabled(): boolean {
      return this.$route.meta.menuEnabled !== false;
    },
  },
});
</script>
<style lang="scss">
@import "./assets/scss/theme.scss";
</style>
