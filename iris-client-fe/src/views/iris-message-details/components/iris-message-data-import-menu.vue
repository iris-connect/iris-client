<template>
  <v-menu
    offset-y
    :disabled="disabled"
    content-class="menu-message-data-import"
  >
    <template v-slot:activator="{ on, attrs }">
      <slot
        name="activator"
        v-bind="{
          on,
          attrs: activatorAttrs(attrs),
        }"
      >
        <v-btn
          v-on="on"
          v-bind="activatorAttrs(attrs)"
          icon
          data-test="message-data.import.activator"
        >
          <v-icon>mdi-download</v-icon>
        </v-btn>
      </slot>
    </template>
    <v-list>
      <confirm-dialog
        title="Daten importieren?"
        text="Dieser Vorgang kann nicht rückgäng gemacht werden."
        @confirm="$emit('import:add')"
      >
        <template #activator="{ on, attrs }">
          <v-list-item
            v-on="on"
            v-bind="attrs"
            data-test="message-data.import.add"
          >
            <v-list-item-title> Neuen Datensatz anlegen </v-list-item-title>
          </v-list-item>
        </template>
      </confirm-dialog>
      <v-list-item
        @click="$emit('import:update')"
        data-test="message-data.import.update"
      >
        <v-list-item-title>
          Bestehenden Datensatz aktualisieren
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ConfirmDialog from "@/components/confirm-dialog.vue";

const IrisMessageDataImportMenuProps = Vue.extend({
  inheritAttrs: false,
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
  },
});
@Component({
  components: {
    ConfirmDialog,
  },
})
export default class IrisMessageDataImportMenu extends IrisMessageDataImportMenuProps {
  activatorAttrs(attrs: Record<string, unknown>) {
    return {
      ...attrs,
      dataTest: "message-data.import.activator",
      disabled: this.disabled,
    };
  }
}
</script>
