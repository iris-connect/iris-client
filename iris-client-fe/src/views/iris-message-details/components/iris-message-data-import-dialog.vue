<template>
  <v-dialog v-model="dialog">
    <template v-slot:activator="{ attrs, on }">
      <slot name="activator" v-bind="{ attrs, on }" />
    </template>
    <iris-message-data-import-form
      :import-data="value"
      :disabled="loading"
      :errors="errors"
      :data-view-config="dataViewConfig"
      @update:target="onUpdateTarget"
      @submit="onSubmit"
      @cancel="dialog = false"
    />
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import {
  IrisMessageDataDiscriminator,
  IrisMessageDataSelectionPayload,
} from "@/api";
import { bundleIrisMessageApi } from "@/modules/iris-message/services/api";
import { ErrorMessage } from "@/utils/axios";
import { getApiErrorMessages, getApiLoading } from "@/utils/api";
import { IrisMessageDataViewSource } from "@/modules/iris-message/modules/message-data/components/iris-message-data-view.vue";
import {
  EventTrackingMessageDataImportSelection,
  normalizeEventTrackingMessageDataImportSelection,
} from "@/modules/event-tracking/modules/message-data/services/normalizer";
import IrisMessageDataImportForm from "@/views/iris-message-details/components/iris-message-data-import-form.vue";

type IrisMessageDataViewPayload = {
  [IrisMessageDataDiscriminator.EventTracking]: EventTrackingMessageDataImportSelection;
};

export type DataViewSource =
  IrisMessageDataViewSource<IrisMessageDataViewPayload>;

const dataViewSource: DataViewSource = {
  [IrisMessageDataDiscriminator.EventTracking]: {
    normalize: normalizeEventTrackingMessageDataImportSelection,
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-message-data.import" */ "../../../modules/event-tracking/modules/message-data/event-tracking-message-data.import.vue"
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
  },
});

@Component({
  components: {
    IrisMessageDataImportForm,
    ErrorMessageAlert,
  },
})
export default class IrisMessageDataImportDialog extends IrisMessageDataImportDialogProps {
  messageDataApi = bundleIrisMessageApi([
    "getMessageDataImportSelectionViewData",
    "importDataAttachmentAndUpdate",
  ]);

  get dialog() {
    return !!this.value;
  }
  set dialog(value) {
    this.$emit("input", null);
  }

  get importSelectionViewPayload() {
    const viewData =
      this.messageDataApi.getMessageDataImportSelectionViewData.state.result;
    return viewData?.payload;
  }

  onUpdateTarget(value: string | null) {
    if (this.value?.id && value) {
      this.messageDataApi.getMessageDataImportSelectionViewData.execute(
        this.value?.id,
        value
      );
    }
  }

  get dataViewConfig() {
    const viewData =
      this.messageDataApi.getMessageDataImportSelectionViewData.state.result;
    return {
      discriminator: this.value?.discriminator,
      source: dataViewSource,
      payload: viewData?.payload,
    };
  }

  get loading(): boolean {
    return getApiLoading(this.messageDataApi);
  }
  get errors(): ErrorMessage[] {
    return getApiErrorMessages(this.messageDataApi);
  }

  async onSubmit(messageData: {
    target: string;
    selection: IrisMessageDataSelectionPayload;
  }) {
    if (this.value?.id && messageData.target) {
      await this.messageDataApi.importDataAttachmentAndUpdate.execute(
        this.value.id,
        messageData.target,
        messageData.selection
      );
      this.dialog = false;
      this.$emit("import:done");
    }
  }
}
</script>
