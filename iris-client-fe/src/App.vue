<template>
  <v-app>
    <v-app-bar app color="white" flat data-test="app-bar">
      <template v-if="authenticated">
        <v-app-bar-nav-icon class="hidden-md-and-up" @click="showMenu = true" />
        <v-menu v-model="showMenu" content-class="hidden-md-and-up">
          <v-list>
            <template v-for="link in links">
              <v-list-item
                :key="link.name"
                v-if="link.meta.menu"
                :to="link.path"
                :exact="link.meta.menuExact"
                :disabled="isLinkDisabled(link)"
                :data-test="`app-bar.nav.link.${link.name}`"
              >
                <component v-bind:is="link.meta.menuComponent || 'span'">
                  <v-list-item-title>
                    {{ link.meta.menuName }}
                  </v-list-item-title>
                </component>
              </v-list-item>
            </template>
          </v-list>
        </v-menu>
      </template>
      <v-img
        alt="IRIS Logo"
        :class="{ shrink: true, 'hidden-sm-and-down': authenticated }"
        contain
        src="@/assets/logo-iris-connect.png"
        transition="scale-transition"
        max-width="150"
        height="36"
        position="0 45%"
      />
      <template v-if="authenticated">
        <v-toolbar-items class="hidden-sm-and-down">
          <template v-for="link in links">
            <v-btn
              :key="link.name"
              v-if="link.meta.menu"
              :to="link.path"
              :exact="link.meta.menuExact"
              :disabled="isLinkDisabled(link)"
              text
              :data-test="`app-bar.nav.link.${link.name}`"
            >
              <component v-bind:is="link.meta.menuComponent || 'span'">
                {{ link.meta.menuName }}
              </component>
            </v-btn>
          </template>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <app-settings-menu />
        <user-menu
          :display-name="userDisplayName"
          :role="userRole"
          :id="userID"
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
import { RouteConfig } from "vue-router";
import { UserRole } from "@/api";
import AppSettingsMenu from "@/components/app-settings-menu.vue";

// @todo: move user functionality to a dedicated user-module?
export default Vue.extend({
  name: "App",
  components: {
    AppSettingsMenu,
    UserMenu,
  },
  created() {
    document.title = "IRIS connect";
  },
  data: () => ({
    links: routes,
    showMenu: false,
  }),
  computed: {
    authenticated(): boolean {
      return this.$store.getters["userLogin/isAuthenticated"];
    },
    userDisplayName(): string {
      return this.$store.getters["userLogin/userDisplayName"];
    },
    userID(): string {
      return this.$store.state.userLogin.user?.id ?? "";
    },
    userRole(): UserRole | undefined {
      return this.$store.state.userLogin.user?.role;
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
            this.$store.commit("userLogin/setUser");
            this.$router.push("/user/login");
          }
        }
      },
    },
  },
  methods: {
    isLinkDisabled(link: RouteConfig): boolean {
      // @todo - indexTracking: remove disabled check once index cases are permanently activated again
      if (
        link.name === "index-new" ||
        link.name === "index-list" ||
        link.name === "index-details"
      ) {
        if (!this.$store.state.indexTrackingSettings.indexTrackingEnabled) {
          return true;
        }
      }
      return link.meta?.disabled;
    },
    logoutUser() {
      this.$store.dispatch("userLogin/logout");
    },
  },
});
</script>
<style lang="scss">
@import "./assets/scss/theme.scss";
</style>
