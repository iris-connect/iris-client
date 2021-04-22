<template>
  <div>
    <v-menu left bottom>
      <template v-slot:activator="{ on, attrs }">
        <v-icon large v-bind="attrs" v-on="on"> mdi-account-circle </v-icon>
      </template>
      <v-list>
        <!-- @todo: show user-admin link only if user has role ADMIN -->
        <v-list-item :to="{ name: 'admin-user-list' }">
          <v-list-item-title> Benutzerverwaltung </v-list-item-title>
        </v-list-item>
        <v-list-item @click="() => logout(false)">
          <v-list-item-title> Abmelden </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
    <v-dialog max-width="400" v-model="logoutConfirmDialog">
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
          <v-btn color="primary" @click="() => logout(true)"> Abmelden </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component
export default class UserMenu extends Vue {
  logoutConfirmDialog = false;
  logout(confirmed: boolean): void {
    this.logoutConfirmDialog = !confirmed;
    if (confirmed) {
      this.$emit("logout");
    }
  }
}
</script>
