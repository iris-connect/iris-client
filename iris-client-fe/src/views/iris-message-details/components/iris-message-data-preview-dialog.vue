<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <v-card>
      <component
        v-if="previewComponent"
        v-bind:is="previewComponent"
        :data="previewPayload"
      />
      <v-card-text v-if="errors.length > 0">
        <error-message-alert :errors="errors" />
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-btn color="secondary" text @click="dialog = false" data-test="close">
          Schließen
        </v-btn>
        <v-spacer></v-spacer>
        <confirm-dialog
          title="Daten importieren?"
          text="Dieser Vorgang kann nicht rückgäng gemacht werden."
          @confirm="handleImport"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-on="on"
              v-bind="attrs"
              color="primary"
              data-test="import-data"
              :disabled="!importEnabled"
            >
              Daten importieren
            </v-btn>
          </template>
        </confirm-dialog>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { AsyncComponent, ComponentOptions, PropType } from "vue";
import { IrisMessageDataDiscriminator, IrisMessageViewData } from "@/api";
import { normalizeDataRequestDetails } from "@/views/event-tracking-details/event-tracking-details.data";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { ErrorMessage } from "@/utils/axios";
import ConfirmDialog from "@/components/confirm-dialog.vue";

type Normalizer = typeof normalizeDataRequestDetails;

type DataPreview = {
  [key in IrisMessageDataDiscriminator]: {
    normalize: Normalizer;
    component: ComponentOptions<Vue> | typeof Vue | AsyncComponent;
  };
};

const dataPreview: DataPreview = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    normalize: normalizeDataRequestDetails,
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-details.preview" */ "../../event-tracking-message-data/event-tracking-details.preview.vue"
      ),
  },
};

const IrisMessageDataPreviewDialogProps = Vue.extend({
  props: {
    value: {
      type: Object as PropType<IrisMessageViewData | null>,
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
    ConfirmDialog,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataPreviewDialog extends IrisMessageDataPreviewDialogProps {
  get dialog() {
    return !!this.value;
  }
  set dialog(value) {
    this.$emit("input", null);
  }
  get previewId() {
    return this.value?.id;
  }
  get previewPayload() {
    try {
      if (this.value?.discriminator) {
        return dataPreview[this.value?.discriminator].normalize(
          this.value.payload
        );
      }
    } catch (e) {
      // ignored
    }
    return null;
  }
  get previewComponent() {
    try {
      if (this.value?.discriminator) {
        return dataPreview[this.value?.discriminator].component;
      }
    } catch (e) {
      // ignored
    }
    return null;
  }
  get errors(): ErrorMessage[] {
    if (!this.value) return [];
    return [
      this.previewPayload === null ? "Fehlende Daten!" : null,
      this.previewComponent === null ? "Fehlende Komponente!" : null,
    ].filter((v) => v);
  }
  handleImport() {
    if (this.value?.id) {
      this.dialog = false;
      this.$emit("import", this.value?.id);
    }
  }
}
</script>
