<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <v-card>
      <iris-message-data-preview :view-data="value" />
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="secondary" text @click="dialog = false" data-test="close">
          Schließen
        </v-btn>
        <v-spacer></v-spacer>
        <slot name="data-import" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { IrisMessageViewData } from "@/api";
import IrisMessageDataPreview from "@/views/iris-message-details/components/iris-message-data-preview.vue";

const IrisMessageDataPreviewDialogProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<IrisMessageViewData | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    IrisMessageDataPreview,
  },
})
export default class IrisMessageDataPreviewDialog extends IrisMessageDataPreviewDialogProps {
  get dialog() {
    return !!this.value;
  }
  set dialog(value) {
    this.$emit("input", null);
  }
}
</script>
