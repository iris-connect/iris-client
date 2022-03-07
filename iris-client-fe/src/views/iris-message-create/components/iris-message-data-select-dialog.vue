<template>
  <v-dialog v-model="dialog" scrollable>
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }">
        <v-btn
          color="red lighten-2"
          dark
          v-on="on"
          v-bind="attrs"
          data-test="message-data.export-dialog.activator"
          :disabled="disabled"
        >
          Daten hinzufügen
        </v-btn>
      </slot>
    </template>
    <iris-message-data-select-form
      v-if="dialog"
      v-bind="$attrs"
      :form-values="value"
      :disabled="disabled"
      @submit="onSubmit"
      @cancel="dialog = false"
    />
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { IrisMessageDataInsert } from "@/api";
import IrisMessageDataSelectForm from "@/views/iris-message-create/components/iris-message-data-select-form.vue";

const IrisMessageDataSelectDialogProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<IrisMessageDataInsert | null>,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
});

@Component({
  components: {
    IrisMessageDataSelectForm,
  },
})
export default class IrisMessageDataSelectDialog extends IrisMessageDataSelectDialogProps {
  dialog = false;
  onSubmit(messageData: IrisMessageDataInsert) {
    this.$emit("input", messageData);
    this.dialog = false;
  }
}
</script>
