<template>
  <v-app>
    <v-app-bar app color="white" flat>
      <v-img
        alt="IRIS Logo"
        class="shrink mt-3 mr-4"
        contain
        src="@/assets/logo-iris-connect.png"
        transition="scale-transition"
        height="150"
        max-width="150"
      />
      <template v-if="authenticated">
        <template v-for="link in links">
          <v-btn
            v-if="link.meta.menu"
            :key="link.name"
            :to="link.path"
            :exact="link.meta.menuExact"
            :disabled="link.meta.disabled"
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
import { routes, setInterceptRoute } from "@/router";
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
      handler(newValue, oldValue) {
        /**
         * watch "authenticated" is triggered
             if newValue === true
                we fetch the user information and do nothing else
             if newValue === false
                this can be caused by one of the following cases:
                   the token is invalid (401 or 403) -> session expires
                     newValue === false, oldValue === true & authenticationError
                       we store the intercepted route
                       we redirect the user to the login screen
                   the user clicks the logout button
                     newValue === false, oldValue === true & no authenticationError
                       we redirect the user to the login screen
                   watch: immediate is triggered on page load even if there isn't any change
                     newValue === false, oldValue !== true
                       we do nothing
         */
        if (newValue) {
          this.$store.dispatch("userLogin/fetchAuthenticatedUser");
        } else {
          if (oldValue === true) {
            if (this.$store.state.userLogin.authenticationError) {
              // this is triggered if an existing session expires (caused by API response status codes 401 and 403).
              setInterceptRoute(this.$router.currentRoute);
            }
            this.$router.push("/user/login");
          }
        }
      },
    },
  },
  methods: {
    logoutUser() {
      this.$store.dispatch("userLogin/logout");
    },
  },
});
</script>
<style lang="scss">
@import "./assets/scss/theme.scss";
</style>
