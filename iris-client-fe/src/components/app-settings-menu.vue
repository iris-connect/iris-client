<template>
  <v-menu left bottom>
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mr-0"
        plain
        icon
        large
        v-bind="attrs"
        v-on="on"
        data-test="app-menu.activator"
      >
        <v-icon large> mdi-cog </v-icon>
      </v-btn>
    </template>
    <v-list data-test="app-menu">
      <v-list-item
        :to="{ name: 'checkin-app-status-list' }"
        data-test="app-menu.item.checkin-app-status-list"
      >
        <v-list-item-title> CheckIn App Status </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const AppSettingsMenuProps = Vue.extend({
  inheritAttrs: false,
});
@Component
export default class AppSettingsMenu extends AppSettingsMenuProps {}
</script>
