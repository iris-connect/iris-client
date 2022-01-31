<template>
  <confirm-dialog v-bind="$attrs" v-on="$listeners">
    <template v-slot:activator="scope">
      <slot name="activator" v-bind="scope" />
    </template>
    <template #title>Daten importieren?</template>
    <template #text>
      Dieser Vorgang kann nicht rückgäng gemacht werden.
    </template>
  </confirm-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ConfirmDialog from "@/components/confirm-dialog.vue";

const IrisMessageDataImportConfirmDialogProps = Vue.extend({});

@Component({
  components: {
    ConfirmDialog,
  },
})
export default class IrisMessageDataImportConfirmDialog extends IrisMessageDataImportConfirmDialogProps {}
</script>
