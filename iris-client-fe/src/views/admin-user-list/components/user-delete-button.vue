<template>
  <v-dialog max-width="400" v-model="confirmDialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="{ ...$attrs, ...attrs }" v-on="on">
        <slot />
      </v-btn>
    </template>
    <v-card>
      <v-card-title> Nutzer {{ userName }} löschen? </v-card-title>
      <v-card-text>
        Sind Sie sicher, dass Sie den Nutzer {{ userName }} löschen möchten?
      </v-card-text>
      <v-card-actions>
        <v-btn color="secondary" text @click="confirmDialog = false">
          Abbrechen
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="() => deleteUser(true)"> Löschen </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

const UserDeleteButtonProps = Vue.extend({
  props: {
    userName: {
      type: String,
      default: "",
    },
  },
});

@Component
export default class UserDeleteButton extends UserDeleteButtonProps {
  confirmDialog = false;
  deleteUser(confirmed: boolean): void {
    this.confirmDialog = !confirmed;
    if (confirmed) {
      this.$emit("click");
    }
  }
}
</script>
