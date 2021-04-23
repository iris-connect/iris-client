<template>
  <v-app>
    <v-app-bar app color="white" flat>
      <v-img
        alt="IRIS Logo"
        class="shrink mt-3 mr-4"
        contain
        src="@/assets/logo.png"
        transition="scale-transition"
        height="100"
        max-width="100"
      />
      <template v-if="authenticated">
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
        <user-menu
          :display-name="userDisplayName"
          :is-admin="isAdmin"
          @logout="logoutUser"
        />
      </template>
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
import UserMenu from "@/views/user-login/components/user-menu.vue";

// @todo: move user functionality to a dedicated user-module?
export default Vue.extend({
  name: "App",
  components: {
    UserMenu,
  },
  data: () => ({
    links: routes,
  }),
  computed: {
    authenticated(): boolean {
      return this.$store.getters["userLogin/isAuthenticated"];
    },
    userDisplayName(): string {
      return this.$store.getters["userLogin/userDisplayName"];
    },
    isAdmin(): boolean {
      return this.$store.getters["userLogin/isAdmin"];
    },
  },
  watch: {
    authenticated: {
      immediate: true,
      handler(newValue) {
        if (newValue) {
          this.$store.dispatch("userLogin/fetchAuthenticatedUser");
        } else {
          this.$router.push("/user/login");
        }
      },
    },
  },
  methods: {
    logoutUser() {
      this.$store.commit("userLogin/setSession");
    },
  },
});
</script>
<style lang="scss">
@import "./assets/scss/theme.scss";
</style>
