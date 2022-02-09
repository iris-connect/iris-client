<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <v-card>
      <v-card-title>Daten importieren</v-card-title>
      <v-card-text>
        <iris-message-data-component v-bind="dataComponentConfig" />
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import ConfirmDialog from "@/components/confirm-dialog.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { IrisMessageDataDiscriminator } from "@/api";
import IrisMessageDataComponent, {
  MessageDataComponentSource,
} from "@/modules/iris-message/components/iris-message-data-component.vue";

const dataComponentSource: MessageDataComponentSource = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-message-data.import" */ "../../event-tracking-message-data/event-tracking-message-data.import.vue"
      ),
  },
};

export interface IrisMessageImportData {
  id: string;
  discriminator?: IrisMessageDataDiscriminator;
}

const IrisMessageDataImportDialogProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<IrisMessageImportData | null>,
      default: null,
    },
    importEnabled: {
      type: Boolean,
      default: false,
    },
  },
});

@Component({
  components: {
    IrisMessageDataComponent,
    ConfirmDialog,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataImportDialog extends IrisMessageDataImportDialogProps {
  get dialog() {
    return !!this.value;
  }
  set dialog(value) {
    this.$emit("input", null);
  }
  get dataComponentConfig() {
    return {
      source: dataComponentSource,
      discriminator: this.value?.discriminator,
    };
  }
}
</script>
