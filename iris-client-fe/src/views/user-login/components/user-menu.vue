<template>
  <v-menu left bottom>
    <template v-slot:activator="{ on, attrs }">
      <v-btn plain v-bind="attrs" v-on="on">
        <span class="mr-3">{{ displayName }}</span>
        <v-icon large right> mdi-account-circle </v-icon>
      </v-btn>
    </template>
    <v-list>
      <v-list-item v-if="isAdmin" :to="{ name: 'admin-user-list' }">
        <v-list-item-title> Kontoverwaltung </v-list-item-title>
      </v-list-item>
      <v-list-item
        v-if="isUser"
        :to="{ name: 'admin-user-edit', params: { id } }"
      >
        <v-list-item-title> Mein Profil </v-list-item-title>
      </v-list-item>
      <v-dialog max-width="400" v-model="logoutConfirmDialog">
        <template v-slot:activator="{ on, attrs }">
          <v-list-item v-bind="attrs" v-on="on">
            <v-list-item-title> Abmelden </v-list-item-title>
          </v-list-item>
        </template>
        <v-card>
          <v-card-title> Abmelden? </v-card-title>
          <v-card-text>
            Sind Sie sicher, dass Sie sich abmelden möchten?
          </v-card-text>
          <v-card-actions>
            <v-btn color="secondary" text @click="logoutConfirmDialog = false">
              Abbrechen
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="() => logout(true)">
              Abmelden
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-list>
  </v-menu>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { UserRole } from "@/api";

const UserMenuProps = Vue.extend({
  props: {
    displayName: {
      type: String,
      default: "",
    },
    role: {
      type: String as () => UserRole | undefined,
    },
    id: {
      type: String,
      default: "",
    },
  },
});
@Component
export default class UserMenu extends UserMenuProps {
  logoutConfirmDialog = false;
  logout(confirmed: boolean): void {
    this.logoutConfirmDialog = !confirmed;
    if (confirmed) {
      this.$emit("logout");
    }
  }
  get isAdmin(): boolean {
    return this.role === UserRole.Admin;
  }
  get isUser(): boolean {
    return this.role === UserRole.User;
  }
}
</script>
